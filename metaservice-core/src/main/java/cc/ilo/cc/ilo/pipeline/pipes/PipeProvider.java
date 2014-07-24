package cc.ilo.cc.ilo.pipeline.pipes;

/**
 * Created by ilo on 22.07.2014.
 */
public interface PipeProvider<I,O> {
    public AbstractPipe<I,O> createPipeInstance();
}
