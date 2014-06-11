package org.metaservice.core;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaservice.api.MetaserviceException;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.Config;
import org.metaservice.api.messaging.MessagingException;
import org.metaservice.api.messaging.PostProcessingHistoryItem;
import org.metaservice.api.messaging.PostProcessingTask;
import org.metaservice.api.messaging.MessageHandler;
import org.openrdf.model.*;
import org.openrdf.model.URI;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.ntriples.NTriplesWriter;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;
import org.openrdf.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.openrdf.sail.memory.MemoryStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by ilo on 17.01.14.
 */
public abstract  class AbstractDispatcher<T> {
    private final Logger LOGGER = LoggerFactory.getLogger(AbstractDispatcher.class);
    private final RepositoryConnection repositoryConnection;
    private final Config config;
    protected final Resource[] NO_CONTEXT = new Resource[0];
    private final MessageHandler messageHandler;
    private final ValueFactory valueFactory;
    private final T target;

    protected AbstractDispatcher(
            RepositoryConnection repositoryConnection,
            Config config,
            MessageHandler messageHandler,
            ValueFactory valueFactory,
            T target
    )  {
        this.repositoryConnection = repositoryConnection;
        this.config = config;
        this.valueFactory = valueFactory;
        this.target = target;
        this.messageHandler = messageHandler;
        try {
            this.messageHandler.init();
        } catch (MessagingException e) {
            LOGGER.error("",e);
        }
    }


    protected void sendDataByLoad(URI metadata,Collection<Statement> generatedStatements) throws MetaserviceException {
        StringWriter stringWriter = new StringWriter();
        NTriplesWriter nTriplesWriter = new NTriplesWriter(stringWriter);

        try {
            nTriplesWriter.startRDF();
            for(Statement statement : generatedStatements){
                nTriplesWriter.handleStatement(statement);
            }
            nTriplesWriter.endRDF();

            Executor executor = Executor.newInstance(HttpClientBuilder.create().setConnectionManager(new BasicHttpClientConnectionManager()).build());

            executor.execute( Request
                    .Post(config.getSparqlEndpoint() + "?context-uri=" + metadata.toString())
                    .bodyStream(new ByteArrayInputStream(stringWriter.getBuffer().toString().getBytes("UTF-8")), ContentType.create("text/plain", Charset.forName("UTF-8"))));
        } catch (RDFHandlerException  | IOException e) {
            throw new MetaserviceException(e);
        }

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

    protected void notifyPostProcessors(@NotNull Set<URI> resourcesThatChanged, @NotNull List<PostProcessingHistoryItem> oldHistory,@NotNull Date time, @Nullable MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor,@Nullable Set<URI> affectedProcessableSubjects){
        LOGGER.debug("START NOTIFICATION OF POSTPROCESSORS");
        //todo only let postprocessing happen for date = or >
        ArrayList<PostProcessingHistoryItem> history = new ArrayList<>();
        history.addAll(oldHistory);

        if(affectedProcessableSubjects != null){
            if(postProcessorDescriptor != null){
                PostProcessingHistoryItem now = new PostProcessingHistoryItem(postProcessorDescriptor.getId(),affectedProcessableSubjects.toArray(new URI[affectedProcessableSubjects.size()]));
                history.add(now);
            }
        }
        try {
            if(resourcesThatChanged.size() ==0){
                LOGGER.info("nothing to notify");
            }
            for(URI uri : resourcesThatChanged){
                LOGGER.info("changed " + uri);
                PostProcessingTask postProcessingTask = new PostProcessingTask(uri,time);
                postProcessingTask.getHistory().addAll(history);
                messageHandler.send(postProcessingTask);
            }
        } catch (MessagingException e) {
            LOGGER.error("Couldn't notify PostProcessors",e);
        }
        LOGGER.debug("STOP NOTIFICATION OF POSTPROCESSORS");
    }

    protected List<Statement> getGeneratedStatements(RepositoryConnection resultConnection,Set<Statement> loadedStatements) throws RepositoryException {
        RepositoryResult<Statement> all = resultConnection.getStatements(null, null, null, true, NO_CONTEXT);
        ArrayList<Statement> allList = new ArrayList<>();
        HashSet<Resource> undefined = new HashSet<>();
        while(all.hasNext()){
            Statement s = all.next();
            if(!loadedStatements.contains(s)){
                if(s.getPredicate().equals(RDFS.SUBPROPERTYOF) || s.getPredicate().equals(RDFS.SUBCLASSOF)){
                    undefined.add(s.getSubject());
                }else{
                    allList.add(s);
                }
           }
        }
        if(undefined.size() != 0){
            //grep for logfiles:
            //grep "not defi"  *| sed -e 's/^.*WARN.*define://' | uniq | sort | uniq | sed -e 's/,/\n/g' | tr -d ' ' | sort | uniq
            LOGGER.warn("Did not define: {}", StringUtils.join(undefined,", "));
        }
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
