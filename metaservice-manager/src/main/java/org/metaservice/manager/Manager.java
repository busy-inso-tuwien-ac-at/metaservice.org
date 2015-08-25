package org.metaservice.manager;

import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import org.jetbrains.annotations.Nullable;
import org.metaservice.api.messaging.PostProcessingTask;
import org.metaservice.api.messaging.descriptors.DescriptorHelper;
import org.metaservice.api.rdf.vocabulary.ADMSSW;
import org.metaservice.api.messaging.config.ManagerConfig;
import org.metaservice.core.injection.ManagerConfigProvider;
import org.jetbrains.annotations.NotNull;
import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.archive.ArchiveException;
import org.metaservice.api.archive.ArchiveParameters;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.rdf.vocabulary.METASERVICE;
import org.metaservice.api.messaging.Config;
import org.metaservice.core.archive.ArchiveParametersImpl;
import org.metaservice.core.archive.GitArchive;
import org.metaservice.api.archive.Archive;
import org.metaservice.core.injection.providers.JAXBMetaserviceDescriptorProvider;
import org.metaservice.api.messaging.MessageHandler;
import org.metaservice.api.messaging.MessagingException;
import org.metaservice.api.messaging.statistics.QueueStatistics;
import org.metaservice.manager.bigdata.FastRangeCountRequestBuilder;
import org.metaservice.manager.bigdata.MutationResult;
import org.openrdf.OpenRDFException;
import org.openrdf.model.*;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.query.*;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXB;
import java.io.File;
import java.io.IOException;

