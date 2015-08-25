package org.metaservice.kryo.run;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.esotericsoftware.minlog.Log;
import org.junit.Test;
import org.metaservice.api.messaging.PostProcessingTask;
import org.metaservice.kryo.*;
import org.metaservice.kryo.beans.PostProcessorMessage;
import org.metaservice.kryo.mongo.MongoConnectionWrapper;
import org.openrdf.model.impl.ValueFactoryImpl;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executors;

/**
 * Created by ilo on 10.06.2014.
 */
public class KryoFoo {


    @Test
    public void testServer() throws IOException, InterruptedException {
        Log.set(Log.LEVEL_TRACE);
        Server server = new Server(1638400, 90000);
        new KryoInitializer().initialize(server.getKryo());
         server.addListener(new Listener.ThreadedListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }));

        server.start();
        server.bind(54555, 54777);
        server.getUpdateThread().join();
    }
    @Test
    public void KryoTest() throws IOException, InterruptedException {
        //  Log.set(Log.LEVEL_TRACE);
        //   mongo.getPostProcessorMessageCollection().remove(DBQuery.empty());
        Client client = new Client(8192*200000, 2048*20);
        new KryoInitializer().initialize(client.getKryo());
        client.addListener(new Listener.ThreadedListener(new Listener() {
            @Override
            public void connected(Connection connection) {


                for (int i = 0; i < 400000; i++) {

                    connection.sendTCP("This is some long String..............................................");
                    //    if (i % 10 == 0)
                    //       Thread.sleep(1);
                    //     System.err.println("sed");

                }

            }
        }));
        client.start();
        int timeout = 5000;
        client.connect(timeout, "127.0.0.1", 54555, 54777);
        client.getUpdateThread().join();
    }

}
