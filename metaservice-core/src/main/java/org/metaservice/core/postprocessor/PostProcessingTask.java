package org.metaservice.core.postprocessor;

import org.openrdf.model.URI;

import java.util.ArrayList;

/**
 * Created by ilo on 17.01.14.
 */
public class PostProcessingTask {

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
