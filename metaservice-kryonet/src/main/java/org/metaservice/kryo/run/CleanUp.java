package org.metaservice.kryo.run;

import org.metaservice.kryo.mongo.MongoConnectionWrapper;

/**
 * Created by ilo on 09.06.2014.
 */
public class CleanUp {
    public static void main(String[] args) {

        MongoConnectionWrapper mongoConnectionWrapper = new MongoConnectionWrapper();
        mongoConnectionWrapper.getPostProcessorMessageCollection().drop();
        mongoConnectionWrapper.getPostProcessorMessageCollectionFailed().drop();
        mongoConnectionWrapper.getProviderRefreshMessageCollection().drop();
        mongoConnectionWrapper.getProviderRefreshMessageCollectionFailed().drop();
        mongoConnectionWrapper.getProviderCreateMessageCollection().drop();
        mongoConnectionWrapper.getProviderCreateMessageCollectionFailed().drop();
        mongoConnectionWrapper.getQueueCollection().drop();
        System.err.println("cleaned up");
    }
}
