package org.metaservice.manager.shell.commands;

import org.jboss.aesh.cl.Arguments;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.core.config.ManagerConfig;
import org.metaservice.manager.Manager;
import org.metaservice.manager.shell.DescriptorHelper;
import org.metaservice.manager.shell.completer.AvailableModuleCompleter;
import org.metaservice.manager.shell.validator.ModuleValidator;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by ilo on 18.02.14.
 */
@CommandDefinition(name = "addMaven",description = "Add a Module to the Manager")
public class AddMavenCommand extends AbstractManagerCommand{
    public AddMavenCommand(Manager manager) {
        super(manager);
    }
    @Arguments(completer = AvailableModuleCompleter.class, validator = ModuleValidator.class)
    private List<String> moduleIdentifier;
    @Override
    public CommandResult execute2(CommandInvocation commandInvocation) throws IOException {
        Collection<ManagerConfig.Module> availableModules = manager.getManagerConfig().getAvailableModules();
        manager.getMavenManager().addUpdateFromMaven(DescriptorHelper.getModuleFromString(availableModules, moduleIdentifier.get(0)).getMetaserviceDescriptor().getModuleInfo());
        return CommandResult.SUCCESS;
    }
}
