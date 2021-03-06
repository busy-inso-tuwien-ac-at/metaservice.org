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

import org.jboss.aesh.cl.Arguments;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.manager.Manager;
import org.metaservice.manager.ManagerException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by ilo on 10.02.14.
 */
@CommandDefinition(name = "add",description = "Add a Module to the Manager")
public class AddModuleCommand extends AbstractManagerCommand{
    public AddModuleCommand(Manager manager) {
        super(manager);
    }

    @Arguments( description = "files to add")
    List<File> fileToAdd;

    @Option(shortName = 'f',description =  "force adding of existing file with same name and version",hasValue = false)
    boolean force;

    @Override
    public CommandResult execute2(CommandInvocation commandInvocation) throws IOException {
        for(File f: fileToAdd){
            try {
                manager.add(f, force);
            } catch (ManagerException e) {
                LOGGER.error("Could not add",e);
            }
        }
        return CommandResult.SUCCESS;
    }
}
