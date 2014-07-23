package cc.ilo.cc.ilo.pipeline.consumer;

import cc.ilo.cc.ilo.pipeline.producer.Producer;
import com.google.common.base.Optional;
import org.slf4j.LoggerFactory;

/**
 * Created by ilo on 22.07.2014.
 */
public class Puller implements PullConsumer {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Puller.class);
    private final Producer producer;

    public Puller(Producer producer) {
        this.producer = producer;
    }

    public void run(){
        try {

            boolean stopped = false;
            while(!stopped){
                Optional optional =  producer.getNext();
                if(optional.isPresent()) {
                    LOGGER.info("pulling: " + optional.get());
                }else{
                    stopped = true;
                }
                Thread.sleep(3000);
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
