package org.metaservice.core.management.shell.validator;

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
