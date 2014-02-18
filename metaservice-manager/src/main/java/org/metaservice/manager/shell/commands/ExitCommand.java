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
