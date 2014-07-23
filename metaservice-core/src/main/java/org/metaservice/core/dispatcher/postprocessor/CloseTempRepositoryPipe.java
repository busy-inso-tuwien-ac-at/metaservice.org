package org.metaservice.core.dispatcher.postprocessor;

import cc.ilo.cc.ilo.pipeline.pipes.SimplePipe;
import com.google.common.base.Optional;
import org.metaservice.core.postprocessor.PostProcessorDispatcher;

/**
* Created by ilo on 23.07.2014.
*/
public class CloseTempRepositoryPipe implements SimplePipe<PostProcessorDispatcher.Context,PostProcessorDispatcher.Context> {
    @Override
    public Optional<PostProcessorDispatcher.Context> process(PostProcessorDispatcher.Context context) throws Exception {
        context.resultConnection.close();
        context.resultRepository.shutDown();
        context.resultConnection = null;
        context.resultRepository = null;
        return Optional.of(context);
    }
}
