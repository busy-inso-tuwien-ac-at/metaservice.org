package org.metaservice.api.provider;

import org.openrdf.repository.RepositoryConnection;

public interface Provider<T>{
    public void provideModelFor(T o, RepositoryConnection resultConnection) throws ProviderException;
}
