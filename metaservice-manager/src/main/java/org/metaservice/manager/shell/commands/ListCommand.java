package org.metaservice.manager.shell.commands;

import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.manager.Manager;
import org.metaservice.core.config.ManagerConfig;
import org.metaservice.core.descriptor.DescriptorHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ilo on 11.02.14.
 */
@CommandDefinition(name = "lsmod",description = "show information on current modules")
public class ListCommand extends AbstractManagerCommand {
    public ListCommand(Manager manager) {
        super(manager);
    }

    @Option(name = "all",shortName = 'a',hasValue = false)
    boolean showAll;


    @Option(name = "sort",shortName = 's',hasValue = false)
    boolean sort;

    @Override
    public CommandResult execute2(CommandInvocation commandInvocation) throws IOException {
        List<ManagerConfig.Module> result;
        if(showAll){
            result = manager.getManagerConfig().getAvailableModules();
        } else{
            result = manager.getManagerConfig().getInstalledModules();
        }

        if(result.size() == 0){
            commandInvocation.getShell().out().println("no modules found");
            return CommandResult.SUCCESS;
        }

        List<String> resultString = new ArrayList<>();
        for(ManagerConfig.Module m : result){
            resultString.add(DescriptorHelper.getModuleIdentifierStringFromModule(m.getMetaserviceDescriptor().getModuleInfo()));
        }
        if(sort){
            Collections.sort(resultString);
        }
        for(String s : resultString){
            commandInvocation.getShell().out().println(s);
        }
        return CommandResult.SUCCESS;
    }
}
