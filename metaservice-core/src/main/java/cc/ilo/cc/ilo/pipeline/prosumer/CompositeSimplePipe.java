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

package cc.ilo.cc.ilo.pipeline.prosumer;

import cc.ilo.cc.ilo.pipeline.pipes.AbstractPipe;
import cc.ilo.cc.ilo.pipeline.pipes.SimplePipe;
import com.google.common.base.Optional;

/**
 * Created by ilo on 23.07.2014.
 */
public class CompositeSimplePipe<I,X,O> implements SimplePipe<I,O> {
    final AbstractPipe<X,O> subConsumer;
    final AbstractPipe<I,X> pipe;

    public CompositeSimplePipe(AbstractPipe<X, O> subConsumer, AbstractPipe<I, X> pipe) {
        this.subConsumer = subConsumer;
        this.pipe = pipe;
    }

    @Override
    public Optional<O> process(I input) throws Exception {
        Optional<X> result = pipe.process(input);
        if(result.isPresent())
            return subConsumer.process(result.get());
        else
            return Optional.absent();
    }
}
