package org.metaservice.core.management.shell.commands;

import org.jboss.aesh.console.command.Command;
import org.metaservice.core.management.Manager;

/**
 * Created by ilo on 10.02.14.
 */
public abstract  class AbstractManagerCommand implements Command{
    protected final Manager manager;

    public AbstractManagerCommand(Manager manager) {
        this.manager = manager;
    }

}
