package org.metaservice.api.postprocessor;

import org.openrdf.model.URI;


public interface PostProcessor {
    public void process(URI uri) throws PostProcessorException;

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
    public boolean abortEarly(URI uri) throws PostProcessorException;
}
