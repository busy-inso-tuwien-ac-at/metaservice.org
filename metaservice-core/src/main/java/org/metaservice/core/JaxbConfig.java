package org.metaservice.core;

import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.core.descriptor.JAXBMetaserviceDescriptorImpl;
import org.metaservice.core.descriptor.MetaserviceDescriptorImpl;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.List;

@XmlRootElement(name = "metaservice-config")
public class JaxbConfig implements Config {

    public static void main(String[] args) throws JAXBException {
        JaxbConfig jaxbConfig = new JaxbConfig();
        ProductionConfig productionConfig = new ProductionConfig();
        jaxbConfig.setArchiveBasePath(productionConfig.getArchiveBasePath());
        jaxbConfig.setBatchSize(productionConfig.getBatchSize());
        jaxbConfig.setDumpRDFBeforeLoad(productionConfig.isDumpRDFBeforeLoad());
        jaxbConfig.setDumpRDFDirectory(productionConfig.getDumpRDFDirectory());
        jaxbConfig.setHttpdDataDirectory(productionConfig.getHttpdDataDirectory());
        jaxbConfig.setJmsBroker(productionConfig.getJmsBroker());
        jaxbConfig.setSparqlEndpoint(productionConfig.getSparqlEndpoint());

        JAXBContext jc = JAXBContext.newInstance(JaxbConfig.class, JAXBMetaserviceDescriptorImpl.class);

        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        JAXB.marshal(jaxbConfig,new File("metaservice-config.xml"));
    }

    private String sparqlEndpoint;
    private String jmsBroker;
    private String archiveBasePath;
    private String httpdDataDirectory;
    private int batchSize;
    private boolean dumpRDFBeforeLoad;
    private String dumpRDFDirectory;

    @XmlElement(name="metaservice",type = JAXBMetaserviceDescriptorImpl.class)
    @XmlElementWrapper( name="installed" )
    private List<MetaserviceDescriptor> installed;

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

    @XmlElement
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

    @XmlAttribute
    public String getHttpdDataDirectory() {
        return httpdDataDirectory;
    }

    public void setHttpdDataDirectory(String httpdDataDirectory) {
        this.httpdDataDirectory = httpdDataDirectory;
    }

    @XmlAttribute
    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    @XmlAttribute
    public boolean isDumpRDFBeforeLoad() {
        return dumpRDFBeforeLoad;
    }

    public void setDumpRDFBeforeLoad(boolean dumpRDFBeforeLoad) {
        this.dumpRDFBeforeLoad = dumpRDFBeforeLoad;
    }
}
