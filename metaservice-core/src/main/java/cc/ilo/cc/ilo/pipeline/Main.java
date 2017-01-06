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

package cc.ilo.cc.ilo.pipeline;

import cc.ilo.cc.ilo.pipeline.builder.CompositePipeBuilder;
import cc.ilo.cc.ilo.pipeline.pipes.AbstractPipe;
import cc.ilo.cc.ilo.pipeline.pipes.SimplePipe;
import cc.ilo.cc.ilo.pipeline.pipes.PipeProvider;
import cc.ilo.cc.ilo.pipeline.producer.IterableProducer;
import cc.ilo.cc.ilo.pipeline.producer.Producer;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;


/**
 * Created by ilo on 22.07.2014.
 */
public class Main {
    public static void main(String[] args) {
        PipeProvider<String, String> provider = new PipeProvider<String, String>() {
            @Override
            public SimplePipe<String, String> createPipeInstance() {
                return new SimplePipe<String, String>() {
                    @Override
                    public Optional<String> process(String input) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return Optional.of(input+".");
                    }
                };
            }
        };


        Producer<String> producer = new IterableProducer<>(Iterables.cycle("I hate Gulash foreveeer".split(" ")));
       /* PullingPipelineBuilder
                .create(producer)
                .pipe(provider,2,2)
                .build()
                .run();*/

        PipeProvider<String,String> duplicate = new PipeProvider<String,String>(){

            @Override
            public SimplePipe<String, String> createPipeInstance() {
                return new SimplePipe<String, String>() {
                    @Override
                    public Optional<String> process(String input) throws Exception {
                        return Optional.of(input+input);
                    }
                };
            }
        };
        CompositePipeBuilder<String,String> builder = CompositePipeBuilder.create(provider.createPipeInstance());
        AbstractPipe<String,String> p = builder.pipe(provider.createPipeInstance()).pipe(duplicate.createPipeInstance()).build();
        try {
            System.err.println(p.process("foolong"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
