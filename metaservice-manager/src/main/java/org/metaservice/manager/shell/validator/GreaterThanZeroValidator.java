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
import org.jboss.aesh.console.command.validator.ValidatorInvocation;

/**
 * Created by ilo on 11.02.14.
 */
public class GreaterThanZeroValidator implements OptionValidator {
    @Override
    public void validate(ValidatorInvocation validatorInvocation) throws OptionValidatorException {
        if(((Integer)validatorInvocation.getValue()) < 1)
            throw new OptionValidatorException(validatorInvocation + " is 0 or negative");

    }

}
