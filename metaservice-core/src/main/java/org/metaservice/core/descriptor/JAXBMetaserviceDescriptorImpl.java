package org.metaservice.core.descriptor;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.metaservice.api.descriptor.MetaserviceDescriptor;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ilo on 15.01.14.
 */
@XmlRootElement(name="metaservice")
public class JAXBMetaserviceDescriptorImpl implements MetaserviceDescriptor {
    private ModuleInfo moduleInfo;
    private List<ProviderDescriptor> providerList = new ArrayList<>();
    private List<PostProcessorDescriptor> postProcessorList = new ArrayList<>();
    private List<TemplateDescriptor> templateList = new ArrayList<>();
    private List<CrawlerDescriptor> crawlerList = new ArrayList<>();
    private List<RepositoryDescriptor> repositoryList = new ArrayList<>();

    @Override
    public String toString() {
        return "JAXBMetaserviceDescriptorImpl{" +
                "providerList=" + providerList +
                ", \npostProcessorList=" + postProcessorList +
                ", \ntemplateList=" + templateList +
                ", \ncrawlerList=" + crawlerList +
                ", \nrepositoryList=" + repositoryList +
                ", \nparserList=" + parserList +
                '}';
    }

    public static void main(String[] args) throws FileNotFoundException {
        StringWriter w = new StringWriter();
        MetaserviceDescriptor descriptor = JAXB.unmarshal(new FileInputStream(new File("C:\\Users\\ilo\\dev\\metaservice.org\\metaservice-core-deb\\src\\main\\resources\\metaservice.xml")),JAXBMetaserviceDescriptorImpl.class);
        // System.err.println(descriptor.getCrawlerList());
        // System.err.println(descriptor.getTemplateList());
        // System.err.println(descriptor);

        System.err.println(descriptor);
    }

    @Override
    @XmlElement(name="parser",type = ParserDescriptorImpl.class)
    public List<ParserDescriptor> getParserList() {
        return parserList;
    }

    public void setParserList(List<ParserDescriptor> parserList) {
        this.parserList = parserList;
    }

    @Override
    @XmlElement(name="repository", type = RepositoryDescriptorImpl.class)
    public List<RepositoryDescriptor> getRepositoryList() {
        return repositoryList;
    }

    public void setRepositoryList(List<RepositoryDescriptor> repositoryList) {
        this.repositoryList = repositoryList;
    }
    @Override
    @XmlElement(name="crawler",type = CrawlerDescriptorImpl.class)
    public List<CrawlerDescriptor> getCrawlerList() {
        return crawlerList;
    }

    public void setCrawlerList(List<CrawlerDescriptor> crawlerList) {
        this.crawlerList = crawlerList;
    }
    @Override
    @XmlElement(name="template",type = TemplateDescriptorImpl.class)
    public List<TemplateDescriptor> getTemplateList() {
        return templateList;
    }

    public void setTemplateList(List<TemplateDescriptor> templateList) {
        this.templateList = templateList;
    }
    @Override
    @XmlElement(name="postprocessor", type = PostProcessorDescriptorImpl.class)
    public List<PostProcessorDescriptor> getPostProcessorList() {
        return postProcessorList;
    }

    public void setPostProcessorList(List<PostProcessorDescriptor> postProcessorList) {
        this.postProcessorList = postProcessorList;
    }

    @Override
    @XmlElement(type = ModuleInfoImpl.class)
    public ModuleInfo getModuleInfo() {
        return moduleInfo;
    }

    public void setModuleInfo(ModuleInfo moduleInfo) {
        this.moduleInfo = moduleInfo;
    }

    public static class ModuleInfoImpl implements ModuleInfo{
        private String groupId;
        private String artifactId;
        private String version;

