/*
 * Copyright 2015 Nikola Ilo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaservice.kryo.run;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.metaservice.kryo.ClientHandler;
import org.metaservice.kryo.KryoInitializer;
import org.metaservice.kryo.QueueContainer;
import org.metaservice.kryo.mongo.MongoConnectionWrapper;
import org.metaservice.kryo.MongoWriterListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ilo on 07.06.2014.
 */
public class KryoServer {
    private final Logger LOGGER = LoggerFactory.getLogger(KryoServer.class);
    public static final int QUEUE_CONTAINER_ID = 1;

    final ExecutorService objectSpaceExecutor;
    final ExecutorService mongoWriterExecutor;
    public KryoServer() {
        ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
        threadFactoryBuilder.setNameFormat("objectSpace-%d");
        objectSpaceExecutor =  Executors.newFixedThreadPool(3,threadFactoryBuilder.build());
        threadFactoryBuilder.setNameFormat("mongoWriter-%d");
        mongoWriterExecutor=Executors.newFixedThreadPool(10,threadFactoryBuilder.build());
    }

    public static void main(String[] args) throws IOException {
        final KryoServer kryoServer = new KryoServer();
        Thread shutDownHook = new Thread(new Runnable() {
            @Override
            public void run() {
                kryoServer.close();
            }
        });
        Runtime.getRuntime().addShutdownHook(shutDownHook);
        kryoServer.run();
    }

    public void close() {
        LOGGER.info("Closing Server");
        LOGGER.info(" - saving Queues");
        queueContainer.saveQueues();
        LOGGER.info(" - cleaning Queues");
        queueContainer.cleanQueues();
        LOGGER.info(" - shuting down kryo");
        server.close();
        LOGGER.info(" - shuting down objectSpaceExecutor");
        objectSpaceExecutor.shutdown();
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
        server.addListener(new Listener.ThreadedListener(new MongoWriterListener(mongo), mongoWriterExecutor));

        server.start();
        server.bind(54555/*, 54777*/);
        final ObjectSpace objectSpace = new ObjectSpace();
        objectSpace.register(QUEUE_CONTAINER_ID, queueContainer);
        objectSpace.setExecutor(objectSpaceExecutor);
        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                objectSpace.addConnection(connection);
            }
        });

    }
}
