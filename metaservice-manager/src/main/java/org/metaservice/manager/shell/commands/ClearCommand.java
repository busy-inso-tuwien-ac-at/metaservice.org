package org.metaservice.manager.shell.commands;

import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.config.ManagerConfig;
import org.metaservice.manager.Manager;
import org.metaservice.manager.ManagerException;
import org.metaservice.manager.shell.completer.InstalledModuleCompleter;
import org.metaservice.manager.shell.completer.PostProcessorCompleter;
import org.metaservice.manager.shell.completer.ProviderCompleter;
import org.metaservice.manager.shell.validator.GreaterThanZeroValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by ilo on 10.02.14.
 */
@CommandDefinition(name = "clear",description = "remove data")
public class ClearCommand extends AbstractManagerCommand {
    public ClearCommand(Manager manager) {
        super(manager);
    }

    @Option(name ="provider",shortName = 's',completer = ProviderCompleter.class)
    String provider;

    @Option(name ="postprocessor",shortName = 'p',completer = PostProcessorCompleter.class)
    String postprocessor;

    @Override
    public CommandResult execute2(CommandInvocation commandInvocation) throws IOException {
        Collection<ManagerConfig.Module> installedModules = manager.getManagerConfig().getInstalledModules();

        if(provider != null){
            ManagerConfig.Module module = descriptorHelper.getModuleFromString(installedModules, provider);
            MetaserviceDescriptor.ProviderDescriptor providerDescriptor = descriptorHelper.getProviderFromString(installedModules, provider);
            try {
                if (providerDescriptor != null && module != null) {
                    manager.removeProviderData(module.getMetaserviceDescriptor(), providerDescriptor);
                }  else {
                    LOGGER.error("module not found");
                }
            } catch (ManagerException e) {
                LOGGER.error("failed",e);
            }
        }
        if(postprocessor != null){
            ManagerConfig.Module module = descriptorHelper.getModuleFromString(installedModules, postprocessor);
            MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor = descriptorHelper.getPostProcessorFromString(installedModules, postprocessor);
            try {
                if(module != null && postProcessorDescriptor != null){
                    manager.removePostProcessorData(module.getMetaserviceDescriptor(),postProcessorDescriptor);

                }else {
                    LOGGER.error("module not found");
                }
            } catch (ManagerException e) {
                LOGGER.error("failed",e);
                }
        }
        return CommandResult.SUCCESS;
    }

}
