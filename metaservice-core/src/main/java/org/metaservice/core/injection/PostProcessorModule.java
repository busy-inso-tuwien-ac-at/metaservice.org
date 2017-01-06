/*
 * Copyright 2015 Nikola Ilo
 * Research Group for Industrial Software, Vienna University of Technology, Austria
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaservice.core.injection;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.core.postprocessor.Debug;
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

            Debug debug;
                debug = new Debug();
            bind(Debug.class).toInstance(debug);
            bind(PostProcessor.class).to(postProcessorClazz);
            bind(MetaserviceDescriptor.PostProcessorDescriptor.class).toInstance(postProcessorDescriptor);
            bind(MetaserviceDescriptor.OntologyLoaderDescriptor.class).toInstance(postProcessorDescriptor);
            bind(new TypeLiteral<org.metaservice.api.messaging.dispatcher.PostProcessorDispatcher>(){}).to((TypeLiteral<? extends PostProcessorDispatcher>) TypeLiteral.get(dispatcherType));
        } catch (ClassNotFoundException e) {
            addError(e);
        }

    }
}
