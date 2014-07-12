package org.metaservice.manager.shell.commands;

import org.jboss.aesh.cl.Arguments;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.api.messaging.config.ManagerConfig;
import org.metaservice.manager.Manager;
import org.metaservice.manager.ManagerException;
import org.metaservice.manager.shell.completer.AvailableModuleCompleter;
import org.metaservice.manager.shell.completer.InstalledModuleCompleter;
import org.metaservice.manager.shell.validator.ModuleValidator;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by ilo on 18.02.14.
 */
@CommandDefinition(name = "reinstall",description = "")
public class ReinstallCommand extends AbstractManagerCommand{
    public ReinstallCommand(Manager manager) {
        super(manager);
    }
    @Arguments(completer = InstalledModuleCompleter.class, validator = ModuleValidator.class)
    private List<String> moduleIdentifier;

    @Option(name="replace",hasValue =false)
    private boolean replace;

    @Override
    public CommandResult execute2(CommandInvocation commandInvocation) throws IOException {
        Collection<ManagerConfig.Module> availableModules = manager.getManagerConfig().getAvailableModules();
        try {
            ManagerConfig.Module installedModule = descriptorHelper.getModuleFromString(availableModules, moduleIdentifier.get(0));
            manager.uninstall(installedModule,false);
            manager.getMavenManager().updateModule(installedModule,replace);
            manager.install(installedModule);
        } catch (ManagerException e) {
            e.printStackTrace();
        }
        return CommandResult.SUCCESS;
    }
}
