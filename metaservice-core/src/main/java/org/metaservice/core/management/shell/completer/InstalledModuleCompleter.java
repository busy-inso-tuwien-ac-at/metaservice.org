package org.metaservice.core.management.shell.completer;


import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.core.management.ManagerConfig;
import org.metaservice.core.management.shell.DescriptorHelper;

/**
 * Created by ilo on 10.02.14.
 */
public class InstalledModuleCompleter extends AbstractManagerCompleter {

    @Override
    public void complete(ManagerCompleteInvocation completerInvocation) {
        for(ManagerConfig.Module m : completerInvocation.getManager().getManagerConfig().getInstalledModules()){
            MetaserviceDescriptor.ModuleInfo moduleInfo = m.getMetaserviceDescriptor().getModuleInfo();
            String identifier = DescriptorHelper.getModuleIdentifierStringFromModule(moduleInfo);
            if(identifier.startsWith(completerInvocation.getGivenCompleteValue())){
                completerInvocation.addCompleterValue(identifier);
            }
        }
    }
}
