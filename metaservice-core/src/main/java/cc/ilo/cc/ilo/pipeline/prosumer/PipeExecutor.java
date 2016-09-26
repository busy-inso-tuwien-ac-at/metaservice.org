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

import cc.ilo.cc.ilo.pipeline.builder.PullingPipelineBuilder;
import cc.ilo.cc.ilo.pipeline.consumer.PullConsumer;
import cc.ilo.cc.ilo.pipeline.pipes.AbstractPipe;
import cc.ilo.cc.ilo.pipeline.pipes.PipeProvider;
import cc.ilo.cc.ilo.pipeline.producer.Producer;
import com.google.common.base.Optional;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ilo on 22.07.2014.
 */
public class PipeExecutor<I,O,E extends Throwable> implements Producer<O>,PullConsumer<I> {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PipeExecutor.class);
    private final PipeProvider provider;
    private final int queueSize;
    private final Producer producer;
    private final LinkedBlockingQueue<Optional<O>> outQueue;
    private final PullingPipelineBuilder.ExceptionCallback exceptionCallback;
    private final PullingPipelineBuilder.PipeExitCallback pipeExitCallback;

    private boolean stopped = false;

    public PipeExecutor(final Producer<I> producer, final PipeProvider<I, O> provider, int threadcount, int queueSize, final PullingPipelineBuilder.ExceptionCallback exceptionCallback, final PullingPipelineBuilder.PipeExitCallback pipeExitCallback) {
        this.provider = provider;
        this.queueSize = queueSize;
        this.producer = producer;
        this.exceptionCallback = exceptionCallback;
        this.pipeExitCallback = pipeExitCallback;
        outQueue = new LinkedBlockingQueue<>(queueSize);
        pullThreads = new ArrayList<>();
        for(int i =0; i< threadcount;i++) {
            pullThreads.add(new Thread(provider.createPipeInstance().getClass().getName() +"-" + i) {
                @Override
                public void run() {
                    AbstractPipe<I,O> pipe = provider.createPipeInstance();
                    while (!isInterrupted() && !stopped) {
                        try {
                            Optional<I> input = producer.getNext();
                            try{
                                if (input.isPresent()) {
                                    Optional<O> output = pipe.process(input.get());
                                    if(output.isPresent()){
                                        if (!outQueue.offer(output)) {
                                            LOGGER.trace("producing too fast - blocking");
                                            outQueue.put(output);
                                        }
                                    }else{
                                        if(pipeExitCallback !=null){
                                            pipeExitCallback.handleExit(input);
                                        }
                                    }
                                } else {
                                    stopped = true;
                                    outQueue.put(Optional.<O>absent());
                                }
                            }catch (Exception e){
                                if(exceptionCallback != null){
                                    exceptionCallback.handlExceptioin(e,input);
                                }else{
                                    LOGGER.error("Exception in Pipeline execution",e);
                                }
                            }
                        } catch (InterruptedException e) {
                            interrupt();
                        }
                    }
                }
            });
        }
        start();
    }
    final ArrayList<Thread> pullThreads;
    public void start(){
        for(Thread thread : pullThreads)
            thread.start();
    }

    public void stop(){
        producer.stop();
    }

    public void stopImmediately(){
        producer.stopImmediately();
        synchronized (pullThreads) {
            for (Thread thread : pullThreads) {
                thread.interrupt();
            }
        }
    }

    @Override
    public synchronized Optional<O> getNext() throws InterruptedException {
        Optional<O> out;
        if((out = outQueue.peek())==null){
            if (stopped){
                return Optional.absent();
            }
            LOGGER.trace("input too slow - blocking");
            out = outQueue.take();
            return out;
        }
        return outQueue.poll();
    }
}