import java.nio.file.*;
import java.util.*;
import java.util.Calendar;

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
    private final RunManager runManager;
    private final Scheduler scheduler;
    private final MavenManager mavenManager;

    private MessageHandler messageHandler;
    private final DescriptorHelper descriptorHelper;

    @Inject
    public Manager(
            RepositoryConnection repositoryConnection,
            ValueFactory valueFactory,
            Config config,
            ManagerConfig managerConfig,
            RunManager runManager,
            Scheduler scheduler,
            MavenManager mavenManager,
            MessageHandler messageHandler,
            DescriptorHelper descriptorHelper
    ) throws ManagerException {
        this.runManager = runManager;
        this.scheduler = scheduler;
        this.managerConfig = managerConfig;
        this.mavenManager = mavenManager;
        this.messageHandler = messageHandler;
        this.descriptorHelper = descriptorHelper;
        //todo dirty circular dependency hack
        this.mavenManager.setManager(this);
        this.config = managerConfig.getConfig();

        this.repositoryConnection = repositoryConnection;
        this.valueFactory = valueFactory;
        init();
    }

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

    public DescriptorHelper getDescriptorHelper() {
        return descriptorHelper;
    }

    public void postProcessAllByClass(URI clazz) throws ManagerException {

        try {
            TupleQuery tupleQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,"SELECT *  WITH {SELECT DISTINCT ?x ?time { {{ GRAPH ?c { ?a ?b ?x.?x a <"+clazz+">.} } UNION { GRAPH ?c { ?x ?b ?a.?x a <"+clazz+">. }}} ?c <"+METASERVICE.DATA_TIME+"> ?time} } AS %data WHERE {INCLUDE %data} ORDER BY  ?time");
            TupleQueryResult result =tupleQuery.evaluate();
            ArrayList<PostProcessingTask> postProcessingTasks = new ArrayList<>();
            while (result.hasNext()){
                BindingSet bindings = result.next();
                URI uri = valueFactory.createURI(bindings.getBinding("x").getValue().stringValue());
                Date date = ((Literal)bindings.getBinding("time").getValue()).calendarValue().toGregorianCalendar().getTime();
                PostProcessingTask  task= new PostProcessingTask(uri,date);
                postProcessingTasks.add(task);
            }
            for(List<PostProcessingTask> tasks : Lists.partition(postProcessingTasks,1000)) {
                messageHandler.bulkSend(tasks);
            }
        } catch (RepositoryException | QueryEvaluationException | MessagingException | MalformedQueryException e) {
            throw new ManagerException(e);
        }
    }


    public void buildIterativeCache() throws ManagerException{
        try {
            TupleQuery tupleQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,
                    "SELECT DISTINCT ?time ?type ?path WHERE { ?c a <" + METASERVICE.OBSERVATION+ "> ;<"+METASERVICE.DATA_TIME+"> ?time; a ?type; <"+ METASERVICE.PATH+"> ?path } ORDER BY ?time");
            TupleQueryResult timeResult =tupleQuery.evaluate();


            class ResultTuple{
                Value time;
                Value type;
                Value path;
                URI selectedURI;
            }

            HashSet<URI> intermediateResults = new HashSet<>();
            ArrayList<ResultTuple> list = new ArrayList<>();

            while(timeResult.hasNext()) {
                BindingSet bs = timeResult.next();
                ResultTuple resultTuple = new ResultTuple();
                resultTuple.time = bs.getValue("time");
                resultTuple.type = bs.getValue("type");
                resultTuple.path = bs.getValue("path");
                resultTuple.selectedURI = valueFactory.createURI("http://metaservice.org/t/"+resultTuple.path.stringValue());
                intermediateResults.add(resultTuple.selectedURI);
                list.add(resultTuple);
            }

            for(URI uri :intermediateResults){
                LOGGER.info("Clearing old {}", uri);
                repositoryConnection.clear(uri);
            }
            for(ResultTuple tuple : list){
                if(!tuple.type.equals(METASERVICE.CONTINUOUS_OBSERVATION)) {
                    LOGGER.info("doing {} {} {} {}",tuple.selectedURI, tuple.time.stringValue(), tuple.path.stringValue(), tuple.type.stringValue());
                    if(tuple.type.equals(METASERVICE.ADD_OBSERVATION)){
                         Update update = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL,
                                 "INSERT {GRAPH ?c {?s ?p ?o}. ?c <rdf:type> <http://quargl>} WHERE {GRAPH ?x {?s ?p ?o}. ?x <"+METASERVICE.DATA_TIME+"> ?time; a ?type; <"+METASERVICE.PATH+"> ?path}");
                        update.setBinding("c",tuple.selectedURI);
                        update.setBinding("time",tuple.time);
                        update.setBinding("path",tuple.path);
                        update.setBinding("type",tuple.type);
                        update.execute();
                    }else if (tuple.type.equals(METASERVICE.REMOVE_OBSERVATION)){
                        Update update = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL,
                                "DELETE {GRAPH ?c {?s ?p ?o}} WHERE {GRAPH ?x {?s ?p ?o}. ?x <"+METASERVICE.DATA_TIME+"> ?time; a ?type; <"+METASERVICE.PATH+"> ?path}");
                        update.setBinding("c",tuple.selectedURI);
                        update.setBinding("time",tuple.time);
                        update.setBinding("path",tuple.path);
                        update.setBinding("type",tuple.type);
                        update.execute();
                    }
                }
            }

            for(URI intermediateResult:  intermediateResults){
                LOGGER.info("Merging {}",intermediateResult);
                Update update = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL,"INSERT {GRAPH <http://metaservice.org/totalresult>{?s ?p ?o}} WHERE {GRAPH ?c { ?s ?p ?o}}");
                update.setBinding("c",intermediateResult);
                update.execute();
            }

            LOGGER.info("Merging continuous");
            Update update = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL,"INSERT {GRAPH <http://metaservice.org/totalresult>{?s ?p ?o}} WHERE {GRAPH ?c {?s ?p ?o}. ?c <"+METASERVICE.LATEST+"> ?time}");
