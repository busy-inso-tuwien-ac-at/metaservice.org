package org.metaservice.core.dispatcher.postprocessor;

import info.aduna.iteration.Iterations;
import org.metaservice.api.MetaserviceException;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.core.AbstractDispatcher;
import org.openrdf.model.Statement;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ilo on 23.07.2014.
 */
@Singleton
public class LoadedStatements {
    private final MetaserviceDescriptor.OntologyLoaderDescriptor ontologyLoaderDescriptor;
    private final Logger LOGGER = LoggerFactory.getLogger(LoadedStatements.class);
    private Set<Statement> statements;

    @Inject
    public LoadedStatements(MetaserviceDescriptor.OntologyLoaderDescriptor ontologyLoaderDescriptor) throws MetaserviceException, RepositoryException {
        this.ontologyLoaderDescriptor = ontologyLoaderDescriptor;
        statements = calculatePreloadedStatements();
    }

    private Set<Statement> calculatePreloadedStatements() throws RepositoryException, MetaserviceException {
        Repository resultRepository = AbstractDispatcher.createTempRepository(true);
        RepositoryConnection resultConnection = resultRepository.getConnection();

        loadNamespaces(resultConnection, ontologyLoaderDescriptor.getNamespaceList());
        loadOntologies(resultConnection, ontologyLoaderDescriptor.getLoadList());
        resultConnection.commit();
        HashSet<Statement> result  =new HashSet<>();
        Iterations.addAll(resultConnection.getStatements(null, null, null, true), result);
        resultConnection.close();
        resultRepository.shutDown();
        return Collections.unmodifiableSet(result);
    }



    private void loadNamespaces(RepositoryConnection resultConnection, List<MetaserviceDescriptor.NamespaceDescriptor> namespaceList) throws RepositoryException {
        for(MetaserviceDescriptor.NamespaceDescriptor namespaceDescriptor: namespaceList){
            resultConnection.setNamespace(namespaceDescriptor.getPrefix(),namespaceDescriptor.getUri().toString());
        }
    }

    private void loadOntologies(RepositoryConnection con, List<MetaserviceDescriptor.LoadDescriptor> loadList){
        LOGGER.info("Loading " + loadList.size() + " Ontologies");
        for(MetaserviceDescriptor.LoadDescriptor loadDescriptor : loadList){
            try {
                LOGGER.info("Loading Ontology {}",loadDescriptor.getUrl());
                con.add(loadDescriptor.getUrl(),null,null);
            } catch (RDFParseException | IOException | RepositoryException e) {
                LOGGER.info("Couldn't load {}",loadDescriptor,e);
            }
        }
    }


    public Set<Statement> getStatements() {
        return statements;
    }
}
