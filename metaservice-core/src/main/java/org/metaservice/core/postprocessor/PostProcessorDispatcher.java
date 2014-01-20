package org.metaservice.core.postprocessor;

import info.aduna.iteration.Iterations;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.api.rdf.vocabulary.METASERVICE;
import org.metaservice.core.AbstractDispatcher;
import org.metaservice.core.Config;
import org.openrdf.model.Literal;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.*;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jms.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ilo on 17.01.14.
 */
public class PostProcessorDispatcher extends AbstractDispatcher<PostProcessor> {
    private final Logger LOGGER = LoggerFactory.getLogger(PostProcessorDispatcher.class);

    private final PostProcessor postProcessor;
    private final MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor;
    private final  RepositoryConnection repositoryConnection;
    private final TupleQuery graphSelect;
    private final ValueFactory valueFactory;

    @Inject
    private PostProcessorDispatcher(
            RepositoryConnection repositoryConnection,
            Config config,
            ConnectionFactory connectionFactory,
            ValueFactory valueFactory,
            PostProcessor postProcessor,
            MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor
    ) throws JMSException, MalformedQueryException, RepositoryException {
        super(repositoryConnection, config, connectionFactory, valueFactory, postProcessor);
        this.postProcessor = postProcessor;
        this.postProcessorDescriptor = postProcessorDescriptor;
        this.repositoryConnection = repositoryConnection;
        this.valueFactory = valueFactory;

        graphSelect = this.repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL, "SELECT ?metadata ?creationtime { graph ?metadata {?resource ?p ?o}.  ?metadata a <"+ METASERVICE.METADATA+">;  <" + METASERVICE.GENERATOR + "> ?postprocessor; <" + METASERVICE.CREATION_TIME+"> ?creationtime. }");
        LOGGER.info(graphSelect.toString());
    }


    public void process(PostProcessingTask task, long jmsTimestamp){
        URI resource = task.getChangedURI();
        for(PostProcessingHistoryItem item  :task.getHistory()){
            if(item.getPostprocessorId().equals(postProcessorDescriptor.getId()) && item.getResource().equals(task.getChangedURI())){
                LOGGER.info("Not processing, because did already process");
                return;
            }
        }
        try {
            if(postProcessor.abortEarly(resource)){
                LOGGER.info("Not continuing processing because abortEarly({}) was true",resource);
                return;
            }
            graphSelect.setBinding("postprocessor",valueFactory.createLiteral(postProcessor.getClass().getCanonicalName()));
            graphSelect.setBinding("resource",resource);
            TupleQueryResult queryResult = graphSelect.evaluate();
            ArrayList<URI> contexts = new ArrayList<>();
            long newesttime = 0;
            while(queryResult.hasNext()){
                BindingSet ser = queryResult.next();
                contexts.add((URI) ser.getBinding("metadata").getValue());
                LOGGER.info("ADDING CONTEXT TO DELETE: " + ser.getBinding("metadata"));

                long creationtime =   ((Literal)ser.getBinding("creationtime").getValue()).calendarValue().toGregorianCalendar().getTime().getTime();
                if(creationtime > newesttime)
                    newesttime = creationtime;
            }

            if(newesttime > jmsTimestamp){
                LOGGER.warn("Ignoring - because request is too old");
                return;
            }



            LOGGER.info("Starting to process " + resource);

            Repository resultRepository = createTempRepository();
            RepositoryConnection resultConnection = resultRepository.getConnection();

            loadNamespaces(resultConnection,postProcessorDescriptor.getNamespaceList());
            loadOntologies(resultConnection,postProcessorDescriptor.getLoadList());
            HashSet<Statement> loadedStatements = new HashSet<>();
            Iterations.addAll(resultConnection.getStatements(null, null, null, true, NO_CONTEXT), loadedStatements);

                try {
                    postProcessor.process(resource, resultConnection);
                } catch (PostProcessorException e) {
                    e.printStackTrace();
                }

            resultConnection.commit();

            if(contexts.size() > 0 ){
                LOGGER.warn("CLEARING CONTEXTS " + contexts);
                repositoryConnection.clear(contexts.toArray(new URI[contexts.size()]));

            }
            List<Statement> generatedStatements  = getGeneratedStatements(resultConnection,loadedStatements);
            if(generatedStatements.size() == 0){
                LOGGER.info("NO STATEMENTS GENERATED! -> aborting");
                return;
            }
            Set<URI> resourcesToPostProcess = getSubjects(generatedStatements);
            URI metadata =  generateMetadata();

            sendData(resultConnection,metadata,generatedStatements);
            notifyPostProcessors(resourcesToPostProcess,task,postProcessorDescriptor);
        } catch (RepositoryException | PostProcessorException | QueryEvaluationException e) {
            LOGGER.error("Couldn't create {}",resource,e);
        }
    }

    private URI generateMetadata() throws RepositoryException {
        //todo uniqueness in uri necessary
        URI metadata = valueFactory.createURI("http://metaservice.org/m/" + postProcessor.getClass().getSimpleName() + "/" + System.currentTimeMillis());
        repositoryConnection.begin();
        repositoryConnection.add(metadata, RDF.TYPE, METASERVICE.METADATA, metadata);
        repositoryConnection.add(metadata, METASERVICE.CREATION_TIME, valueFactory.createLiteral(System.currentTimeMillis()), metadata);
       /* for(URI resource: resources){
            model.add(metadata,METASERVICE.SCOPE,resource,metadata);
        }*/
        repositoryConnection.add(metadata, METASERVICE.GENERATOR, valueFactory.createLiteral(postProcessor.getClass().getCanonicalName()), metadata);
         repositoryConnection.commit();
        return metadata;
    }


}
