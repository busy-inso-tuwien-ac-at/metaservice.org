package org.metaservice.kryo;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.metaservice.kryo.beans.PostProcessorMessage;
import org.metaservice.kryo.beans.ProviderCreateMessage;
import org.metaservice.kryo.beans.ProviderRefreshMessage;
import org.metaservice.kryo.beans.RegisterClientMessage;
import org.metaservice.kryo.mongo.MongoConnectionWrapper;

import java.util.HashMap;


/**
 * Created by ilo on 07.06.2014.
 */
public class ClientHandler extends Listener {
    private final QueueContainer queueContainer;
    public ClientHandler(QueueContainer queueContainer) {
        this.queueContainer = queueContainer;
    }
    HashMap<Integer,Listener> listenerMap = new HashMap<>();
    @Override
    public void received(final Connection connection, Object o) {
        if(o instanceof RegisterClientMessage){
            RegisterClientMessage registerMessage = (RegisterClientMessage) o;
            System.err.println("registering... " + registerMessage);
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
                    System.err.println("UNKNOWN TYPE " + registerMessage.getType());
                    return;
            }
            System.err.println(connection.getID());
            listenerMap.put(connection.getID(),new Listener.ThreadedListener(individualClientHandler));
            individualClientHandler.init(connection);
        }else{
            if(listenerMap.containsKey(connection.getID())) {
                listenerMap.get(connection.getID()).received(connection, o);
            }
        }
    }
    @Override
    public void connected(Connection connection) {
        System.err.println("ClientHandler connect");
    }

    @Override
    public void disconnected(Connection connection) {
        if(listenerMap.containsKey(connection.getID())) {
            listenerMap.get(connection.getID()).disconnected(connection);
            listenerMap.remove(connection.getID());
        }
    }
}
