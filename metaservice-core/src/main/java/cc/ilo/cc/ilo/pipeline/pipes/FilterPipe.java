package cc.ilo.cc.ilo.pipeline.pipes;

/**
 * Created by ilo on 22.07.2014.
 */
public interface FilterPipe<T>extends AbstractPipe<T,T>{
    boolean accept(T t) throws Exception;
}
