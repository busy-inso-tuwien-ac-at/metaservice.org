package org.metaservice.core.dispatcher.postprocessor;

import com.google.common.base.Optional;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.core.dispatcher.MetaserviceSimplePipe;
import org.metaservice.core.postprocessor.PostProcessorDispatcher;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Set;

/**
* Created by ilo on 23.07.2014.
*/
public class RunPostprocessorPipe extends MetaserviceSimplePipe<PostProcessorDispatcher.Context,PostProcessorDispatcher.Context> {
    private final PostProcessor postProcessor;
    private final Set<Statement> loadedStatements;

    @Inject
    public RunPostprocessorPipe(PostProcessor postProcessor, Set<Statement> loadedStatements, Logger logger) {
        super(logger);
        this.postProcessor = postProcessor;
        this.loadedStatements = loadedStatements;
    }

    @Override
    public Optional<PostProcessorDispatcher.Context> process(PostProcessorDispatcher.Context context) throws Exception {
        try {
            URI resource =context.task.getChangedURI();
            LOGGER.info("Starting to process {} ", resource);
            postProcessor.process(resource, context.resultConnection,context.task.getTime());
        } catch (PostProcessorException e) {
            e.printStackTrace();
        }
        context.resultConnection.add(loadedStatements);
        context.resultConnection.commit();
        return Optional.of(context);
    }
}
