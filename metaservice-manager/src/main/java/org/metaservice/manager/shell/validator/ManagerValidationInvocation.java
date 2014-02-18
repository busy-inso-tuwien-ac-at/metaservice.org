package org.metaservice.manager.shell.validator;

import org.jboss.aesh.console.command.validator.ValidatorInvocation;
import org.metaservice.manager.Manager;

/**
 * Created by ilo on 10.02.14.
 */
public class ManagerValidationInvocation implements ValidatorInvocation {
    private final Manager manager;
    private final Object value;
    public ManagerValidationInvocation(Manager manager, Object value) {
        this.manager = manager;
        this.value = value;
    }

    public Manager getManager() {
        return manager;
    }

    @Override
    public Object getValue() {
        return value;
    }


}
