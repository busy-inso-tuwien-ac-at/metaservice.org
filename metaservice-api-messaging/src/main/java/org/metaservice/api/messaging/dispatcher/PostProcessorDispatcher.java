package org.metaservice.api.messaging.dispatcher;

import org.metaservice.api.messaging.PostProcessingTask;
import org.metaservice.api.postprocessor.PostProcessorException;

/**
 * Created by ilo on 09.06.2014.
 */
public interface PostProcessorDispatcher {
    void process(PostProcessingTask task, long jmsTimestamp) throws PostProcessorException;
    void processAsync(PostProcessingTask task, long timestamp, Callback callback);
}
