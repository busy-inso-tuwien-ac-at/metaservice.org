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
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.manager.Manager;

import java.io.IOException;

/**
 * Created by ilo on 10.02.14.
 */
@CommandDefinition(name="save",description = "save the current configuration")
public class SaveCommand  extends AbstractManagerCommand{
    public SaveCommand(Manager manager) {
        super(manager);
    }


    @Override
    public CommandResult execute2(CommandInvocation commandInvocation) throws IOException {
        manager.saveConfig();
        commandInvocation.getShell().out().println("Config saved!");
        return CommandResult.SUCCESS;
    }
}
