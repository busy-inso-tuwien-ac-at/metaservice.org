package org.metaservice.core.postprocessor;

import info.aduna.iteration.Iterations;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.api.rdf.vocabulary.METASERVICE;
import org.metaservice.core.AbstractDispatcher;
import org.metaservice.core.config.Config;
import org.metaservice.core.descriptor.DescriptorHelper;
import org.openrdf.model.Literal;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.*;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jms.*;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ilo on 17.01.14.
 */
public class PostProcessorDispatcher extends AbstractDispatcher<PostProcessor> {
    private final Logger LOGGER = LoggerFactory.getLogger(PostProcessorDispatcher.class);

    private final PostProcessor postProcessor;
    private final MetaserviceDescriptor metaserviceDescriptor;
    private final MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor;
    private final RepositoryConnection repositoryConnection;
    private final TupleQuery graphSelect;
    private final ValueFactory valueFactory;
    private final Set<Statement> loadedStatements;
    private final Debug debug;

    @Inject
    private PostProcessorDispatcher(
            RepositoryConnection repositoryConnection,
            Config config,
            ConnectionFactory connectionFactory,
            MetaserviceDescriptor metaserviceDescriptor, ValueFactory valueFactory,
            PostProcessor postProcessor,
            MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor,
            Debug debug) throws JMSException, MalformedQueryException, RepositoryException {
        super(repositoryConnection, config, connectionFactory, valueFactory, postProcessor);
        this.metaserviceDescriptor = metaserviceDescriptor;
        this.postProcessor = postProcessor;
        this.postProcessorDescriptor = postProcessorDescriptor;
        this.repositoryConnection = repositoryConnection;
        this.valueFactory = valueFactory;
        this.debug = debug;

        graphSelect = this.repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL, "SELECT DISTINCT ?metadata ?lastchecked { graph ?metadata {?resource ?p ?o}.  ?metadata a <"+ METASERVICE.METADATA+">;  <" + METASERVICE.GENERATOR + "> ?postprocessor; <" + METASERVICE.LAST_CHECKED_TIME+"> ?lastchecked. }");

