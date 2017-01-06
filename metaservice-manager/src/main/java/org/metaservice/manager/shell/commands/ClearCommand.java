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

package org.metaservice.manager.shell.commands;

import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.config.ManagerConfig;
import org.metaservice.kryo.mongo.MongoConnectionWrapper;
import org.metaservice.manager.Manager;
import org.metaservice.manager.ManagerException;
import org.metaservice.manager.shell.completer.InstalledModuleCompleter;
import org.metaservice.manager.shell.completer.PostProcessorCompleter;
import org.metaservice.manager.shell.completer.ProviderCompleter;
import org.metaservice.manager.shell.validator.GreaterThanZeroValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by ilo on 10.02.14.
 */
@CommandDefinition(name = "clear",description = "remove data")
public class ClearCommand extends AbstractManagerCommand {
    public ClearCommand(Manager manager) {
        super(manager);
    }

    @Option(name ="provider",shortName = 's',completer = ProviderCompleter.class)
    String provider;

    @Option(name ="postprocessor",shortName = 'p',completer = PostProcessorCompleter.class)
    String postprocessor;

    @Option(name ="raw",shortName = 'r')
    String raw;

    @Option(name ="messaging",shortName = 'm',hasValue = false)
    boolean messaging;

    @Override
    public CommandResult execute2(CommandInvocation commandInvocation) throws IOException {
        Collection<ManagerConfig.Module> installedModules = manager.getManagerConfig().getInstalledModules();

        if(provider != null){
            ManagerConfig.Module module = descriptorHelper.getModuleFromString(installedModules, provider);
            MetaserviceDescriptor.ProviderDescriptor providerDescriptor = descriptorHelper.getProviderFromString(installedModules, provider);
            try {
                if (providerDescriptor != null && module != null) {
                    manager.removeProviderData(module.getMetaserviceDescriptor(), providerDescriptor);
                }  else {
                    LOGGER.error("module not found");
                }
            } catch (ManagerException e) {
                LOGGER.error("failed",e);
            }
        }
        if(postprocessor != null){
            ManagerConfig.Module module = descriptorHelper.getModuleFromString(installedModules, postprocessor);
            MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor = descriptorHelper.getPostProcessorFromString(installedModules, postprocessor);
            try {
                if(module != null && postProcessorDescriptor != null){
                    manager.removePostProcessorData(module.getMetaserviceDescriptor(), postProcessorDescriptor);

                }else {
                    LOGGER.error("module not found");
                }
            } catch (ManagerException e) {
                LOGGER.error("failed",e);
                }
        }
        if(raw != null){
            try{
                manager.removeDataFromGenerator(raw);
            }  catch (ManagerException e) {
                LOGGER.error("failed", e);
            }
        }
        if(messaging){
            MongoConnectionWrapper mongoConnectionWrapper = new MongoConnectionWrapper();
            mongoConnectionWrapper.getPostProcessorMessageCollection().drop();
            mongoConnectionWrapper.getPostProcessorMessageCollectionFailed().drop();
            mongoConnectionWrapper.getProviderRefreshMessageCollection().drop();
            mongoConnectionWrapper.getProviderRefreshMessageCollectionFailed().drop();
            mongoConnectionWrapper.getProviderCreateMessageCollection().drop();
            mongoConnectionWrapper.getProviderCreateMessageCollectionFailed().drop();
            mongoConnectionWrapper.getQueueCollection().drop();
        }
        return CommandResult.SUCCESS;
    }

}
