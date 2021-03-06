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
