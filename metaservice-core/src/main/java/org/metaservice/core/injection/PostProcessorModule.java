package org.metaservice.core.injection;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.core.postprocessor.PostProcessorDispatcher;

import java.lang.reflect.Type;

/**
 * Created by ilo on 05.01.14.
 */
public class PostProcessorModule extends AbstractModule {
    private final MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor;
    public PostProcessorModule(MetaserviceDescriptor.PostProcessorDescriptor selectedPostProcessor) {
        postProcessorDescriptor = selectedPostProcessor;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void configure() {
        try {
            final Class<? extends PostProcessor> postProcessorClazz   =
                    (Class<? extends PostProcessor>) Class.forName(postProcessorDescriptor.getClassName());
            Type dispatcherType = Types.newParameterizedType(PostProcessorDispatcher.class, postProcessorClazz);

            bind(PostProcessor.class).to(postProcessorClazz);
            bind(MetaserviceDescriptor.PostProcessorDescriptor.class).toInstance(postProcessorDescriptor);
            bind(new TypeLiteral<PostProcessorDispatcher>(){}).to((TypeLiteral<? extends PostProcessorDispatcher>) TypeLiteral.get(dispatcherType));

        } catch (ClassNotFoundException e) {
            addError(e);
        }

    }
}
