package org.metaservice.manager.shell.commands;

import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.manager.Manager;
import org.metaservice.manager.ManagerException;

import java.io.IOException;

/**
 * Created by ilo on 22.02.14.
 */
@CommandDefinition(name = "test",description = "")
public class Test extends AbstractManagerCommand{

    @Option(name="reponame",required = true)
    String repo;

    public Test(Manager manager) {
        super(manager);
    }

    @Override
    public CommandResult execute2(CommandInvocation commandInvocation) throws IOException, ManagerException {
        manager.loadAllDataFromArchive(repo);
        return CommandResult.SUCCESS;
    }
}
