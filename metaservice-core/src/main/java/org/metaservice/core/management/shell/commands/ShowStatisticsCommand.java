package org.metaservice.core.management.shell.commands;


import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.core.management.Manager;

import java.io.IOException;

@CommandDefinition(name = "stats", description = "show statistics")
public class ShowStatisticsCommand extends AbstractManagerCommand {
    public ShowStatisticsCommand(Manager manager) {
        super(manager);
    }

    @Override
    public CommandResult execute(CommandInvocation commandInvocation) throws IOException {
        System.out.println("NOT YET IMPLEMENTED");
        return null;
    }
}
