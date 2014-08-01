package cc.ilo.cc.ilo.pipeline.builder;

import cc.ilo.cc.ilo.pipeline.pipes.AbstractPipe;
import cc.ilo.cc.ilo.pipeline.pipes.PipeProvider;
import cc.ilo.cc.ilo.pipeline.prosumer.CompositeSimplePipe;


/**
 * Created by ilo on 23.07.2014.
 */
public class CompositePipeBuilder<I,O>  {

    public final AbstractPipe<I,O> pipe;

    private CompositePipeBuilder(AbstractPipe<I,O> pipe){
        this.pipe = pipe;
    }
    public static <I,O> CompositePipeBuilder<I,O> create(AbstractPipe<I,O> pipe){
        return new CompositePipeBuilder<>(pipe);
    }

    public static <I,O> CompositePipeBuilder<I,O> create(PipeProvider<I,O> pipeProvider){
        return new CompositePipeBuilder<>(pipeProvider.createPipeInstance());
    }

    public <X> CompositePipeBuilder<I,X> pipe(AbstractPipe<O,X> newPipe){
        return new CompositePipeBuilder<I,X>(new CompositeSimplePipe<I,O,X>(newPipe,this.pipe));
    }

    public <X> CompositePipeBuilder<I,X> pipe(PipeProvider<O,X> pipeProvider){
        return new CompositePipeBuilder<I,X>(new CompositeSimplePipe<I,O,X>(pipeProvider.createPipeInstance(),this.pipe));
    }

    public AbstractPipe<I,O> build() {
        return pipe;
    }
}