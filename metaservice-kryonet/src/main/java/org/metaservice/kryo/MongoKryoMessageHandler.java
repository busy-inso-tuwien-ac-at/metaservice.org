package org.metaservice.kryo;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.messaging.MessageHandler;
import org.metaservice.api.messaging.MessagingException;
import org.metaservice.api.messaging.PostProcessingHistoryItem;
import org.metaservice.api.messaging.statistics.QueueStatistics;
import org.metaservice.api.messaging.PostProcessingTask;
import org.metaservice.kryo.beans.PostProcessorMessage;
import org.metaservice.kryo.beans.ProviderCreateMessage;
import org.metaservice.kryo.beans.ProviderRefreshMessage;
import org.metaservice.kryo.mongo.MongoConnectionWrapper;
import org.metaservice.kryo.run.KryoServer;
import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.rmi.server.ServerRef;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ilo on 08.06.2014.
 */
public class MongoKryoMessageHandler implements MessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoKryoMessageHandler.class);
    private Client client;
    private int i;

    @Override
    public void send(String s) throws MessagingException {
        ProviderRefreshMessage providerRefreshMessage = new ProviderRefreshMessage();
        providerRefreshMessage.setTimestamp(System.currentTimeMillis());
        providerRefreshMessage.setUri(s);
        client.sendTCP(providerRefreshMessage);
        LOGGER.info("SENT");
        if(i++>100){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public void bulkSend(Iterable<PostProcessingTask> postProcessingTasks){
        MongoConnectionWrapper mongoConnectionWrapper= new MongoConnectionWrapper();
        ArrayList<PostProcessorMessage> postProcessorMessages = new ArrayList<>();
        for(PostProcessingTask task : postProcessingTasks){
            PostProcessorMessage postProcessorMessage = new PostProcessorMessage();
            postProcessorMessage.setTimestamp(System.currentTimeMillis());
            postProcessorMessage.setTask(task);
            postProcessorMessages.add(postProcessorMessage);
        }
        mongoConnectionWrapper.getPostProcessorMessageCollection().insert(postProcessorMessages);
    }

    @Override
    public void send(ArchiveAddress archiveAddress) throws MessagingException {
        ProviderCreateMessage providerCreateMessage = new ProviderCreateMessage();
        providerCreateMessage.setTimestamp(System.currentTimeMillis());
        providerCreateMessage.setArchiveAddress(archiveAddress);
        client.sendTCP(providerCreateMessage);
        LOGGER.info("SENT");
        if(i++>100){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public void send(PostProcessingTask postProcessingTask) throws MessagingException {
        PostProcessingTask cloned = new PostProcessingTask(new URIImpl(postProcessingTask.getChangedURI().stringValue()),postProcessingTask.getTime());
        for(PostProcessingHistoryItem item : postProcessingTask.getHistory()){
            URI[] clonedUris = new URI[item.getResources().length];
            for(int i =0 ; i < clonedUris.length;i++){
                clonedUris[i] = new URIImpl(item.getResources()[i].stringValue());
            }
            PostProcessingHistoryItem clonedItem = new PostProcessingHistoryItem(item.getPostprocessorId(),clonedUris);
            cloned.getHistory().add(clonedItem);
        }

        PostProcessorMessage postProcessorMessage = new PostProcessorMessage();
        postProcessorMessage.setTimestamp(System.currentTimeMillis());
        postProcessorMessage.setTask(cloned);
        client.sendTCP(postProcessorMessage);
        LOGGER.info("SENT");
        if(i++>100){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public void commit() throws MessagingException {
        //not implemented
    }

    @Override
    public List<QueueStatistics> getStatistics() throws MessagingException {
        StatisticsProvider queueContainer = ObjectSpace.getRemoteObject(client, KryoServer.QUEUE_CONTAINER_ID, StatisticsProvider.class);
        return queueContainer.getQueueStatistics();
    }

    @Override
    public void close() throws MessagingException {
        client.close();
    }

    @Override
    public void init() throws MessagingException {
        if(client == null) {
            client = new Client(31457280, 9000);
            new KryoInitializer().initialize(client.getKryo());
            client.start();
        client.addListener(new Listener(){
            @Override
            public void connected(Connection connection) {
                LOGGER.info("connected");
            }

            @Override
            public void disconnected(Connection connection) {
                LOGGER.info("disconnected");
            }

            @Override
            public void received(Connection connection, Object o) {
                LOGGER.trace("received");
            }

            @Override
            public void idle(Connection connection) {
                LOGGER.trace("idle");
            }
        });
            int timeout = 5000;
            int retries = 0;
            while (!client.isConnected()){
                try {
                    client.connect(timeout, "127.0.0.1", 54555, 54777);
                    retries++;
                }catch (IOException e){
                    if(retries> 10){
                        LOGGER.error("stoping to retry ",e);
                        throw new MessagingException(e);
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
        }
    }
}
