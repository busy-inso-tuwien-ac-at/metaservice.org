package org.metaservice.core.postprocessor;

import org.jetbrains.annotations.NotNull;
import org.openrdf.model.URI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ilo on 17.01.14.
 */
public class PostProcessingTask implements Serializable {
    private static final long serialVersionUID = -13895398349827644l;

    public PostProcessingTask(@NotNull URI changedURI,@NotNull Date time) {
        this.changedURI = changedURI;
        this.time = time;
    }

    @NotNull
    public URI getChangedURI() {
        return changedURI;
    }

    @NotNull
    public Date getTime() {return  time;}
    @NotNull
    public ArrayList<PostProcessingHistoryItem> getHistory() {
        return history;
    }

    private final URI changedURI;
    private final Date time;
    private final ArrayList<PostProcessingHistoryItem> history = new ArrayList<>();
}
