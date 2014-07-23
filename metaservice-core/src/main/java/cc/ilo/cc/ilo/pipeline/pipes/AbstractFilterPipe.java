package cc.ilo.cc.ilo.pipeline.pipes;

import com.google.common.base.Optional;

/**
 * Created by ilo on 23.07.2014.
 */
public abstract class AbstractFilterPipe<T> implements FilterPipe<T> {

    @Override
    public Optional<T> process(T input) throws Exception {
        if(accept(input)) {
            return Optional.of(input);
        }
        else{
            return Optional.absent();
        }

    }
}
