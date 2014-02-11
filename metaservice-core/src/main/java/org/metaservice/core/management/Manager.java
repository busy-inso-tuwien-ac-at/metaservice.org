package org.metaservice.core.management;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.archive.ArchiveException;
import org.metaservice.api.archive.ArchiveParameters;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.rdf.vocabulary.METASERVICE;
import org.metaservice.core.Config;
import org.metaservice.core.archive.ArchiveParametersImpl;
import org.metaservice.core.archive.GitArchive;
import org.metaservice.api.archive.Archive;
import org.metaservice.core.injection.providers.JAXBMetaserviceDescriptorProvider;
import org.metaservice.core.jms.JMSProducerUtil;
import org.metaservice.core.management.shell.DescriptorHelper;
import org.openrdf.OpenRDFException;
import org.openrdf.model.*;
import org.openrdf.query.*;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jms.*;
import javax.xml.bind.JAXB;
import java.io.File;
import java.io.IOException;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ilo on 05.01.14.
 */
public class Manager {
    private static Logger LOGGER = LoggerFactory.getLogger(Manager.class);

    private final String managerConfigFileName= "managerconfig.xml";
    private ManagerConfig managerConfig;
    private final Config config;
    private final RepositoryConnection repositoryConnection;
    private final ValueFactory valueFactory;
    private final JMSProducerUtil jmsProducerUtil;
    private Scheduler scheduler;

    @Inject
    public Manager(
            RepositoryConnection repositoryConnection,
            ValueFactory valueFactory,
            JMSProducerUtil jmsProducerUtil) throws ManagerException {
        this.jmsProducerUtil = jmsProducerUtil;
        File managerConfigFile = new File(managerConfigFileName);
        if(managerConfigFile.exists()){
            managerConfig = JAXB.unmarshal(new File(managerConfigFileName),ManagerConfig.class);
        }else{
            managerConfig = new ManagerConfig();
        }

        this.config = managerConfig.getConfig();
        this.repositoryConnection = repositoryConnection;
        this.valueFactory = valueFactory;
        init();
    }

    public void init() throws ManagerException {
        try {
            SchedulerFactory schedulerFactory = new org.quartz.impl.StdSchedulerFactory();

            scheduler = schedulerFactory.getScheduler();
            scheduler.start();

            for(ManagerConfig.Module module : managerConfig.getInstalledModules()){
                scheduleCrawlers(module.getMetaserviceDescriptor());
            }

        } catch (SchedulerException e) {
            throw new ManagerException(e);
        }
    }

    public Archive getArchiveWithName(@NotNull String name) throws ArchiveException {
        ArchiveParameters archiveParameters = new ArchiveParametersImpl(null,null);
        return new GitArchive(archiveParameters);
    }

    public void loadAllDataFromArchive(@NotNull final String name) throws ManagerException {
        try {
            jmsProducerUtil.executeTopicProducerTask("VirtualTopic.Create", new JMSProducerUtil.ProducerTask<ArchiveException>()  {
                @Override
                public void execute(Session session, MessageProducer producer) throws JMSException,ArchiveException  {
                    Archive gitArchive = getArchiveWithName(name);
                    for(String commitTime : gitArchive.getTimes()){
                        for(String path : gitArchive.getChangedPaths(commitTime)){
                            LOGGER.info("Sending " + commitTime +" s " + path);
                            ArchiveAddress archiveAddress = new ArchiveAddress(gitArchive.getSourceBaseUri(),commitTime,path);
                            ObjectMessage message = session.createObjectMessage();
                            message.setObject(archiveAddress);
                            producer.send(message);
                        }
                    }
                }
            });
        } catch (JMSException|ArchiveException e) {
            throw new ManagerException(e);
        }
    }

