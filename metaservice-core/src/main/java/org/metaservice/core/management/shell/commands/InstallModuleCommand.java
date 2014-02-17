package org.metaservice.core.management.shell.commands;

import org.jboss.aesh.cl.Arguments;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.core.management.Manager;
import org.metaservice.core.management.ManagerConfig;
import org.metaservice.core.management.ManagerException;
import org.metaservice.core.management.shell.DescriptorHelper;
import org.metaservice.core.management.shell.completer.AvailableModuleCompleter;
import org.metaservice.core.management.shell.validator.ModuleValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ilo on 10.02.14.
 */
@CommandDefinition(name = "install",description = "install/upgrade a module previously added")
public class InstallModuleCommand extends AbstractManagerCommand {
    public InstallModuleCommand(Manager manager) {
        super(manager);
    }


    @Arguments(completer = AvailableModuleCompleter.class, validator = ModuleValidator.class)
    private List<String> moduleIdentifier;

    @Override
    public CommandResult execute2(CommandInvocation commandInvocation) throws IOException {
        Collection<ManagerConfig.Module> installedModules = manager.getManagerConfig().getInstalledModules();
        if(installedModules == null)
            installedModules= new ArrayList<>();
        Collection<ManagerConfig.Module> availableModules = manager.getManagerConfig().getAvailableModules();
        if(availableModules == null)
            availableModules = new ArrayList<>();
        for(String s : moduleIdentifier){

            ManagerConfig.Module installedModule = DescriptorHelper.getModuleFromString(installedModules, s);
            if(installedModule != null){
                commandInvocation.getShell().out().println("already installed: " + s);
                continue;
            }
            ManagerConfig.Module availableModule = DescriptorHelper.getModuleFromString(availableModules, s);
            try {
                manager.install(availableModule.getLocation());
            } catch (ManagerException e) {
                e.printStackTrace();
            }
        }
        return CommandResult.SUCCESS;
    }
}
