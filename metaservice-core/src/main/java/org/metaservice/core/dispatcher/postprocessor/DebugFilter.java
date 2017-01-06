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

import org.metaservice.core.dispatcher.MetaserviceFilterPipe;
import org.metaservice.core.postprocessor.Debug;
import org.metaservice.core.postprocessor.PostProcessorDispatcher;
import org.slf4j.Logger;

import javax.inject.Inject;

/**
* Created by ilo on 23.07.2014.
*/
public class DebugFilter extends MetaserviceFilterPipe<PostProcessorDispatcher.Context> {
    private final Debug debug;

    @Inject
    public DebugFilter(Logger logger, Debug debug) {
        super(logger);
        this.debug = debug;
    }

    @Override
    public boolean accept(PostProcessorDispatcher.Context context) {

        if(debug.isEnabled() &&!debug.process(context.task)){
            LOGGER.debug("not processing -> debug");
            return false;
        }
        return true;
    }
}
