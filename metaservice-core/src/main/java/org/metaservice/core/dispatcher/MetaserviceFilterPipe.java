package org.metaservice.core.dispatcher;

import cc.ilo.cc.ilo.pipeline.pipes.AbstractFilterPipe;
import org.slf4j.Logger;

/**
 * Created by ilo on 23.07.2014.
 */
public abstract class MetaserviceFilterPipe<T> extends AbstractFilterPipe<T>{
    protected final Logger LOGGER;

    public MetaserviceFilterPipe(Logger logger) {
        LOGGER = logger;
    }
}
