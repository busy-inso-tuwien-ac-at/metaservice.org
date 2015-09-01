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
import org.metaservice.api.messaging.*;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.api.provider.ProviderException;
import org.metaservice.core.dispatcher.NotifyPipe;
import org.metaservice.core.postprocessor.PostProcessorDispatcher;
import org.openrdf.model.*;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.model.vocabulary.RDF;
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
import org.openrdf.sail.NotifyingSail;
import org.openrdf.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.openrdf.sail.memory.MemoryStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by ilo on 17.01.14.
 */
public abstract  class AbstractDispatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDispatcher.class);

    private final Config config;
    private final MessageHandler messageHandler;

    protected AbstractDispatcher(
            Config config,
            MessageHandler messageHandler
    )  {
        this.config = config;
        this.messageHandler = messageHandler;
    }


    protected void sendDataByLoad(URI metadata,Collection<Statement> generatedStatements,Set<Statement> loadedStatements ) throws MetaserviceException {
        StringWriter stringWriter = new StringWriter();
        try {
            NTriplesWriter nTriplesWriter = new NTriplesWriter(stringWriter);
            Repository inferenceRepository = createTempRepository(true);
            RepositoryConnection inferenceRepositoryConnection = inferenceRepository.getConnection();
            LOGGER.debug("Start inference...");
            inferenceRepositoryConnection.add(loadedStatements);
            inferenceRepositoryConnection.add(generatedStatements);
            LOGGER.debug("Finished inference");
            List<Statement> filteredStatements =  getGeneratedStatements(inferenceRepositoryConnection,loadedStatements);
            nTriplesWriter.startRDF();
            for(Statement statement : filteredStatements){
                nTriplesWriter.handleStatement(statement);
            }
            nTriplesWriter.endRDF();
            inferenceRepositoryConnection.close();
            inferenceRepository.shutDown();

            Executor executor = Executor.newInstance(HttpClientBuilder.create().setConnectionManager(new BasicHttpClientConnectionManager()).build());

            executor.execute( Request
                    .Post(config.getSparqlEndpoint() + "?context-uri=" + metadata.toString())
                    .bodyStream(new ByteArrayInputStream(stringWriter.getBuffer().toString().getBytes("UTF-8")), ContentType.create("text/plain", Charset.forName("UTF-8"))));
        } catch (RDFHandlerException  | IOException | RepositoryException e) {
            throw new MetaserviceException(e);
        }
    }

    public static Set<URI> getSubjects(@NotNull Iterable<Statement> statements){
        Set<URI> subjects = new HashSet<>();
        for(Statement statement : statements){
            Resource  subject = statement.getSubject();
            if(subject instanceof  URI){
                subjects.add((URI) subject);
            }
        }
        return subjects;
    }

    public static Set<URI> getURIObject(@NotNull Iterable<Statement> statements){
        Set<URI> objects = new HashSet<>();
        for(Statement statement : statements){
            Value object = statement.getObject();
            if(object instanceof  URI){
                //ignore classes
                if(statement.getPredicate().equals(RDF.TYPE) ||
                        statement.getPredicate().equals(RDFS.SUBCLASSOF) ||
                        statement.getPredicate().equals(RDFS.SUBPROPERTYOF))
                    continue;
                objects.add((URI) object);
            }
        }
        return objects;
    }


    @Deprecated
    protected void notifyPostProcessors(@NotNull Set<URI> resourcesThatChanged, @NotNull List<PostProcessingHistoryItem> oldHistory,@NotNull Date time, @Nullable MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor,@Nullable Set<URI> affectedProcessableSubjects) throws ProviderException {
        NotifyPipe notifyPipe = null;
        try {
            notifyPipe = new NotifyPipe(postProcessorDescriptor,LOGGER,messageHandler);
        } catch (MessagingException e) {
            throw new ProviderException(e);
        }
        notifyPipe.notifyPostProcessors(resourcesThatChanged,oldHistory,time,postProcessorDescriptor,affectedProcessableSubjects);
    }

    public static List<Statement> getGeneratedStatements(RepositoryConnection resultConnection, Set<Statement> loadedStatements) throws RepositoryException {
        RepositoryResult<Statement> all = resultConnection.getStatements(null, null, null, true);
        ArrayList<Statement> allList = new ArrayList<>();
        HashSet<Resource> undefined = new HashSet<>();
        while(all.hasNext()){
            Statement s = all.next();
            if(!loadedStatements.contains(s)){
                if(s.getPredicate().equals(RDFS.SUBPROPERTYOF) ||
                        s.getPredicate().equals(RDFS.SUBCLASSOF) ||
                        s.getPredicate().equals(RDF.TYPE) && s.getObject().equals(RDFS.RESOURCE)||
                        s.getPredicate().equals(RDF.TYPE) && s.getObject().equals(OWL.THING)||
                        s.getPredicate().equals(RDF.TYPE) && s.getObject().equals(RDF.PROPERTY)){
                    if(!s.getSubject().stringValue().startsWith("http://metaservice.org/d/")) {
                        LOGGER.debug("UNDEFINED {} {} {}",s.getSubject(),s.getPredicate(),s.getObject());
                        undefined.add(s.getSubject());
                    }
                }else{
                    if(s.getSubject() instanceof BNode ||s.getObject() instanceof BNode){
                        LOGGER.error("ATTENTION - BNodes are not supported by Metaservice, skipping statement");
                        continue;
                    }
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

    public static Repository createTempRepository(boolean inference) throws MetaserviceException {
        try {
            NotifyingSail sail = new MemoryStore();
            if(inference) {
                sail = new ForwardChainingRDFSInferencer(sail);
                //  sail = new PropertyReificationInferencer(sail); todo buggy -> endless loop
            }
            Repository repo = new SailRepository(sail);
            repo.initialize();
            return repo;
        } catch (RepositoryException /*| SailException | MalformedQueryException*/ e) {
            throw new MetaserviceException(e);
        }
    }

    public static void recoverSparqlConnection(RepositoryConnection repositoryConnection) throws MetaserviceException {
        //try to get into defined state in case anything broke previously
        try {
            if(repositoryConnection.isActive()){
                repositoryConnection.rollback();
            }
        }catch (Exception e){
            throw new MetaserviceException("failed to recover",e);
        }

    }

}
