package org.metaservice.core.rdf;

import com.google.inject.Provider;
import org.metaservice.core.Config;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sparql.SPARQLRepository;

import javax.inject.Inject;

public class SPARQLRepositoryProvider implements Provider<SPARQLRepository> {
    private final Config config;
    @Inject
    public SPARQLRepositoryProvider(Config config){
        this.config = config;
    }
    @Override
    public SPARQLRepository get() {
        SPARQLRepository sparqlRepository = new SPARQLRepository(config.getSparqlEndpoint());
        try {
            sparqlRepository.initialize();
            return sparqlRepository;

        } catch (RepositoryException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }
    }
}
