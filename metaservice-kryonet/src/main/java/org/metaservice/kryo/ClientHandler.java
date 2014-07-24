package org.metaservice.kryo;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.metaservice.kryo.beans.PostProcessorMessage;
import org.metaservice.kryo.beans.ProviderCreateMessage;
import org.metaservice.kryo.beans.ProviderRefreshMessage;
import org.metaservice.kryo.beans.RegisterClientMessage;
import org.metaservice.kryo.mongo.MongoConnectionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.concurrent.Executors;


/**
 * Created by ilo on 07.06.2014.
 */
public class ClientHandler extends Listener {
    private final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);
    private final QueueContainer queueContainer;
    public ClientHandler(QueueContainer queueContainer) {
        this.queueContainer = queueContainer;
    }
    final HashMap<Integer,Listener> listenerMap = new HashMap<>();
    @Override
    public void received(final Connection connection, Object o) {
        if(o instanceof RegisterClientMessage){
            RegisterClientMessage registerMessage = (RegisterClientMessage) o;
            LOGGER.info("registering... " + registerMessage);
            IndividualClientHandler individualClientHandler;
            switch (registerMessage.getType()){
                case POSTPROCESS:
                    individualClientHandler = new IndividualClientHandler<>(queueContainer.addPostProcessorQueue(registerMessage.getName()),registerMessage.getMessageCount());
                    break;
                case PROVIDER_CREATE:
                    individualClientHandler = new IndividualClientHandler<>(queueContainer.addProviderCreateQueue(registerMessage.getName()),registerMessage.getMessageCount());
                    break;
                case PROVIDER_REFRESH:
                    individualClientHandler = new IndividualClientHandler<>(queueContainer.addProviderRefreshQueue(registerMessage.getName()),registerMessage.getMessageCount());
                    break;
                default:
                    LOGGER.warn("UNKNOWN TYPE " + registerMessage.getType());
                    return;
            }
            LOGGER.debug("on Connection {}",connection.getID());
            ThreadFactoryBuilder builder = new ThreadFactoryBuilder().setNameFormat("indivClient-" + registerMessage.getName()+"-%d");
            listenerMap.put(connection.getID(), new Listener.ThreadedListener(individualClientHandler,Executors.newFixedThreadPool(1,builder.build())));
            individualClientHandler.init(connection);
        }else{
            if(listenerMap.containsKey(connection.getID())) {
                listenerMap.get(connection.getID()).received(connection, o);
            }/*else{
            //gets all write messages and keep alive messages
                LOGGER.warn("no listener to inform for message {}", o.getClass().getName());
            }*/
        }
    }
    @Override
    public void connected(Connection connection) {
        LOGGER.info("ClientHandler connect");
    }

    @Override
    public void disconnected(Connection connection) {
        if(listenerMap.containsKey(connection.getID())) {
            listenerMap.get(connection.getID()).disconnected(connection);
            listenerMap.remove(connection.getID());
        }
    }
}
