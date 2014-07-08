package org.metaservice.kryo;

import com.google.common.collect.Ordering;
import org.bson.types.ObjectId;
import org.metaservice.api.messaging.statistics.QueueStatistics;
import org.metaservice.api.messaging.statistics.QueueStatisticsImpl;
import org.metaservice.kryo.beans.*;
import org.metaservice.kryo.mongo.MongoConnectionWrapper;
import org.mongojack.DBQuery;
import org.mongojack.JacksonDBCollection;

import java.util.*;

/**
 * Created by ilo on 09.06.2014.
 */
public class QueueContainer implements StatisticsProvider{
    private final MongoConnectionWrapper mongoConnectionWrapper;

    HashMap<String,Queue<PostProcessorMessage>> postProcessorMessageQueues= new HashMap<>();
    HashMap<String,Queue<ProviderCreateMessage>> providerCreateMessageQueues= new HashMap<>();
    HashMap<String,Queue<ProviderRefreshMessage>> providerRefreshMessageQueues= new HashMap<>();

    public QueueContainer(MongoConnectionWrapper mongoConnectionWrapper) {
        this.mongoConnectionWrapper = mongoConnectionWrapper;
        org.mongojack.DBCursor<QueueConfig> queueConfigDBCursor = this.mongoConnectionWrapper.getQueueCollection().find();
        while(queueConfigDBCursor.hasNext()){
            QueueConfig config =queueConfigDBCursor.next();
            switch (config.getType()){
                case POSTPROCESS:
                    System.err.println("restoring " + config);
                    Queue queue = addPostProcessorQueue(config.getName());
                    queue.setLast(config.getLast());
                    break;
                case PROVIDER_CREATE:
                    queue = addProviderCreateQueue(config.getName());
                    queue.setLast(config.getLast());
                    break;
                case PROVIDER_REFRESH:
                    queue = addProviderRefreshQueue(config.getName());
                    queue.setLast(config.getLast());
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }
    }

    public Queue<PostProcessorMessage> addPostProcessorQueue(String name){
        if(!postProcessorMessageQueues.containsKey(name)) {
            Queue<PostProcessorMessage> queue = new Queue<>(
                    mongoConnectionWrapper,
                    mongoConnectionWrapper.getPostProcessorMessageCollection(),
                    mongoConnectionWrapper.getPostProcessorMessageCollectionFailed(),
                    null);
            postProcessorMessageQueues.put(name, queue);
        }
        return postProcessorMessageQueues.get(name);
    }


    public Queue<ProviderCreateMessage> addProviderCreateQueue(String name){
        if(!providerCreateMessageQueues.containsKey(name)) {
            Queue<ProviderCreateMessage> queue = new Queue<>(
                    mongoConnectionWrapper,
                    mongoConnectionWrapper.getProviderCreateMessageCollection(),
                    mongoConnectionWrapper.getProviderCreateMessageCollectionFailed(),
                    null);
            providerCreateMessageQueues.put(name, queue);
        }
        return providerCreateMessageQueues.get(name);
    }


    public Queue<ProviderRefreshMessage> addProviderRefreshQueue(String name){
        if(!providerRefreshMessageQueues.containsKey(name)) {
            Queue<ProviderRefreshMessage> queue = new Queue<>(
                    mongoConnectionWrapper,
                    mongoConnectionWrapper.getProviderRefreshMessageCollection(),
                    mongoConnectionWrapper.getProviderRefreshMessageCollectionFailed(),
                    null);
            providerRefreshMessageQueues.put(name, queue);
        }
        return providerRefreshMessageQueues.get(name);
    }

    @Override
    public List<QueueStatistics> getQueueStatistics(){
        ArrayList<QueueStatistics> result = new ArrayList<>();

        for(Map.Entry<String,Queue<PostProcessorMessage>> entry : postProcessorMessageQueues.entrySet()){
            QueueStatisticsImpl queueStatistics = new QueueStatisticsImpl(entry.getKey(),entry.getValue().getCount());
            result.add(queueStatistics);
        }
        for(Map.Entry<String,Queue<ProviderCreateMessage>> entry : providerCreateMessageQueues.entrySet()){
            QueueStatisticsImpl queueStatistics = new QueueStatisticsImpl(entry.getKey(),entry.getValue().getCount());
            result.add(queueStatistics);
        }
        for(Map.Entry<String,Queue<ProviderRefreshMessage>> entry : providerRefreshMessageQueues.entrySet()){
            QueueStatisticsImpl queueStatistics = new QueueStatisticsImpl(entry.getKey(),entry.getValue().getCount());
            result.add(queueStatistics);
        }
        QueueStatisticsImpl queueStatistics = new QueueStatisticsImpl("postprocess_fail",mongoConnectionWrapper.getPostProcessorMessageCollectionFailed().count());
        result.add(queueStatistics);
        queueStatistics = new QueueStatisticsImpl("provider_create_fail",mongoConnectionWrapper.getProviderCreateMessageCollectionFailed().count());
        result.add(queueStatistics);
        queueStatistics = new QueueStatisticsImpl("provider_refresh_fail",mongoConnectionWrapper.getProviderRefreshMessageCollectionFailed().count());
        result.add(queueStatistics);
        queueStatistics = new QueueStatisticsImpl("postprocess",mongoConnectionWrapper.getPostProcessorMessageCollection().count());
        result.add(queueStatistics);
        queueStatistics = new QueueStatisticsImpl("provider_create",mongoConnectionWrapper.getProviderCreateMessageCollection().count());
        result.add(queueStatistics);
        queueStatistics = new QueueStatisticsImpl("provider_refresh",mongoConnectionWrapper.getProviderRefreshMessageCollection().count());
        result.add(queueStatistics);
        return result;
    }


    public void cleanQueues(){
        clean(postProcessorMessageQueues.values(),mongoConnectionWrapper.getPostProcessorMessageCollection());
        clean(providerRefreshMessageQueues.values(),mongoConnectionWrapper.getProviderRefreshMessageCollection());
        clean(providerCreateMessageQueues.values(),mongoConnectionWrapper.getProviderCreateMessageCollection());
    }

    private <T extends AbstractMessage> void clean(Collection<Queue<T>> queues, JacksonDBCollection collection){
        if(queues.size() > 0){
            Ordering<ObjectId> objectIdOrdering = new Ordering<ObjectId>() {
                @Override
                public int compare(ObjectId left, ObjectId right) {
                    if(left != null)
                        return left.compareTo(right);
                    else
                        return -1;
                }
            };
            ArrayList<ObjectId> list = new ArrayList<>();
            for(Queue queue : queues){
                list.add(queue.getLast());
            }
            ObjectId maxPostProcess = objectIdOrdering.min(list);
            collection.remove(DBQuery.lessThanEquals("_id",maxPostProcess));
        }
    }

    public void saveQueues(){
        mongoConnectionWrapper.getQueueCollection().drop();
        for(Map.Entry<String,Queue<PostProcessorMessage>> entry : postProcessorMessageQueues.entrySet()){
            entry.getValue().shutDown();
            QueueConfig config = new QueueConfig();
            config.setLast(entry.getValue().getLast());
            config.setName(entry.getKey());
            config.setType(RegisterClientMessage.Type.POSTPROCESS);

            mongoConnectionWrapper.getQueueCollection().insert(config);
        }
        for(Map.Entry<String,Queue<ProviderCreateMessage>> entry : providerCreateMessageQueues.entrySet()){
            QueueConfig config = new QueueConfig();
            config.setLast(entry.getValue().getLast());
            config.setName(entry.getKey());
            config.setType(RegisterClientMessage.Type.PROVIDER_CREATE);
            mongoConnectionWrapper.getQueueCollection().insert(config);
        }
        for(Map.Entry<String,Queue<ProviderRefreshMessage>> entry : providerRefreshMessageQueues.entrySet()){
            QueueConfig config = new QueueConfig();
            config.setLast(entry.getValue().getLast());
            config.setName(entry.getKey());
            config.setType(RegisterClientMessage.Type.PROVIDER_REFRESH);
            mongoConnectionWrapper.getQueueCollection().insert(config);
        }
    }
}
