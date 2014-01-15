package org.metaservice.api.descriptor;

import java.net.URI;
import java.net.URL;
import java.util.List;

public interface MetaserviceDescriptor {
    List<ProviderDescriptor> getProviderList();
    List<ParserDescriptor> getParserList();
    List<RepositoryDescriptor> getRepositoryList();
    List<CrawlerDescriptor> getCrawlerList();
    List<TemplateDescriptor> getTemplateList();
    List<PostProcessorDescriptor> getPostProcessorList();


    public static interface ProviderDescriptor {
        String getId();
        String getType();
        String getModel();
        String getClassName();
        List<NamespaceDescriptor> getNamespaceList();
        List<LoadDescriptor>  getLoadList();

        public static interface NamespaceDescriptor{
            String getPrefix();
            URI getUri();
        }

        public static interface LoadDescriptor{
            URL getUrl();
        }
    }

    public static interface ParserDescriptor{
        String getId();
        String getType();
        String getClassName();
    }

    public static interface RepositoryDescriptor{
        String getId();
        String getType();
        boolean isActive();
        String getArchiveClassName();
        String getBaseUri();
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
    }

    public static interface TemplateDescriptor{
        String getName();
        String getAppliesTo();
    }

}
