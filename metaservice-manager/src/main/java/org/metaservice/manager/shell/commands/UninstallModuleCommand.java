package org.metaservice.manager.shell.commands;

import org.metaservice.manager.Manager;
import org.metaservice.core.config.ManagerConfig;
import org.metaservice.manager.ManagerException;
import org.metaservice.manager.shell.DescriptorHelper;
import org.metaservice.manager.shell.completer.AvailableModuleCompleter;
import org.metaservice.manager.shell.validator.ModuleValidator;
import org.jboss.aesh.cl.Arguments;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ilo on 12.02.14.
 */
@CommandDefinition(name="uninstall", description = "uninstall a module")
public class UninstallModuleCommand extends AbstractManagerCommand{
    public UninstallModuleCommand(Manager manager) {
        super(manager);
    }

    @Arguments(completer = AvailableModuleCompleter.class, validator = ModuleValidator.class)
    private List<String> moduleIdentifier;

    @Option(name = "remove-data",shortName = 'd',hasValue = false)
    private boolean removeData;

    @Override
    public CommandResult execute2(CommandInvocation commandInvocation) throws IOException {
        Collection<ManagerConfig.Module> installedModules = manager.getManagerConfig().getInstalledModules();
        if(installedModules == null)
            installedModules= new ArrayList<>();
        for(String s : moduleIdentifier){
            ManagerConfig.Module installedModule = DescriptorHelper.getModuleFromString(installedModules, s);
            if(installedModule == null){
                commandInvocation.getShell().out().println("not installed: " + s);
                continue;
            }
            try {
                manager.uninstall(installedModule,removeData);
            } catch (ManagerException e) {
                e.printStackTrace();
            }
        }
        return CommandResult.SUCCESS;
    }
}
