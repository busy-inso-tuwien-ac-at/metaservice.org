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