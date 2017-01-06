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
import org.metaservice.core.descriptor.DescriptorHelperImpl;
import org.metaservice.manager.Manager;
import org.metaservice.api.messaging.config.ManagerConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ilo on 11.02.14.
 */
@CommandDefinition(name = "lsmod",description = "show information on current modules")
public class ListCommand extends AbstractManagerCommand {
    public ListCommand(Manager manager) {
        super(manager);
    }

    @Option(name = "all",shortName = 'a',hasValue = false)
    boolean showAll;


    @Option(name = "sort",shortName = 's',hasValue = false)
    boolean sort;

    @Override
    public CommandResult execute2(CommandInvocation commandInvocation) throws IOException {
        List<ManagerConfig.Module> result;
        if(showAll){
            result = manager.getManagerConfig().getAvailableModules();
        } else{
            result = manager.getManagerConfig().getInstalledModules();
        }

        if(result.size() == 0){
            commandInvocation.getShell().out().println("no modules found");
            return CommandResult.SUCCESS;
        }

        List<String> resultString = new ArrayList<>();
        for(ManagerConfig.Module m : result){
            resultString.add(descriptorHelper.getModuleIdentifierStringFromModule(m.getMetaserviceDescriptor().getModuleInfo()));
        }
        if(sort){
            Collections.sort(resultString);
        }
        for(String s : resultString){
            commandInvocation.getShell().out().println(s);
        }
        return CommandResult.SUCCESS;
    }
}
