package org.metaservice.api.descriptor;

import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public interface MetaserviceDescriptor {
    ModuleInfo getModuleInfo();

    @NotNull
    List<ProviderDescriptor> getProviderList();
    @NotNull
    List<ParserDescriptor> getParserList();
    @NotNull
    List<RepositoryDescriptor> getRepositoryList();
    @NotNull
    List<CrawlerDescriptor> getCrawlerList();
    @NotNull
    List<TemplateDescriptor> getTemplateList();
    @NotNull
    List<OntologyDescriptor> getOntologyList();
    @NotNull
    List<PostProcessorDescriptor> getPostProcessorList();


    public static interface ModuleInfo{
        String getGroupId();
        String getArtifactId();
        String getVersion();
    }

    public static interface ProviderDescriptor {
        String getId();
        String getModel();
        String getClassName();
        @NotNull
        List<NamespaceDescriptor> getNamespaceList();
        @NotNull
        List<LoadDescriptor>  getLoadList();
    }

    public static interface ParserDescriptor{
        String getId();
        String getModel();
        String getType();
        String getClassName();
    }

    public static interface RepositoryDescriptor{
        String getId();
        String getType();
        boolean getActive();
        String getArchiveClassName();
        String getBaseUri();
        @NotNull
        HashMap<String, String> getProperties();
        String getStartUri();
        String getCrawler();
    }

    public static interface CrawlerDescriptor{
        String getId();
        String getArchiveClassName();
    }

    public static interface PostProcessorDescriptor{
        String getId();
        String getClassName();
        @NotNull
        List<NamespaceDescriptor> getNamespaceList();
        @NotNull
        List<LoadDescriptor>  getLoadList();
    }

    public static interface TemplateDescriptor{
        String getName();
        String getAppliesTo();
    }

    public static interface OntologyDescriptor{
        String getName();
        boolean getApply();
        boolean getDistribute();
    }


    public static interface NamespaceDescriptor {
        String getPrefix();
        URI getUri();
    }

    public static interface LoadDescriptor {
        URL getUrl();
    }
}

