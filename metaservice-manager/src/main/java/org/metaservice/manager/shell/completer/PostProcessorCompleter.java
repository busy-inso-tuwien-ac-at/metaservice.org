package org.metaservice.manager.shell.completer;

import org.metaservice.api.messaging.config.ManagerConfig;
import org.metaservice.api.messaging.descriptors.DescriptorHelper;
import org.metaservice.core.descriptor.DescriptorHelperImpl;
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
                String identifier = descriptorHelper.getStringFromPostProcessor(moduleInfo, postProcessorDescriptor);
                if(identifier.startsWith(completerInvocation.getGivenCompleteValue())){
                    completerInvocation.addCompleterValue(identifier);
                }

            }
        }
    }
}
