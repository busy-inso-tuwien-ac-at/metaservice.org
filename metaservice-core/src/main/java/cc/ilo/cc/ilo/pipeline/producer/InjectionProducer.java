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

package cc.ilo.cc.ilo.pipeline.producer;

import com.google.common.base.Optional;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ilo on 22.07.2014.
 */
public class InjectionProducer<T> implements Producer<T> {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(InjectionProducer.class);
    private final LinkedBlockingQueue<Optional<T>> outQueue = new LinkedBlockingQueue<>();

    private boolean stopped =false;

    public void inject(T t){
        outQueue.add(Optional.of(t));
    }

    @Override
    public Optional<T> getNext() throws InterruptedException {
        Optional<T> out;
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

    @Override
    public void stop() {
        stopped = true;
    }

    @Override
    public void stopImmediately() {
        outQueue.clear();
        stop();
    }
}
