package org.metaservice.api.provider;

import org.jetbrains.annotations.NotNull;
import org.openrdf.repository.RepositoryConnection;

import java.util.HashMap;

public interface Provider<T>{
    public void provideModelFor(@NotNull T o,@NotNull RepositoryConnection resultConnection,@NotNull HashMap<String,String> properties) throws ProviderException;
}
