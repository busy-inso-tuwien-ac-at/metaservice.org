package org.metaservice.core.dispatcher.postprocessor;

import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.core.dispatcher.MetaserviceFilterPipe;
import org.metaservice.core.postprocessor.PostProcessorDispatcher;
import org.openrdf.model.URI;
import org.slf4j.Logger;

import javax.inject.Inject;

/**
* Created by ilo on 23.07.2014.
*/
public class AbortEarlyFilter extends MetaserviceFilterPipe<PostProcessorDispatcher.Context> {
    private final PostProcessor postProcessor;

    @Inject
    public AbortEarlyFilter(PostProcessor postProcessor, Logger logger) {
        super(logger);
        this.postProcessor = postProcessor;
    }

    @Override
    public boolean accept(PostProcessorDispatcher.Context context) throws PostProcessorException {
        URI resource = context.task.getChangedURI();
        if(postProcessor.abortEarly(resource)){
            LOGGER.debug("Not continuing processing because abortEarly({}) was true", resource);
            return false;
        }
        return true;
    }
}
