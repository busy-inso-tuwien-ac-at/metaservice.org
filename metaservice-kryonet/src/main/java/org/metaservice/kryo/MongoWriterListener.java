package org.metaservice.kryo;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.metaservice.kryo.beans.AbstractMessage;
import org.metaservice.kryo.beans.PostProcessorMessage;
import org.metaservice.kryo.beans.ProviderCreateMessage;
import org.metaservice.kryo.beans.ProviderRefreshMessage;
import org.metaservice.kryo.mongo.MongoConnectionWrapper;
import org.slf4j.LoggerFactory;


/**
 * Created by ilo on 07.06.2014.
 */
public class MongoWriterListener extends Listener {
private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MongoWriterListener.class);
    private final MongoConnectionWrapper mongo;

    public MongoWriterListener(MongoConnectionWrapper mongo){
        this.mongo = mongo;
    }

    public void received (Connection connection, Object object) {
        System.err.println(object);
        if(object instanceof AbstractMessage){
            LOGGER.info("storing " + object);
            if (object instanceof PostProcessorMessage) {
                mongo.getPostProcessorMessageCollection().insert((PostProcessorMessage) object);
            }else if(object instanceof ProviderCreateMessage){
                mongo.getProviderCreateMessageCollection().insert((ProviderCreateMessage) object);
            } else if(object instanceof ProviderRefreshMessage){
                mongo.getProviderRefreshMessageCollection().insert((ProviderRefreshMessage) object);
            }
        }
    }

}
