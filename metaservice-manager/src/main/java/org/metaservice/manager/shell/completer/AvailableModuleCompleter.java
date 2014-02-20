package org.metaservice.manager.shell.completer;


import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.core.config.ManagerConfig;
import org.metaservice.core.descriptor.DescriptorHelper;

/**
 * Created by ilo on 10.02.14.
 */
public class AvailableModuleCompleter extends AbstractManagerCompleter {

    @Override
    public void complete(ManagerCompleteInvocation completerInvocation) {
        for(ManagerConfig.Module m : completerInvocation.getManager().getManagerConfig().getAvailableModules()){
            MetaserviceDescriptor.ModuleInfo moduleInfo = m.getMetaserviceDescriptor().getModuleInfo();
            String identifier = DescriptorHelper.getModuleIdentifierStringFromModule(moduleInfo);
            if(identifier.startsWith(completerInvocation.getGivenCompleteValue())){
                completerInvocation.addCompleterValue(identifier);
            }
        }
    }
}
