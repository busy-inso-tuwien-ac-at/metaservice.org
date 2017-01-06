/*
 * Copyright 2015 Nikola Ilo
 * Research Group for Industrial Software, Vienna University of Technology, Austria
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaservice.core.provider;

import com.google.common.collect.Sets;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.jetbrains.annotations.NotNull;
import org.metaservice.api.MetaserviceException;
import org.metaservice.api.archive.Archive;
import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.descriptors.DescriptorHelper;
import org.metaservice.api.rdf.vocabulary.METASERVICE;
import org.metaservice.api.provider.Provider;
import org.metaservice.api.provider.ProviderException;
import org.metaservice.core.AbstractDispatcher;
import org.metaservice.api.messaging.Config;
import org.metaservice.api.messaging.PostProcessingHistoryItem;
import org.metaservice.api.messaging.MessageHandler;
import org.metaservice.core.dispatcher.postprocessor.LoadedStatements;
import org.openrdf.model.*;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.*;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.inject.Inject;
import java.io.Reader;
import java.util.*;

/**
 * Scope - Distributed
 * @param <T>
 */
public class ProviderDispatcher<T>  extends AbstractDispatcher implements org.metaservice.api.messaging.dispatcher.ProviderDispatcher{
    private final Logger LOGGER = LoggerFactory.getLogger(ProviderDispatcher.class);

    private final MetaserviceDescriptor metaserviceDescriptor;
    private final MetaserviceDescriptor.ProviderDescriptor providerDescriptor;
    private final Provider<T> provider;
    private final ParserFactory<T> parserFactory;

    private final ValueFactory valueFactory;
    private final RepositoryConnection repositoryConnection;
    private final TupleQuery repoSelect;
    private final TupleQuery oldGraphSelect;
    private final Set<Archive> archives;

    private final Set<Statement> loadedStatements;
    private final DescriptorHelper descriptorHelper;

    //TODO make repoMap dynamic - such that it may be cloned automatically, or use an existing copy
    // this could be achieved by using a hash to search in the local filesystem

