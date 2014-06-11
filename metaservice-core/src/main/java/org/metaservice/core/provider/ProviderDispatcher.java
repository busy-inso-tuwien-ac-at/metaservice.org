package org.metaservice.core.provider;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import info.aduna.iteration.Iterations;
import org.metaservice.api.MetaserviceException;
import org.metaservice.api.archive.Archive;
import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.archive.ArchiveException;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.descriptors.DescriptorHelper;
import org.metaservice.api.rdf.vocabulary.METASERVICE;
import org.metaservice.api.parser.Parser;
import org.metaservice.api.provider.Provider;
import org.metaservice.api.provider.ProviderException;
import org.metaservice.core.AbstractDispatcher;
import org.metaservice.api.messaging.Config;
import org.metaservice.core.descriptor.DescriptorHelperImpl;
import org.metaservice.api.messaging.PostProcessingHistoryItem;
import org.metaservice.api.messaging.MessageHandler;
import org.openrdf.model.*;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.*;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.inject.Inject;
import java.util.*;

/**
 * Scope - Distributed
 * @param <T>
 */
public class ProviderDispatcher<T>  extends AbstractDispatcher<Provider<T>> implements org.metaservice.api.messaging.dispatcher.ProviderDispatcher{
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
    private final DescriptorHelper descriptorHelper;

    //TODO make repoMap dynamic - such that it may be cloned automatically, or use an existing copy
    // this could be achieved by using a hash to search in the local filesystem

    @Inject
    public ProviderDispatcher(
            MetaserviceDescriptor.ProviderDescriptor providerDescriptor,
            Provider<T> provider,
            Parser<T> parser,
            Set<Archive> archives,
            ValueFactory valueFactory,
            MessageHandler messageHandler,
            RepositoryConnection repositoryConnection,
            DescriptorHelper descriptorHelper,
            Config config,
            MetaserviceDescriptor metaserviceDescriptor) throws RepositoryException, MalformedQueryException{
        super(repositoryConnection, config, messageHandler, valueFactory, provider);
        this.provider = provider;
        this.parser = parser;
        this.valueFactory = valueFactory;
        this.repositoryConnection = repositoryConnection;
        this.archives = archives;
        this.providerDescriptor = providerDescriptor;
        this.metaserviceDescriptor = metaserviceDescriptor;
        this.descriptorHelper = descriptorHelper;
        repoSelect = this.repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL, "SELECT DISTINCT ?repositoryId ?source ?time ?path ?metadata { graph ?metadata {?resource ?p ?o}.  ?metadata a <"+METASERVICE.METADATA+">;  <" + METASERVICE.SOURCE + "> ?source; <" + METASERVICE.TIME + "> ?time; <" + METASERVICE.PATH + "> ?path; <"+METASERVICE.REPOSITORY_ID+"> ?repositoryId.}");

