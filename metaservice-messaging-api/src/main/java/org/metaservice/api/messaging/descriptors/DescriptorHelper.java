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
