package org.metaservice.core.management.shell.commands;

import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.core.management.Manager;

import java.io.IOException;

/**
 * Created by ilo on 10.02.14.
 */
public abstract  class AbstractManagerCommand implements Command{
    protected final Manager manager;

    public AbstractManagerCommand(Manager manager) {
        this.manager = manager;
    }

    @Override
    public CommandResult execute(CommandInvocation commandInvocation) throws IOException {
        try{
            return execute2(commandInvocation);
        }catch (Throwable e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public abstract  CommandResult execute2(CommandInvocation commandInvocation) throws IOException;
}
