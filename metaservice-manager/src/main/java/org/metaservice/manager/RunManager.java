package org.metaservice.manager;

import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.core.config.Config;
import org.metaservice.core.config.ManagerConfig;
import org.metaservice.core.utils.ProcessUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ilo on 12.02.14.
 */
@Singleton
public class RunManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(RunManager.class);
    private static final String RUN_EXECUTABLE = "/opt/metaservice/run.sh";
    public static final String MAINCLASS = "MAINCLASS";
    public static final String GROUP_ID = "GROUP_ID";
    public static final String ARTIFACT_ID = "ARTIFACT_ID";
    public static final String VERSION = "VERSION";

    private final List<RunEntry> runEntries = Collections.synchronizedList(new ArrayList<RunEntry>());
    private final AtomicInteger processCounter = new AtomicInteger();
    public List<RunEntry> getRunEntries() {
        return runEntries;
    }
    private final Config config;
    @Inject
    public RunManager(Config config) {
        this.config = config;
    }

    public void shutdown(){
        for(RunEntry runEntry : runEntries){
            if(runEntry.getStatus() == RunEntry.Status.RUNNING){
                LOGGER.info("Killing " + runEntry.getMpid() + " " + runEntry.getName());
                runEntry.getProcess().destroy();
            }
        }
    }

    public void runProvider(ManagerConfig.Module module,MetaserviceDescriptor.ProviderDescriptor providerDescriptor) throws ManagerException {
        ProcessBuilder pb = new ProcessBuilder(RUN_EXECUTABLE,providerDescriptor.getId());

        MetaserviceDescriptor.ModuleInfo moduleInfo =module.getMetaserviceDescriptor().getModuleInfo();
        Map<String, String> env = pb.environment();
        env.put(MAINCLASS, "org.metaservice.core.jms.JMSProviderRunner");
        env.put(GROUP_ID, moduleInfo.getGroupId());
        env.put(ARTIFACT_ID,moduleInfo.getArtifactId());
        env.put(VERSION, moduleInfo.getVersion());
        try {
            Process p = pb.start();
        } catch (IOException e) {
            throw new ManagerException(e);
        }
    }

    public void runPostProcessor(@NotNull ManagerConfig.Module module,@NotNull MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor) throws ManagerException {
        MetaserviceDescriptor.ModuleInfo moduleInfo = module.getMetaserviceDescriptor().getModuleInfo();
        Path directory;
        File logFile =new File("/opt/metaservice/log/" + postProcessorDescriptor.getId() + new Date().toString()+".log");
        File errorFile =new File("/opt/metaservice/log/" + postProcessorDescriptor.getId() + new Date().toString()+".err");


        try {
             directory = Files.createTempDirectory(postProcessorDescriptor.getId());
        } catch (IOException e) {
            LOGGER.error("could not create temp directory");
            throw new ManagerException(e);
        }

        loadMaven(directory,module);

        ArrayList<String> command = new ArrayList<>();
        command.add("java");
        command.addAll(Arrays.asList(config.getDefaultPostProcessorOpts().split(" ")));
        command.add("-cp");
        command.add( moduleInfo.getArtifactId()+"-"+moduleInfo.getVersion()+".jar:lib/*");
        command.add( "org.metaservice.core.jms.JMSPostProcessorRunner");
        command.add(postProcessorDescriptor.getId());
        ProcessBuilder pb =
                new ProcessBuilder(command)
                .directory(directory.toFile())
                .redirectOutput(logFile)
                .redirectError(errorFile);

        LOGGER.info("Standard Out: {}",logFile);
        LOGGER.info("Standard Err: {}",errorFile);
        LOGGER.info(pb.command().toString());

        final RunEntry runEntry = new RunEntry();
        runEntry.setName(postProcessorDescriptor.getId());
        runEntry.setStartTime(new Date());
        runEntry.setStderr(errorFile);
        runEntry.setStdout(logFile);
        runEntry.setStatus(RunEntry.Status.RUNNING);
        runEntry.setMpid(processCounter.incrementAndGet());
        try {
            runEntry.setProcess(pb.start());
            runEntries.add(runEntry);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        runEntry.getProcess().waitFor();

                    } catch (InterruptedException e) {
                    }
                    runEntry.setStatus(RunEntry.Status.FINISHED);
                    runEntry.setExitValue(runEntry.getProcess().exitValue());
                }
            }).start();

            LOGGER.error("added RUNENTRY");

        } catch (IOException e) {
            throw new ManagerException(e);
        }
    }

    private void loadMaven(Path directory, ManagerConfig.Module module) throws ManagerException {
        try {
        MetaserviceDescriptor.ModuleInfo moduleInfo =module.getMetaserviceDescriptor().getModuleInfo();
        ProcessBuilder pb =
                new ProcessBuilder(
                        "mvn",
                        "org.apache.maven.plugins:maven-dependency-plugin:2.7:get",
                        "-DgroupId=" + moduleInfo.getGroupId(),
                        "-DartifactId=" + moduleInfo.getArtifactId(),
                        "-Dversion=" + moduleInfo.getVersion(),
                        "-U")
                        .directory(directory.toFile())
                        .inheritIO();
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
                    .directory(directory.toFile())
                    .inheritIO();
            LOGGER.debug(pb.command().toString());
            pb.start().waitFor();
        } catch (InterruptedException | IOException e) {
            throw new ManagerException(e);
        }
    }

    public void runFrontend() throws ManagerException {
        ProcessBuilder pb = new ProcessBuilder(RUN_EXECUTABLE);

        Map<String, String> env = pb.environment();
        env.put(MAINCLASS, "org.metaservice.frontend.rest.RestFrontend");
        env.put(GROUP_ID, "org.metaservice");
        env.put(ARTIFACT_ID,"metaservice-frontend-rest");
        env.put(VERSION,"0.1");
        try {
            Process p = pb.start();
        } catch (IOException e) {
            throw new ManagerException(e);
        }
    }

    public void runCrawlerAndWaitForFinish(String id, String group_id, String artifact_id, String version) throws ManagerException {
        try{
            System.out.println("Starting Crawler " + id);
            ProcessBuilder pb = new ProcessBuilder(RUN_EXECUTABLE,id);
            Map<String, String> env = pb.environment();
            env.put(MAINCLASS, "org.metaservice.core.crawler.CrawlerRunner");
            env.put(GROUP_ID, group_id);
            env.put(ARTIFACT_ID, artifact_id);
            env.put(VERSION,version);

            Process p = pb.start();
            ProcessUtil.debug(p);

            System.out.println("Finished Crawler " + id);
        } catch (IOException | InterruptedException e) {
            throw new ManagerException(e);
        }
    }
}