//todo bind latest time
            update.execute();
        } catch (RepositoryException | MalformedQueryException | QueryEvaluationException | UpdateExecutionException e) {
            throw new ManagerException(e);
        }
    }

    public void rebuildLatestCache() throws  ManagerException{
        try {
            URI latestGraphContext = valueFactory.createURI("http://metaservice.org/t/latest");
            repositoryConnection.clear(latestGraphContext);
            //todo replace
            Update  update  =repositoryConnection.prepareUpdate(QueryLanguage.SPARQL,
                " INSERT { GRAPH ?latestGraph { ?context <"+METASERVICE.LATEST+"> ?latesttime }}" +
                        "WHERE {{\n" +
                    "?context <"+METASERVICE.DATA_TIME+"> ?maxtime; <"+METASERVICE.GENERATOR+"> ?generator;\n" +
                    "<"+METASERVICE.AUTHORITIVE_SUBJECT +"> ?subject.\n" +
                    "{\n" +
                    "   SELECT (count(?c) as ?count) (max(?time) as ?maxtime) ?generator ?subject {" +
                    "       ?c a <"+METASERVICE.CONTINUOUS_OBSERVATION+">;" +
                    "       <" +METASERVICE.AUTHORITIVE_SUBJECT + "> ?subject;" +
                    "       <"+METASERVICE.GENERATOR+"> ?generator;" +
                    "       <" + METASERVICE.DATA_TIME+ "> ?time." +
                    "       FILTER( ?time <= ?latesttime)"+
                    "  } group by ?subject ?generator\n" +
                    "}\n" +
                    "FILTER NOT EXISTS {\n" +
                    "    GRAPH ?context { ?foo <"+METASERVICE.DUMMY+"> <"+METASERVICE.DUMMY+">}\n" +
                    "    FILTER(?count = 1)\n" +
                    "}\n" +
                    "}"+
                "}");
            update.setBinding("latestGraph",latestGraphContext);
            Calendar calendar = DatatypeConverter.parseDateTime( "2025-01-01T00:00:00Z");
            //System.err.println(DatatypeConverter.printDateTime(calendar));
            update.setBinding("latesttime",valueFactory.createLiteral(calendar.getTime()));
            update.execute();
        } catch (RepositoryException | MalformedQueryException | UpdateExecutionException e) {
            throw new ManagerException(e);
        }

    }

    public void postProcess(String postProcess) throws ManagerException{
        URI uri = valueFactory.createURI(postProcess);
        PostProcessingTask postProcessingTask =new PostProcessingTask(uri,new Date());
        try {
            messageHandler.send(postProcessingTask);
        } catch (MessagingException e) {
            throw new ManagerException(e);
        }
    }

    public static class StatementStatisticsEntry{
        public StatementStatisticsEntry(String name, int count) {
            this.name = name;
            this.count = count;
        }

        private String name;
        private int count;

        public String getName() {
            return name;
        }


        public int getCount() {
            return count;
        }

    }

    public List<StatementStatisticsEntry> getStatementStatistics() throws ManagerException {
        List<StatementStatisticsEntry> result = new ArrayList<>();

        MutationResult mutationResult = new FastRangeCountRequestBuilder()
                .path(config.getSparqlEndpoint())
                .execute();
        result.add(new StatementStatisticsEntry("Statements", mutationResult.getRangeCount()));
        int bugcount = 0;
        mutationResult = new FastRangeCountRequestBuilder()
                .path(config.getSparqlEndpoint())
                .predicate(RDFS.SUBCLASSOF)
                .execute();
        bugcount += mutationResult.getRangeCount();
        mutationResult = new FastRangeCountRequestBuilder()
                .path(config.getSparqlEndpoint())
                .predicate(RDFS.SUBPROPERTYOF)
                .execute();
        bugcount += mutationResult.getRangeCount();
        mutationResult = new FastRangeCountRequestBuilder()
                .path(config.getSparqlEndpoint())
                .predicate(RDF.TYPE)
                .object(RDFS.CLASS)
                .execute();
        bugcount += mutationResult.getRangeCount();
        result.add(new StatementStatisticsEntry("Bug Statements", bugcount));
        mutationResult = new FastRangeCountRequestBuilder()
                .path(config.getSparqlEndpoint())
                .predicate(METASERVICE.DUMMY)
                .object(METASERVICE.DUMMY)
                .execute();
        result.add(new StatementStatisticsEntry("Empty/Dummy Observations", mutationResult.getRangeCount()));
        mutationResult = new FastRangeCountRequestBuilder()
                .path(config.getSparqlEndpoint())
                .predicate(RDF.TYPE)
                .object(METASERVICE.OBSERVATION)
                .execute();
        result.add(new StatementStatisticsEntry("Observations", mutationResult.getRangeCount()));
        mutationResult = new FastRangeCountRequestBuilder()
                .path(config.getSparqlEndpoint())
                .predicate(RDF.TYPE)
                .object(ADMSSW.SOFTWARE_REPOSITORY)
                .execute();
        result.add(new StatementStatisticsEntry("Repositories", mutationResult.getRangeCount()));
        mutationResult = new FastRangeCountRequestBuilder()
                .path(config.getSparqlEndpoint())
                .predicate(RDF.TYPE)
                .object(ADMSSW.SOFTWARE_PROJECT)
                .execute();
        result.add(new StatementStatisticsEntry("Projects", mutationResult.getRangeCount()));
        mutationResult = new FastRangeCountRequestBuilder()
                .path(config.getSparqlEndpoint())
                .predicate(RDF.TYPE)
                .object(ADMSSW.SOFTWARE_RELEASE)
                .execute();
        result.add(new StatementStatisticsEntry("Releases", mutationResult.getRangeCount()));
        mutationResult = new FastRangeCountRequestBuilder()
                .path(config.getSparqlEndpoint())
                .predicate(RDF.TYPE)
                .object(ADMSSW.SOFTWARE_PACKAGE)
                .execute();
        result.add(new StatementStatisticsEntry("Packages", mutationResult.getRangeCount()));

        for(ManagerConfig.Module module : managerConfig.getInstalledModules()){
            MetaserviceDescriptor metaserviceDescriptor = module.getMetaserviceDescriptor();
            MetaserviceDescriptor.ModuleInfo moduleInfo = metaserviceDescriptor.getModuleInfo();
            int sum =0;
            for(MetaserviceDescriptor.ProviderDescriptor providerDescriptor : metaserviceDescriptor.getProviderList()){
                String id = descriptorHelper.getStringFromProvider(moduleInfo, providerDescriptor);
                mutationResult = new FastRangeCountRequestBuilder()
                        .path(config.getSparqlEndpoint())
                        .predicate(METASERVICE.GENERATOR)
                        .object(valueFactory.createLiteral(id))
                        .execute();
                result.add(new StatementStatisticsEntry("Provider " + id, mutationResult.getRangeCount()));
                sum += mutationResult.getRangeCount();
            }
            for(MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor : metaserviceDescriptor.getPostProcessorList()){
                String id = descriptorHelper.getStringFromPostProcessor(moduleInfo, postProcessorDescriptor);
                mutationResult = new FastRangeCountRequestBuilder()
                        .path(config.getSparqlEndpoint())
                        .predicate(METASERVICE.GENERATOR)
                        .object(valueFactory.createLiteral(id))
                        .execute();
                result.add(new StatementStatisticsEntry("PostProcessor " + id, mutationResult.getRangeCount()));
                sum += mutationResult.getRangeCount();
            }
            result.add(new StatementStatisticsEntry("Sum " + descriptorHelper.getModuleIdentifierStringFromModule(moduleInfo), sum));
        }
        return result;
    }

    public void init() throws ManagerException {
        getRunManager().runKryoServer();
        try {
            messageHandler.init();
        } catch (MessagingException e) {
            throw new ManagerException(e);
        }
        //start crawling of installed modules
        for(ManagerConfig.Module module : managerConfig.getInstalledModules()){
            scheduleCrawlers(module.getMetaserviceDescriptor());
        }
    }




    public void shutdown() throws ManagerException{
        try {
            messageHandler.close();
            runManager.shutdown();
            scheduler.shutdown();
        } catch (SchedulerException | MessagingException e) {
            throw new ManagerException(e);
        }
    }

    public Archive getArchiveForRepository(@NotNull MetaserviceDescriptor.RepositoryDescriptor repositoryDescriptor) throws ArchiveException {
        ArchiveParameters archiveParameters = new ArchiveParametersImpl(
                repositoryDescriptor.getBaseUri(),
                new File(config.getArchiveBasePath() + repositoryDescriptor.getId()) //todo retrieve from cmdb?
        );
        return new GitArchive(archiveParameters);
    }

    public void loadAllDataFromArchive(@NotNull final String name) throws ManagerException {
        try {
            MetaserviceDescriptor.RepositoryDescriptor repositoryDescriptor = null;
            MetaserviceDescriptor.ModuleInfo moduleInfo =null;
            for(ManagerConfig.Module module :managerConfig.getInstalledModules()){
                for(MetaserviceDescriptor.RepositoryDescriptor t : module.getMetaserviceDescriptor().getRepositoryList()){
                    if(t.getId().equals(name)){
                        moduleInfo = module.getMetaserviceDescriptor().getModuleInfo();
                        repositoryDescriptor = t;
                        break;
                    }
                }
            }

            if(repositoryDescriptor == null|| moduleInfo == null){
                LOGGER.error("NO SUCH REPOSITORY {}", name);
                return;
            }

            final MetaserviceDescriptor.RepositoryDescriptor selectedRepositoryDescriptor = repositoryDescriptor;
            final MetaserviceDescriptor.ModuleInfo selectedModuleInfo = moduleInfo;

            Archive gitArchive = getArchiveForRepository(selectedRepositoryDescriptor);
            for (Date commitTime : gitArchive.getTimes()) {
                for (String path : gitArchive.getChangedPaths(commitTime)) {
                    LOGGER.info("Sending " + commitTime + " s " + path);
                    ArchiveAddress archiveAddress = new ArchiveAddress(
                            selectedRepositoryDescriptor.getId(),
                            gitArchive.getSourceBaseUri(),
                            commitTime,
                            path,
                            selectedRepositoryDescriptor.getType());
                    archiveAddress.setParameters(selectedRepositoryDescriptor.getProperties());
                    messageHandler.send(archiveAddress);
                }
            }
        } catch (MessagingException |ArchiveException e) {
            throw new ManagerException(e);
        }
    }
    private URI generateMetadata(MetaserviceDescriptor.ModuleInfo moduleInfo) throws RepositoryException {
        //todo uniqueness in uri necessary
        URI metadata = valueFactory.createURI("http://metaservice.org/m/" + descriptorHelper.getModuleIdentifierStringFromModule(moduleInfo) + "/" + System.currentTimeMillis());

        Value timeLiteral = valueFactory.createLiteral(new Date(0));
        repositoryConnection.begin();
        repositoryConnection.add(metadata,RDF.TYPE, METASERVICE.OBSERVATION,metadata);
        repositoryConnection.add(metadata, RDF.TYPE, METASERVICE.ADD_OBSERVATION, metadata);
        repositoryConnection.add(metadata, METASERVICE.DATA_TIME, timeLiteral,metadata);
        repositoryConnection.add(metadata, METASERVICE.PATH,valueFactory.createLiteral(descriptorHelper.getModuleIdentifierStringFromModule(moduleInfo)),metadata); //required to be seen as normal add operation -> add is only valid per path see sparql
        repositoryConnection.add(metadata, METASERVICE.CREATION_TIME, valueFactory.createLiteral(new Date()),metadata);
        repositoryConnection.add(metadata, METASERVICE.SOURCE_PROPERTY, metadata,metadata);
        repositoryConnection.add(metadata, METASERVICE.GENERATOR, valueFactory.createLiteral(descriptorHelper.getModuleIdentifierStringFromModule(moduleInfo)),metadata);
        repositoryConnection.commit();
        return metadata;
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
        saveConfig();
    }

    private void removeData(MetaserviceDescriptor descriptor) throws ManagerException {
        for(MetaserviceDescriptor.ProviderDescriptor providerDescriptor: descriptor.getProviderList()){
            removeProviderData(descriptor,providerDescriptor);
        }
        for(MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor : descriptor.getPostProcessorList()){
            removePostProcessorData(descriptor,postProcessorDescriptor);
        }
    }

    public void removePostProcessorData(MetaserviceDescriptor descriptor, @NotNull MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor) throws ManagerException {
        removeDataFromGenerator(descriptorHelper.getStringFromPostProcessor(descriptor.getModuleInfo(), postProcessorDescriptor));

    }

    public void removeProviderData(MetaserviceDescriptor descriptor, @NotNull MetaserviceDescriptor.ProviderDescriptor providerDescriptor) throws ManagerException {
        removeDataFromGenerator(descriptorHelper.getStringFromProvider(descriptor.getModuleInfo(), providerDescriptor));
    }


    private void removeDataFromGenerator(String generator) throws ManagerException {
        try {
            TupleQuery repoSelect = this.repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL, "SELECT DISTINCT ?metadata { ?metadata a <" + METASERVICE.OBSERVATION + ">;  <" + METASERVICE.GENERATOR + "> ?generator }");
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
                LOGGER.info("Clearing {} contexts from generator {}", uris.size(), generator);
                List<List<URI>> partitions = Lists.partition(uris,200);
                for(List<URI> partition : partitions) {
                    repositoryConnection.clear(partition.toArray(new URI[partition.size()]));
                }
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
            messageHandler.send(s);
        } catch (MessagingException e) {
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
            query.setBinding("id", valueFactory.createLiteral(descriptorHelper.getStringFromPostProcessor(descriptor.getModuleInfo(), postProcessorDescriptor)));
            TupleQueryResult result = query.evaluate();
            while(result.hasNext()){
                BindingSet bs = result.next();
                Value value = bs.getBinding("resource").getValue();
                if(value instanceof Resource)
                    toRefresh.add(value.stringValue());
            }

            for (String s : toRefresh) {
                messageHandler.send(s);
            }
        } catch ( MessagingException |OpenRDFException e) {
            throw new ManagerException("Could not refresh " + postProcessorDescriptor.getId(),e);
        }
    }

    private void refreshProvider(@NotNull MetaserviceDescriptor descriptor, MetaserviceDescriptor.ProviderDescriptor providerDescriptor) throws ManagerException {
        try {
            final ArrayList<String> toRefresh = new ArrayList<>();
            TupleQuery query = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,"SELECT DISTINCT ?resource { GRAPH ?metadata {?resource ?y ?z}. ?metadata <"+METASERVICE.GENERATOR+"> ?id}");
            query.setBinding("id", valueFactory.createLiteral(descriptorHelper.getStringFromProvider(descriptor.getModuleInfo(), providerDescriptor)));
            TupleQueryResult result = query.evaluate();
            while(result.hasNext()){
                BindingSet bs = result.next();
                Value value = bs.getBinding("resource").getValue();
                if(value instanceof Resource)
                    toRefresh.add(value.stringValue());
            }
            for (String s : toRefresh) {
                messageHandler.send(s);
            }
        } catch (MessagingException | OpenRDFException e) {
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

    public List<QueueStatistics> getMessagingStatistics() throws ManagerException {
        try {
            return messageHandler.getStatistics();
        } catch (MessagingException e) {
            throw new ManagerException(e);
        }
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
            Path template =  Paths.get(config.getHttpdDataDirectory() + templateDescriptor.getName());
            LOGGER.info("Deleting {}",template);
            try{
                if(template.toFile().exists()){
                    Files.delete(template);
                }
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

    private void installOntologies(@NotNull FileSystem zipFs,@NotNull MetaserviceDescriptor descriptor) throws IOException, ManagerException {
        for(MetaserviceDescriptor.OntologyDescriptor ontologyDescriptor : descriptor.getOntologyList()){
            if(ontologyDescriptor.getApply() || ontologyDescriptor.getDistribute()) //todo think about apply and distribute
            {
                Path from = zipFs.getPath("/ontologies/" + ontologyDescriptor.getName());
                Path to = getOntologyPath(ontologyDescriptor);
                LOGGER.info("Copying {} to {}",from,to);
                Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    private void installTemplates(@NotNull FileSystem zipFs,@NotNull MetaserviceDescriptor descriptor) throws IOException, ManagerException {
        //todo namespace the paths of templates
        for(MetaserviceDescriptor.TemplateDescriptor templateDescriptor : descriptor.getTemplateList()){
            Path from = zipFs.getPath("/templates/" + templateDescriptor.getName());
            Path to = getTemplatePath(templateDescriptor);
            LOGGER.info("Copying {} to {}",from,to);
            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
            Statement s = null;
            try{
                s= getTemplateStatement(templateDescriptor);
                LOGGER.info("Adding Statement {} to the database", s);
                repositoryConnection.add(s,generateMetadata(descriptor.getModuleInfo()));
            } catch (RepositoryException e) {
                throw new ManagerException("Could not add the Statement " + s +" to the database",e);
            }
        }
    }

    public @NotNull Path getTemplatePath(@NotNull MetaserviceDescriptor.TemplateDescriptor templateDescriptor){
        return  Paths.get(config.getHttpdDataDirectory(), templateDescriptor.getName());
    }


    public @NotNull Path getOntologyPath(@NotNull MetaserviceDescriptor.OntologyDescriptor ontologyDescriptor){
        return  Paths.get(config.getHttpdDataDirectory(),"ns",ontologyDescriptor.getName());
    }


    public @NotNull Statement getTemplateStatement(@NotNull MetaserviceDescriptor.TemplateDescriptor templateDescriptor){
        URI subject= valueFactory.createURI(templateDescriptor.getAppliesTo());
        Literal filename = valueFactory.createLiteral(templateDescriptor.getName());
        return valueFactory.createStatement(subject, METASERVICE.VIEW_PROPERTY,filename);
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

    public void add(File fileToAdd, boolean override) throws ManagerException {
        LOGGER.info("Starting adding of {}",fileToAdd.getName());
        Path p = Paths.get(fileToAdd.toURI());
        try (FileSystem zipfs = FileSystems.newFileSystem(p,Thread.currentThread().getContextClassLoader())){
            MetaserviceDescriptor descriptor = new JAXBMetaserviceDescriptorProvider(Files.newInputStream(zipfs.getPath("/metaservice.xml"))).get();
            ManagerConfig.Module module = new ManagerConfig.Module();
            module.setMetaserviceDescriptor(descriptor);
            if(managerConfig.getInstalledModules().contains(module)){
                throw new ManagerException("module is installed - please uninstall before adding");
            }
            MetaserviceDescriptor.ModuleInfo moduleInfo = descriptor.getModuleInfo();
            Path target = Paths.get("./modules", moduleInfo.getGroupId(), moduleInfo.getArtifactId() + "-" + moduleInfo.getVersion() + ".jar");
            Files.createDirectories(target.getParent());
            if(Files.exists(target) && !override){
                LOGGER.info("Already installed - please use override");
                return;
            }
            Files.copy(fileToAdd.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
            module.setLocation(target.toFile());
            if(managerConfig.getAvailableModules().contains(module)){
                managerConfig.getAvailableModules().remove(module);
            }
            managerConfig.getAvailableModules().add(module);
            saveConfig();
        } catch (IOException e) {
            throw new ManagerException("Could not add " + fileToAdd.getName(),e);
        }
    }

    public MavenManager getMavenManager() {
        return mavenManager;
    }
}