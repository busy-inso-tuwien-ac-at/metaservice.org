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

import cc.ilo.cc.ilo.pipeline.consumer.Puller;
import cc.ilo.cc.ilo.pipeline.pipes.AbstractPipe;
import cc.ilo.cc.ilo.pipeline.pipes.PipeProvider;
import cc.ilo.cc.ilo.pipeline.producer.Producer;
import cc.ilo.cc.ilo.pipeline.prosumer.PipeExecutor;
import com.google.common.base.Optional;

/**
 * Created by ilo on 22.07.2014.
 */
public class PullingPipelineBuilder<T>  {
    private final Producer<T> producer;
    private final ExceptionCallback exceptionCallback;
    private final PipeExitCallback pipeExitCallback;

    private PullingPipelineBuilder(Producer<T> producer, ExceptionCallback exceptionCallback, PipeExitCallback pipeExitCallback) {
        this.producer = producer;
        this.exceptionCallback = exceptionCallback;
        this.pipeExitCallback = pipeExitCallback;
    }

    public static <I> PullingPipelineBuilder<I> create(Producer<I> producer,ExceptionCallback exceptionCallback,PipeExitCallback pipeExitCallback){
        return new PullingPipelineBuilder<>(producer, exceptionCallback, pipeExitCallback);
    }

    public <O> PullingPipelineBuilder<O> pipe(final AbstractPipe<T, O> pipe){
        return pipe(new PipeProvider<T,O>() {
            @Override
            public AbstractPipe<T,O> createPipeInstance() {
                return pipe;
            }
        },1,1);
    }
    public <O> PullingPipelineBuilder<O> pipe(final PipeProvider<T, O> provider){
        return pipe(provider,1,1);
    }

    public <O,P  extends AbstractPipe<T,O>> PullingPipelineBuilder<O> pipe(PipeProvider<T,O> provider,int threadcount, int buffersize){
        return new PullingPipelineBuilder<>(new PipeExecutor<>(producer,provider,threadcount,buffersize,exceptionCallback,pipeExitCallback), exceptionCallback, pipeExitCallback);
    }

    public Puller build(){
        return new Puller(producer,pipeExitCallback);
    }


    public static interface ExceptionCallback{
        public void handlExceptioin(Exception e,Optional input);
    }

    public static interface PipeExitCallback{
        public void handleExit(Optional input);
    }
}
