package cc.ilo.cc.ilo.pipeline.prosumer;

import cc.ilo.cc.ilo.pipeline.consumer.PullConsumer;
import cc.ilo.cc.ilo.pipeline.pipes.AbstractPipe;
import cc.ilo.cc.ilo.pipeline.pipes.PipeProvider;
import cc.ilo.cc.ilo.pipeline.producer.Producer;
import com.google.common.base.Optional;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ilo on 22.07.2014.
 */
public class PipeExecutor<I,O,E extends Throwable> implements Producer<O>,PullConsumer<I> {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PipeExecutor.class);
    private final PipeProvider provider;
    private final int queueSize;
    private final Producer producer;
    private final LinkedBlockingQueue<Optional<O>> outQueue;

    private boolean stopped = false;
    private boolean stopping;

    public PipeExecutor(final Producer<I> producer, final PipeProvider<? extends AbstractPipe<I,O>> provider,int threadcount,int queueSize) {
        this.provider = provider;
        this.queueSize = queueSize;
        this.producer = producer;
        outQueue = new LinkedBlockingQueue<>(queueSize);
        pullThreads = new ArrayList<>();
        for(int i =0; i< threadcount;i++) {
            pullThreads.add(new Thread(provider.createPipeInstance().getClass().getName() +"-" + i) {
                @Override
                public void run() {
                    AbstractPipe<I,O> pipe = provider.createPipeInstance();
                    while (!isInterrupted() && !stopped) {
                        try {
                            Optional<I> input = producer.getNext();
                            if (input.isPresent()) {
                                Optional<O> output = pipe.process(input.get());
                                if (!outQueue.offer(output)) {
                                    LOGGER.info("producing too fast -> blocking push");
                                    outQueue.put(output);
                                }
                            } else {
                                stopped = true;
                                outQueue.put(Optional.<O>absent());
                            }
                        } catch (InterruptedException e) {
                            interrupt();
                        } catch (Throwable e){
                            //todo handle exceptions :-)
                        }
                    }
                }
            });
        }
        start();
    }
    final ArrayList<Thread> pullThreads;
    public void start(){
        for(Thread thread : pullThreads)
            thread.start();
    }

    public void stop(){
        producer.stop();
    }

    public void stopImmediately(){
        producer.stopImmediately();
        synchronized (pullThreads) {
            for (Thread thread : pullThreads) {
                thread.interrupt();
            }
        }
    }

    @Override
    public synchronized Optional<O> getNext() throws InterruptedException {
        Optional<O> out;
        if((out = outQueue.peek())==null){
            if (stopped){
                return Optional.absent();
            }
            LOGGER.debug("producing to slow - blocking push");
            out = outQueue.take();
            return out;
        }
        return outQueue.poll();
    }
}
