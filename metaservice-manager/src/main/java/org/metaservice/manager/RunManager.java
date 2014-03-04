package org.metaservice.manager;

import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.core.config.Config;
import org.metaservice.core.config.ManagerConfig;
import org.metaservice.core.descriptor.DescriptorHelper;
import org.metaservice.core.descriptor.JAXBMetaserviceDescriptorImpl;
import org.metaservice.core.utils.ProcessUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ilo on 12.02.14.
 */
@Singleton
public class RunManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(RunManager.class);

    private final List<RunEntry> runEntries = Collections.synchronizedList(new ArrayList<RunEntry>());
    private final AtomicInteger processCounter = new AtomicInteger();
    public List<RunEntry> getRunEntries() {
        return runEntries;
    }
    private final Config config;
    private final Path logdir;
    @Inject
    public RunManager(Config config) throws ManagerException {
        this.config = config;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        logdir = Paths.get("/opt/metaservice/log/",format.format(new Date()));
        try {
            Files.createDirectory(logdir);
        } catch (IOException e) {
            if(!logdir.toFile().isDirectory())
                throw new ManagerException("could not create log directory for run");
        }
    }

    @Nullable
    public RunEntry getRunEntryByMPid(int mpid){
        for(RunEntry runEntry : runEntries){
            if(runEntry.getMpid() == mpid)
                return runEntry;
        }
        return null;
    }

    public void shutdown(){
        for(RunEntry runEntry : runEntries){
           shutdown(runEntry);
        }
    }

    public void shutdown(RunEntry runEntry){
        if(runEntry.getStatus() == RunEntry.Status.RUNNING){
            runEntry.setStatus(RunEntry.Status.SHUTTING_DOWN);
            LOGGER.info("Killing " + runEntry.getMpid() + " " + runEntry.getName());
            runEntry.getProcess().destroy();
        }
    }

    public void runProvider(
            @NotNull ManagerConfig.Module module,
            @NotNull MetaserviceDescriptor.ProviderDescriptor providerDescriptor
    ) throws ManagerException {

        run(
                module.getMetaserviceDescriptor().getModuleInfo(),
                DescriptorHelper.getStringFromProvider(module.getMetaserviceDescriptor().getModuleInfo(),providerDescriptor),
                "org.metaservice.core.jms.JMSProviderRunner",
                new String[]{providerDescriptor.getId()},
                config.getDefaultProviderOpts().split(" "),
                false
        );
    }

    public void runPostProcessor(
            @NotNull ManagerConfig.Module module,
            @NotNull MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor
    ) throws ManagerException {
        run(
                module.getMetaserviceDescriptor().getModuleInfo(),
                DescriptorHelper.getStringFromPostProcessor(module.getMetaserviceDescriptor().getModuleInfo(),postProcessorDescriptor),
                "org.metaservice.core.jms.JMSPostProcessorRunner",
                new String[]{postProcessorDescriptor.getId()},
                config.getDefaultPostProcessorOpts().split(" "),
                false
        );
    }

    public void runFrontend() throws ManagerException {
        MetaserviceDescriptor.ModuleInfo moduleInfo = new JAXBMetaserviceDescriptorImpl.ModuleInfoImpl("org.metaservice","metaservice-frontend-rest","0.1");
        run(
                moduleInfo,
                "frontend",
                "org.metaservice.frontend.rest.RestFrontend",
                new String[]{},
                config.getDefaultPostProcessorOpts().split(" "),//todo own defaults
                false
        );
    }

    public void runCrawlerAndWaitForFinish(
            @NotNull String id,
            @NotNull String group_id,
            @NotNull String artifact_id,
            @NotNull String version
    ) throws ManagerException {
        System.out.println("Starting Crawler " + id);
        MetaserviceDescriptor.ModuleInfo moduleInfo = new JAXBMetaserviceDescriptorImpl.ModuleInfoImpl(group_id,artifact_id,version);
        run(
                moduleInfo,
                DescriptorHelper.getStringFromRepository(moduleInfo, id),
                "org.metaservice.core.crawler.CrawlerRunner",
                new String[]{id},
                config.getDefaultPostProcessorOpts().split(" "),//todo own defaults
                true
        );

        System.out.println("Finished Crawler " + id);
    }

    private void run(@NotNull final MetaserviceDescriptor.ModuleInfo moduleInfo,String name,String mainClass,String[] args,String[] jvmargs,boolean wait) throws ManagerException {

        final RunEntry runEntry = new RunEntry();
        runEntry.setMpid(processCounter.incrementAndGet());
        runEntry.setStatus(RunEntry.Status.STARTING);
        runEntry.setName(name);
        try {
            runEntry.setMachine(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            runEntry.setMachine("localhost");
        }
        runEntry.setStartTime(new Date());
        setLogFile(runEntry);
        setErrorFile(runEntry);
        runEntries.add(runEntry);

        final Path directory;


        try {
            directory = Files.createTempDirectory(runEntry.getMpid() + "_"  + name);
        } catch (IOException e) {
            LOGGER.error("could not create temp directory");
            throw new ManagerException(e);
        }

        ArrayList<String> command = new ArrayList<>();
        command.add("java");
        command.addAll(Arrays.asList(jvmargs));
        command.add("-cp");
        command.add( moduleInfo.getArtifactId()+"-"+moduleInfo.getVersion()+".jar:lib/*");
        command.add(mainClass );
        command.addAll(Arrays.asList(args));



        final ProcessBuilder pb =
                new ProcessBuilder(command)
                        .directory(directory.toFile())
                        .redirectOutput(runEntry.getStdout())
                        .redirectError(runEntry.getStderr());

        LOGGER.debug(pb.command().toString());


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                LOGGER.info("STARTING waiting");
                try {
                    loadMaven(directory,moduleInfo);
                    Process p = pb.start();
                    runEntry.setStatus(RunEntry.Status.RUNNING);
                    runEntry.setProcess(p);
                    p.waitFor();

                } catch (InterruptedException e) {
                    LOGGER.error("INFO INTERRUPTED",e);
                } catch (IOException e) {
                    LOGGER.error("IO",e);
                } catch (ManagerException e) {
                    LOGGER.error("MANAGER",e);
                }
                runEntry.setStatus(RunEntry.Status.FINISHED);
                LOGGER.info("Setting finished");
                runEntry.setExitValue(runEntry.getProcess().exitValue());
            }
        };
        if(wait){
            runnable.run();
        }else{
            new Thread(runnable).start();
        }

    }


    private void loadMaven(Path directory, MetaserviceDescriptor.ModuleInfo moduleInfo) throws ManagerException {
        try {
            ProcessBuilder pb =
                    new ProcessBuilder(
                            "mvn",
                            "org.apache.maven.plugins:maven-dependency-plugin:2.7:get",
                            "-DgroupId=" + moduleInfo.getGroupId(),
                            "-DartifactId=" + moduleInfo.getArtifactId(),
                            "-Dversion=" + moduleInfo.getVersion(),
                            "-U")
                            .directory(directory.toFile());
            if(LOGGER.isDebugEnabled())
                pb.inheritIO();
            LOGGER.debug(pb.command().toString());

            pb.start().waitFor();

            Path from = Paths.get("/root",".m2","repository",moduleInfo.getGroupId().replaceAll("\\.","/"),moduleInfo.getArtifactId(),moduleInfo.getVersion(),moduleInfo.getArtifactId()+"-"+moduleInfo.getVersion()+".jar");
            Path to =directory.resolve(moduleInfo.getArtifactId()+"-"+moduleInfo.getVersion()+".jar");
            LOGGER.debug("FROM: " + from.toAbsolutePath());
            LOGGER.debug("TO: " + to.toAbsolutePath());


            Files.copy(from,to);

            pb =    new ProcessBuilder(
                    "mvn",
                    "org.apache.maven.plugins:maven-dependency-plugin:2.7:copy-dependencies",
                    "-f",
                    "/root/.m2/repository/" + moduleInfo.getGroupId().replaceAll("\\.","/") + "/" + moduleInfo.getArtifactId()+ "/" + moduleInfo.getVersion() + "/" + moduleInfo.getArtifactId() +"-" +moduleInfo.getVersion() +".pom",
                    "-DoutputDirectory="+directory.toAbsolutePath().toString()+"/lib",
                    "-DexcludeTypes=test-jar"
            )
                    .directory(directory.toFile());
            if(LOGGER.isDebugEnabled())
                    pb.inheritIO();
            LOGGER.debug(pb.command().toString());
            ProcessUtil.debug(pb.start());
        } catch (InterruptedException | IOException |ProcessUtil.ProcessUtilException e) {
            throw new ManagerException(e);
        }
    }

    private File setLogFile(RunEntry runEntry){
        runEntry.setStdout(logdir.resolve(runEntry.getMpid() + "_" + runEntry.getName().replaceAll(":","_") + ".log").toFile());
        return runEntry.getStdout();
    }

    private File setErrorFile(RunEntry runEntry){
        runEntry.setStderr(logdir.resolve(runEntry.getMpid() + "_" + runEntry.getName().replaceAll(":","_") + ".err").toFile());
        return runEntry.getStderr();
    }


    public void shutdown(ManagerConfig.Module module, List<ManagerConfig.Module> installedModules) {
        for(RunEntry runEntry : runEntries){
            if(module.equals(DescriptorHelper.getModuleFromString(installedModules, runEntry.getName()))){
                shutdown(runEntry);
            }
        }
    }
}

