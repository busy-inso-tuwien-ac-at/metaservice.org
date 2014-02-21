package org.metaservice.manager;

import com.google.inject.Singleton;
import org.jetbrains.annotations.Nullable;
import org.metaservice.core.config.ManagerConfig;
import org.metaservice.core.injection.ManagerConfigProvider;
import org.metaservice.core.descriptor.DescriptorHelper;
import org.jetbrains.annotations.NotNull;
import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.archive.ArchiveException;
import org.metaservice.api.archive.ArchiveParameters;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.rdf.vocabulary.METASERVICE;
import org.metaservice.core.config.Config;
import org.metaservice.core.archive.ArchiveParametersImpl;
import org.metaservice.core.archive.GitArchive;
import org.metaservice.api.archive.Archive;
import org.metaservice.core.injection.providers.JAXBMetaserviceDescriptorProvider;
import org.metaservice.core.jms.JMSMessageConverter;
import org.metaservice.core.jms.JMSUtil;
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
import java.util.*;

/**
 * Created by ilo on 05.01.14.
 */
@Singleton
public class Manager {
    private static Logger LOGGER = LoggerFactory.getLogger(Manager.class);

    private ManagerConfig managerConfig;
    private final Config config;
    private final RepositoryConnection repositoryConnection;
    private final ValueFactory valueFactory;
    private final JMSUtil jmsUtil;
    private final RunManager runManager;
    private final Scheduler scheduler;
    private final Map<String, Map<String, Object>> currentActiveMQStatisitics;
    private final MavenManager mavenManager;

    private JMSUtil.ShutDownHandler activeMQStatisticsShutdownHandler;
    @Inject
    public Manager(
            RepositoryConnection repositoryConnection,
            ValueFactory valueFactory,
            JMSUtil jmsUtil,
            Config config,
            ManagerConfig managerConfig,
            RunManager runManager,
            Scheduler scheduler,
            MavenManager mavenManager
    ) throws ManagerException {
        this.jmsUtil = jmsUtil;
        this.runManager = runManager;
        this.scheduler = scheduler;
        this.managerConfig = managerConfig;
        this.mavenManager = mavenManager;
        //todo dirty circular dependency hack
        this.mavenManager.setManager(this);
        this.config = managerConfig.getConfig();

        this.repositoryConnection = repositoryConnection;
        this.valueFactory = valueFactory;
        this.currentActiveMQStatisitics = Collections.synchronizedMap(new HashMap<String, Map<String, Object>>());
        init();
    }

    private static final String STATISTICSQUEUE = "metaservice.manager.statistics";

    public void removeModule(ManagerConfig.Module availableModule) {
        boolean delete = availableModule.getLocation().delete();
        if(!delete)
        {
            LOGGER.warn("Attention could not delete {}", availableModule.getLocation());
        }
        if( availableModule.getLocation().exists() )        {
            LOGGER.error("ERROR deleting {}" ,availableModule.getLocation());
        }
        managerConfig.getAvailableModules().remove(availableModule);
        saveConfig();
    }

    public static class ActiveMqstatisticsRequestJob implements Job{
        private final JMSUtil jmsUtil;

