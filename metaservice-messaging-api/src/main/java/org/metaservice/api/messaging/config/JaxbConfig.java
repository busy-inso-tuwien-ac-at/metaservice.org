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

package org.metaservice.api.messaging.config;

import org.metaservice.api.messaging.Config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "metaservice-config")
public class JaxbConfig implements Config {
    private String sparqlEndpoint;
    private String jmsBroker;
    private String archiveBasePath;
    private String httpdDataDirectory;
    private int batchSize;
    private boolean dumpRDFBeforeLoad;
    private String dumpRDFDirectory;
    private String defaultProviderOpts;
    private String defaultPostProcessorOpts;

    @XmlElement
    public String getDumpRDFDirectory() {
        return dumpRDFDirectory;
    }

    @Override
    @XmlElement
    public String getDefaultProviderOpts() {
        if(defaultProviderOpts == null){
            defaultProviderOpts = "";
        }
        return defaultProviderOpts;
    }

    @Override
    @XmlElement
    public String getDefaultPostProcessorOpts() {
        if(defaultPostProcessorOpts == null){
            defaultPostProcessorOpts = "";
        }
        return defaultPostProcessorOpts;
    }

    public void setDefaultPostProcessorOpts(String defaultPostProcessorOpts) {
        this.defaultPostProcessorOpts = defaultPostProcessorOpts;
    }

    public void setDefaultProviderOpts(String defaultProviderOpts) {

        this.defaultProviderOpts = defaultProviderOpts;
    }

    public void setDumpRDFDirectory(String dumpRDFDirectory) {
        this.dumpRDFDirectory = dumpRDFDirectory;
    }

    @XmlElement
    public String getSparqlEndpoint() {
        return sparqlEndpoint;
    }

    public void setSparqlEndpoint(String sparqlEndpoint) {
        this.sparqlEndpoint = sparqlEndpoint;
    }
    @XmlElement
    public String getJmsBroker() {
        return jmsBroker;
    }

    public List<String> getArchivesForProvider(String provider) {
        return null;
    }

    public void setJmsBroker(String jmsBroker) {
        this.jmsBroker = jmsBroker;
    }

    @XmlElement
    public String getArchiveBasePath() {
        return archiveBasePath;
    }

    public void setArchiveBasePath(String archiveBasePath) {
        this.archiveBasePath = archiveBasePath;
    }

    @XmlElement
    public String getHttpdDataDirectory() {
        return httpdDataDirectory;
    }

    public void setHttpdDataDirectory(String httpdDataDirectory) {
        this.httpdDataDirectory = httpdDataDirectory;
    }

    @XmlElement
    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    @XmlElement
    public boolean getDumpRDFBeforeLoad() {
        return dumpRDFBeforeLoad;
    }

    public void setDumpRDFBeforeLoad(boolean dumpRDFBeforeLoad) {
        this.dumpRDFBeforeLoad = dumpRDFBeforeLoad;
    }
}
