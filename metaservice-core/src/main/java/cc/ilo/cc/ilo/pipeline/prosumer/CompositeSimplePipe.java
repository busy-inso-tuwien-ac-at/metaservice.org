package cc.ilo.cc.ilo.pipeline.prosumer;

import cc.ilo.cc.ilo.pipeline.pipes.AbstractPipe;
import cc.ilo.cc.ilo.pipeline.pipes.SimplePipe;
import com.google.common.base.Optional;

/**
 * Created by ilo on 23.07.2014.
 */
public class CompositeSimplePipe<I,X,O> implements SimplePipe<I,O> {
    final AbstractPipe<X,O> subConsumer;
    final AbstractPipe<I,X> pipe;

    public CompositeSimplePipe(AbstractPipe<X, O> subConsumer, AbstractPipe<I, X> pipe) {
        this.subConsumer = subConsumer;
        this.pipe = pipe;
    }

    @Override
    public Optional<O> process(I input) throws Exception {
        Optional<X> result = pipe.process(input);
        if(result.isPresent())
            return subConsumer.process(result.get());
        else
            return Optional.absent();
    }
}
