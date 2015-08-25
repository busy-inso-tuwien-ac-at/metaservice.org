package org.metaservice.api.messaging.descriptors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.config.ManagerConfig;

import java.util.Collection;

/**
 * Created by ilo on 09.06.2014.
 */
public interface DescriptorHelper {
    @NotNull
    String getModuleIdentifierStringFromModule(@NotNull MetaserviceDescriptor.ModuleInfo moduleInfo);

    @Nullable
    ManagerConfig.Module getModuleFromString(@NotNull Collection<ManagerConfig.Module> modules, @NotNull String identifierString);

    @NotNull
    String getStringFromProvider(@NotNull MetaserviceDescriptor.ModuleInfo moduleInfo, @NotNull MetaserviceDescriptor.ProviderDescriptor providerDescriptor);

    @Nullable
    MetaserviceDescriptor.ProviderDescriptor getProviderFromString(@NotNull Collection<ManagerConfig.Module> modules, @NotNull String identifierString);

    @NotNull
    String getStringFromPostProcessor(MetaserviceDescriptor.ModuleInfo moduleInfo, MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor);

    @Nullable
    MetaserviceDescriptor.PostProcessorDescriptor getPostProcessorFromString(@NotNull Collection<ManagerConfig.Module> modules, @NotNull String identifierString);

    @NotNull
    String getStringFromRepository(@NotNull MetaserviceDescriptor.ModuleInfo moduleInfo, @NotNull String id);

    @NotNull
    String getStringFromRepository(@NotNull MetaserviceDescriptor.ModuleInfo moduleInfo, @NotNull MetaserviceDescriptor.RepositoryDescriptor repositoryDescriptor);
}