    public void runProvider(ManagerConfig.Module module,MetaserviceDescriptor.ProviderDescriptor providerDescriptor){
        ProcessBuilder pb = new ProcessBuilder("/opt/metaservice/run.sh",providerDescriptor.getId());

        MetaserviceDescriptor.ModuleInfo moduleInfo =module.metaserviceDescriptor.getModuleInfo();
        Map<String, String> env = pb.environment();
        env.put("MAINCLASS", "org.metaservice.core.jms.JMSProviderRunner");
        env.put("GROUP_ID", moduleInfo.getGroupId());
        env.put("ARTIFACT_ID",moduleInfo.getArtifactId());
        env.put("VERSION", moduleInfo.getVersion());
        try {
            Process p = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void runPostProcessor(ManagerConfig.Module module,MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor){
        ProcessBuilder pb = new ProcessBuilder("/opt/metaservice/run.sh",postProcessorDescriptor.getId());

        MetaserviceDescriptor.ModuleInfo moduleInfo =module.metaserviceDescriptor.getModuleInfo();
        Map<String, String> env = pb.environment();
        env.put("MAINCLASS", "org.metaservice.core.jms.JMSPostProcessorRunner");
        env.put("GROUP_ID", moduleInfo.getGroupId());
        env.put("ARTIFACT_ID",moduleInfo.getArtifactId());
        env.put("VERSION",moduleInfo.getVersion());
        try {
            Process p = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uninstall(@NotNull ManagerConfig.Module module,boolean removeData) throws ManagerException {
        if(!managerConfig.getInstalledModules().contains(module)){
            throw new ManagerException("Module currently not installed");
        }

        MetaserviceDescriptor descriptor = module.getMetaserviceDescriptor();
        uninstallOntologies(descriptor);
       // uninstallTemplates(descriptor);
        unscheduleCrawlers(descriptor);
        if(removeData){
            removeData(descriptor);
        }

    }

    private void removeData(MetaserviceDescriptor descriptor) {

    }

    private void unscheduleCrawlers(MetaserviceDescriptor descriptor) throws ManagerException {
        for(MetaserviceDescriptor.RepositoryDescriptor repositoryDescriptor : descriptor.getRepositoryList()){
            try {
                scheduler.getTriggersOfJob(new JobKey(repositoryDescriptor.getId()));
            } catch (SchedulerException e) {
                throw new ManagerException(e);
            }
        }
    }

    public void install(@NotNull File f) throws ManagerException {
        LOGGER.info("Installing {}",f.getName());
        Path p = Paths.get(f.toURI());
        try (FileSystem zipFs = FileSystems.newFileSystem(p,Thread.currentThread().getContextClassLoader())){
            MetaserviceDescriptor descriptor = new JAXBMetaserviceDescriptorProvider(Files.newInputStream(zipFs.getPath("/metaservice.xml"))).get();
            ManagerConfig.Module updateFrom = isUpdate(descriptor);
            if(updateFrom != null){
                refreshProviders(descriptor);
                refreshPostProcessors(descriptor);
            }

            // installTemplates(zipFs, descriptor);
            installOntologies(zipFs,descriptor);
            scheduleCrawlers(descriptor);

            ManagerConfig.Module module = new ManagerConfig.Module();
            module.setLocation(f);
            module.setMetaserviceDescriptor(descriptor);
            if(managerConfig.getInstalledModules() == null){
                managerConfig.setInstalledModules(new ArrayList<ManagerConfig.Module>());
            }
            managerConfig.getInstalledModules().add(module);
            saveConfig();
        } catch (IOException e) {
            LOGGER.error("Could not install {}", f.getName(),e);
            throw new ManagerException("Could not install "+ f.getName(),e);
        }
        //run and monitor providers and postprocessors
        //if upgrade trigger refresh of outdated
    }

    private void refreshResource(@NotNull final String s) throws ManagerException {
        try {
            jmsProducerUtil.executeTopicProducerTask("VirtualTopic.Refresh",new JMSProducerUtil.ProducerTask<JMSException>() {
                @Override
                public void execute(Session session, MessageProducer producer) throws JMSException {
                    TextMessage message  = session.createTextMessage(s);
                    producer.send(message);
                }
            });
        } catch (JMSException e) {
            throw new ManagerException(e);
        }
    }

    private void refreshPostProcessors(@NotNull MetaserviceDescriptor descriptor) throws ManagerException {
        for(MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor : descriptor.getPostProcessorList()){
            refreshPostProcessor(descriptor, postProcessorDescriptor);
        }
    }

    private void refreshPostProcessor(MetaserviceDescriptor descriptor, MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor) throws ManagerException {
        try {
            final ArrayList<String> toRefresh = new ArrayList<>();
            TupleQuery query = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,"SELECT DISTINCT ?resource { GRAPH ?metadata {?resource ?y ?z}. ?metadata <"+METASERVICE.GENERATOR+"> ?id}");
            query.setBinding("id", valueFactory.createLiteral(DescriptorHelper.getStringFromPostProcessor(descriptor.getModuleInfo(), postProcessorDescriptor)));
            TupleQueryResult result = query.evaluate();
            while(result.hasNext()){
                BindingSet bs = result.next();
                Value value = bs.getBinding("resource").getValue();
                if(value instanceof Resource)
                    toRefresh.add(value.stringValue());
            }
            jmsProducerUtil.executeTopicProducerTask("VirtualTopic.Refresh",new JMSProducerUtil.ProducerTask<OpenRDFException>() {
                @Override
                public void execute(Session session, MessageProducer producer) throws JMSException, OpenRDFException {
                    for(String s : toRefresh){
                        TextMessage message  = session.createTextMessage(s);
                        producer.send(message);
                    }
                }
            });
        } catch (JMSException | OpenRDFException e) {
            throw new ManagerException("Could not refresh " + postProcessorDescriptor.getId(),e);
        }
    }

    private void refreshProvider(@NotNull MetaserviceDescriptor descriptor, MetaserviceDescriptor.ProviderDescriptor providerDescriptor) throws ManagerException {
        try {
            final ArrayList<String> toRefresh = new ArrayList<>();
            TupleQuery query = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,"SELECT DISTINCT ?resource { GRAPH ?metadata {?resource ?y ?z}. ?metadata <"+METASERVICE.GENERATOR+"> ?id}");
            query.setBinding("id", valueFactory.createLiteral(DescriptorHelper.getStringFromProvider(descriptor.getModuleInfo(), providerDescriptor)));
            TupleQueryResult result = query.evaluate();
            while(result.hasNext()){
                BindingSet bs = result.next();
                Value value = bs.getBinding("resource").getValue();
                if(value instanceof Resource)
                    toRefresh.add(value.stringValue());
            }

            jmsProducerUtil.executeTopicProducerTask("VirtualTopic.Refresh",new JMSProducerUtil.ProducerTask<OpenRDFException>() {
                @Override
                public void execute(Session session, MessageProducer producer) throws JMSException, OpenRDFException {
                    for(String s : toRefresh){
                        TextMessage message  = session.createTextMessage(s);
                        producer.send(message);
                    }
                }
            });
        } catch (JMSException | OpenRDFException e) {
            throw new ManagerException("Could not refresh " + providerDescriptor.getId(),e);
        }
    }
    private void refreshProviders(@NotNull MetaserviceDescriptor descriptor) throws ManagerException {
        for(MetaserviceDescriptor.ProviderDescriptor providerDescriptor : descriptor.getProviderList()){
            refreshProvider(descriptor,providerDescriptor);
        }
    }

    private ManagerConfig.Module isUpdate(@NotNull MetaserviceDescriptor descriptor) {
        return null;
    }

    @DisallowConcurrentExecution
    public static class CrawlJob implements Job{

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            try {
                JobDataMap jobDataMap = context.getMergedJobDataMap();
                String id = jobDataMap.getString("REPOSITORY");
                System.out.println("Starting Crawler " + id);
                ProcessBuilder pb = new ProcessBuilder("/opt/metaservice/run.sh",id);
                Map<String, String> env = pb.environment();
                env.put("MAINCLASS", "org.metaservice.core.crawler.CrawlerRunner");
                env.put("GROUP_ID", context.getMergedJobDataMap().getString("GROUP_ID"));
                env.put("ARTIFACT_ID", context.getMergedJobDataMap().getString("ARTIFACT_ID"));
                env.put("VERSION",context.getMergedJobDataMap().getString("VERSION"));
                try {
                    Process p = pb.start();
                    p.waitFor();
                    System.out.println("Finished Crawler " + id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void scheduleCrawlers(@NotNull MetaserviceDescriptor descriptor) throws ManagerException {
        MetaserviceDescriptor.ModuleInfo moduleInfo = descriptor.getModuleInfo();
        for(MetaserviceDescriptor.RepositoryDescriptor repositoryDescriptor : descriptor.getRepositoryList()){
            JobDetail job = JobBuilder
                    .newJob()
                    .ofType(CrawlJob.class)
                    .withIdentity(repositoryDescriptor.getId())
                    .usingJobData("GROUP_ID",moduleInfo.getGroupId())
                    .usingJobData("ARTIFACT_ID",moduleInfo.getArtifactId())
                    .usingJobData("VERSION",moduleInfo.getVersion())
                    .usingJobData("REPOSITORY",repositoryDescriptor.getId())
                    .build(); //todo namespace it
            Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .startNow()
                    .forJob(job)
                    .withSchedule(SimpleScheduleBuilder
                            .simpleSchedule()
                            .repeatForever()
                            .withIntervalInMinutes(1)
                    )
                    .build();
            try {
                scheduler.scheduleJob(job,trigger);
            } catch (SchedulerException e) {
                throw new ManagerException(e);
            }
        }
    }

    private void uninstallOntologies(@NotNull MetaserviceDescriptor metaserviceDescriptor){

    }

    private void uninstallTemplates(@NotNull MetaserviceDescriptor descriptor) throws ManagerException {
        for(MetaserviceDescriptor.TemplateDescriptor templateDescriptor : descriptor.getTemplateList()){
            Path to =  Paths.get(config.getHttpdDataDirectory() + templateDescriptor.getName());
            LOGGER.info("Deleting {}",to);
            try{
                Files.delete(to);
                Statement s = getTemplateStatement(templateDescriptor);
                LOGGER.info("Removing Statement {} from the database", s);
                repositoryConnection.remove(s);
            } catch (RepositoryException e) {
                throw new ManagerException("Could not remove statement from the database",e);
            } catch (IOException e) {
                throw new ManagerException("Could not delete Template",e);
            }
        }
    }

    private void installOntologies(@NotNull FileSystem zipFs,@NotNull MetaserviceDescriptor descriptor) {

    }

    private void installTemplates(@NotNull FileSystem zipFs,@NotNull MetaserviceDescriptor descriptor) throws IOException, ManagerException {
        //todo namespace the paths of templates
        for(MetaserviceDescriptor.TemplateDescriptor templateDescriptor : descriptor.getTemplateList()){
            Path from = zipFs.getPath("/templates/" + templateDescriptor.getName());
            Path to = getTemplatePath(templateDescriptor);
            LOGGER.info("Copying {} to {}",from,to);
            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);

            Statement s = getTemplateStatement(templateDescriptor);
            try{
                LOGGER.info("Adding Statement {} to the database", s);
                repositoryConnection.add(s);
            } catch (RepositoryException e) {
                throw new ManagerException("Could not add the Statement " + s.toString() +" to the database",e);
            }
        }
    }

    public @NotNull Path getTemplatePath(@NotNull MetaserviceDescriptor.TemplateDescriptor templateDescriptor){
        return  Paths.get(config.getHttpdDataDirectory() + templateDescriptor.getName());
    }

    public @NotNull Statement getTemplateStatement(@NotNull MetaserviceDescriptor.TemplateDescriptor templateDescriptor){
        URI subject= valueFactory.createURI(templateDescriptor.getAppliesTo());
        Literal filename = valueFactory.createLiteral(templateDescriptor.getName());
        return valueFactory.createStatement(subject, METASERVICE.VIEW,filename);
    }


    public ManagerConfig getManagerConfig() {
        return managerConfig;
    }

    public void saveConfig(){
        try {
            File file =new File(managerConfigFileName);
            File tmpFile = new File(managerConfigFileName+".tmp");
            File oldFile = new File(managerConfigFileName+".old");
            JAXB.marshal(managerConfig,tmpFile);
            if(file.exists()){
                Files.move(file.toPath(),oldFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
            }
            Files.move(tmpFile.toPath(), file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void add(File fileToAdd, boolean override) {
        LOGGER.info("Starting adding of {}",fileToAdd.getName());
        Path p = Paths.get(fileToAdd.toURI());
        try (FileSystem zipfs = FileSystems.newFileSystem(p,Thread.currentThread().getContextClassLoader())){
            MetaserviceDescriptor descriptor = new JAXBMetaserviceDescriptorProvider(Files.newInputStream(zipfs.getPath("/metaservice.xml"))).get();
            ManagerConfig.Module module = new ManagerConfig.Module();
            MetaserviceDescriptor.ModuleInfo moduleInfo = descriptor.getModuleInfo();
            Path target = Paths.get("./modules", moduleInfo.getGroupId(), moduleInfo.getArtifactId() + "-" + moduleInfo.getVersion() + ".jar");
            Files.createDirectories(target.getParent());
            if(Files.exists(target) && !override){
                LOGGER.info("Already installed - please use override");
                return;
            }
            Files.copy(fileToAdd.toPath(),target,StandardCopyOption.REPLACE_EXISTING);
            module.setLocation(target.toFile());
            module.setMetaserviceDescriptor(descriptor);
            if(managerConfig.getAvailableModules() == null){
                managerConfig.setAvailableModules(new ArrayList<ManagerConfig.Module>());
            }
            managerConfig.getAvailableModules().add(module);
            saveConfig();
        } catch (IOException e) {
            LOGGER.info("Could not add {}", fileToAdd.getName(),e);
        }
    }

    public void runFrontend() {

    }
}