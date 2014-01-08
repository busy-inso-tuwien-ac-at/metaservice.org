package org.metaservice.core.injection;

import com.google.inject.AbstractModule;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.postprocessor.PostProcessor;

/**
 * Created by ilo on 05.01.14.
 */
public class PostProcessorModule extends AbstractModule {
    private final MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor;
    public PostProcessorModule(MetaserviceDescriptor.PostProcessorDescriptor selectedPostProcessor) {
        postProcessorDescriptor = selectedPostProcessor;
    }

    @Override
    protected void configure() {
        try {
            final Class<? extends PostProcessor> postProcessorClazz   =
                    (Class<? extends PostProcessor>) Class.forName(postProcessorDescriptor.getClassName());
            bind(PostProcessor.class).to(postProcessorClazz);

        } catch (ClassNotFoundException e) {
            addError(e);
        }

    }
}