        LOGGER.info(graphSelect.toString());
        loadedStatements = calculatePreloadedStatements();

    }

    private Set<Statement> calculatePreloadedStatements() throws RepositoryException {
        Repository resultRepository = createTempRepository();
        RepositoryConnection resultConnection = resultRepository.getConnection();

        loadNamespaces(resultConnection,postProcessorDescriptor.getNamespaceList());
        loadOntologies(resultConnection,postProcessorDescriptor.getLoadList());
        resultConnection.commit();
        HashSet<Statement> result  =new HashSet<>();
        Iterations.addAll(resultConnection.getStatements(null, null, null, true, NO_CONTEXT),result);
        return Collections.unmodifiableSet(result);
    }


    public void process(PostProcessingTask task, long jmsTimestamp){
        URI resource = task.getChangedURI();
        try{
            if(!isOkCheapCheck(task, jmsTimestamp) || isRequestTooOld(task,jmsTimestamp))
                return;
            LOGGER.info("Starting to process " + resource);

            Repository resultRepository = createTempRepository();
            RepositoryConnection resultConnection = resultRepository.getConnection();
            try {
                postProcessor.process(resource, resultConnection,task.getTime());
            } catch (PostProcessorException e) {
                e.printStackTrace();
            }
            resultConnection.add(loadedStatements);
            resultConnection.commit();
            List<Statement> generatedStatements  = getGeneratedStatements(resultConnection,loadedStatements);
            Set<URI> subjects = getSubjects(generatedStatements);
            Set<URI> processableSubjects = getProcessableSubjects(subjects);

            if(generatedStatements.size() == 0){
                LOGGER.info("NO STATEMENTS GENERATED! -> adding empty statement");
                generatedStatements.add(valueFactory.createStatement(task.getChangedURI(),METASERVICE.DUMMY,METASERVICE.DUMMY));
            }
            Set<URI> graphsToDelete = getGraphsToDelete(processableSubjects,repositoryConnection);
            if(graphsToDelete.size() == 1){
                URI graph = graphsToDelete.iterator().next();
                if(contentsUnchanged(graph, generatedStatements)){
                    LOGGER.info("There was no change, only updating last checked time");
                    repositoryConnection.remove(graph,METASERVICE.TIME,null);
                    repositoryConnection.add(graph,METASERVICE.TIME,valueFactory.createLiteral(new Date()));
                    return;
                }
            }

            for(URI context : graphsToDelete){
                try {
                    LOGGER.warn("Dropping Graph {}", context);
                    Update dropGraphUpdate = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL,"DROP GRAPH <"+context.toString()+">");
                    //  dropGraphUpdate.setBinding("graph", context);
                    dropGraphUpdate.execute();
                } catch (MalformedQueryException e) {
                    LOGGER.error("Could not parse drop query", e);
                }

            }

            URI metadata =  generateMetadata(task,"add");
            sendData(resultConnection,metadata,generatedStatements);
            notifyPostProcessors(subjects,task.getHistory(),task.getTime(),postProcessorDescriptor,processableSubjects);
        } catch (RepositoryException | PostProcessorException e) {
            LOGGER.error("Couldn't create {}",resource,e);
        } catch (UpdateExecutionException e) {
            LOGGER.error("Couldn't drop graph {}", e);
        }
    }

    private boolean contentsUnchanged(URI metadataUri, List<Statement> generatedStatements) throws RepositoryException {
        Set<Statement> currentValues = new HashSet<>();
        Set<Statement> newValues = new HashSet<>();
        RepositoryResult<Statement> fullQuery = repositoryConnection.getStatements(null, null, null, false, metadataUri);
        while (fullQuery.hasNext()){
            Statement statement = fullQuery.next();
            if(!statement.getSubject().equals(metadataUri)){
                currentValues.add(statement);
            }
        }
        newValues.addAll(generatedStatements);
        return newValues.equals(currentValues);
    }

    private Set<URI> getProcessableSubjects(Set<URI> subjects) {
        Set<URI> resultSet = new HashSet<>();
        for(URI subject: subjects){
            try {
                if(!postProcessor.abortEarly(subject)){
                    resultSet.add(subject);
                }
            } catch (PostProcessorException ignored) {
            }
        }
        return resultSet;
    }

    private Set<URI> getGraphsToDelete(Set<URI> subjects, RepositoryConnection repositoryConnection) {
        Set<URI> resultSet = new HashSet<>();
        if(subjects.size() == 0)
            return resultSet;

        try {
            StringBuilder builder = new StringBuilder();
            builder
                    .append("SELECT DISTINCT ?metadata {");
            ArrayList<String> uris = new ArrayList<>();

            for(URI uri : subjects){
                uris.add("{ GRAPH ?metadata {<" + uri.toString() + "> ?p ?o}. ?metadata a <"
                        + METASERVICE.METADATA +
                        ">;  <" +
                        METASERVICE.GENERATOR +
                        "> ?postprocessor.}");
            }
            builder.append(StringUtils.join(uris," UNION" )).append("}");

            String query = builder.toString();
            LOGGER.trace(query);
            TupleQuery tupleQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,query);
            tupleQuery.setBinding("postprocessor", valueFactory.createLiteral(DescriptorHelper.getStringFromPostProcessor(metaserviceDescriptor.getModuleInfo(),postProcessorDescriptor)));
            TupleQueryResult result  = tupleQuery.evaluate();


            while(result.hasNext()){
                BindingSet bindings = result.next();
                resultSet.add((URI) bindings.getBinding("metadata").getValue());
            }
            LOGGER.debug("Graphs To delete {}", resultSet);
        } catch (RepositoryException | QueryEvaluationException | MalformedQueryException e) {
            LOGGER.error("ERROR evaluation ", e);
        }
        return resultSet;
    }

    @NotNull
    private URI generateMetadata(@NotNull PostProcessingTask task, String action) throws RepositoryException {
        Date now = new Date();
        //todo uniqueness in uri necessary
        URI metadata = valueFactory.createURI("http://metaservice.org/m/" + postProcessor.getClass().getSimpleName() + "/" + System.currentTimeMillis());
        repositoryConnection.begin();
        repositoryConnection.add(metadata, RDF.TYPE, METASERVICE.METADATA, metadata);
        repositoryConnection.add(metadata, METASERVICE.ACTION, valueFactory.createLiteral(action), metadata);
        repositoryConnection.add(metadata, METASERVICE.TIME, valueFactory.createLiteral(task.getTime()),metadata); //todo fix it to be based on the used data
        repositoryConnection.add(metadata, METASERVICE.CREATION_TIME, valueFactory.createLiteral(now),metadata);
        repositoryConnection.add(metadata, METASERVICE.LAST_CHECKED_TIME, valueFactory.createLiteral(now),metadata);
        repositoryConnection.add(metadata, METASERVICE.GENERATOR, valueFactory.createLiteral(DescriptorHelper.getStringFromPostProcessor(metaserviceDescriptor.getModuleInfo(),postProcessorDescriptor)), metadata);
        repositoryConnection.commit();
        return metadata;
    }


    public boolean isOkCheapCheck(PostProcessingTask task, long jmsTimestamp) throws PostProcessorException {
        if(debug.isEnabled() &&!debug.process(task)){
            LOGGER.debug("not processing -> debug");
            return false;
        }
        URI resource = task.getChangedURI();
        LOGGER.debug("dispatching {}", resource);
        for(PostProcessingHistoryItem item  :task.getHistory()){
            if(item.getPostprocessorId().equals(postProcessorDescriptor.getId())){
                for(URI uri: item.getResources()){
                    if(uri.equals(task.getChangedURI())){
                        LOGGER.debug("Not processing, because did already process");
                    }
                }
                return false;
            }
        }
        if(postProcessor.abortEarly(resource)){
            LOGGER.debug("Not continuing processing because abortEarly({}) was true", resource);
            return false;
        }
        return true;
    }

    public boolean isRequestTooOld(PostProcessingTask task, long jmsTimestamp){
        try {

            URI resource = task.getChangedURI();
            graphSelect.setBinding("postprocessor",valueFactory.createLiteral(DescriptorHelper.getStringFromPostProcessor(metaserviceDescriptor.getModuleInfo(), postProcessorDescriptor)));
            graphSelect.setBinding("resource",resource);
            TupleQueryResult queryResult = graphSelect.evaluate();
            XMLGregorianCalendar newestTime = null;
            while(queryResult.hasNext()){
                BindingSet ser = queryResult.next();
                LOGGER.debug("EXISTING METADATA FOR THE RESOURCE : " + ser.getBinding("metadata"));
                Binding binding =ser.getBinding("lastchecked");
                LOGGER.trace("" + ((Literal) binding.getValue()).getDatatype());
                XMLGregorianCalendar creationTime =   ((Literal)binding.getValue()).calendarValue();
                if(newestTime == null || creationTime.compare(newestTime) == DatatypeConstants.GREATER)
                    newestTime = creationTime;
            }

            if(newestTime != null){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
                LOGGER.trace("Comparing " + simpleDateFormat.format(newestTime.toGregorianCalendar().getTime()) + " with " + simpleDateFormat.format(new Date(jmsTimestamp)));
                if(newestTime.toGregorianCalendar().getTimeInMillis() > jmsTimestamp){
                    LOGGER.debug("Ignoring - because request is too old");
                    return true;
                }
            }
        } catch (QueryEvaluationException e) {
            LOGGER.error("Could not check if too old ",e);
            return true;
        }
        return false;
    }
}
