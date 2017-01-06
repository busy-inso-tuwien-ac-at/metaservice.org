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

package org.metaservice.manager.shell.commands;

import org.jboss.aesh.cl.Arguments;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.api.messaging.config.ManagerConfig;
import org.metaservice.core.descriptor.DescriptorHelperImpl;
import org.metaservice.manager.Manager;
import org.metaservice.manager.ManagerException;
import org.metaservice.manager.shell.completer.AvailableModuleCompleter;
import org.metaservice.manager.shell.validator.ModuleValidator;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by ilo on 19.02.14.
 */
@CommandDefinition(name="rm",description = "foo")
public class RemoveModuleCommand extends AbstractManagerCommand{

    public RemoveModuleCommand(Manager manager) {
        super(manager);
    }

    @Arguments(completer = AvailableModuleCompleter.class, validator = ModuleValidator.class)
    private List<String> moduleIdentifier;


    @Option(name = "hard",description = "do not make any checks, just remove from list", hasValue = false)
    boolean hard;

    @Override
    public CommandResult execute2(CommandInvocation commandInvocation) throws IOException, ManagerException {
        Collection<ManagerConfig.Module> installedModules = manager.getManagerConfig().getInstalledModules();
        Collection<ManagerConfig.Module> availableModules = manager.getManagerConfig().getAvailableModules();

        if(hard){
            for(String identifier : moduleIdentifier){
                ManagerConfig.Module installedModule = descriptorHelper.getModuleFromString(installedModules, identifier);
                if(installedModule != null){
                    LOGGER.warn("{} is installed, ignoring", identifier);
                    continue;
                }
                ManagerConfig.Module availableModule = descriptorHelper.getModuleFromString(availableModules, identifier);
                if(availableModule == null){
                    LOGGER.warn("{} not found, ignoring", identifier);
                }
                manager.removeModule(availableModule);
            }
        }else{
            throw new RuntimeException("Not implemented");
        }

        return CommandResult.SUCCESS;
    }
}
