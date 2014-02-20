package org.metaservice.manager.shell.completer;

import org.metaservice.core.config.ManagerConfig;
import org.metaservice.core.descriptor.DescriptorHelper;
import org.metaservice.api.descriptor.MetaserviceDescriptor;

/**
 * Created by ilo on 11.02.14.
 */
public class PostProcessorCompleter extends AbstractManagerCompleter {

    @Override
    public void complete(ManagerCompleteInvocation completerInvocation) {
        for(ManagerConfig.Module module : completerInvocation.getManager().getManagerConfig().getInstalledModules()){
            MetaserviceDescriptor descriptor = module.getMetaserviceDescriptor();
            MetaserviceDescriptor.ModuleInfo moduleInfo = descriptor.getModuleInfo();
            for(MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor : descriptor.getPostProcessorList()){
                String identifier = DescriptorHelper.getStringFromPostProcessor(moduleInfo, postProcessorDescriptor);
                if(identifier.startsWith(completerInvocation.getGivenCompleteValue())){
                    completerInvocation.addCompleterValue(identifier);
                }

            }
        }
    }
}
