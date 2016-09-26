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

package cc.ilo.cc.ilo.pipeline.builder;

import cc.ilo.cc.ilo.pipeline.pipes.AbstractPipe;
import cc.ilo.cc.ilo.pipeline.pipes.PipeProvider;
import cc.ilo.cc.ilo.pipeline.prosumer.CompositeSimplePipe;


/**
 * Created by ilo on 23.07.2014.
 */
public class CompositePipeBuilder<I,O>  {

    public final AbstractPipe<I,O> pipe;

    private CompositePipeBuilder(AbstractPipe<I,O> pipe){
        this.pipe = pipe;
    }
    public static <I,O> CompositePipeBuilder<I,O> create(AbstractPipe<I,O> pipe){
        return new CompositePipeBuilder<>(pipe);
    }

    public static <I,O> CompositePipeBuilder<I,O> create(PipeProvider<I,O> pipeProvider){
        return new CompositePipeBuilder<>(pipeProvider.createPipeInstance());
    }

    public <X> CompositePipeBuilder<I,X> pipe(AbstractPipe<O,X> newPipe){
        return new CompositePipeBuilder<I,X>(new CompositeSimplePipe<I,O,X>(newPipe,this.pipe));
    }

    public <X> CompositePipeBuilder<I,X> pipe(PipeProvider<O,X> pipeProvider){
        return new CompositePipeBuilder<I,X>(new CompositeSimplePipe<I,O,X>(pipeProvider.createPipeInstance(),this.pipe));
    }

    public AbstractPipe<I,O> build() {
        return pipe;
    }
}
