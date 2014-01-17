package org.metaservice.core.rdf;

import com.google.inject.Provider;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import javax.inject.Inject;

public class RepositoryConnectionProvider implements Provider<RepositoryConnection> {
    private final Repository repository;

    @Inject
    public RepositoryConnectionProvider(Repository repository) {
        this.repository = repository;
    }

    @Override
    public RepositoryConnection get() {
        try {
            return repository.getConnection();
            //TODO exception handling?
        } catch (RepositoryException e) {
            e.printStackTrace();
            return null;
        }
    }
}
