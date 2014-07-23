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
