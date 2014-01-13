package org.metaservice.core;

import info.aduna.iteration.Iterations;
import org.metaservice.api.archive.Archive;
import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.archive.ArchiveException;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.rdf.vocabulary.METASERVICE;
import org.metaservice.api.parser.Parser;
import org.metaservice.api.provider.Provider;
import org.metaservice.api.provider.ProviderException;
import org.openrdf.model.*;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.*;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.openrdf.sail.memory.MemoryStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;


import javax.inject.Inject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Scope - Distributed
 * @param <T>
 */
public class Dispatcher<T> {
    private final Logger LOGGER = LoggerFactory.getLogger(Dispatcher.class);
    private final Resource[] NO_CONTEXT = new Resource[0];

    private final MetaserviceDescriptor.ProviderDescriptor providerDescriptor;
    private final Provider<T> provider;
    private final Parser<T> parser;

    private final ValueFactory valueFactory;
    private final RepositoryConnection connection;
    private final TupleQuery repoSelect;
    private final Set<Archive> archives;
    private final Config config;

    //TODO make repoMap dynamic - such that it may be cloned automatically, or use an existing copy
    // this could be achieved by using a hash to search in the local filesystem

    @Inject
    public Dispatcher(
            MetaserviceDescriptor.ProviderDescriptor providerDescriptor,
            Provider<T> provider,
            Parser<T> parser,
            Set<Archive> archives,
            ValueFactory valueFactory,
            RepositoryConnection connection,
            Config config
    ) throws RepositoryException, MalformedQueryException {
        this.provider = provider;
        this.parser = parser;
        this.valueFactory = valueFactory;
        this.connection = connection;
        this.archives = archives;
        this.config = config;
        this.providerDescriptor = providerDescriptor;
        repoSelect = this.connection.prepareTupleQuery(QueryLanguage.SPARQL, "SELECT ?repo ?time ?path { graph ?sid {?resource ?p ?o}.  ?sid a <"+METASERVICE.METADATA+">;  <" + METASERVICE.SOURCE + "> ?repo; <" + METASERVICE.TIME + "> ?time; <" + METASERVICE.PATH + "> ?path.}");

    }

