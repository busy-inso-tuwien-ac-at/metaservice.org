package org.metaservice.core.provider;

import info.aduna.iteration.Iterations;
import org.metaservice.api.archive.Archive;
import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.archive.ArchiveException;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.rdf.vocabulary.METASERVICE;
import org.metaservice.api.parser.Parser;
import org.metaservice.api.provider.Provider;
import org.metaservice.api.provider.ProviderException;
import org.metaservice.core.AbstractDispatcher;
import org.metaservice.core.Config;
import org.openrdf.model.*;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.*;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.inject.Inject;
import javax.jms.*;
import java.util.*;

/**
 * Scope - Distributed
 * @param <T>
 */
public class ProviderDispatcher<T>  extends AbstractDispatcher<Provider<T>> {
    private final Logger LOGGER = LoggerFactory.getLogger(ProviderDispatcher.class);

    private final MetaserviceDescriptor.ProviderDescriptor providerDescriptor;
    private final Provider<T> provider;
    private final Parser<T> parser;

    private final ValueFactory valueFactory;
    private final RepositoryConnection repositoryConnection;
    private final TupleQuery repoSelect;
    private final Set<Archive> archives;


    //TODO make repoMap dynamic - such that it may be cloned automatically, or use an existing copy
    // this could be achieved by using a hash to search in the local filesystem

    @Inject
    public ProviderDispatcher(
            MetaserviceDescriptor.ProviderDescriptor providerDescriptor,
            Provider<T> provider,
            Parser<T> parser,
            Set<Archive> archives,
            ValueFactory valueFactory,
            ConnectionFactory connectionFactory,
            RepositoryConnection repositoryConnection,
            Config config
    ) throws RepositoryException, MalformedQueryException, JMSException {
        super(repositoryConnection, config, connectionFactory, valueFactory, provider);
        this.provider = provider;
        this.parser = parser;
        this.valueFactory = valueFactory;
        this.repositoryConnection = repositoryConnection;
        this.archives = archives;
        this.providerDescriptor = providerDescriptor;
        repoSelect = this.repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL, "SELECT DISTINCT ?repo ?time ?path ?metadata { graph ?metadata {?resource ?p ?o}.  ?metadata a <"+METASERVICE.METADATA+">;  <" + METASERVICE.SOURCE + "> ?repo; <" + METASERVICE.TIME + "> ?time; <" + METASERVICE.PATH + "> ?path.}");
    }

    public void refresh(URI uri) {
        try {
            LOGGER.error("refreshing: " + uri);
            repoSelect.setBinding("resource", uri);
            TupleQueryResult queryResult =repoSelect.evaluate();
            if(!queryResult.hasNext())
                throw new QueryEvaluationException("Result set is empty");
            while (queryResult.hasNext()){

            BindingSet set = queryResult.next();

            Value timeValue = set.getBinding("time").getValue();
            Value repoValue = set.getBinding("repo").getValue();
            Value pathValue = set.getBinding("path").getValue();
            URI oldMetadata = (URI) set.getBinding("metadata").getValue();
            String time = timeValue.stringValue();
            String repo = repoValue.stringValue();
            String path = pathValue.stringValue();
            LOGGER.info("READING time {}",time);
            LOGGER.info("READING repo {}",repo);
            LOGGER.info("READING path {}", path);

            Archive archive = getArchive(repo);
            if(archive == null){
                return;
            }

            ArchiveAddress address = new ArchiveAddress(repo,time,path);
            //todo add parameters to address

            try{
                LOGGER.info("Starting to process " + address);
                String content = archive.getContent(time,path);
                if(content == null ||content.length() < 20){
                    LOGGER.warn("Cannot process content '{}' of address {}, skipping.",content,address );
                    return;
                }

                URI metadata =  generateMetadata(address);
                Repository resultRepository = createTempRepository();
                RepositoryConnection resultConnection = resultRepository.getConnection();
                loadNamespaces(resultConnection,providerDescriptor.getNamespaceList());
                loadOntologies(resultConnection,providerDescriptor.getLoadList());
                resultConnection.commit();
                HashSet<Statement> loadedStatements = new HashSet<>();
                Iterations.addAll(resultConnection.getStatements(null, null, null, true, NO_CONTEXT), loadedStatements);
                List<T> objects = parser.parse(content,address);
                HashMap<String,String> parameters = new HashMap<>(address.getParameters());
                for(T object: objects){
                    try {
                        provider.provideModelFor(object,resultConnection,parameters);
                    } catch (ProviderException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
                resultConnection.commit();
                repositoryConnection.clear(oldMetadata);
                List<Statement> generatedStatements = getGeneratedStatements(resultConnection,loadedStatements);
                sendData(resultConnection,metadata,generatedStatements);
                Set<URI> resourcesToPostProcess = getSubjects(generatedStatements);
                notifyPostProcessors(resourcesToPostProcess,null,null,null);
                LOGGER.info("done processing {} {}", time, path);
            } catch (RepositoryException | ArchiveException e) {
                LOGGER.error("Could not Refresh {}" ,address,e);
            }
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
            URI metadata =  generateMetadata(address);
            Repository resultRepository = createTempRepository();
            RepositoryConnection resultConnection = resultRepository.getConnection();

            loadNamespaces(resultConnection,providerDescriptor.getNamespaceList());
            loadOntologies(resultConnection,providerDescriptor.getLoadList());
            resultConnection.commit();
            HashSet<Statement> loadedStatements = new HashSet<>();
            Iterations.addAll(resultConnection.getStatements(null, null, null, true, NO_CONTEXT),loadedStatements);
            List<T> objects = parser.parse(content,address);
            HashMap<String,String> parameters = new HashMap<>(address.getParameters());
            for(T object: objects){
                try {
                    provider.provideModelFor(object,resultConnection,parameters);
                } catch (ProviderException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            resultConnection.commit();
            List<Statement> generatedStatements  = getGeneratedStatements(resultConnection,loadedStatements);
            sendData(resultConnection,metadata,generatedStatements);
            Set<URI> resourcesToPostProcess = getSubjects(generatedStatements);
            notifyPostProcessors(resourcesToPostProcess,null,null, null);
        } catch (RepositoryException | ArchiveException e) {
            LOGGER.error("Couldn't create {}",address,e);
        }
    }

    private URI generateMetadata(ArchiveAddress address) throws RepositoryException {
        //todo uniqueness in uri necessary
        URI metadata = valueFactory.createURI("http://metaservice.org/m/" + provider.getClass().getSimpleName() + "/" + System.currentTimeMillis());
        Value pathLiteral = valueFactory.createLiteral(address.getPath());
        Value timeLiteral = valueFactory.createLiteral(address.getTime());
        Value repoLiteral = valueFactory.createLiteral(address.getArchiveUri());
        repositoryConnection.begin();
        repositoryConnection.add(metadata, RDF.TYPE,METASERVICE.METADATA,metadata);
        repositoryConnection.add(metadata, METASERVICE.PATH, pathLiteral,metadata);
        repositoryConnection.add(metadata, METASERVICE.TIME, timeLiteral,metadata);
        repositoryConnection.add(metadata, METASERVICE.SOURCE, repoLiteral,metadata);
        repositoryConnection.add(metadata, METASERVICE.CREATION_TIME, valueFactory.createLiteral(new Date()),metadata);
        repositoryConnection.add(metadata, METASERVICE.GENERATOR, valueFactory.createLiteral(provider.getClass().getCanonicalName()),metadata);
        repositoryConnection.commit();
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


}
