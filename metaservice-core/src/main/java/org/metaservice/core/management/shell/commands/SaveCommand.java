package org.metaservice.core.management.shell.commands;

import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.core.management.Manager;

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
