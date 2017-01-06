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

package cc.ilo.cc.ilo.pipeline.producer;

import com.google.common.base.Optional;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by ilo on 22.07.2014.
 */
public class IterableProducer<T> implements Producer<T> {
    private final Iterator<T> iterator;
    private boolean stopped = false;
    public IterableProducer(Iterable<T> iterable) {
        iterator = iterable.iterator();
    }

    public IterableProducer(T[] array) {
        iterator = Arrays.asList(array).iterator();
    }

    @Override
    public synchronized Optional<T> getNext() throws InterruptedException {
        if(iterator.hasNext()&&!stopped) {
            return Optional.of(iterator.next());
        }else {
            return Optional.absent();
        }
    }

    @Override
    public void stop() {
        stopped = true;
    }

    @Override
    public void stopImmediately() {
        stop();
    }
}
