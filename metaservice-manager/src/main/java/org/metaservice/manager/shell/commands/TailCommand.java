package org.metaservice.manager.shell.commands;

import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.manager.Manager;
import org.metaservice.manager.ManagerException;
import org.metaservice.manager.shell.completer.MPidCompleter;

import java.io.IOException;

/**
 * Created by ilo on 20.02.14.
 */
@CommandDefinition(name="tail",description = "description")
public class TailCommand extends AbstractManagerCommand{
    public TailCommand(Manager manager) {
        super(manager);
    }

    @Option(name="mpid",shortName = 'p',required = true,completer = MPidCompleter.class)
    private int mpid;

    @Override
    public CommandResult execute2(CommandInvocation commandInvocation) throws IOException, ManagerException {
        commandInvocation.getShell().enableAlternateBuffer();
        commandInvocation.getShell().enableMainBuffer();
        return CommandResult.SUCCESS;
    }
}
