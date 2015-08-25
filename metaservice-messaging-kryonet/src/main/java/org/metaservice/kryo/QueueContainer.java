package org.metaservice.kryo;

import com.google.common.collect.Ordering;
import org.bson.types.ObjectId;
import org.metaservice.api.messaging.statistics.QueueStatistics;
import org.metaservice.api.messaging.statistics.QueueStatisticsImpl;
import org.metaservice.kryo.beans.*;
import org.metaservice.kryo.mongo.MongoConnectionWrapper;
import org.mongojack.DBQuery;
import org.mongojack.JacksonDBCollection;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by ilo on 09.06.2014.
 */
public class QueueContainer implements StatisticsProvider{
    private final MongoConnectionWrapper mongoConnectionWrapper;
    private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(QueueContainer.class);
    final HashMap<String,Queue<PostProcessorMessage>> postProcessorMessageQueues= new HashMap<>();
    final HashMap<String,Queue<ProviderCreateMessage>> providerCreateMessageQueues= new HashMap<>();
    final HashMap<String,Queue<ProviderRefreshMessage>> providerRefreshMessageQueues= new HashMap<>();

    public QueueContainer(MongoConnectionWrapper mongoConnectionWrapper) {
        this.mongoConnectionWrapper = mongoConnectionWrapper;
        org.mongojack.DBCursor<QueueConfig> queueConfigDBCursor = this.mongoConnectionWrapper.getQueueCollection().find();
        while(queueConfigDBCursor.hasNext()){
            QueueConfig config =queueConfigDBCursor.next();
            LOGGER.info("restoring " + config);
            switch (config.getType()){
                case POSTPROCESS:
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
                    null,
                    name);
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
                    null,
                    name);
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
                    null,
                    name);
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
        LOGGER.info("running Clean Queues PostProcessor");
        clean(postProcessorMessageQueues.values(), mongoConnectionWrapper.getPostProcessorMessageCollection());
        LOGGER.info("running Clean Queues ProviderRefresh");
        clean(providerRefreshMessageQueues.values(),mongoConnectionWrapper.getProviderRefreshMessageCollection());
        LOGGER.info("running Clean Queues ProviderCreate");
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
        LOGGER.info("SaveQueues");
        LOGGER.info(" - drop");
        mongoConnectionWrapper.getQueueCollection().drop();
        for(Map.Entry<String,Queue<PostProcessorMessage>> entry : postProcessorMessageQueues.entrySet()){
            try {
                entry.getValue().shutDown();
                QueueConfig config = new QueueConfig();
                config.setLast(entry.getValue().getLast());
                config.setName(entry.getKey());
                config.setType(RegisterClientMessage.Type.POSTPROCESS);
                LOGGER.info(" - insert postprocess " + entry.getKey());
                mongoConnectionWrapper.getQueueCollection().insert(config);
            }catch (Exception e){
                LOGGER.error("failed to save {}" + entry.getValue().getName());
            }
        }
        for(Map.Entry<String,Queue<ProviderCreateMessage>> entry : providerCreateMessageQueues.entrySet()){
            try {
                QueueConfig config = new QueueConfig();
                config.setLast(entry.getValue().getLast());
                config.setName(entry.getKey());
                config.setType(RegisterClientMessage.Type.PROVIDER_CREATE);
                LOGGER.info(" - insert provider_create" + entry.getKey() );
                mongoConnectionWrapper.getQueueCollection().insert(config);
            }catch (Exception e){
                LOGGER.error("failed to save {}" + entry.getValue().getName());
            }

        }
        for(Map.Entry<String,Queue<ProviderRefreshMessage>> entry : providerRefreshMessageQueues.entrySet()){
            try {
                QueueConfig config = new QueueConfig();
                config.setLast(entry.getValue().getLast());
                config.setName(entry.getKey());
                config.setType(RegisterClientMessage.Type.PROVIDER_REFRESH);
                LOGGER.info(" - insert provider_refresh " + entry.getKey());
                mongoConnectionWrapper.getQueueCollection().insert(config);
            }catch (Exception e){
                LOGGER.error("failed to save {}" + entry.getValue().getName());
            }
        }
    }
}
