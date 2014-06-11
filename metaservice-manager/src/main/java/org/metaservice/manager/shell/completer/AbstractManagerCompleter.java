package org.metaservice.manager.shell.completer;

import org.jboss.aesh.cl.completer.OptionCompleter;
import org.metaservice.api.messaging.descriptors.DescriptorHelper;
import org.metaservice.core.descriptor.DescriptorHelperImpl;

/**
 * Created by ilo on 10.02.14.
 */
public abstract class AbstractManagerCompleter implements OptionCompleter<ManagerCompleteInvocation> {
    protected final DescriptorHelper descriptorHelper;

    public AbstractManagerCompleter() {
        descriptorHelper = new DescriptorHelperImpl();
    }
}
