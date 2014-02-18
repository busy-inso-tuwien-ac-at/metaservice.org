package org.metaservice.manager.shell.activator;

import org.jboss.aesh.cl.activation.OptionActivator;
import org.jboss.aesh.cl.internal.ProcessedCommand;
import org.jboss.aesh.cl.internal.ProcessedOption;

/**
 * Created by ilo on 11.02.14.
 */
public class ModuleActivator implements OptionActivator {
    @Override
    public boolean isActivated(ProcessedCommand processedCommand) {
        ProcessedOption option = processedCommand.findLongOption("module");
        return (option.getValue() != null);
    }
}
