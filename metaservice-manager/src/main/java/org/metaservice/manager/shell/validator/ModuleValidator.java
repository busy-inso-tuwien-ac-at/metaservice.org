package org.metaservice.manager.shell.validator;

import org.jboss.aesh.cl.validator.OptionValidator;
import org.jboss.aesh.cl.validator.OptionValidatorException;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.config.ManagerConfig;
import org.metaservice.api.messaging.descriptors.DescriptorHelper;
import org.metaservice.core.descriptor.DescriptorHelperImpl;

/**
 * Created by ilo on 10.02.14.
 */
public class ModuleValidator implements OptionValidator<ManagerValidationInvocation> {
    private final DescriptorHelper descriptorHelper = new DescriptorHelperImpl();
    @Override
    public void validate(ManagerValidationInvocation validatorInvocation) throws OptionValidatorException {
        boolean found = false;
        for(ManagerConfig.Module m : validatorInvocation.getManager().getManagerConfig().getAvailableModules()){
            MetaserviceDescriptor.ModuleInfo moduleInfo = m.getMetaserviceDescriptor().getModuleInfo();
            String identifier = descriptorHelper.getModuleIdentifierStringFromModule(moduleInfo);
            if(identifier.equals(validatorInvocation.getValue())){
                found = true;
                break;
            }
        }
        if(!found)
            throw new OptionValidatorException("Module not found");
    }
}
