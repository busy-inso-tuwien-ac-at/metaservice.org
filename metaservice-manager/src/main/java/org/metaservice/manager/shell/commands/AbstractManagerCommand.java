package org.metaservice.manager.shell.commands;

import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.api.messaging.descriptors.DescriptorHelper;
import org.metaservice.manager.Manager;
import org.metaservice.manager.ManagerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by ilo on 10.02.14.
 */
public abstract  class AbstractManagerCommand implements Command{
    protected final Manager manager;
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    protected final DescriptorHelper descriptorHelper;
    public AbstractManagerCommand(Manager manager) {
        this.manager = manager;
        descriptorHelper = manager.getDescriptorHelper();
    }

    @Override
    public CommandResult execute(CommandInvocation commandInvocation) throws IOException {
        try{
            return execute2(commandInvocation);
        }catch (ManagerException e){
            LOGGER.error("Could not execute Command",e);
            return CommandResult.FAILURE;
        }
        catch (Throwable e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public abstract  CommandResult execute2(CommandInvocation commandInvocation) throws IOException, ManagerException;
}
