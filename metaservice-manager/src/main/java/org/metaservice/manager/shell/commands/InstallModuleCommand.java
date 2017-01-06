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

import org.metaservice.core.descriptor.DescriptorHelperImpl;
import org.metaservice.manager.ManagerException;
import org.metaservice.manager.shell.completer.AvailableModuleCompleter;
import org.metaservice.manager.shell.validator.ModuleValidator;
import org.jboss.aesh.cl.Arguments;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.manager.Manager;
import org.metaservice.api.messaging.config.ManagerConfig;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by ilo on 10.02.14.
 */
@CommandDefinition(name = "install",description = "install/upgrade a module previously added")
public class InstallModuleCommand extends AbstractManagerCommand {
    public InstallModuleCommand(Manager manager) {
        super(manager);
    }


    @Arguments(completer = AvailableModuleCompleter.class, validator = ModuleValidator.class)
    private List<String> moduleIdentifier;

    @Override
    public CommandResult execute2(CommandInvocation commandInvocation) throws IOException {
        Collection<ManagerConfig.Module> installedModules = manager.getManagerConfig().getInstalledModules();
        Collection<ManagerConfig.Module> availableModules = manager.getManagerConfig().getAvailableModules();
        for(String s : moduleIdentifier){

            ManagerConfig.Module installedModule = descriptorHelper.getModuleFromString(installedModules, s);
            if(installedModule != null){
                commandInvocation.getShell().out().println("already installed: " + s);
                continue;
            }
            ManagerConfig.Module availableModule = descriptorHelper.getModuleFromString(availableModules, s);
            if(availableModule == null){
                LOGGER.warn("module not found");
                continue;
            }
            try {
                manager.install(availableModule);
            } catch (ManagerException e) {
                e.printStackTrace();
            }
        }
        return CommandResult.SUCCESS;
    }
}
