package org.metaservice.core.postprocessor;

import org.openrdf.model.URI;

import java.io.Serializable;

/**
 * Created by ilo on 17.01.14.
 */
public class PostProcessingHistoryItem implements Serializable{
    private static final long serialVersionUID = -5274232640077302343L;

    public PostProcessingHistoryItem(String postprocessorId, URI[] resources) {
        this.postprocessorId = postprocessorId;
        this.resources = resources;
    }

    public URI[] getResources() {
        return resources;
    }

    public String getPostprocessorId() {
        return postprocessorId;
    }

    private final String postprocessorId;
    private final URI[] resources;
}
