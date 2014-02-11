package org.metaservice.core.management.shell.commands;

import org.jboss.aesh.cl.Arguments;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.core.management.Manager;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by ilo on 10.02.14.
 */
@CommandDefinition(name = "add",description = "Add a Module to the Manager")
public class AddModuleCommand extends AbstractManagerCommand{
    public AddModuleCommand(Manager manager) {
        super(manager);
    }

    @Arguments( description = "files to add")
    List<File> fileToAdd;

    @Option(shortName = 'f',description =  "force adding of existing file with same name and version",hasValue = false)
    boolean force;

    @Override
    public CommandResult execute(CommandInvocation commandInvocation) throws IOException {
        for(File f: fileToAdd){
            manager.add(f, force);
        }
        return CommandResult.SUCCESS;
    }
}
