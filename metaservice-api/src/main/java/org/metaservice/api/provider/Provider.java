package org.metaservice.api.provider;

import org.openrdf.model.URI;

public interface Provider<T>{

    public void refresh(URI uri);
    public void create(T o) throws ProviderException;
}
