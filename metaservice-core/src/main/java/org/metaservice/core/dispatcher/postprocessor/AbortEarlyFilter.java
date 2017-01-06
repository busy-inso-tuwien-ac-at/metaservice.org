/*
 * Copyright 2015 Nikola Ilo
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
