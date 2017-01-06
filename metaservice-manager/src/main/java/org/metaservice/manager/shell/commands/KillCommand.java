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
import org.metaservice.manager.Manager;
import org.metaservice.manager.ManagerException;
import org.metaservice.manager.RunEntry;
import org.metaservice.manager.shell.completer.MPidCompleter;

import java.io.IOException;

/**
 * Created by ilo on 21.02.14.
 */
@CommandDefinition(name = "kill",description = "send termination signal to process")
public class KillCommand extends AbstractManagerCommand {
    public KillCommand(Manager manager) {
        super(manager);
    }

    @Option(name="mpid",shortName = 'p',required = true,completer = MPidCompleter.class)
    private int mpid;

    @Override
    public CommandResult execute2(CommandInvocation commandInvocation) throws IOException, ManagerException {
        RunEntry runEntry = manager.getRunManager().getRunEntryByMPid(mpid);
        if(runEntry == null){
            System.out.println("Could not find process");
            return CommandResult.FAILURE;
        }
        if( runEntry.getProcess() == null)
        {
            System.out.println("Could not access process");
            return CommandResult.FAILURE;
        }
        manager.getRunManager().shutdown(runEntry);
        return CommandResult.SUCCESS;
    }
}