    public void refresh(URI uri) {
        try {
            repoSelect.setBinding("resource",uri);
            TupleQueryResult queryResult =repoSelect.evaluate();
            if(!queryResult.hasNext())
                throw new QueryEvaluationException("Result set is empty");
            BindingSet set = queryResult.next();

            Value timeValue = set.getBinding("time").getValue();
            Value repoValue = set.getBinding("repo").getValue();
            Value pathValue = set.getBinding("path").getValue();
            String time = timeValue.stringValue();
            String repo = repoValue.stringValue();
            String path = pathValue.stringValue();
            LOGGER.info("READING time {}",time);
            LOGGER.info("READING repo {}",repo);
            LOGGER.info("READING path {}",path);

            Archive archive = getArchive(repo);
            if(archive == null){
                return;
            }

            ArchiveAddress address = new ArchiveAddress(repo,time,path);

            try{
                LOGGER.info("Starting to process " + address);
                String content = archive.getContent(time,path);
                if(content == null ||content.length() < 20){
                    LOGGER.warn("Cannot process content '{}' of address {}, skipping.",content,address );
                    return;
                }

                URI metadata =  generateMetadata(address,connection);
                Repository resultRepository = createTempRepository();
                RepositoryConnection resultConnection = resultRepository.getConnection();
                loadNamespaces(resultConnection,providerDescriptor);
                loadOntologies(resultConnection,providerDescriptor);
                HashSet<Statement> loadedStatements = new HashSet<>();
                Iterations.addAll(resultConnection.getStatements(null, null, null, true, NO_CONTEXT), loadedStatements);
                List<T> objects = parser.parse(content);
                for(T object: objects){
                    try {
                        provider.provideModelFor(object,resultConnection);
                    } catch (ProviderException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
                resultConnection.commit();
                connection.clear(metadata);
                sendData(resultConnection,metadata,loadedStatements);
                LOGGER.info("done processing {} {}", time, path);
            } catch (RepositoryException | ArchiveException e) {
                LOGGER.error("Could not Refresh {}" ,address,e);
            }
        } catch (QueryEvaluationException e) {
            LOGGER.error("Could not Refresh" ,e);
        }
    }

    public void create(ArchiveAddress address){
        Archive archive = getArchive(address.getArchiveUri());
        if(archive == null){
            return;
        }

        try {
            LOGGER.info("Starting to process " + address);
            String content = archive.getContent(address.getTime(),address.getPath());
            if(content == null ||content.length() < 20){
                LOGGER.warn("Cannot process content '{}' of address {}, skipping.",content,address );
                return;
            }
            URI metadata =  generateMetadata(address,connection);
            Repository resultRepository = createTempRepository();
            RepositoryConnection resultConnection = resultRepository.getConnection();

            loadNamespaces(resultConnection,providerDescriptor);
            loadOntologies(resultConnection,providerDescriptor);
            HashSet<Statement> loadedStatements = new HashSet<>();
            Iterations.addAll(resultConnection.getStatements(null, null, null, true, NO_CONTEXT),loadedStatements);

            List<T> objects = parser.parse(content);
            for(T object: objects){
                try {
                    provider.provideModelFor(object,resultConnection);
                } catch (ProviderException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            resultConnection.commit();

            sendData(resultConnection,metadata,loadedStatements);
        } catch (RepositoryException | ArchiveException e) {
            LOGGER.error("Couldn't create {}",address,e);
        }
    }

    private void loadNamespaces(RepositoryConnection resultConnection, MetaserviceDescriptor.ProviderDescriptor providerDescriptor) throws RepositoryException {
        for(MetaserviceDescriptor.ProviderDescriptor.NamespaceDescriptor namespaceDescriptor: providerDescriptor.getNamespaceList()){
            resultConnection.setNamespace(namespaceDescriptor.getPrefix(),namespaceDescriptor.getUri().toString());
        }
    }

    private void sendData(RepositoryConnection resultConnection,URI metadata,Set<Statement> loadedStatements) throws  RepositoryException {
        LOGGER.info("starting to send data");
        RDFXMLPrettyWriter writer = null;
        String writerFile = null;
        if(config.isDumpRDFBeforeLoad()){
            try {
                writerFile =config.getDumpRDFDirectory()+ "/"+ provider.getClass().getSimpleName()+"_" + System.currentTimeMillis() +".rdf";
                writer = new RDFXMLPrettyWriter(new FileWriter(writerFile));
            } catch (IOException e) {
                LOGGER.error("Couldn't dump rdf data to {}", writerFile, e);
            }
        }
        RepositoryResult<Namespace> ns = resultConnection.getNamespaces();
        while(ns.hasNext()){
            Namespace n = ns.next();
            connection.setNamespace(n.getPrefix(),n.getName());
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
        RepositoryResult<Statement> all = resultConnection.getStatements(null, null, null, true, NO_CONTEXT);
        int i = 0;
        ArrayList<ArrayList<Statement>> result = new ArrayList<>();
        ArrayList<Statement> currentResult = new ArrayList<>();
        result.add(currentResult);
        while(all.hasNext()){
            i++;
            if(i >config.getBatchSize()){
                i = 0;
                currentResult = new ArrayList<>();
                result.add(currentResult);
            }
            Statement s = all.next();
            if(!loadedStatements.contains(s)){
                currentResult.add(valueFactory.createStatement(s.getSubject(),s.getPredicate(),s.getObject()));
            }
        }
        resultConnection.close();
        int j = 0;
        for(ArrayList<Statement> r  : result ){
            LOGGER.info("Sending BATCH " + j++);
            connection.add(r,metadata);
        }
    }

    private URI generateMetadata(ArchiveAddress address, RepositoryConnection model) throws RepositoryException {
        URI metadata = valueFactory.createURI("http://metaservice.org/m/" + provider.getClass().getSimpleName());
        Value pathLiteral = valueFactory.createLiteral(address.getPath());
        Value timeLiteral = valueFactory.createLiteral(address.getTime());
        Value repoLiteral = valueFactory.createLiteral(address.getArchiveUri());
        model.add(metadata, RDF.TYPE,METASERVICE.METADATA);
        model.add(metadata, METASERVICE.PATH, pathLiteral);
        model.add(metadata, METASERVICE.TIME, timeLiteral);
        model.add(metadata, METASERVICE.SOURCE, repoLiteral);
        model.add(metadata, METASERVICE.VIEW, valueFactory.createLiteral(System.currentTimeMillis()));
        model.add(metadata, METASERVICE.VIEW, valueFactory.createLiteral(provider.getClass().getCanonicalName()));
        return metadata;
    }

    private Archive getArchive(String archiveUri) {
        for(Archive archive : archives){
            if(archive.getSourceBaseUri().equals(archiveUri)){
                return archive;
            }
        }
        return null;
    }

    private void loadOntologies(RepositoryConnection con, MetaserviceDescriptor.ProviderDescriptor providerDescriptor){
        for(MetaserviceDescriptor.ProviderDescriptor.LoadDescriptor loadDescriptor : providerDescriptor.getLoadList()){
            try {
                con.add(loadDescriptor.getUrl(),null,null,NO_CONTEXT);
            } catch (RDFParseException | IOException | RepositoryException e) {
                LOGGER.warn("Couldn't load {}",loadDescriptor,e);
            }
        }

    }


    private Repository createTempRepository() throws RepositoryException {
        Repository repo = new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore()));
        repo.initialize();
        return repo;
    }
}
