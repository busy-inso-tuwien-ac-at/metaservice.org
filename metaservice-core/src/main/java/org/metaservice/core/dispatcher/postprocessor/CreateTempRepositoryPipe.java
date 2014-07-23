package org.metaservice.core.dispatcher.postprocessor;

import cc.ilo.cc.ilo.pipeline.pipes.SimplePipe;
import com.google.common.base.Optional;
import org.metaservice.core.AbstractDispatcher;
import org.metaservice.core.postprocessor.PostProcessorDispatcher;

/**
* Created by ilo on 23.07.2014.
*/
public class CreateTempRepositoryPipe implements SimplePipe<PostProcessorDispatcher.Context,PostProcessorDispatcher.Context> {
    @Override
    public Optional<PostProcessorDispatcher.Context> process(PostProcessorDispatcher.Context context) throws Exception {
        context.resultRepository = AbstractDispatcher.createTempRepository(true);
        context.resultConnection = context.resultRepository.getConnection();
        return Optional.of(context);
    }
}