        loadedStatements = calculatePreloadedStatements();
    }

    private Set<Statement> calculatePreloadedStatements() throws RepositoryException {
        Repository resultRepository = createTempRepository();
        RepositoryConnection resultConnection = resultRepository.getConnection();

        loadNamespaces(resultConnection,providerDescriptor.getNamespaceList());
        loadOntologies(resultConnection,providerDescriptor.getLoadList());
        resultConnection.commit();
        HashSet<Statement> result  =new HashSet<>();
        Iterations.addAll(resultConnection.getStatements(null, null, null, true, NO_CONTEXT), result);
        return Collections.unmodifiableSet(result);
    }

    @Override
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
                Value sourceValue = set.getBinding("source").getValue();
                Value pathValue = set.getBinding("path").getValue();
                Value idValue = set.getBinding("repositoryId").getValue();
                URI oldMetadata = (URI) set.getBinding("metadata").getValue();
                Date time = XMLGregorianCalendarImpl.parse(timeValue.stringValue()).toGregorianCalendar().getTime();
                String source = sourceValue.stringValue();
                String path = pathValue.stringValue();
                String repositoryId = idValue.stringValue();
                LOGGER.debug("READING repositoryId {}", repositoryId);
                LOGGER.debug("READING time {}", time);
                LOGGER.debug("READING source {}", source);
                LOGGER.debug("READING path {}", path);


                MetaserviceDescriptor.RepositoryDescriptor repositoryDescriptor = null;
                for(MetaserviceDescriptor.RepositoryDescriptor descriptor : metaserviceDescriptor.getRepositoryList()){
                     if(descriptor.getId().equals(repositoryId)){
                         repositoryDescriptor = descriptor;
                         break;
                     }
                }
                if(repositoryDescriptor  == null){
                    LOGGER.error("Repository not found {} ", repositoryId );
                    return;
                }
                ArchiveAddress address = new ArchiveAddress(
                        repositoryDescriptor.getId(),
                        source,
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
            Archive.Contents contents = archive.getContent(archiveAddress.getTime(),archiveAddress.getPath());
            if(contents == null ||contents.now == null ||contents.prev == null){
                LOGGER.warn("Cannot process content  of address {}, skipping.",archiveAddress );
                return;
            }
            List<Statement> nowGeneratedStatements;
            List<Statement> prevGeneratedStatements;
            if (contents.now == null || contents.now.length() < 20){
                nowGeneratedStatements = new ArrayList<>();
            }  else {
                nowGeneratedStatements  = getStatements(contents.now, archiveAddress);

            }
            if (contents.prev == null || contents.prev.length() < 20){
                prevGeneratedStatements = new ArrayList<>();
            }  else {
                prevGeneratedStatements  = getStatements(contents.prev, archiveAddress);
            }
            URI metadataAdd =  generateMetadata(archiveAddress, repositoryConnection,"add"); //todo
            URI metadataRemove =  generateMetadata(archiveAddress,repositoryConnection,"remove");//todo

            HashSet<Statement> nowSet = new HashSet<>();
            nowSet.addAll(nowGeneratedStatements);
            HashSet<Statement> prevSet = new HashSet<>();
            prevSet.addAll(prevGeneratedStatements);

            HashSet<Statement> addedSet = new HashSet<>(nowSet);
            nowSet.removeAll(prevSet);
            HashSet<Statement> removedSet = new HashSet<>(prevSet);
            nowSet.removeAll(nowSet);


            sendDataByLoad(metadataAdd, addedSet);
            sendDataByLoad(metadataRemove, removedSet);
            repositoryConnection.clear(oldMetadata);//todo which?
            Set<URI> resourcesToPostProcess = getSubjects(nowGeneratedStatements);
            resourcesToPostProcess.addAll(getSubjects(prevGeneratedStatements));
            notifyPostProcessors(resourcesToPostProcess,new ArrayList<PostProcessingHistoryItem>(),archiveAddress.getTime(),null,null);
            LOGGER.info("done processing {} {}", archiveAddress.getTime(), archiveAddress.getPath());
        } catch (RepositoryException | ArchiveException e) {
            LOGGER.error("Could not Refresh {}" ,archiveAddress,e);
        } catch (MetaserviceException e) {
            e.printStackTrace();
        }
    }

    private List<Statement> getStatements(String now, ArchiveAddress archiveAddress) throws RepositoryException {
        Repository tempRepository = createTempRepository();
        RepositoryConnection tempRepositoryConnection = tempRepository.getConnection();
        List<T> objects = parser.parse(now,archiveAddress);
        HashMap<String,String> parameters = new HashMap<>(archiveAddress.getParameters());
        copyMetadataToProperty(archiveAddress,parameters);
        for(T object: objects){
            try {
                provider.provideModelFor(object,tempRepositoryConnection,parameters);
            } catch (ProviderException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        tempRepositoryConnection.add(loadedStatements);
        tempRepositoryConnection.commit();
        List<Statement> generatedStatements = getGeneratedStatements(tempRepositoryConnection,loadedStatements);
        return generatedStatements;
    }

    @Override
    public void create(ArchiveAddress archiveAddress){
        Archive archive = getArchive(archiveAddress.getArchiveUri());
        if(archive == null){
            return;
        }

        try {
            LOGGER.info("Starting to process " + archiveAddress);
            Archive.Contents contents = archive.getContent(archiveAddress.getTime(),archiveAddress.getPath());
            if(contents == null){
                LOGGER.warn("Cannot process content  of address {}, skipping.",archiveAddress );
                return;
            }
            List<Statement> nowGeneratedStatements;
            List<Statement> prevGeneratedStatements;
            if (contents.now == null || contents.now.length() < 20){
                nowGeneratedStatements = new ArrayList<>();
            }  else {
                nowGeneratedStatements  = getStatements(contents.now, archiveAddress);

            }
            if (contents.prev == null || contents.prev.length() < 20){
                prevGeneratedStatements = new ArrayList<>();
            }  else {
                prevGeneratedStatements  = getStatements(contents.prev, archiveAddress);

            }
            URI metadataAdd =  generateMetadata(archiveAddress, repositoryConnection,"add"); //todo
            URI metadataRemove =  generateMetadata(archiveAddress,repositoryConnection,"remove");//todo

            HashSet<Statement> nowSet = new HashSet<>();
            nowSet.addAll(nowGeneratedStatements);
            HashSet<Statement> prevSet = new HashSet<>();
            prevSet.addAll(prevGeneratedStatements);

            HashSet<Statement> addedSet = new HashSet<>(nowSet);
            nowSet.removeAll(prevSet);
            HashSet<Statement> removedSet = new HashSet<>(prevSet);
            nowSet.removeAll(nowSet);


            sendDataByLoad(metadataAdd, addedSet);
            sendDataByLoad(metadataRemove, removedSet);
            Set<URI> resourcesToPostProcess = getSubjects(nowGeneratedStatements);
            resourcesToPostProcess.addAll(getSubjects(prevGeneratedStatements));
            notifyPostProcessors(resourcesToPostProcess,new ArrayList<PostProcessingHistoryItem>(),archiveAddress.getTime(),null,null);
            LOGGER.info("done processing {} {}", archiveAddress.getTime(), archiveAddress.getPath());
        } catch (RepositoryException | ArchiveException e) {
            LOGGER.error("Couldn't create {}",archiveAddress,e);
        } catch (MetaserviceException e) {
            e.printStackTrace();
        }
    }

    private URI generateMetadata(ArchiveAddress address, RepositoryConnection resultRepositoryConnection,String action) throws RepositoryException {
        // todo uniqueness in uri necessary
        URI metadata = valueFactory.createURI("http://metaservice.org/m/" + provider.getClass().getSimpleName() + "/" + System.currentTimeMillis());
        Value idLiteral = valueFactory.createLiteral(address.getRepository());
        Value pathLiteral = valueFactory.createLiteral(address.getPath());
        Value timeLiteral = valueFactory.createLiteral(address.getTime());
        Value repoLiteral = valueFactory.createLiteral(address.getArchiveUri());
        resultRepositoryConnection.begin();
        resultRepositoryConnection.add(metadata, RDF.TYPE, METASERVICE.METADATA, metadata);
        resultRepositoryConnection.add(metadata, METASERVICE.ACTION, valueFactory.createLiteral(action), metadata);
        resultRepositoryConnection.add(metadata, METASERVICE.PATH, pathLiteral, metadata);
        resultRepositoryConnection.add(metadata, METASERVICE.TIME, timeLiteral, metadata);
        resultRepositoryConnection.add(metadata, METASERVICE.SOURCE, repoLiteral, metadata);
        resultRepositoryConnection.add(metadata, METASERVICE.REPOSITORY_ID, idLiteral, metadata);
        resultRepositoryConnection.add(metadata, METASERVICE.CREATION_TIME, valueFactory.createLiteral(new Date()), metadata);
        resultRepositoryConnection.add(metadata, METASERVICE.GENERATOR, valueFactory.createLiteral(descriptorHelper.getStringFromProvider(metaserviceDescriptor.getModuleInfo(), providerDescriptor)), metadata);
        resultRepositoryConnection.commit();
        return metadata;
    }

    private void copyMetadataToProperty(ArchiveAddress archiveAddress,HashMap<String, String> parameters){
        parameters.put("metadata_path",archiveAddress.getPath());
        parameters.put("metadata_time",archiveAddress.getTime().toString());//todo fix format?
        parameters.put("metadata_source",archiveAddress.getArchiveUri());

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
