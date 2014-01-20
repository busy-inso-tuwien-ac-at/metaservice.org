package org.metaservice.core.postprocessor;

import org.openrdf.model.URI;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ilo on 17.01.14.
 */
public class PostProcessingTask implements Serializable {
    private static final long serialVersionUID = -13895398349828434l;

    public PostProcessingTask(URI changedURI) {
        this.changedURI = changedURI;
    }

    public URI getChangedURI() {
        return changedURI;
    }

    public ArrayList<PostProcessingHistoryItem> getHistory() {
        return history;
    }

    private final URI changedURI;
    private final ArrayList<PostProcessingHistoryItem> history = new ArrayList<>();
}