    @Inject
    public ProviderDispatcher(
            MetaserviceDescriptor.ProviderDescriptor providerDescriptor,
            Provider<T> provider,
            ParserFactory<T> parserFactory,
            Set<Archive> archives,
            ValueFactory valueFactory,
            MessageHandler messageHandler,
            RepositoryConnection repositoryConnection,
            DescriptorHelper descriptorHelper,
            Config config,
            LoadedStatements loadedStatements,
            MetaserviceDescriptor metaserviceDescriptor) throws RepositoryException, MalformedQueryException, MetaserviceException {
        super(config, messageHandler);
        this.provider = provider;
        this.parserFactory = parserFactory;
        this.valueFactory = valueFactory;
        this.repositoryConnection = repositoryConnection;
        this.archives = archives;
        this.providerDescriptor = providerDescriptor;
        this.metaserviceDescriptor = metaserviceDescriptor;
        this.descriptorHelper = descriptorHelper;
        repoSelect = this.repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL, "SELECT DISTINCT ?repositoryId ?source ?time ?path ?metadata { graph ?metadata {?resource ?p ?o}.  ?metadata a <"+METASERVICE.OBSERVATION+">;  <" + METASERVICE.SOURCE + "> ?source; <" + METASERVICE.DATA_TIME + "> ?time; <" + METASERVICE.PATH + "> ?path; <"+METASERVICE.SOURCE_PROPERTY+"> ?repositoryId.}");
        oldGraphSelect = this.repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,"SELECT DISTINCT ?metadata { ?metadata a <"+METASERVICE.OBSERVATION+">; <"+ METASERVICE.SOURCE + "> ?source; <" + METASERVICE.DATA_TIME + "> ?time; <" + METASERVICE.PATH + "> ?path; <"+METASERVICE.SOURCE_PROPERTY+"> ?repositoryId.}");
        this.loadedStatements = loadedStatements.getStatements();
    }


    @Override
    public void refresh(URI uri) {
        try {
            AbstractDispatcher.recoverSparqlConnection(repositoryConnection);
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
                        path,
                        repositoryDescriptor.getType());
                address.setParameters(repositoryDescriptor.getProperties());
                Set<URI> metadataToDelete = new HashSet<>();
                metadataToDelete.add(oldMetadata);
                refreshEntry(address,metadataToDelete);
            }
        } catch (QueryEvaluationException e) {
            LOGGER.error("Could not Refresh" ,e);
        } catch (MetaserviceException e) {
            LOGGER.warn("check me",e);
        }
    }

    /**
     * this is a long running and heap intensive method. therefore, collections are explicitly nulled when not used any more
     * @param archiveAddress
     * @param oldMetadata
     */
    private void refreshEntry(ArchiveAddress archiveAddress,@NotNull Set<URI> oldMetadata) {
        Archive archive = getArchive(archiveAddress.getArchiveUri());
        if(archive == null){
            return;
        }
        try{
            LOGGER.info("Starting to process " + archiveAddress);
            Archive.Contents contents = archive.getContent(archiveAddress.getTime(),archiveAddress.getPath());
            if(contents == null){
                LOGGER.warn("Cannot process content  of address {}, skipping.",archiveAddress );
                return;
            }
            List<Statement> nowGeneratedStatements;
            List<Statement> prevGeneratedStatements;
            if (contents.now == null){
                nowGeneratedStatements = new ArrayList<>();
            }  else {
                nowGeneratedStatements  = getStatements(contents.now, archiveAddress);
            }
            //let gc collect contents.now
            contents.now = null;
            if (contents.prev == null){
                prevGeneratedStatements = new ArrayList<>();
            }  else {
                prevGeneratedStatements  = getStatements(contents.prev, archiveAddress);
            }
            //let gc collect rest of contents
            contents = null;
            LOGGER.debug("finished parsing");
            URI metadataAdd =  generateMetadata(archiveAddress, repositoryConnection,METASERVICE.ADD_OBSERVATION);
            URI metadataRemove =  generateMetadata(archiveAddress,repositoryConnection,METASERVICE.REMOVE_OBSERVATION);

            HashSet<Statement> nowSet = new HashSet<>();
            nowSet.addAll(nowGeneratedStatements);
            //noinspection UnusedAssignment
            nowGeneratedStatements = null;
            HashSet<Statement> prevSet = new HashSet<>();
            prevSet.addAll(prevGeneratedStatements);
            //noinspection UnusedAssignment
            prevGeneratedStatements = null;

            Sets.SetView<Statement> intersection = Sets.intersection(prevSet,nowSet);
            HashSet<Statement> addedSet = new HashSet<>(nowSet);
            addedSet.removeAll(intersection);
            HashSet<Statement> removedSet = new HashSet<>(prevSet);
            removedSet.removeAll(intersection);
            //noinspection UnusedAssignment
            nowSet = null;
            //noinspection UnusedAssignment
            prevSet = null;

            sendDataByLoad(metadataAdd, addedSet,loadedStatements);
            sendDataByLoad(metadataRemove, removedSet,loadedStatements);
            if( oldMetadata.size()>0) {
                LOGGER.info("clearing {} {}",oldMetadata.size(), oldMetadata);
                repositoryConnection.clear(oldMetadata.toArray(new URI[oldMetadata.size()]));
            }
            Set<URI> resourcesToPostProcess = Sets.union(Sets.union(getSubjects(addedSet),getSubjects(removedSet)),Sets.union(getURIObject(addedSet),getURIObject(removedSet)));
            notifyPostProcessors(resourcesToPostProcess,new ArrayList<PostProcessingHistoryItem>(),archiveAddress.getTime(),null,null);
            LOGGER.info("done processing {} {}", archiveAddress.getTime(), archiveAddress.getPath());
        } catch (MetaserviceException|RepositoryException  e) {
            LOGGER.error("Could not process {}" ,archiveAddress,e);
        }
    }

    private List<Statement> getStatements(Reader now, ArchiveAddress archiveAddress) throws RepositoryException, MetaserviceException {
        Repository tempRepository = createTempRepository(false);
        RepositoryConnection tempRepositoryConnection = tempRepository.getConnection();
        LOGGER.debug("started parsing type {}",archiveAddress.getType());
        List<T> objects = parserFactory.getParser(archiveAddress.getType()).parse(now,archiveAddress);
        LOGGER.debug("end parsing {} objects" , objects.size());
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
        tempRepositoryConnection.close();
        tempRepository.shutDown();
        return generatedStatements;
    }

    @Override
    public void create(ArchiveAddress archiveAddress){
        try {
            AbstractDispatcher.recoverSparqlConnection(repositoryConnection);
        } catch (MetaserviceException e) {
            LOGGER.warn("checkme",e);
        }
        oldGraphSelect.setBinding("time",valueFactory.createLiteral(archiveAddress.getTime()));
        oldGraphSelect.setBinding("source",valueFactory.createLiteral(archiveAddress.getArchiveUri()));
        oldGraphSelect.setBinding("repositoryId",valueFactory.createLiteral(archiveAddress.getRepository()));
        oldGraphSelect.setBinding("path",valueFactory.createLiteral(archiveAddress.getPath()));
        HashSet<URI> oldGraphs = new HashSet<>();
        try {
            TupleQueryResult result = oldGraphSelect.evaluate();


            while (result.hasNext()){
                oldGraphs.add((URI) result.next().getBinding("metadata").getValue());
            }
        } catch (QueryEvaluationException e) {
            LOGGER.error("couldn't determine old graphs to delete");
        }
        refreshEntry(archiveAddress, oldGraphs);
    }

    private URI generateMetadata(ArchiveAddress address, RepositoryConnection resultRepositoryConnection,URI subType) throws RepositoryException {
        // todo uniqueness in uri necessary
        URI metadata = valueFactory.createURI("http://metaservice.org/m/" + provider.getClass().getSimpleName() + "/" + System.currentTimeMillis());
        Value idLiteral = valueFactory.createLiteral(address.getRepository());
        Value pathLiteral = valueFactory.createLiteral(address.getPath());
        Value timeLiteral = valueFactory.createLiteral(address.getTime());
        Value repoLiteral = valueFactory.createLiteral(address.getArchiveUri());
        resultRepositoryConnection.begin();
        resultRepositoryConnection.add(metadata, RDF.TYPE, METASERVICE.OBSERVATION,metadata);
        resultRepositoryConnection.add(metadata, RDF.TYPE, subType, metadata);
        resultRepositoryConnection.add(metadata, METASERVICE.PATH, pathLiteral, metadata);
        resultRepositoryConnection.add(metadata, METASERVICE.DATA_TIME, timeLiteral, metadata);
        resultRepositoryConnection.add(metadata, METASERVICE.SOURCE, repoLiteral, metadata);
        //TODO probably introduced a bug by uncommenting
        //resultRepositoryConnection.add(metadata, METASERVICE.SOURCE_SUBJECT, valueFactory.createLiteral(address.getArchiveUri()+ address.getPath()), metadata);
        resultRepositoryConnection.add(metadata, METASERVICE.SOURCE_PROPERTY, idLiteral, metadata);
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
