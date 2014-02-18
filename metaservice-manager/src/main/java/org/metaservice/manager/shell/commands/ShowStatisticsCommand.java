package org.metaservice.manager.shell.commands;


import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.manager.Manager;

import java.io.IOException;
import java.util.Map;

@CommandDefinition(name = "stats", description = "show statistics")
public class ShowStatisticsCommand extends AbstractManagerCommand {
    public ShowStatisticsCommand(Manager manager) {
        super(manager);
    }

    @Option(name = "activemq",hasValue = false)
    boolean activemq;

    @Override
    public CommandResult execute2(CommandInvocation commandInvocation) throws IOException {
        if(activemq){
            Map<String,Map<String,Object>> stats = manager.getCurrentActiveMQStatisitics();
            for(Map<String,Object> entry: stats.values()){
                System.out.println(entry.get("size") + " " + entry.get("destinationName"));
            }
        }
        return CommandResult.SUCCESS;
    }
}
