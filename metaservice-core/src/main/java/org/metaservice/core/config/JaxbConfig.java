package org.metaservice.core.config;

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

    @XmlElement
    public String getDumpRDFDirectory() {
        return dumpRDFDirectory;
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
