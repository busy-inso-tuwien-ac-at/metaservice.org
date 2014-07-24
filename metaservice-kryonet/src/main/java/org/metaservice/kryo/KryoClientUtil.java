package org.metaservice.kryo;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executors;

/**
 * Created by ilo on 07.06.2014.
 */
public class KryoClientUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(KryoClientUtil.class);
    public Client startClient(Listener listener) throws IOException {
        int size1 = 31457280;
        int size2 = 180000;
        LOGGER.info("Starting Client with sizes {}/{}",size1,size2);
        Client client =  new Client(size1,size2 );
        new KryoInitializer().initialize(client.getKryo());
        ThreadFactoryBuilder builder = new ThreadFactoryBuilder();
        builder.setNameFormat("threaded_listener_thread_%d");
        client.addListener(new Listener.ThreadedListener(listener, Executors.newSingleThreadExecutor(builder.build())));
        client.start();
        int timeout = 5000;
        int retries = 0;
        while (!client.isConnected()){
            try {
                client.connect(timeout, "127.0.0.1", 54555/*, 54777*/);
                retries++;
            }catch (IOException e){
                if(retries> 10){
                    LOGGER.error("stopping to retry ",e);
                    throw e;
                }else{
                    try {
                        LOGGER.info("connection failed, retrying in 3 seconds");
                        Thread.sleep(3000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }

            }
        }
        return client;
    }
}
