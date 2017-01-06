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

package org.metaservice.core.postprocessor;

import cc.ilo.cc.ilo.pipeline.builder.CompositePipeBuilder;
import cc.ilo.cc.ilo.pipeline.builder.PullingPipelineBuilder;
import cc.ilo.cc.ilo.pipeline.consumer.Puller;
import cc.ilo.cc.ilo.pipeline.pipes.AbstractPipe;
import cc.ilo.cc.ilo.pipeline.pipes.PipeProvider;
import cc.ilo.cc.ilo.pipeline.producer.InjectionProducer;
import com.google.common.base.Optional;
import org.metaservice.api.MetaserviceException;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.*;
import org.metaservice.api.messaging.dispatcher.Callback;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.core.AbstractDispatcher;
import org.metaservice.core.dispatcher.NotifyPipe;
import org.metaservice.core.dispatcher.SendDataPipe;
import org.metaservice.core.dispatcher.postprocessor.*;
import org.metaservice.core.injection.providers.GuicePipeProviderFactory;
import org.openrdf.model.*;
import org.openrdf.query.*;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;

/**
 * Created by ilo on 17.01.14.
 */
public class PostProcessorDispatcher extends AbstractDispatcher implements org.metaservice.api.messaging.dispatcher.PostProcessorDispatcher {
    private final Logger LOGGER = LoggerFactory.getLogger(PostProcessorDispatcher.class);
    private final GuicePipeProviderFactory providerFactory;
    private AbstractPipe<Context,Context> pipeline;

    @Inject
    private PostProcessorDispatcher(
            Config config,
            MessageHandler messageHandler,
            MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor,
            GuicePipeProviderFactory providerFactory
    ) throws MalformedQueryException, RepositoryException, MetaserviceException {
        super(config,messageHandler);
        this.providerFactory = providerFactory;
        pipeline = getStaticPipeline();
    }

    public static class Context{
        public Callback callback;
        public PostProcessingTask task;
        public long messagingTimestamp;
        public Set<URI> objects;
        public Set<URI> subjects;
        public Set<URI> processableSubjects;
        public URI metadata;
        public Repository resultRepository;
        public RepositoryConnection resultConnection;
        public List<Statement> generatedStatements;
        public Set<URI> existingGraphs;
    }

    private <I,O> PipeProvider<I,O> p(Class<? extends AbstractPipe<I,O>> pipeClass){
        return providerFactory.createProvider(pipeClass);
    }
    public InjectionProducer<Context> getDynamicPipeline(){
        InjectionProducer<Context> producer =new InjectionProducer<Context>();
        final Puller puller =PullingPipelineBuilder
                .create(producer,new PullingPipelineBuilder.ExceptionCallback() {
                    @Override
                    public void handlExceptioin(Exception e, Optional input) {
                        if(input.isPresent() && input.get() instanceof Context){
                           ((Context) input.get()).callback.handleException(e);
                        }else{
                            LOGGER.error("Could not handle Exception in Callback -> no Context",e);
                        }
                    }
                },new PullingPipelineBuilder.PipeExitCallback() {
                    @Override
                    public void handleExit(Optional input) {
                        if(input.isPresent() && input.get() instanceof Context){
                            ((Context) input.get()).callback.handleOk();
                        }else{
                            LOGGER.error("Could not handle Pipe Exit -> no Context");
                        }
                    }
                })
                .pipe(p(AlreadyProcessedFilter.class))
                .pipe(p(AbortEarlyFilter.class))
                .pipe(p(RequestToOldFilter.class),4,1)

                        //setup
                .pipe(p(CreateTempRepositoryPipe.class))

                        // run the processor
                .pipe(p(RunPostprocessorPipe.class),10,1)

                        //check the statements
                .pipe(p(DetermineGeneratedStatementsPipe.class))

                        //check existing graphs
                .pipe(p(CalculateExistingGraphsForSubjectsPipe.class))

                        //only update last checked date, if no changes
                .pipe(p(NoChangeFilter.class),10,1)

                        //drop existing
                .pipe(p(DropExistingGraphsPipe.class),5,1)

                        //create metadata in repository
                .pipe(p(MetadataPipe.class),3,1)

                        //send to repository
                .pipe(p(SendDataPipe.class),5,1)

                        //clear resources
                .pipe(p(CloseTempRepositoryPipe.class),1,5)

                        // Notfiy postprocessors
                .pipe(p(NotifyPipe.class),5,1)
                .build();
        new Thread(){
            @Override
            public void run() {
               puller.run();;
            }
        }.start();
        return producer;
    }

    public  AbstractPipe<Context,Context> getStaticPipeline() throws PostProcessorException {
        try {
            return CompositePipeBuilder
                    //Filter
                    .create(p(DebugFilter.class))
                    .pipe(p(AlreadyProcessedFilter.class))
                    .pipe(p(AbortEarlyFilter.class))
                    .pipe(p(RequestToOldFilter.class))

                            //setup
                    .pipe(p(CreateTempRepositoryPipe.class))

                            // run the processor
                    .pipe(p(RunPostprocessorPipe.class))

                            //check the statements
                    .pipe(p(DetermineGeneratedStatementsPipe.class))

                            //check existing graphs
                    .pipe(p(CalculateExistingGraphsForSubjectsPipe.class))

                            //only update last checked date, if no changes
                    .pipe(p(NoChangeFilter.class))

                            //drop existing
                    .pipe(p(DropExistingGraphsPipe.class))

                            //create metadata in repository
                    .pipe(p(MetadataPipe.class))

                            //send to repository
                    .pipe(p(SendDataPipe.class))

                            //clear resources
                    .pipe(p(CloseTempRepositoryPipe.class))

                            // Notfiy postprocessors
                    .pipe(p(NotifyPipe.class))
                    .build()
                    ;
        } catch (Exception e) {
            throw new PostProcessorException("Could not create Pipeline",e);
        }
    }

    @Override
    public void process(PostProcessingTask task, long messagingTimestamp) throws PostProcessorException{
        Context context = new Context();
        context.task =task;
        context.messagingTimestamp =messagingTimestamp;
        try{
            if(pipeline == null){
                pipeline= getStaticPipeline();
            }
            pipeline.process(context);

        } catch (RepositoryException | MetaserviceException e) {
            LOGGER.error("Couldn't create {}",context.task,e);
            throw new PostProcessorException(e);
        } catch (Exception e) {
            throw new PostProcessorException(e);
        }
    }

    InjectionProducer<Context> injectionProducer = null;

    @Override
    public void processAsync(PostProcessingTask task, long messagingTimestamp, Callback callback) {
        if(injectionProducer == null){
            injectionProducer=getDynamicPipeline();
        }
        Context context = new Context();
        context.task =task;
        context.messagingTimestamp = messagingTimestamp;
        context.callback = callback;
        injectionProducer.inject(context);
    }

}
