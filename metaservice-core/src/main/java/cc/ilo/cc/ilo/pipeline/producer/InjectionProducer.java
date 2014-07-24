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
            LOGGER.debug("input too slow - blocking");
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
