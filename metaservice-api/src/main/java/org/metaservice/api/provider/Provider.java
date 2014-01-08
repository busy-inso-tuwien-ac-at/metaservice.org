package org.metaservice.api.provider;

import org.openrdf.model.Model;

public interface Provider<T>{
    public Model provideModelFor(T o) throws ProviderException;
}
