package org.metaservice.manager.shell.completer;

import org.jboss.aesh.cl.completer.CompleterData;
import org.jboss.aesh.console.AeshContext;
import org.jboss.aesh.console.command.Command;
import org.metaservice.manager.Manager;

/**
 * Created by ilo on 10.02.14.
 */
public class ManagerCompleteInvocation extends CompleterData {
    private final Manager manager;

    public ManagerCompleteInvocation(AeshContext aeshContext, String completeValue, Command command, Manager manager) {
        super(aeshContext, completeValue, command);
        this.manager = manager;
    }

    public Manager getManager() {
        return manager;
    }
}
