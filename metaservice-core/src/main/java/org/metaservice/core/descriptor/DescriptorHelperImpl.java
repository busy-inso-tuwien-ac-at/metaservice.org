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

package org.metaservice.core.descriptor;

import org.metaservice.api.messaging.config.JAXBMetaserviceDescriptorImpl;
import org.metaservice.api.messaging.descriptors.DescriptorHelper;
import org.metaservice.api.messaging.config.ManagerConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaservice.api.descriptor.MetaserviceDescriptor;

import java.util.Collection;

/**
 * Created by ilo on 10.02.14.
 */
public class DescriptorHelperImpl implements DescriptorHelper {
    @Override
    public @NotNull String getModuleIdentifierStringFromModule(@NotNull MetaserviceDescriptor.ModuleInfo moduleInfo){
        return moduleInfo.getGroupId()+":"+moduleInfo.getArtifactId()+":" +moduleInfo.getVersion();
    }

    static String regex_module123 = "^([^:]+):([^:]+):([^:]+).*$";
    static  String regex_module4 = "^[^:]+:[^:]+:[^:]+:([^:]+)$";
    @Override
    public @Nullable
    ManagerConfig.Module getModuleFromString(@NotNull Collection<ManagerConfig.Module> modules, @NotNull String identifierString){
        String groupId = identifierString.replaceAll(regex_module123, "$1");
        String artifactId = identifierString.replaceAll(regex_module123, "$2");
        String version = identifierString.replaceAll(regex_module123,"$3");


        JAXBMetaserviceDescriptorImpl.ModuleInfoImpl search = new JAXBMetaserviceDescriptorImpl.ModuleInfoImpl();
        search.setArtifactId(artifactId);
        search.setGroupId(groupId);
        search.setVersion(version);


        for(ManagerConfig.Module m : modules){
            if(m.getMetaserviceDescriptor().getModuleInfo().equals(search)){
                return m;
            }
        }
        return null;
    }

    @Override
    public @NotNull String getStringFromProvider(@NotNull MetaserviceDescriptor.ModuleInfo moduleInfo, @NotNull MetaserviceDescriptor.ProviderDescriptor providerDescriptor){
        return getModuleIdentifierStringFromModule(moduleInfo) +":" + providerDescriptor.getId();
    }

    @Override
    public @Nullable
    MetaserviceDescriptor.ProviderDescriptor getProviderFromString(@NotNull Collection<ManagerConfig.Module> modules, @NotNull String identifierString){
        String provider = identifierString.replaceAll(regex_module4, "$1");
        ManagerConfig.Module m = getModuleFromString(modules,identifierString);
        if(m != null){
            for(MetaserviceDescriptor.ProviderDescriptor p : m.getMetaserviceDescriptor().getProviderList()){
                if(provider.equals(p.getId())){
                    return p;
                }
            }
        }
        return null;
    }

    @Override
    @NotNull
    public String getStringFromPostProcessor(MetaserviceDescriptor.ModuleInfo moduleInfo, MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor) {
        return getModuleIdentifierStringFromModule(moduleInfo) +":" + postProcessorDescriptor.getId();
    }

    @Override
    public @Nullable
    MetaserviceDescriptor.PostProcessorDescriptor getPostProcessorFromString(@NotNull Collection<ManagerConfig.Module> modules, @NotNull String identifierString){
        String postProcessor = identifierString.replaceAll(regex_module4,"$1");

        ManagerConfig.Module m = getModuleFromString(modules,identifierString);
        if(m != null){
            for(MetaserviceDescriptor.PostProcessorDescriptor p : m.getMetaserviceDescriptor().getPostProcessorList()){
                if(postProcessor.equals(p.getId())){
                    return p;
                }
            }
        }
        return null;
    }

    @Override
    @NotNull
    public String getStringFromRepository(@NotNull MetaserviceDescriptor.ModuleInfo moduleInfo, @NotNull String id) {
        return getModuleIdentifierStringFromModule(moduleInfo) +":" + id;
    }
    @Override
    @NotNull
    public String getStringFromRepository(@NotNull MetaserviceDescriptor.ModuleInfo moduleInfo, @NotNull MetaserviceDescriptor.RepositoryDescriptor repositoryDescriptor) {
        return getModuleIdentifierStringFromModule(moduleInfo) +":" + repositoryDescriptor.getId();
    }
}
