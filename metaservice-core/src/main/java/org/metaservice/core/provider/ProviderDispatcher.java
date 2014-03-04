package org.metaservice.core.provider;

import info.aduna.iteration.Iterations;
import org.metaservice.api.MetaserviceException;
import org.metaservice.api.archive.Archive;
import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.archive.ArchiveException;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.rdf.vocabulary.METASERVICE;
import org.metaservice.api.parser.Parser;
import org.metaservice.api.provider.Provider;
import org.metaservice.api.provider.ProviderException;
import org.metaservice.core.AbstractDispatcher;
import org.metaservice.core.config.Config;
import org.metaservice.core.descriptor.DescriptorHelper;
import org.metaservice.core.postprocessor.PostProcessingHistoryItem;
import org.openrdf.model.*;
import org.openrdf.model.impl.CalendarLiteralImpl;
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

    private final MetaserviceDescriptor metaserviceDescriptor;
    private final MetaserviceDescriptor.ProviderDescriptor providerDescriptor;
    private final Provider<T> provider;
    private final Parser<T> parser;

    private final ValueFactory valueFactory;
    private final RepositoryConnection repositoryConnection;
    private final TupleQuery repoSelect;
    private final Set<Archive> archives;

    private final Set<Statement> loadedStatements;

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
            Config config,
            MetaserviceDescriptor metaserviceDescriptor) throws RepositoryException, MalformedQueryException, JMSException {
        super(repositoryConnection, config, connectionFactory, valueFactory, provider);
        this.provider = provider;
        this.parser = parser;
        this.valueFactory = valueFactory;
        this.repositoryConnection = repositoryConnection;
        this.archives = archives;
        this.providerDescriptor = providerDescriptor;
        this.metaserviceDescriptor = metaserviceDescriptor;
        repoSelect = this.repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL, "SELECT DISTINCT ?repositoryId ?repo ?time ?path ?metadata { graph ?metadata {?resource ?p ?o}.  ?metadata a <"+METASERVICE.METADATA+">;  <" + METASERVICE.SOURCE + "> ?repo; <" + METASERVICE.TIME + "> ?time; <" + METASERVICE.PATH + "> ?path; <"+METASERVICE.REPOSITORY_ID+"> ?repositoryId.}");

        loadedStatements = calculatePreloadedStatements();
    }

    private Set<Statement> calculatePreloadedStatements() throws RepositoryException {
        Repository resultRepository = createTempRepository();
        RepositoryConnection resultConnection = resultRepository.getConnection();

        loadNamespaces(resultConnection,providerDescriptor.getNamespaceList());
        loadOntologies(resultConnection,providerDescriptor.getLoadList());
        resultConnection.commit();
        HashSet<Statement> result  =new HashSet<>();
        Iterations.addAll(resultConnection.getStatements(null, null, null, true, NO_CONTEXT),result);
        return Collections.unmodifiableSet(result);
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
                Value idValue = set.getBinding("repositoryId").getValue();
                URI oldMetadata = (URI) set.getBinding("metadata").getValue();
                Date time = ((CalendarLiteralImpl)timeValue).calendarValue().toGregorianCalendar().getTime();
                String repo = repoValue.stringValue();
                String path = pathValue.stringValue();
                String repositoryId = idValue.stringValue();
                LOGGER.debug("READING repositoryId {}", repositoryId);
                LOGGER.debug("READING time {}", time);
                LOGGER.debug("READING repo {}", repo);
                LOGGER.debug("READING path {}", path);


                MetaserviceDescriptor.RepositoryDescriptor repositoryDescriptor = null;
                for(MetaserviceDescriptor.RepositoryDescriptor descriptor : metaserviceDescriptor.getRepositoryList()){
                     if(DescriptorHelper.getStringFromRepository(metaserviceDescriptor.getModuleInfo(),descriptor).equals(repositoryId)){
                         repositoryDescriptor = descriptor;
                         break;
                     }
                }
                if(repositoryDescriptor  == null){
                    LOGGER.error("Repository not found {} ", repositoryId );
                    return;
                }
                ArchiveAddress address = new ArchiveAddress(
                        DescriptorHelper.getStringFromRepository(metaserviceDescriptor.getModuleInfo(),repositoryDescriptor),
                        repo,
                        time,
                        path);
                address.setParameters(repositoryDescriptor.getProperties());
                refreshEntry(address, oldMetadata);
            }
        } catch (QueryEvaluationException e) {
            LOGGER.error("Could not Refresh" ,e);
        }
    }

    private void refreshEntry(ArchiveAddress archiveAddress, URI oldMetadata) {
        Archive archive = getArchive(archiveAddress.getArchiveUri());
        if(archive == null){
            return;
        }
        try{
            LOGGER.info("Starting to process " + archiveAddress);
            String content = archive.getContent(archiveAddress.getTime(),archiveAddress.getPath());
            if(content == null ||content.length() < 20){
                LOGGER.warn("Cannot process content '{}' of address {}, skipping.",content,archiveAddress );
                return;
            }

            Repository resultRepository = createTempRepository();
            RepositoryConnection resultConnection = resultRepository.getConnection();
            List<T> objects = parser.parse(content,archiveAddress);
            HashMap<String,String> parameters = new HashMap<>(archiveAddress.getParameters());
            URI metadata =  generateMetadata(archiveAddress, parameters);
            for(T object: objects){
                try {
                    provider.provideModelFor(object,resultConnection,parameters);
                } catch (ProviderException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            resultConnection.add(loadedStatements);
            resultConnection.commit();
            List<Statement> generatedStatements = getGeneratedStatements(resultConnection,loadedStatements);
            sendDataByLoad(metadata, generatedStatements);
            repositoryConnection.clear(oldMetadata);
            Set<URI> resourcesToPostProcess = getSubjects(generatedStatements);
            notifyPostProcessors(resourcesToPostProcess,new ArrayList<PostProcessingHistoryItem>(),archiveAddress.getTime(),null,null);
            LOGGER.info("done processing {} {}", archiveAddress.getTime(), archiveAddress.getPath());
        } catch (RepositoryException | ArchiveException e) {
            LOGGER.error("Could not Refresh {}" ,archiveAddress,e);
        } catch (MetaserviceException e) {
            e.printStackTrace();
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
            Repository resultRepository = createTempRepository();
            RepositoryConnection resultConnection = resultRepository.getConnection();
            List<T> objects = parser.parse(content,address);
            HashMap<String,String> parameters = new HashMap<>(address.getParameters());
            URI metadata =  generateMetadata(address,parameters);
            for(T object: objects){
                try {
                    provider.provideModelFor(object,resultConnection,parameters);
                } catch (ProviderException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            resultConnection.add(loadedStatements);
            resultConnection.commit();
            List<Statement> generatedStatements  = getGeneratedStatements(resultConnection,loadedStatements);
            sendDataByLoad(metadata, generatedStatements);
            Set<URI> resourcesToPostProcess = getSubjects(generatedStatements);
            notifyPostProcessors(resourcesToPostProcess,new ArrayList<PostProcessingHistoryItem>(),address.getTime(),null, null);
        } catch (RepositoryException | ArchiveException e) {
            LOGGER.error("Couldn't create {}",address,e);
        } catch (MetaserviceException e) {
            e.printStackTrace();
        }
    }

    private URI generateMetadata(ArchiveAddress address, HashMap<String, String> parameters) throws RepositoryException {
        // todo uniqueness in uri necessary
        URI metadata = valueFactory.createURI("http://metaservice.org/m/" + provider.getClass().getSimpleName() + "/" + System.currentTimeMillis());
        Value idLiteral = valueFactory.createLiteral(address.getRepository());
        Value pathLiteral = valueFactory.createLiteral(address.getPath());
        Value timeLiteral = valueFactory.createLiteral(address.getTime());
        Value repoLiteral = valueFactory.createLiteral(address.getArchiveUri());
        repositoryConnection.begin();
        repositoryConnection.add(metadata, RDF.TYPE, METASERVICE.METADATA, metadata);
        repositoryConnection.add(metadata, METASERVICE.PATH, pathLiteral, metadata);
        repositoryConnection.add(metadata, METASERVICE.TIME, timeLiteral,metadata);
        repositoryConnection.add(metadata, METASERVICE.SOURCE, repoLiteral,metadata);
        repositoryConnection.add(metadata, METASERVICE.REPOSITORY_ID, idLiteral,metadata);
        repositoryConnection.add(metadata, METASERVICE.CREATION_TIME, valueFactory.createLiteral(new Date()),metadata);
        repositoryConnection.add(metadata, METASERVICE.GENERATOR, valueFactory.createLiteral(DescriptorHelper.getStringFromProvider(metaserviceDescriptor.getModuleInfo(),providerDescriptor)),metadata);
        repositoryConnection.commit();
        parameters.put("metadata_path",address.getPath());
        parameters.put("metadata_time",address.getTime().toString());//todo fix format?
        parameters.put("metadata_source",address.getArchiveUri());
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
