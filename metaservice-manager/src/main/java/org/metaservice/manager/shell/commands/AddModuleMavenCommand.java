package org.metaservice.manager.shell.commands;

import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.manager.Manager;
import org.metaservice.manager.ManagerException;
import java.io.IOException;

/**
 * Created by ilo on 18.02.14.
 */
@CommandDefinition(name = "addMaven",description = "Add a Module to the Manager")
public class AddModuleMavenCommand extends AbstractManagerCommand{
    public AddModuleMavenCommand(Manager manager) {
        super(manager);
    }

    @Option(name="groupId", required = true)
    String groupId;

    @Option(name="artifactId",required = true)
    String artifactId;

    @Option(name="version", required = true)
    String version;

    @Option(shortName = 'f',description =  "force adding of existing file with same name and version",hasValue = false)
    boolean force;



    @Override
    public CommandResult execute2(CommandInvocation commandInvocation) throws IOException, ManagerException {
            manager.getMavenManager().retrieveAndAddModule(groupId, artifactId, version,force);
        return CommandResult.SUCCESS;
    }
}
