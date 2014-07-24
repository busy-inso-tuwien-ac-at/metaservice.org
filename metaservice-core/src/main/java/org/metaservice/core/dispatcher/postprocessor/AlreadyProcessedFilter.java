package org.metaservice.core.dispatcher.postprocessor;

import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.PostProcessingHistoryItem;
import org.metaservice.core.dispatcher.MetaserviceFilterPipe;
import org.metaservice.core.postprocessor.PostProcessorDispatcher;
import org.openrdf.model.URI;
import org.slf4j.Logger;

import javax.inject.Inject;

/**
* Created by ilo on 23.07.2014.
*/
public class AlreadyProcessedFilter extends MetaserviceFilterPipe<PostProcessorDispatcher.Context> {
    private final MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor;

    @Inject
    public AlreadyProcessedFilter(Logger logger, MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor) {
        super(logger);
        this.postProcessorDescriptor = postProcessorDescriptor;
    }

    @Override
    public boolean accept(PostProcessorDispatcher.Context context){
        for(PostProcessingHistoryItem item  :context.task.getHistory()){
            if(item.getPostprocessorId().equals(postProcessorDescriptor.getId())){
                for(URI uri: item.getResources()){
                    if(uri.equals(context.task.getChangedURI())){
                        LOGGER.debug("Already processed the same uri");
                        //this would be a more strict variant, but is not required such that everything can run faster :-)
                    }
                }
                LOGGER.debug("Not processing, because did already process");
                return false;
            }
        }
        return true;
    }
}
