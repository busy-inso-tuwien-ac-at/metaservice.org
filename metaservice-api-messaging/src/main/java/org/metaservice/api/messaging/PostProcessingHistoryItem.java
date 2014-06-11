package org.metaservice.api.messaging;

import org.openrdf.model.URI;

import java.io.Serializable;

/**
 * Created by ilo on 17.01.14.
 */
public class PostProcessingHistoryItem implements Serializable{
    private static final long serialVersionUID = -5274232640077302343L;

    private PostProcessingHistoryItem(){}
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

    private String postprocessorId;
    private URI[] resources;
}
