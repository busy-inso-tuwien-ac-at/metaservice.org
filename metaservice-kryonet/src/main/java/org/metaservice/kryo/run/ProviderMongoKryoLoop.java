package org.metaservice.kryo.run;

import com.esotericsoftware.kryonet.Client;
import org.metaservice.api.messaging.MessagingException;
import org.metaservice.api.messaging.ProviderMessagingLoop;
import org.metaservice.kryo.KryoClientUtil;
import org.metaservice.kryo.ProviderListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by ilo on 08.06.2014.
 */
public class ProviderMongoKryoLoop implements ProviderMessagingLoop{
    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderMongoKryoLoop.class);


    private final ProviderListener providerListener;

    @Inject
    public ProviderMongoKryoLoop(ProviderListener providerListener) {
        this.providerListener = providerListener;
    }


    @Override
    public void run() throws MessagingException {
        try {
            LOGGER.error("STARTING CLIENT");
            Client client =new KryoClientUtil().startClient(providerListener);
            LOGGER.error("Update thread is alive " + client.getUpdateThread().isAlive());
            client.getUpdateThread().join();
            LOGGER.error("Client Update Thread joined");
        } catch (IOException | InterruptedException e) {
            LOGGER.error("Error during running ", e);
        }
    }
}
