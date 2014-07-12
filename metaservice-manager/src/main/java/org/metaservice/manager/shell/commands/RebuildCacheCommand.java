package org.metaservice.manager.shell.commands;

import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.manager.Manager;
import org.metaservice.manager.ManagerException;

import java.io.IOException;

/**
 * Created by ilo on 30.06.2014.
 */
@CommandDefinition(name = "rebuildCache",description = "rebuild the \"latest\" cache")
public class RebuildCacheCommand extends AbstractManagerCommand {
    public RebuildCacheCommand(Manager manager) {
        super(manager);
    }

    @Option(name="iterative",hasValue =false)
    private boolean iterative;
    @Override
    public CommandResult execute2(CommandInvocation commandInvocation) throws IOException, ManagerException {
        if(iterative){
            manager.buildIterativeCache();
        }else {
            manager.rebuildLatestCache();
        }
        return CommandResult.SUCCESS;
    }
}
