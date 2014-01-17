package org.metaservice.core.postprocessor;

import org.openrdf.model.URI;

import java.io.Serializable;

/**
 * Created by ilo on 17.01.14.
 */
public class PostProcessingHistoryItem implements Serializable{
    private static final long serialVersionUID = -5274232640077304953L;

    public PostProcessingHistoryItem(String postprocessorId, URI resource) {
        this.postprocessorId = postprocessorId;
        this.resource = resource;
    }

    public URI getResource() {

        return resource;
    }

    public String getPostprocessorId() {
        return postprocessorId;
    }

    private final String postprocessorId;
    private final URI resource;
}
