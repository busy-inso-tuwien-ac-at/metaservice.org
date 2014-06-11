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

    @Option(name="reponame")
    String repo;

    @Option(name="packages",hasValue = false)
    boolean packages;

    public Test(Manager manager) {
        super(manager);
    }

    @Override
    public CommandResult execute2(CommandInvocation commandInvocation) throws IOException, ManagerException {
        if(repo != null) {
            manager.loadAllDataFromArchive(repo);
        }
        if(packages){
            manager.postProcessAllPackages();
        }
        return CommandResult.SUCCESS;
    }
}
