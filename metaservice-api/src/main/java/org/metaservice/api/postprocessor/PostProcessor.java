package org.metaservice.api.postprocessor;

import org.jetbrains.annotations.NotNull;
import org.openrdf.model.URI;
import org.openrdf.repository.RepositoryConnection;


public interface PostProcessor {
    public void process(@NotNull final URI uri,@NotNull final RepositoryConnection resultConnection) throws PostProcessorException;

    /**
     * This method should not do the same as process, but be a lightweight check.
     *
     * It is ok to:
     * * Check if uri is processable
     * * ???
     * @param uri
     * @return
     * @throws PostProcessorException
     */
    public boolean abortEarly(@NotNull final URI uri) throws PostProcessorException;
}
