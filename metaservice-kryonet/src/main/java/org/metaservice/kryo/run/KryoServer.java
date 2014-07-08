package org.metaservice.kryo.run;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.esotericsoftware.minlog.Log;
import org.metaservice.kryo.ClientHandler;
import org.metaservice.kryo.KryoInitializer;
import org.metaservice.kryo.QueueContainer;
import org.metaservice.kryo.mongo.MongoConnectionWrapper;
import org.metaservice.kryo.MongoWriterListener;
import org.mongojack.DBQuery;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ilo on 07.06.2014.
 */
public class KryoServer {
    public static final int QUEUE_CONTAINER_ID = 1;

    ExecutorService executor = Executors.newFixedThreadPool(3);
    public static void main(String[] args) throws IOException {
        final KryoServer kryoServer = new KryoServer();
        kryoServer.run();
        Thread shutDownHook = new Thread(new Runnable() {
            @Override
            public void run() {
                kryoServer.close();
            }
        });
        Runtime.getRuntime().addShutdownHook(shutDownHook);
    }

    public void close() {
        queueContainer.saveQueues();
        queueContainer.cleanQueues();
        server.close();
        executor.shutdown();
    }

    private Server server;
    private QueueContainer queueContainer;
    public void run() throws IOException {
      //  Log.set(Log.LEVEL_TRACE);
        MongoConnectionWrapper mongo = new MongoConnectionWrapper();
     //   mongo.getPostProcessorMessageCollection().remove(DBQuery.empty());
        queueContainer = new QueueContainer(mongo);
        server = new Server(16384*20, 90000);
        new KryoInitializer().initialize(server.getKryo());
        server.addListener(new Listener.ThreadedListener(new ClientHandler(queueContainer)));
        server.addListener(new Listener.ThreadedListener(new MongoWriterListener(mongo), Executors.newFixedThreadPool(10)));

        server.start();
        server.bind(54555, 54777);
        final ObjectSpace objectSpace = new ObjectSpace();
        objectSpace.register(QUEUE_CONTAINER_ID, queueContainer);
        objectSpace.setExecutor(executor);
        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                objectSpace.addConnection(connection);
            }
        });

    }
}
