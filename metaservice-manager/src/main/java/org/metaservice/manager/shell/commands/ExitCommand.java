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

import org.metaservice.manager.Manager;
import org.metaservice.manager.ManagerException;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;

import java.io.IOException;

/**
 * Created by ilo on 17.02.14.
 */
@CommandDefinition(name="exit", description = "exit the program")
public class ExitCommand extends AbstractManagerCommand {
    public ExitCommand(Manager manager) {
        super(manager);
    }

    @Override
    public CommandResult execute2(CommandInvocation commandInvocation) throws IOException {
        try {
            manager.shutdown();
            commandInvocation.stop();
            return CommandResult.SUCCESS;
        } catch (ManagerException e) {
            throw  new RuntimeException(e);
        }
    }
}
