/*
 * Copyright 2015 Nikola Ilo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