        public void setVersion(String version) {
            this.version = version;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public void setArtifactId(String artifactId) {
            this.artifactId = artifactId;
        }

        @Override
        public String getGroupId() {
            return groupId;
        }

        @Override
        public String getArtifactId() {
            return artifactId;
        }

        @Override
        public String getVersion() {
            return version;
        }


        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (obj == this)
                return true;
            if (!(obj instanceof ModuleInfo))
                return false;

            ModuleInfo rhs = (ModuleInfo) obj;
            return new EqualsBuilder()
                    .append(getArtifactId(), rhs.getArtifactId())
                    .append(getGroupId(), rhs.getGroupId())
                    .append(getVersion(),rhs.getVersion())
                    .build();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder()
                    .append(artifactId)
                    .append(groupId)
                    .append(version)
                    .build();
        }
    }

    @Override
    @XmlElement(name="provider",type = ProviderDescriptorImpl.class)
    public List<ProviderDescriptor> getProviderList() {
        return providerList;
    }

    public void setProviderList(List<ProviderDescriptor> providerList) {
        this.providerList = providerList;
    }

    private List<ParserDescriptor> parserList = new ArrayList<>();

    public static class ProviderDescriptorImpl implements ProviderDescriptor {
        private String id;
        private String type;
        private String className;
        private String archiveClassName;
        private String model;
        private List<NamespaceDescriptor> namespaceList;
        private List<LoadDescriptor> loadList;

        @Override
        public String toString() {
            return "ProviderDescriptorImpl{" +
                    "id='" + id + '\'' +
                    ", type='" + type + '\'' +
                    ", className='" + className + '\'' +
                    ", archiveClassName='" + archiveClassName + '\'' +
                    ", model='" + model + '\'' +
                    ", namespaceList=" + namespaceList +
                    ", loadList=" + loadList +
                    '}';
        }

        @XmlAttribute
        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }
        @XmlAttribute
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @XmlAttribute
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @XmlAttribute(name="class")
        public String getClassName() {
            return className;
        }

        @Override
        @XmlElement(name="namespace",type=NamespaceDescriptorImpl.class)
        public List<NamespaceDescriptor> getNamespaceList() {
            return namespaceList;
        }

        @Override
        @XmlElement(name="load",type = LoadDescriptorImpl.class)
        public List<LoadDescriptor> getLoadList() {
            return loadList;
        }

        public void setNamespaceList(List<NamespaceDescriptor> namespaceList) {
            this.namespaceList = namespaceList;
        }

        public void setLoadList(List<LoadDescriptor> loadList) {
            this.loadList = loadList;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getArchiveClassName() {
            return archiveClassName;
        }

        public void setArchiveClassName(String archiveClassName) {
            this.archiveClassName = archiveClassName;
        }
    }

    public static class NamespaceDescriptorImpl implements NamespaceDescriptor {
        private String prefix;
        private URI uri;

        @XmlAttribute
        public String getPrefix() {
            return prefix;
        }

        @XmlAttribute
        public URI getUri() {
            return uri;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public void setUri(URI uri) {
            this.uri = uri;
        }

        @Override
        public String toString() {
            return "NamespaceDescriptorImpl{" +
                    "prefix='" + prefix + '\'' +
                    ", \nuri=" + uri +
                    '}';
        }
    }

    public static class LoadDescriptorImpl implements LoadDescriptor {
        private URL url;

        @XmlAttribute
        public URL getUrl() {
            return url;
        }

        public void setUrl(URL url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "LoadDescriptorImpl{" +
                    "url=" + url +
                    '}';
        }
    }

    public static class TemplateDescriptorImpl implements TemplateDescriptor{
        private String name;
        private String appliesTo;

        @XmlAttribute
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @XmlAttribute
        public String getAppliesTo() {
            return appliesTo;
        }

        public void setAppliesTo(String appliesTo) {
            this.appliesTo = appliesTo;
        }

        @Override
        public String toString() {
            return "TemplateDescriptorImpl{" +
                    "name='" + name + '\'' +
                    ", \nappliesTo='" + appliesTo + '\'' +
                    '}';
        }
    }
    public static class ParserDescriptorImpl implements ParserDescriptor{
        private String id;
        private String className;
        private String type;

        @Override
        public String toString() {
            return "ParserDescriptorImpl{" +
                    "id='" + id + '\'' +
                    ", \nclassName='" + className + '\'' +
                    ", \ntype='" + type + '\'' +
                    '}';
        }

