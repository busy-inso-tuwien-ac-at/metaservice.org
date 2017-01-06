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

package org.metaservice.kryo;

import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.metaservice.kryo.beans.AbstractMessage;
import org.metaservice.kryo.beans.ResponseMessage;
import org.metaservice.kryo.mongo.MongoConnectionWrapper;
import org.mongojack.DBQuery;
import org.mongojack.JacksonDBCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by ilo on 07.06.2014.
 */
public class Queue<T extends AbstractMessage> {
    private final static int MILLIES_BETWEEN_UNSUCCESSFULL_MONGO_FETCHES = 1000;
    private final Logger LOGGER = LoggerFactory.getLogger(Queue.class);
    public Queue(MongoConnectionWrapper mongo, JacksonDBCollection<T, Long> db, JacksonDBCollection<ResponseMessage, Long> failed, ObjectId last, String name) {
        this.mongo = mongo;
        this.db = db;
        this.failed = failed;
        this.last = last;
        this.name = name;
    }
    private final String name;
    private final MongoConnectionWrapper mongo;
    private final JacksonDBCollection<T,Long> db;
    private final JacksonDBCollection<ResponseMessage,Long> failed;

    private final LinkedList<T> pushBackQueue = new LinkedList<>();
    private ObjectId last;
    private org.mongojack.DBCursor<T> cursor;
    private Long nullTime;
    public synchronized T getNonBlocking(){
        if(pushBackQueue.size()>0){
            return pushBackQueue.pop();
        }
        if (cursor == null || !cursor.hasNext()) {
            if (last != null) {
                cursor = db.find(DBQuery.greaterThan("_id", last));
            } else {
                cursor = db.find();
            }
            cursor = cursor.sort(new BasicDBObject("_id", 1)).limit(10);
        }
        if(!cursor.hasNext()){
            nullTime = System.currentTimeMillis();
            return null;
        }
        T t = cursor.next();
        LOGGER.trace("new id {}", t.get_id());
        last = t.get_id();

        return t;
    }

    public T getBufferdNonBlock(){
        Long x = nullTime;
        if(x == null || (x< System.currentTimeMillis()-MILLIES_BETWEEN_UNSUCCESSFULL_MONGO_FETCHES)){
            return getNonBlocking();
        }
        return null;
    }

    public T get(){
        do {
            T t = getBufferdNonBlock();
            if(t != null)
                return t;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }while (true);
    }

    public int getCount(){
        org.mongojack.DBCursor<T> countCursor;
        if (last != null) {
            countCursor = db.find(DBQuery.greaterThan("_id", last));
        } else {
            countCursor = db.find();
        }
        return countCursor.count();
    }

    public synchronized void pushBack(T t){
        pushBackQueue.addLast(t);
    }

    public void markAsFailed(ResponseMessage responseMessage){
        failed.insert(responseMessage);
    }

    public void setLast(ObjectId last) {
        this.last = last;
    }

    public ObjectId getLast() {
        return last;
    }

    public void shutDown() {
        LOGGER.info("Shutdown queue {}",name);
        for(T t:pushBackQueue){
            ResponseMessage responseMessage = new ResponseMessage();
            responseMessage.setStatus(ResponseMessage.Status.FAILED);
            responseMessage.setAboutMessage(t);
            responseMessage.setTimestamp(System.currentTimeMillis());
            markAsFailed(responseMessage);
        }
    }

    public String getName() {
        return name;
    }
}
