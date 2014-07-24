package cc.ilo.cc.ilo.pipeline.consumer;

import cc.ilo.cc.ilo.pipeline.builder.PullingPipelineBuilder;
import cc.ilo.cc.ilo.pipeline.producer.Producer;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ilo on 22.07.2014.
 */
public class Puller implements PullConsumer {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Puller.class);
    private final Producer producer;
    private final PullingPipelineBuilder.PipeExitCallback pipeExitCallback;

    public Puller(Producer producer, PullingPipelineBuilder.PipeExitCallback pipeExitCallback) {
        this.producer = producer;
        this.pipeExitCallback = pipeExitCallback;
    }

    public void run(){
        try {
            LOGGER.info("Starting Puller");
            boolean stopped = false;
            while(!stopped){
                Optional optional =  producer.getNext();
                if(optional.isPresent()) {
                    LOGGER.info("pulling: " + optional.get());
                }else{
                    LOGGER.warn("Stopping...");
                    stopped = true;
                }
                pipeExitCallback.handleExit(optional);
            }
        } catch (InterruptedException e )
        {
           LOGGER.info("interrupted");
        }
    }

    public void stop(){
        producer.stop();
    }

    @Override
    public void stopImmediately() {
        producer.stopImmediately();
    }
}
