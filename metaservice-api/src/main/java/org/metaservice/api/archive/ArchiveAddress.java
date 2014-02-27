package org.metaservice.api.archive;

import java.io.Serializable;
import java.util.HashMap;

public class ArchiveAddress implements Serializable{
    private static final long serialVersionUID = -5874232640077304953L;
    private String repository;
    private String archiveUri;
    private String time;
    private String path;
    private HashMap<String,String> parameters = new HashMap<>();

    public HashMap<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }

    public ArchiveAddress(String repository,String archiveUri, String time, String path) {
        this.repository = repository;
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

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    @Override
    public String toString() {
        return "ArchiveAddress{" +
                "repository='" + repository + '\'' +
                ", archiveUri='" + archiveUri + '\'' +
                ", time='" + time + '\'' +
                ", path='" + path + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
