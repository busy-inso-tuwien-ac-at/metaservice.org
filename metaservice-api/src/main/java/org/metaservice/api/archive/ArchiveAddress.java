/*
 * Copyright 2015 Nikola Ilo
 * Research Group for Industrial Software, Vienna University of Technology, Austria
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaservice.api.archive;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class ArchiveAddress implements Serializable{
    private static final long serialVersionUID = -5874232640077304953L;
    private String repository;
    private String archiveUri;
    private Date time;
    private String path;
    private String type;
    private HashMap<String,String> parameters = new HashMap<>();

    public HashMap<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }

    private ArchiveAddress(){}
    public ArchiveAddress(String repository,String archiveUri, Date time, String path,String type) {
        this.repository = repository;
        this.archiveUri = archiveUri;
        this.time = time;
        this.path = path;
        this.type = type;
    }

    public String getArchiveUri() {
        return archiveUri;
    }

    public void setArchiveUri(String archiveUri) {
        this.archiveUri = archiveUri;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ArchiveAddress{" +
                "repository='" + repository + '\'' +
                ", archiveUri='" + archiveUri + '\'' +
                ", time=" + time +
                ", path='" + path + '\'' +
                ", type='" + type + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
