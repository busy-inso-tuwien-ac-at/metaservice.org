package cc.ilo.cc.ilo.pipeline.consumer;

/**
 * Created by ilo on 22.07.2014.
 */
public interface PullConsumer<I> {
    public void stop();
    public void stopImmediately();
}