        @XmlAttribute
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @XmlAttribute
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @XmlAttribute(name="class")
        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }
    }
    public static class PostProcessorDescriptorImpl implements PostProcessorDescriptor{
        private String id;
        private String className;
        private List<NamespaceDescriptor> namespaceList;
        private List<LoadDescriptor> loadList;

        @Override
        public String toString() {
            return "PostProcessorDescriptorImpl{" +
                    "id='" + id + '\'' +
                    ", className='" + className + '\'' +
                    ", namespaceList=" + namespaceList +
                    ", loadList=" + loadList +
                    '}';
        }

        @Override
        @XmlElement(name="namespace",type=NamespaceDescriptorImpl.class)
        public List<NamespaceDescriptor> getNamespaceList() {
            return namespaceList;
        }

        public void setNamespaceList(List<NamespaceDescriptor> namespaceList) {
            this.namespaceList = namespaceList;
        }

        public void setLoadList(List<LoadDescriptor> loadList) {
            this.loadList = loadList;
        }

        @Override
        @XmlElement(name="load",type = LoadDescriptorImpl.class)
        public List<LoadDescriptor> getLoadList() {
            return loadList;
        }

        @XmlAttribute(name="class")
        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        @XmlAttribute
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
    public static class RepositoryDescriptorImpl implements RepositoryDescriptor{
        private String id;
        private String type;

        @Override
        public String toString() {
            return "RepositoryDescriptorImpl{" +
                    "id='" + id + '\'' +
                    ", \ntype='" + type + '\'' +
                    ", \nstartUri='" + startUri + '\'' +
                    ", \nbaseUri='" + baseUri + '\'' +
                    ", \ncrawler='" + crawler + '\'' +
                    ", \nactive=" + active +
                    ", \narchiveClassName='" + archiveClassName + '\'' +
                    ", \nproperties='" + properties + '\'' +
                    '}';
        }

        private String startUri;
        private String baseUri;
        private String crawler;
        private Boolean active;
        private String archiveClassName;


        private HashMap<String,String> properties;

        @XmlJavaTypeAdapter(MapPropertiesAdapter.class)
        public HashMap<String, String> getProperties() {
            return properties;
        }

        public void setProperties(HashMap<String, String> properties) {
            this.properties = properties;
        }

        @XmlAttribute
        public String getBaseUri() {
            return baseUri;
        }

        public void setBaseUri(String baseUri) {
            this.baseUri = baseUri;
        }

        @XmlAttribute(name="archiveClass")
        public String getArchiveClassName() {
            return archiveClassName;
        }

        public void setArchiveClassName(String archiveClassName) {
            this.archiveClassName = archiveClassName;
        }

        @XmlAttribute
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @XmlAttribute
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @XmlAttribute
        public String getStartUri() {
            return startUri;
        }

        public void setStartUri(String startUri) {
            this.startUri = startUri;
        }

        @XmlAttribute
        public String getCrawler() {
            return crawler;
        }

        public void setCrawler(String crawler) {
            this.crawler = crawler;
        }

        @XmlAttribute
        public boolean getActive() {
            if(active==null)
                return true;
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }

    public static class CrawlerDescriptorImpl implements CrawlerDescriptor{
        private String id;
        private String archiveClassName;

        @Override
        public String toString() {
            return "CrawlerDescriptorImpl{" +
                    "id='" + id + '\'' +
                    ", archiveClassName='" + archiveClassName + '\'' +
                    '}';
        }

        @XmlAttribute
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @XmlAttribute(name="archiveClass")
        public String getArchiveClassName() {
            return archiveClassName;
        }

        public void setArchiveClassName(String archiveClassName) {
            this.archiveClassName = archiveClassName;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof MetaserviceDescriptor))
            return false;

        MetaserviceDescriptor rhs = (MetaserviceDescriptor) obj;
        return new EqualsBuilder()
                .append(getModuleInfo(),rhs.getModuleInfo())
                .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getModuleInfo()).build();
    }
}
