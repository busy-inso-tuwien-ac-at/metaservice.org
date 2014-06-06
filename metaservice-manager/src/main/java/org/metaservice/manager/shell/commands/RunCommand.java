package org.metaservice.manager.shell.commands;

import org.metaservice.core.config.ManagerConfig;
import org.metaservice.manager.ManagerException;
import org.metaservice.manager.shell.completer.InstalledModuleCompleter;
import org.metaservice.manager.shell.validator.GreaterThanZeroValidator;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.manager.Manager;
import org.metaservice.core.descriptor.DescriptorHelper;
import org.metaservice.manager.shell.completer.PostProcessorCompleter;
import org.metaservice.manager.shell.completer.ProviderCompleter;

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

    @Option(name ="provider",shortName = 's',completer = ProviderCompleter.class)
    String provider;

    @Option(name ="postprocessor",shortName = 'p',completer = PostProcessorCompleter.class)
    String postprocessor;

    @Option(name="crawler",shortName = 'c')
    String repository;

    @Option(name="allof",completer = InstalledModuleCompleter.class)
    String moduleId;

    @Option(name ="frontend",shortName = 'f',hasValue = false)
    boolean frontend;

    @Option(name ="debug",shortName = 'd',hasValue = false)
    boolean debug;

    @Option(name= "vcs",shortName = 'v',hasValue = false)
    boolean vcs;

    @Override
    public CommandResult execute2(CommandInvocation commandInvocation) throws IOException {
        Collection<ManagerConfig.Module> installedModules = manager.getManagerConfig().getInstalledModules();

        for(int i = 0; i < n ;i++){
            if(provider != null){
                ManagerConfig.Module module = DescriptorHelper.getModuleFromString(installedModules, provider);
                MetaserviceDescriptor.ProviderDescriptor providerDescriptor = DescriptorHelper.getProviderFromString(installedModules, provider);
                try {
                    manager.getRunManager().runProvider(module,providerDescriptor);
                } catch (ManagerException e) {
                    e.printStackTrace();
                }
            }
            if(postprocessor != null){
                ManagerConfig.Module module = DescriptorHelper.getModuleFromString(installedModules, postprocessor);
                MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor = DescriptorHelper.getPostProcessorFromString(installedModules, postprocessor);
                try {
                    if(module != null && postProcessorDescriptor != null){
                        manager.getRunManager().runPostProcessor(module,postProcessorDescriptor,debug);

                    }else {
                        LOGGER.error("module not found");
                    }
                } catch (ManagerException e) {
                    e.printStackTrace();
                }
            }
            if(repository != null){
                System.out.println("Starting crawler by shell is not yet implemented");
            }
            if(moduleId !=null){
                ManagerConfig.Module module = DescriptorHelper.getModuleFromString(manager.getManagerConfig().getInstalledModules(),moduleId);
                if(module!=null){
                    for(MetaserviceDescriptor.ProviderDescriptor providerDescriptor : module.getMetaserviceDescriptor().getProviderList()){
                        try {
                            manager.getRunManager().runProvider(module, providerDescriptor);
                        } catch (ManagerException e) {
                            e.printStackTrace();
                        }
                    }
                    for(MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor : module.getMetaserviceDescriptor().getPostProcessorList()){
                        try {
                            manager.getRunManager().runPostProcessor(module,postProcessorDescriptor, debug);
                        } catch (ManagerException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        if(frontend)
        {
            System.out.println("Trying to start frontend");
            try {
                manager.getRunManager().runFrontend();
            } catch (ManagerException e) {
                e.printStackTrace();
            }
        }
        if(vcs){
            System.out.println("Trying to start vcs");
            try {
                manager.getRunManager().runVcs();
            } catch (ManagerException e) {
                e.printStackTrace();
            }
        }
        return CommandResult.SUCCESS;
    }

}
