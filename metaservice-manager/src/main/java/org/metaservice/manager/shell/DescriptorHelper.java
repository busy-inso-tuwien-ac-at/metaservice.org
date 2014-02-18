package org.metaservice.manager.shell;

import org.metaservice.core.config.ManagerConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.core.descriptor.JAXBMetaserviceDescriptorImpl;

import java.util.Collection;

/**
 * Created by ilo on 10.02.14.
 */
public class DescriptorHelper {
    public static @NotNull String getModuleIdentifierStringFromModule(@NotNull MetaserviceDescriptor.ModuleInfo moduleInfo){
        return moduleInfo.getGroupId()+"/"+moduleInfo.getArtifactId()+"@" +moduleInfo.getVersion();
    }

    public static @Nullable
    ManagerConfig.Module getModuleFromString(@NotNull Collection<ManagerConfig.Module> modules, @NotNull String identifierString){
        int slashPosition = identifierString.indexOf('/');
        int atPosition = identifierString.lastIndexOf('@');
        int colonPosition = identifierString.lastIndexOf(':');
        String groupId = identifierString.substring(0, slashPosition);
        String artifactId = identifierString.substring(slashPosition+1,atPosition);
        String version = (colonPosition==-1)?identifierString.substring(atPosition+1):identifierString.substring(atPosition+1,colonPosition);


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
        return moduleInfo.getGroupId()+"/"+moduleInfo.getArtifactId()+"@" +moduleInfo.getVersion() +":" + providerDescriptor.getId();
    }

    public static @Nullable
    MetaserviceDescriptor.ProviderDescriptor getProviderFromString(@NotNull Collection<ManagerConfig.Module> modules,@NotNull String identifierString){
        int colonPosition = identifierString.lastIndexOf(':');
        String provider = identifierString.substring(colonPosition+1);
        ManagerConfig.Module m = getModuleFromString(modules,identifierString);

        for(MetaserviceDescriptor.ProviderDescriptor p : m.getMetaserviceDescriptor().getProviderList()){
            if(provider.equals(p.getId())){
                return p;
            }
        }
        return null;
    }

    public static String getStringFromPostProcessor(MetaserviceDescriptor.ModuleInfo moduleInfo, MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor) {
        return moduleInfo.getGroupId()+"/"+moduleInfo.getArtifactId()+"@" +moduleInfo.getVersion() +":" + postProcessorDescriptor.getId();
    }

    public static @Nullable
    MetaserviceDescriptor.PostProcessorDescriptor getPostProcessorFromString(@NotNull Collection<ManagerConfig.Module> modules,@NotNull String identifierString){
        int colonPosition = identifierString.lastIndexOf(':');
        String postProcessor = identifierString.substring(colonPosition+1);

        ManagerConfig.Module m = getModuleFromString(modules,identifierString);
        for(MetaserviceDescriptor.PostProcessorDescriptor p : m.getMetaserviceDescriptor().getPostProcessorList()){
            if(postProcessor.equals(p.getId())){
                return p;
            }
        }
        return null;
    }

}
