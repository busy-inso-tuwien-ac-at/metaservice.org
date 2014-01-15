package org.metaservice.core.descriptor;

import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class MetaserviceDescriptorImpl implements MetaserviceDescriptor {
    public final Logger LOGGER = LoggerFactory.getLogger(MetaserviceDescriptorImpl.class);

    private final List<ProviderDescriptor> providerList = new ArrayList<>();
    private final List<PostProcessorDescriptor> postProcessorList = new ArrayList<>();
    private final List<TemplateDescriptor> templateDescriptorList = new ArrayList<>();
    private final List<CrawlerDescriptor> crawlerDescriptorList = new ArrayList<>();
    private final List<RepositoryDescriptor> repositoryDescriptorList = new ArrayList<>();
    private final List<ParserDescriptor> parserDescriptorList = new ArrayList<>();

    @Inject
    public MetaserviceDescriptorImpl(){
         this(MetaserviceDescriptorImpl.class.getResourceAsStream("/metaservice.xml"));
    }
    public MetaserviceDescriptorImpl(InputStream inputStream){
        try {
            load(inputStream);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public void load(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputStream);

        //optional, but recommended
        //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        doc.getDocumentElement().normalize();

        NodeList providers =doc.getElementsByTagName("provider");
        for(int i = 0; i < providers.getLength();i++){
            Element e = (Element) providers.item(i);
            ProviderDescriptorImpl impl = new ProviderDescriptorImpl();
            impl.setId(e.getAttribute("id"));
            impl.setClassName(e.getAttribute("class"));
            impl.setType(e.getAttribute("type"));
            impl.setModel(e.getAttribute("model"));
            ArrayList<ProviderDescriptor.NamespaceDescriptor> namespaceDescriptors = new ArrayList<>();
            impl.setNamespaceList(namespaceDescriptors);
            NodeList namespaces = e.getElementsByTagName("namespace");
            for(int j = 0; j < namespaces.getLength();j++){
                try{
                Element x = (Element) namespaces.item(j);
                NamespaceDescriptorImpl ns = new NamespaceDescriptorImpl();
                ns.setPrefix(x.getAttribute("prefix"));
                ns.setUri(new URI(x.getAttribute("uri")));
                } catch (URISyntaxException e1) {
                    LOGGER.warn("Could not add namespace", e1);
                }
            }
            ArrayList<ProviderDescriptor.LoadDescriptor> loadDescriptors = new ArrayList<>();
            impl.setLoadList(loadDescriptors);
            NodeList loads = e.getElementsByTagName("load");
            for(int j = 0; j < loads.getLength();j++){
                Element x = (Element) loads.item(j);
                LoadDescriptorImpl ns = new LoadDescriptorImpl();
                ns.setUrl(new URL(x.getAttribute("url")));
            }
            providerList.add(impl);
        }
        NodeList postProcessors =doc.getElementsByTagName("postprocessor");
        for(int i = 0; i < postProcessors.getLength();i++){
            Element e = (Element) postProcessors.item(i);
            PostProcessorDescriptorImpl impl= new PostProcessorDescriptorImpl();
            impl.setClassName(e.getAttribute("class"));
            impl.setId(e.getAttribute("id"));
            postProcessorList.add(impl);
        }
        NodeList parsers =doc.getElementsByTagName("parser");
        for(int i = 0; i < parsers.getLength();i++){
            Element e = (Element) parsers.item(i);
            ParserDescriptorImpl impl= new ParserDescriptorImpl();
            impl.setClassName(e.getAttribute("class"));
            impl.setId(e.getAttribute("id"));
            impl.setType(e.getAttribute("type"));
            parserDescriptorList.add(impl);
        }

        NodeList repositories =doc.getElementsByTagName("repository");
        for(int i = 0; i < repositories.getLength();i++){
            Element e = (Element) repositories.item(i);
            RepositoryDescriptorImpl impl= new RepositoryDescriptorImpl();
            impl.setId(e.getAttribute("id"));
            impl.setCrawler(e.getAttribute("crawler"));
            impl.setType(e.getAttribute("type"));
            impl.setStartUri(e.getAttribute("starturi"));
            impl.setBaseUri(e.getAttribute("baseuri"));
            impl.setArchiveClassName(e.getAttribute("archiveClass"));
            impl.setActive(true);
            String activeAttribute =  e.getAttribute("active");
            if(!"".equals(activeAttribute)){
                impl.setActive(Boolean.valueOf(activeAttribute));
            }
            repositoryDescriptorList.add(impl);
        }


        NodeList templates =doc.getElementsByTagName("template");
        for(int i = 0; i < templates.getLength();i++){
            Element e = (Element) templates.item(i);
            TemplateDescriptorImpl impl= new TemplateDescriptorImpl();
            impl.setAppliesTo(e.getAttribute("appliesTo"));
            impl.setName(e.getAttribute("name"));
            templateDescriptorList.add(impl);
        }

        NodeList crawlers =doc.getElementsByTagName("crawler");
        for(int i = 0; i < crawlers.getLength();i++){
            Element e = (Element) crawlers.item(i);
            CrawlerDescriptorImpl impl= new CrawlerDescriptorImpl();
            impl.setArchiveClassName(e.getAttribute("archiveClass"));
            impl.setId(e.getAttribute("id"));
            crawlerDescriptorList.add(impl);
        }
    }


    public List<ProviderDescriptor> getProviderList() {
        return providerList;
    }

    @Override
    public List<ParserDescriptor> getParserList() {
        return parserDescriptorList;
    }

    @Override
    public List<RepositoryDescriptor> getRepositoryList() {
        return repositoryDescriptorList;
    }

    @Override
    public List<CrawlerDescriptor> getCrawlerList() {
        return crawlerDescriptorList;
    }

    @Override
    public List<TemplateDescriptor> getTemplateList() {
        return templateDescriptorList;
    }

    public List<PostProcessorDescriptor> getPostProcessorList() {
        return postProcessorList;
    }

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
                    '}';
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getClassName() {
            return className;
        }

        @Override
        public List<NamespaceDescriptor> getNamespaceList() {
            return namespaceList;
        }

        @Override
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

    public static class NamespaceDescriptorImpl implements ProviderDescriptor.NamespaceDescriptor {
        private String prefix;
        private URI uri;

        public String getPrefix() {
            return prefix;
        }

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
                    ", uri=" + uri +
                    '}';
        }
    }

    public static class LoadDescriptorImpl implements ProviderDescriptor.LoadDescriptor {
        private URL url;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

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
                    ", appliesTo='" + appliesTo + '\'' +
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
                    ", className='" + className + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

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
                    ", type='" + type + '\'' +
                    ", startUri='" + startUri + '\'' +
                    ", baseUri='" + baseUri + '\'' +
                    ", crawler='" + crawler + '\'' +
                    ", active=" + active +
                    ", archiveClassName='" + archiveClassName + '\'' +
                    '}';
        }

        private String startUri;
        private String baseUri;
        private String crawler;
        private boolean active;
        private String archiveClassName;


        public String getBaseUri() {
            return baseUri;
        }

        public void setBaseUri(String baseUri) {
            this.baseUri = baseUri;
        }

        public String getArchiveClassName() {
            return archiveClassName;
        }

        public void setArchiveClassName(String archiveClassName) {
            this.archiveClassName = archiveClassName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStartUri() {
            return startUri;
        }

        public void setStartUri(String startUri) {
            this.startUri = startUri;
        }

        public String getCrawler() {
            return crawler;
        }

        public void setCrawler(String crawler) {
            this.crawler = crawler;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }

    public static class CrawlerDescriptorImpl implements CrawlerDescriptor{
        private String id;
        private String archiveClassName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getArchiveClassName() {
            return archiveClassName;
        }

        public void setArchiveClassName(String archiveClassName) {
            this.archiveClassName = archiveClassName;
        }
    }
}