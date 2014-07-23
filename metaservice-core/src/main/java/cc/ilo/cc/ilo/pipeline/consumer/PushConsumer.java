package cc.ilo.cc.ilo.pipeline.consumer;

/**
 * Created by ilo on 23.07.2014.
 */
public interface PushConsumer<I,O> {

    public com.google.common.base.Optional<O> push(I i) throws Exception;
}
