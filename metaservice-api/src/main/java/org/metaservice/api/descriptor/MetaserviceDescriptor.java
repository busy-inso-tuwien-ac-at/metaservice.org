/*
 * Copyright 2015 Nikola Ilo
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
    public static interface OntologyLoaderDescriptor {
        @NotNull
        List<NamespaceDescriptor> getNamespaceList();
        @NotNull
        List<LoadDescriptor>  getLoadList();
    }
    public static interface ProviderDescriptor extends OntologyLoaderDescriptor {
        String getId();
        String getModel();
        String getClassName();
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

    public static interface PostProcessorDescriptor extends OntologyLoaderDescriptor {
        String getId();
        String getClassName();
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

