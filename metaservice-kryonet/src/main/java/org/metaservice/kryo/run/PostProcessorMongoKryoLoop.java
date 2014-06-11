package org.metaservice.kryo.run;

import com.esotericsoftware.kryonet.Client;
import org.metaservice.api.messaging.PostProcessorMessagingLoop;
import org.metaservice.kryo.KryoClientUtil;
import org.metaservice.kryo.PostProcessorListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by ilo on 07.06.2014.
 */
public class PostProcessorMongoKryoLoop implements PostProcessorMessagingLoop {
    public static final Logger LOGGER = LoggerFactory.getLogger(PostProcessorMongoKryoLoop.class);

    private final PostProcessorListener postProcessorListener;

    @Inject
    public PostProcessorMongoKryoLoop(PostProcessorListener postProcessorListener) {
        this.postProcessorListener = postProcessorListener;
    }

    public void run() {
        try {
            Client client =new KryoClientUtil().startClient(postProcessorListener);
            client.getUpdateThread().join();
        } catch (IOException | InterruptedException e) {
            LOGGER.error("Error during running ", e);
        }
    }
}
