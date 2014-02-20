package org.metaservice.manager.shell.commands;

import org.jboss.aesh.cl.Arguments;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.core.config.ManagerConfig;
import org.metaservice.manager.Manager;
import org.metaservice.manager.ManagerException;
import org.metaservice.core.descriptor.DescriptorHelper;
import org.metaservice.manager.shell.completer.AvailableModuleCompleter;
import org.metaservice.manager.shell.validator.ModuleValidator;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by ilo on 19.02.14.
 */
@CommandDefinition(name="rm",description = "foo")
public class RemoveModuleCommand extends AbstractManagerCommand{

    public RemoveModuleCommand(Manager manager) {
        super(manager);
    }

    @Arguments(completer = AvailableModuleCompleter.class, validator = ModuleValidator.class)
    private List<String> moduleIdentifier;


    @Option(name = "hard",description = "do not make any checks, just remove from list", hasValue = false)
    boolean hard;

    @Override
    public CommandResult execute2(CommandInvocation commandInvocation) throws IOException, ManagerException {
        Collection<ManagerConfig.Module> installedModules = manager.getManagerConfig().getInstalledModules();
        Collection<ManagerConfig.Module> availableModules = manager.getManagerConfig().getAvailableModules();

        if(hard){
            for(String identifier : moduleIdentifier){
                ManagerConfig.Module installedModule = DescriptorHelper.getModuleFromString(installedModules, identifier);
                if(installedModule != null){
                    LOGGER.warn("{} is installed, ignoring", identifier);
                    continue;
                }
                ManagerConfig.Module availableModule = DescriptorHelper.getModuleFromString(availableModules,identifier);
                if(availableModule == null){
                    LOGGER.warn("{} not found, ignoring", identifier);
                }
                manager.removeModule(availableModule);
            }
        }else{
            throw new RuntimeException("Not implemented");
        }

        return CommandResult.SUCCESS;
    }
}
