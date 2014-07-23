package cc.ilo.cc.ilo.pipeline.builder;

import cc.ilo.cc.ilo.pipeline.consumer.Puller;
import cc.ilo.cc.ilo.pipeline.pipes.AbstractPipe;
import cc.ilo.cc.ilo.pipeline.pipes.PipeProvider;
import cc.ilo.cc.ilo.pipeline.producer.Producer;
import cc.ilo.cc.ilo.pipeline.prosumer.PipeExecutor;

/**
 * Created by ilo on 22.07.2014.
 */
public class PullingPipelineBuilder<T>  {
    private final Producer<T> producer;

    private PullingPipelineBuilder(Producer<T> producer) {
        this.producer = producer;
    }

    public static <I> PullingPipelineBuilder<I> create(Producer<I> producer){
        return new PullingPipelineBuilder<>(producer);
    }

    public <O> PullingPipelineBuilder<O> pipe(final AbstractPipe<T, O> pipe){
        return pipe(new PipeProvider<AbstractPipe<T,O>>() {
            @Override
            public AbstractPipe<T,O> createPipeInstance() {
                return pipe;
            }
        },1,1);
    }
    public <O,P  extends AbstractPipe<T,O>> PullingPipelineBuilder<O> pipe(PipeProvider<P> provider,int threadcount, int buffersize){
        return new PullingPipelineBuilder<>(new PipeExecutor<>(producer,provider,threadcount,buffersize));
    }

    public Puller build(){
        return new Puller(producer);
    }


}
