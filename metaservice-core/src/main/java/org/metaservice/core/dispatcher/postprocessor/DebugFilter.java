package org.metaservice.core.dispatcher.postprocessor;

import org.metaservice.core.dispatcher.MetaserviceFilterPipe;
import org.metaservice.core.postprocessor.Debug;
import org.metaservice.core.postprocessor.PostProcessorDispatcher;
import org.slf4j.Logger;

import javax.inject.Inject;

/**
* Created by ilo on 23.07.2014.
*/
public class DebugFilter extends MetaserviceFilterPipe<PostProcessorDispatcher.Context> {
    private final Debug debug;

    @Inject
    public DebugFilter(Logger logger, Debug debug) {
        super(logger);
        this.debug = debug;
    }

    @Override
    public boolean accept(PostProcessorDispatcher.Context context) {

        if(debug.isEnabled() &&!debug.process(context.task)){
            LOGGER.debug("not processing -> debug");
            return false;
        }
        return true;
    }
}
