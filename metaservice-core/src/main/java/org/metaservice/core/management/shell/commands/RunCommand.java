package org.metaservice.core.management.shell.commands;

import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.core.management.Manager;
import org.metaservice.core.management.ManagerConfig;
import org.metaservice.core.management.shell.DescriptorHelper;
import org.metaservice.core.management.shell.completer.PostProcessorCompleter;
import org.metaservice.core.management.shell.completer.ProviderCompleter;
import org.metaservice.core.management.shell.validator.GreaterThanZeroValidator;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by ilo on 10.02.14.
 */
@CommandDefinition(name = "run",description = "start a provider instance")
public class RunCommand extends AbstractManagerCommand {
    public RunCommand(Manager manager) {
        super(manager);
    }

    @Option(name = "count",shortName = 'n', description = "number of instances to be spawned", defaultValue = {"1"},validator = GreaterThanZeroValidator.class)
    int n;

    @Option(name ="provider",shortName = 'c',completer = ProviderCompleter.class)
    String provider;

    @Option(name ="postprocessor",shortName = 'p',completer = PostProcessorCompleter.class)
    String postprocessor;

    @Option(name ="frontend",shortName = 'f',hasValue = false)
    boolean frontend;

    @Override
    public CommandResult execute(CommandInvocation commandInvocation) throws IOException {
        Collection<ManagerConfig.Module> installedModules = manager.getManagerConfig().getInstalledModules();
        ManagerConfig.Module module = DescriptorHelper.getModuleFromString(installedModules, provider);

        for(int i = 0; i < n ;i++){
            if(provider != null){
                MetaserviceDescriptor.ProviderDescriptor providerDescriptor = DescriptorHelper.getProviderFromString(installedModules, provider);
                manager.runProvider(module,providerDescriptor);
            }
            if(postprocessor != null){
                MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor = DescriptorHelper.getPostProcessorFromString(installedModules, postprocessor);
                manager.runPostProcessor(module,postProcessorDescriptor);
            }
        }
        if(frontend)
        {
            manager.runFrontend();
        }
        return CommandResult.SUCCESS;
    }

}
