package org.metaservice.core.utils;

import com.mongodb.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;

public class MongoCache {
    private static Logger LOGGER = LoggerFactory.getLogger(MongoCache.class);
    private MongoClient client;
    private DB db;
    private DBCollection collection;
    private DBCollection fileCollection;

    public MongoCache(){
        this("metaservice.org","crawlcache","nilo","alokin");
    }
    public MongoCache(@NotNull String address,@NotNull String database,@NotNull String username, @NotNull String password){
        try {
            MongoCredential credential = MongoCredential.createMongoCRCredential(username, database, password.toCharArray());
            client = new MongoClient(new ServerAddress(address), Arrays.asList(credential));
            db = client.getDB(database);
            db.authenticate(username,password.toCharArray());
            collection = db.getCollection("debiansnapshot");
            fileCollection = db.getCollection("files");
            collection.ensureIndex("uri");
            fileCollection.ensureIndex("md5");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }


    public @Nullable String get(String accessType, @NotNull String path) {
        DBObject query = new BasicDBObject();
        query.put("uri", path);
        DBObject result = collection.findOne(query);
        if(result != null && result.get("content")!=null)
        {
            LOGGER.info("CACHE HIT");
            return (String) result.get("content");
        }
        else
            return null;
    }

    public void put(String accessType,@NotNull String path, @NotNull String result) {
        DBObject object = new BasicDBObject();
        object.put("uri",path);
        object.put("type","GET");
        object.put("format","text");
        object.put("date",new Date());
        object.put("content",result);
        collection.insert(object);
        LOGGER.info("NO CACHE HIT");
    }


    public @Nullable byte[] getByte(String accessType, @NotNull String path) {
        DBObject query = new BasicDBObject();
        query.put("uri", path);
        DBObject result = collection.findOne(query);
        if(result != null){
            ObjectId entry = (ObjectId) result.get("contentId");
            result = fileCollection.findOne(entry);
            if(result != null && result.get("content")!=null)
            {
                LOGGER.info("CACHE HIT");
                return (byte[]) result.get("content");
            }
        }
        return null;
    }

    /**
     * DEDUP :-)
     * @param accessType
     * @param path
     * @param data
     */
    public void putByte(String accessType, @NotNull String path, @NotNull byte[] data) {
        DBObject object = new BasicDBObject();
        object.put("uri",path);
        object.put("type","GET");
        object.put("format","byte");
        object.put("date",new Date());

        //get Correct file reference
        String md5 = DigestUtils.md5Hex(data);
        DBObject query = new BasicDBObject();
        query.put("md5",md5);
        LOGGER.info("Storing {} md5:{}", path, md5);

        DBObject result = fileCollection.findOne(query);
        if(result != null){
            LOGGER.info("already found - only adding reference");
            object.put("contentId",result.get("_id"));
        } else {
            LOGGER.info("not found - adding file completely");
            DBObject file = new BasicDBObject();
            file.put("content",data);
            file.put("md5",md5);
            fileCollection.insert(file);
            ObjectId id =  (ObjectId)file.get( "_id" );
            object.put("contentId",id);
        }
        collection.insert(object);
    }

    public boolean isCached(String uri) {
        DBObject query = new BasicDBObject();
        query.put("uri", uri);
        DBObject result = collection.findOne(query);
        return (result != null);
    }
}