        @Inject
        public ActiveMqstatisticsRequestJob(JMSUtil jmsUtil) {
            this.jmsUtil = jmsUtil;
        }

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            try {
                jmsUtil.executeProducerTask(JMSUtil.Type.QUEUE,"ActiveMQ.Statistics.Destination.>", new JMSUtil.ProducerTask<JMSException>() {
                    @Override
                    public void execute(Session session, MessageProducer producer) throws JMSException {
                        TextMessage message = session.createTextMessage();
                        message.setJMSReplyTo(session.createQueue(STATISTICSQUEUE));
                        producer.send(message);
                    }
                });
            } catch (JMSException e) {
                throw new JobExecutionException(e);
            }
        }
    }

    public void init() throws ManagerException {
        LoggerFactory.getLogger("org.apache.activemq.transport.failover.FailoverTransport");
        //start retrieval of activemq statistics
        JobDetail job = JobBuilder
                .newJob()
                .ofType(ActiveMqstatisticsRequestJob.class)
                .withIdentity("ACTIVEMQ_STATISTICS")
                .build();
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .startNow()
                .forJob(job)
                .withSchedule(SimpleScheduleBuilder
                        .simpleSchedule()
                        .repeatForever()
                        .withIntervalInSeconds(5)
                )
                .build();
        try {
            scheduler.scheduleJob(job,trigger);
            try {
                activeMQStatisticsShutdownHandler = jmsUtil.runListener(new JMSUtil.ListenerBean() {
                    @Override
                    public String getName() {
                        return STATISTICSQUEUE;
                    }

                    @Override
                    public JMSUtil.Type getType() {
                        return JMSUtil.Type.QUEUE;
                    }

                    @Override
                    public void onMessage(Message message) {
                        if(message instanceof MapMessage){
                            try {
                                String destinationName = ((MapMessage) message).getString("destinationName");
                                currentActiveMQStatisitics.put(destinationName, JMSMessageConverter.getMap((MapMessage) message));
                            } catch (JMSException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            } catch (JMSException e) {
                e.printStackTrace();
            }


        } catch (SchedulerException e) {
            throw new ManagerException(e);
        }


        //start crawling of installed modules
        for(ManagerConfig.Module module : managerConfig.getInstalledModules()){
            scheduleCrawlers(module.getMetaserviceDescriptor());
        }
    }




    public void shutdown() throws ManagerException{
        try {
            activeMQStatisticsShutdownHandler.shutdown();
            runManager.shutdown();
            scheduler.shutdown();
        } catch (SchedulerException e) {
            throw new ManagerException(e);
        }
    }

    public Archive getArchiveForRepository(@NotNull MetaserviceDescriptor.RepositoryDescriptor repositoryDescriptor) throws ArchiveException {
        ArchiveParameters archiveParameters = new ArchiveParametersImpl(null,null);
        return new GitArchive(archiveParameters);
    }

    public void loadAllDataFromArchive(@NotNull final String name) throws ManagerException {
        try {
            MetaserviceDescriptor.RepositoryDescriptor repositoryDescriptor = null;
            for(ManagerConfig.Module module :managerConfig.getInstalledModules()){
                for(MetaserviceDescriptor.RepositoryDescriptor t : module.getMetaserviceDescriptor().getRepositoryList()){
                    if(t.getId().equals(name)){
                        repositoryDescriptor = t;
                        break;
                    }
                }
            }

            if(repositoryDescriptor == null){
                LOGGER.error("NO SUCH REPOSITORY {}", name);
            }

            final MetaserviceDescriptor.RepositoryDescriptor selectedRepositoryDescriptor = repositoryDescriptor;

            jmsUtil.executeProducerTask(JMSUtil.Type.TOPIC,"VirtualTopic.Create", new JMSUtil.ProducerTask<ArchiveException>() {
                @Override
                public void execute(Session session, MessageProducer producer) throws JMSException, ArchiveException {
                    Archive gitArchive = getArchiveForRepository(selectedRepositoryDescriptor);
                    for (String commitTime : gitArchive.getTimes()) {
                        for (String path : gitArchive.getChangedPaths(commitTime)) {
                            LOGGER.info("Sending " + commitTime + " s " + path);
                            ArchiveAddress archiveAddress = new ArchiveAddress(gitArchive.getSourceBaseUri(), commitTime, path);
                            archiveAddress.setParameters(selectedRepositoryDescriptor.getProperties());
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


    public void uninstall(@NotNull ManagerConfig.Module module,boolean removeData) throws ManagerException {
        if(!managerConfig.getInstalledModules().contains(module)){
            throw new ManagerException("Module currently not installed");
        }

        runManager.shutdown(module, managerConfig.getInstalledModules());
        MetaserviceDescriptor descriptor = module.getMetaserviceDescriptor();
        uninstallOntologies(descriptor);
        uninstallTemplates(descriptor);
        unscheduleCrawlers(descriptor);
        managerConfig.getInstalledModules().remove(module);
        if(removeData){
            removeData(descriptor);
        }
    }

    private void removeData(MetaserviceDescriptor descriptor) throws ManagerException {
        for(MetaserviceDescriptor.ProviderDescriptor providerDescriptor: descriptor.getProviderList()){
            removeProviderData(descriptor,providerDescriptor);
        }
        for(MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor : descriptor.getPostProcessorList()){
            removePostProcessorData(descriptor,postProcessorDescriptor);
        }
    }

    private void removePostProcessorData(MetaserviceDescriptor descriptor, @NotNull MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor) throws ManagerException {
        removeDataFromGenerator(DescriptorHelper.getStringFromPostProcessor(descriptor.getModuleInfo(), postProcessorDescriptor));

    }

    private void removeProviderData(MetaserviceDescriptor descriptor, @NotNull MetaserviceDescriptor.ProviderDescriptor providerDescriptor) throws ManagerException {
        removeDataFromGenerator(DescriptorHelper.getStringFromProvider(descriptor.getModuleInfo(),providerDescriptor));
    }


    private void removeDataFromGenerator(String generator) throws ManagerException {
        try {
            TupleQuery repoSelect = this.repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL, "SELECT DISTINCT ?metadata { ?metadata a <" + METASERVICE.METADATA + ">;  <" + METASERVICE.GENERATOR + "> ?generator }");
            repoSelect.setBinding("generator",valueFactory.createLiteral(generator));
            TupleQueryResult queryResult =repoSelect.evaluate();
            ArrayList<URI> uris = new ArrayList<>();
            while (queryResult.hasNext()){
                BindingSet set = queryResult.next();
                URI oldMetadata = (URI) set.getBinding("metadata").getValue();
                if(oldMetadata != null) //safety check -> null deletes whole repository
                    uris.add(oldMetadata);
            }
            if(uris.size() > 0)    //safety check -> empty deletes whole repository
            {
                LOGGER.info("Clearing {} contexts from generator {}",uris.size(),generator);
                repositoryConnection.clear();
            }
        } catch (RepositoryException | MalformedQueryException | QueryEvaluationException e) {
            throw new ManagerException(e);
        }
    }

    private void unscheduleCrawlers(MetaserviceDescriptor descriptor) throws ManagerException {
        for(MetaserviceDescriptor.RepositoryDescriptor repositoryDescriptor : descriptor.getRepositoryList()){
            try {
                JobKey jobKey = new JobKey(repositoryDescriptor.getId());
                scheduler.deleteJob(jobKey);
               /* for(Trigger trigger :  scheduler.getTriggersOfJob(jobKey)){

                }*/

            } catch (SchedulerException e) {
                throw new ManagerException(e);
            }
        }
    }

    public void install(@NotNull ManagerConfig.Module m) throws ManagerException {
        LOGGER.info("Installing {}",m.getLocation().getName());
        Path p = Paths.get(m.getLocation().toURI());
        try (FileSystem zipFs = FileSystems.newFileSystem(p,Thread.currentThread().getContextClassLoader())){
            MetaserviceDescriptor descriptor = new JAXBMetaserviceDescriptorProvider(Files.newInputStream(zipFs.getPath("/metaservice.xml"))).get();
            ManagerConfig.Module updateFrom = isUpdate(descriptor);
            if(updateFrom != null){
                uninstall(updateFrom, false);
                refreshProviders(updateFrom.getMetaserviceDescriptor());
                refreshPostProcessors(updateFrom.getMetaserviceDescriptor());
            }
            installTemplates(zipFs, descriptor);
            installOntologies(zipFs,descriptor);
            scheduleCrawlers(descriptor);
            managerConfig.getInstalledModules().add(m);
            saveConfig();
        } catch (IOException e) {
            LOGGER.error("Could not install {}", m.getLocation().getName(),e);
            throw new ManagerException("Could not install "+ m.getLocation().getName(),e);
        }
        //run and monitor providers and postprocessors
        //if upgrade trigger refresh of outdated
    }

    private void refreshResource(@NotNull final String s) throws ManagerException {
        try {
            jmsUtil.executeProducerTask(JMSUtil.Type.TOPIC,"VirtualTopic.Refresh", new JMSUtil.ProducerTask<JMSException>() {
                @Override
                public void execute(Session session, MessageProducer producer) throws JMSException {
                    TextMessage message = session.createTextMessage(s);
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
            jmsUtil.executeProducerTask(JMSUtil.Type.TOPIC,"VirtualTopic.Refresh", new JMSUtil.ProducerTask<OpenRDFException>() {
                @Override
                public void execute(Session session, MessageProducer producer) throws JMSException, OpenRDFException {
                    for (String s : toRefresh) {
                        TextMessage message = session.createTextMessage(s);
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

            jmsUtil.executeProducerTask(JMSUtil.Type.TOPIC,"VirtualTopic.Refresh", new JMSUtil.ProducerTask<OpenRDFException>() {
                @Override
                public void execute(Session session, MessageProducer producer) throws JMSException, OpenRDFException {
                    for (String s : toRefresh) {
                        TextMessage message = session.createTextMessage(s);
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

    @Nullable
    private ManagerConfig.Module isUpdate(@NotNull MetaserviceDescriptor moduleDescriptor) {
       MetaserviceDescriptor.ModuleInfo i1 = moduleDescriptor.getModuleInfo();
       for(ManagerConfig.Module installedModule : managerConfig.getInstalledModules()){
           MetaserviceDescriptor.ModuleInfo i2 = installedModule.getMetaserviceDescriptor().getModuleInfo();
            if(i1.getArtifactId().equals(i2.getArtifactId()) && i1.getGroupId().equals(i2.getGroupId())){
                return installedModule;
            }
        }
        return null;
    }

    public RunManager getRunManager() {
        return runManager;
    }

    public Map<String, Map<String, Object>> getCurrentActiveMQStatisitics() {
        return currentActiveMQStatisitics;
    }

    @DisallowConcurrentExecution
    public static class CrawlJob implements Job{

        @Inject
        private RunManager runManager;

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap jobDataMap = context.getMergedJobDataMap();
            try {
                runManager.runCrawlerAndWaitForFinish(
                        jobDataMap.getString("REPOSITORY"),
                        jobDataMap.getString("GROUP_ID"),
                        jobDataMap.getString("ARTIFACT_ID"),
                        jobDataMap.getString("VERSION"));
            } catch (ManagerException e) {
                LOGGER.error("could not crawler job",e);
                throw new JobExecutionException(e);
            }catch (Exception e){
                LOGGER.error("WEIRD",e);
            }
        }
    }
    private void scheduleCrawlers(@NotNull MetaserviceDescriptor descriptor) throws ManagerException {
        MetaserviceDescriptor.ModuleInfo moduleInfo = descriptor.getModuleInfo();
        for(MetaserviceDescriptor.RepositoryDescriptor repositoryDescriptor : descriptor.getRepositoryList()){
            if(!repositoryDescriptor.getActive()){
                LOGGER.debug("{} is disabled, not scheduling crawler",repositoryDescriptor.getId());
                continue;
            }
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
                            .withIntervalInHours(12)
                    )
                    .build();
            LOGGER.info("{} crawler scheduled", repositoryDescriptor.getId());
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
            File file =new File(ManagerConfigProvider.MANAGERCONFIG_XML);
            File tmpFile = new File(ManagerConfigProvider.MANAGERCONFIG_XML +".tmp");
            File oldFile = new File(ManagerConfigProvider.MANAGERCONFIG_XML +".old");
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
            managerConfig.getAvailableModules().add(module);
            saveConfig();
        } catch (IOException e) {
            LOGGER.info("Could not add {}", fileToAdd.getName(),e);
        }
    }

    public MavenManager getMavenManager() {
        return mavenManager;
    }
}