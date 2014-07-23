package cc.ilo.cc.ilo.pipeline.pipes;

/**
 * Created by ilo on 22.07.2014.
 */
public interface PipeProvider<P extends AbstractPipe<?,?>> {
    public P createPipeInstance();
}
