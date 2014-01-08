package org.metaservice.api.archive;

import java.io.Serializable;

public class ArchiveAddress implements Serializable{
    private String archiveUri;
    private String time;
    private String path;

    public ArchiveAddress(String archiveUri, String time, String path) {
        this.archiveUri = archiveUri;
        this.time = time;
        this.path = path;
    }

    public String getArchiveUri() {
        return archiveUri;
    }

    public void setArchiveUri(String archiveUri) {
        this.archiveUri = archiveUri;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
