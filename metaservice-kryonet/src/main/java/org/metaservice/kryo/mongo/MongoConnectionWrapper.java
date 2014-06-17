package org.metaservice.kryo.mongo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.metaservice.kryo.QueueConfig;
import org.metaservice.kryo.beans.PostProcessorMessage;
import org.metaservice.kryo.beans.ProviderCreateMessage;
import org.metaservice.kryo.beans.ProviderRefreshMessage;
import org.metaservice.kryo.beans.ResponseMessage;
import org.mongojack.JacksonDBCollection;
import org.mongojack.internal.MongoJackModule;
import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;

import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by ilo on 07.06.2014.
 */
public class MongoConnectionWrapper {
    public JacksonDBCollection<QueueConfig, Long> getQueueCollection() {
        return queueCollection;
    }

    private final JacksonDBCollection<QueueConfig, Long> queueCollection;
    private JacksonDBCollection<PostProcessorMessage,Long> postProcessorMessageCollection;
    private JacksonDBCollection<ProviderCreateMessage,Long> providerCreateMessageCollection;
    private JacksonDBCollection<ProviderRefreshMessage,Long> providerRefreshMessageCollection;

    private JacksonDBCollection<ResponseMessage,Long> postProcessorMessageCollectionFailed;
    private JacksonDBCollection<ResponseMessage,Long> providerCreateMessageCollectionFailed;
    private JacksonDBCollection<ResponseMessage,Long> providerRefreshMessageCollectionFailed;

    public JacksonDBCollection<PostProcessorMessage, Long> getPostProcessorMessageCollection() {
        return postProcessorMessageCollection;
    }

    public JacksonDBCollection<ProviderCreateMessage, Long> getProviderCreateMessageCollection() {
        return providerCreateMessageCollection;
    }

    public JacksonDBCollection<ProviderRefreshMessage, Long> getProviderRefreshMessageCollection() {
        return providerRefreshMessageCollection;
    }
    public JacksonDBCollection<ResponseMessage, Long> getPostProcessorMessageCollectionFailed() {
        return postProcessorMessageCollectionFailed;
    }

    public JacksonDBCollection<ResponseMessage, Long> getProviderCreateMessageCollectionFailed() {
        return providerCreateMessageCollectionFailed;
    }

    public JacksonDBCollection<ResponseMessage, Long> getProviderRefreshMessageCollectionFailed() {
        return providerRefreshMessageCollectionFailed;
    }

    public MongoConnectionWrapper(){
        this("metaservice.org","ms_messaging","nilo","alokin");
    }

    public void close() {
        client.close();
    }

    public static class JacksonUriImpl implements URI{
        public JacksonUriImpl(){}
        private String namespace;
        private String localName;

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        public void setLocalName(String localName) {
            this.localName = localName;
        }

        @Override
        public String getNamespace() {
            return namespace;
        }

        @Override
        public String getLocalName() {
            return localName;
        }

        @Override
        public String stringValue() {
            return getNamespace()+getLocalName();
        }

        @Override
        public String toString(){
            return stringValue();
        }
    }
    @JsonDeserialize(as = JacksonUriImpl.class)
    @JsonSerialize(as = URIImpl.class)
    abstract class UriMixin{}

    private final MongoClient client;
    public MongoConnectionWrapper(String address, String database, String username, String password){
        try {
            MongoCredential credential = MongoCredential.createMongoCRCredential(username, database, password.toCharArray());
            client = new MongoClient(new ServerAddress(address), Arrays.asList(credential));
            DB db = client.getDB(database);

            db.authenticate(username,password.toCharArray());
            ObjectMapper objectMapper = MongoJackModule.configure(new ObjectMapper());
            objectMapper.addMixInAnnotations(URI.class,UriMixin.class);

            queueCollection = JacksonDBCollection.wrap(
                    db.getCollection("queues"),
                    QueueConfig.class,
                    Long.class,
                    objectMapper
                    );
            postProcessorMessageCollection = JacksonDBCollection.wrap(
                    db.getCollection("postProcessor"),
                    PostProcessorMessage.class,
                    Long.class,
                    objectMapper);
            providerCreateMessageCollection = JacksonDBCollection.wrap(
                    db.getCollection("create"),
                    ProviderCreateMessage.class,
                    Long.class,
                    objectMapper);
            providerRefreshMessageCollection = JacksonDBCollection.wrap(
                    db.getCollection("refresh"),
                    ProviderRefreshMessage.class,
                    Long.class,
                    objectMapper);
            postProcessorMessageCollectionFailed = JacksonDBCollection.wrap(
                    db.getCollection("postProcessorFailed"),
                    ResponseMessage.class,
                    Long.class,
                    objectMapper);
            providerCreateMessageCollectionFailed = JacksonDBCollection.wrap(
                    db.getCollection("createFailed"),
                    ResponseMessage.class,
                    Long.class,
                    objectMapper);
            providerRefreshMessageCollectionFailed = JacksonDBCollection.wrap(
                    db.getCollection("refreshFailed"),
                    ResponseMessage.class,
                    Long.class,
                    objectMapper);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
