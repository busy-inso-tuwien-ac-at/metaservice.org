package org.metaservice.core.descriptor;

import org.metaservice.core.config.ManagerConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.core.descriptor.JAXBMetaserviceDescriptorImpl;
import org.openrdf.query.algebra.Regex;

import java.util.Collection;

/**
 * Created by ilo on 10.02.14.
 */
public class DescriptorHelper {
    public static @NotNull String getModuleIdentifierStringFromModule(@NotNull MetaserviceDescriptor.ModuleInfo moduleInfo){
        return moduleInfo.getGroupId()+":"+moduleInfo.getArtifactId()+":" +moduleInfo.getVersion();
    }

    static String regex_module123 = "^([^:]+):([^:]+):([^:]+).*$";
    static  String regex_module4 = "^[^:]+:[^:]+:[^:]+:([^:]+)$";
    public static @Nullable
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

    public static @NotNull String getStringFromProvider(@NotNull MetaserviceDescriptor.ModuleInfo moduleInfo, @NotNull MetaserviceDescriptor.ProviderDescriptor providerDescriptor){
        return getModuleIdentifierStringFromModule(moduleInfo) +":" + providerDescriptor.getId();
    }

    public static @Nullable
    MetaserviceDescriptor.ProviderDescriptor getProviderFromString(@NotNull Collection<ManagerConfig.Module> modules,@NotNull String identifierString){
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

    @NotNull
    public static String getStringFromPostProcessor(MetaserviceDescriptor.ModuleInfo moduleInfo, MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor) {
        return getModuleIdentifierStringFromModule(moduleInfo) +":" + postProcessorDescriptor.getId();
    }

    public static @Nullable
    MetaserviceDescriptor.PostProcessorDescriptor getPostProcessorFromString(@NotNull Collection<ManagerConfig.Module> modules,@NotNull String identifierString){
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

    @NotNull
    public static String getStringFromRepository(@NotNull MetaserviceDescriptor.ModuleInfo moduleInfo, @NotNull String id) {
        return getModuleIdentifierStringFromModule(moduleInfo) +":" + id;
    }
}
