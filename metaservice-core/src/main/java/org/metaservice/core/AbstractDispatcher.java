package org.metaservice.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.core.postprocessor.PostProcessingHistoryItem;
import org.metaservice.core.postprocessor.PostProcessingTask;
import org.openrdf.model.*;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;
import org.openrdf.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.openrdf.sail.memory.MemoryStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ilo on 17.01.14.
 */
public abstract  class AbstractDispatcher<T> {
    private final Logger LOGGER = LoggerFactory.getLogger(AbstractDispatcher.class);
    private final RepositoryConnection repositoryConnection;
    private final Config config;
    protected final Resource[] NO_CONTEXT = new Resource[0];
    private final MessageProducer producer;
    private final Session session;
    private final ValueFactory valueFactory;
    private final T target;

    protected AbstractDispatcher(
            RepositoryConnection repositoryConnection,
            Config config,
            ConnectionFactory connectionFactory,
            ValueFactory valueFactory,
            T target
    ) throws JMSException {
        this.repositoryConnection = repositoryConnection;
        this.config = config;
        this.valueFactory = valueFactory;
        this.target = target;

        Connection jmsConnection = connectionFactory.createConnection();

        session = jmsConnection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        this.producer = session.createProducer(session.createTopic("VirtualTopic.PostProcess"));
    }


    protected void sendData(RepositoryConnection resultConnection,URI metadata,List<Statement> generatedStatements) throws RepositoryException {
        LOGGER.info("starting to send data");
        RDFXMLPrettyWriter writer = null;
        String writerFile = null;
        if(config.getDumpRDFBeforeLoad()){
            try {
                writerFile =config.getDumpRDFDirectory()+ "/"+ target.getClass().getSimpleName()+"_" + System.currentTimeMillis() +".rdf";
                writer = new RDFXMLPrettyWriter(new FileWriter(writerFile));
            } catch (IOException e) {
                LOGGER.error("Couldn't dump rdf data to {}", writerFile, e);
            }
        }
        RepositoryResult<Namespace> ns = resultConnection.getNamespaces();
        while(ns.hasNext()){
            Namespace n = ns.next();
            repositoryConnection.setNamespace(n.getPrefix(), n.getName());
            if(writer != null){
                writer.handleNamespace(n.getPrefix(),n.getName());
            }
            LOGGER.debug("Using namespace "+ n.getPrefix() + " , " +n.getName() );
        }
        if(writer != null){
            try {
                resultConnection.exportStatements(null, null, null, true, writer,NO_CONTEXT);
            } catch (RDFHandlerException e) {
                LOGGER.error("Couldn't dump rdf data to {}",writerFile,e);
            }
        }
        resultConnection.close();
        ArrayList<ArrayList<Statement>> result = split(generatedStatements);
        int j = 0;
        for(ArrayList<Statement> r  : result ){
            LOGGER.info("Sending BATCH " + j++);
            repositoryConnection.begin();
            repositoryConnection.add(r, metadata);
            repositoryConnection.commit();
        }
        LOGGER.info("finished to send data");
    }

    protected Set<URI> getSubjects(@NotNull List<Statement> statements){
        Set<URI> subjects = new HashSet<>();
        for(Statement statement : statements){
            Resource  subject = statement.getSubject();
            if(subject instanceof  URI){
                subjects.add((URI) subject);
            }
        }
        return subjects;
    }

    protected void notifyPostProcessors(@NotNull Set<URI> resourcesThatChanged, @Nullable PostProcessingTask originalTask, @Nullable MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor, Set<URI> affectedProcessableSubjects){
        LOGGER.info("START NOTIFICATION OF POSTPROCESSORS");

        ArrayList<PostProcessingHistoryItem> history = new ArrayList<>();

        if(originalTask != null && affectedProcessableSubjects != null){
            history.addAll(originalTask.getHistory());
            if(postProcessorDescriptor != null){
                PostProcessingHistoryItem now = new PostProcessingHistoryItem(postProcessorDescriptor.getId(),affectedProcessableSubjects.toArray(new URI[affectedProcessableSubjects.size()]));
                history.add(now);
            }
        }


        for(URI uri : resourcesThatChanged){
            try {
                PostProcessingTask postProcessingTask = new PostProcessingTask(uri);
                postProcessingTask.getHistory().addAll(history);
                ObjectMessage objectMessage = session.createObjectMessage(postProcessingTask);
                objectMessage.setJMSExpiration(1000*60*15); // 15 min
                producer.send(objectMessage);
            } catch (JMSException e) {
                LOGGER.error("Couldn't notify PostProcessResource of {}",uri.toString(),e);
            }
        }

        LOGGER.info("STOP NOTIFICATION OF POSTPROCESSORS");

    }

    protected List<Statement> getGeneratedStatements(RepositoryConnection resultConnection,Set<Statement> loadedStatements) throws RepositoryException {
        RepositoryResult<Statement> all = resultConnection.getStatements(null, null, null, true, NO_CONTEXT);
        ArrayList<Statement> allList = new ArrayList<>();
      //  ArrayList<Statement> deletedList = new ArrayList<>();
        while(all.hasNext()){
            Statement s = all.next();
            if(!loadedStatements.contains(s)){
                allList.add(s);
          /*      LOGGER.debug("{}",s);
            }else{
                deletedList.add(s);*/
            }

            //todo fixme loaded statements are not deleted
        }
    /*    LOGGER.debug("ATTENTION: the following statements should be deleted {}", loadedStatements.size());
        LOGGER.debug("ATTENTION: the following statements where deleted {}", deletedList.size());
*/
        return  allList;
    }

    private ArrayList<ArrayList<Statement>> split(List<Statement> statements){
        ArrayList<ArrayList<Statement>> result = new ArrayList<>();
        ArrayList<Statement> currentResult = new ArrayList<>();

        int i = config.getBatchSize()+1;

        LOGGER.info("SPLITTING : ");

        for(Statement statement : statements){
            if(i >config.getBatchSize()){
                i = 0;
                currentResult = new ArrayList<>();
                result.add(currentResult);
            }
            i++;
            currentResult.add(valueFactory.createStatement(statement.getSubject(), statement.getPredicate(), statement.getObject()));
        }
        LOGGER.info(""+result.size());
        return result;
    }


    protected void loadNamespaces(RepositoryConnection resultConnection, List<MetaserviceDescriptor.NamespaceDescriptor> namespaceList) throws RepositoryException {
        for(MetaserviceDescriptor.NamespaceDescriptor namespaceDescriptor: namespaceList){
            resultConnection.setNamespace(namespaceDescriptor.getPrefix(),namespaceDescriptor.getUri().toString());
        }
    }

    protected void loadOntologies(RepositoryConnection con, List<MetaserviceDescriptor.LoadDescriptor> loadList){
       LOGGER.info("Loading " + loadList.size() + " Ontologies");
        for(MetaserviceDescriptor.LoadDescriptor loadDescriptor : loadList){
            try {
                LOGGER.info("Loading Ontology {}",loadDescriptor.getUrl());
                con.add(loadDescriptor.getUrl(),null,null,NO_CONTEXT);
            } catch (RDFParseException | IOException | RepositoryException e) {
                LOGGER.info("Couldn't load {}",loadDescriptor,e);
            }
        }

    }


    protected Repository createTempRepository() throws RepositoryException {
        Repository repo = new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore()));
        repo.initialize();
        return repo;
    }

}
