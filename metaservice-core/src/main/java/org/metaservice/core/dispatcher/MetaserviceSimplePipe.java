package org.metaservice.core.dispatcher;

import cc.ilo.cc.ilo.pipeline.pipes.SimplePipe;
import org.slf4j.Logger;

/**
 * Created by ilo on 23.07.2014.
 */
public abstract class MetaserviceSimplePipe<I,O> implements SimplePipe<I,O> {
    protected final Logger LOGGER;

    protected MetaserviceSimplePipe(Logger logger) {
        LOGGER = logger;
    }
}
