package cc.ilo.cc.ilo.pipeline.producer;

import com.google.common.base.Optional;

/**
 * Created by ilo on 22.07.2014.
 */
public interface Producer<T> {
    public Optional<T> getNext() throws InterruptedException;

    void stop();
    public void stopImmediately();}
