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

package org.metaservice;

import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: ilo
 * Date: 17.10.13
 * Time: 21:39
 * To change this template use File | Settings | File Templates.
 */
public class Md5Runner {
    static MongoClient client;
    static DB db;
    static DBCollection collection;
    static DBCollection fileCollection;

    public static void main(String[] args) {
        try {
            MongoCredential credential = MongoCredential.createMongoCRCredential("nilo", "crawlcache", "alokin".toCharArray());
            client = new MongoClient(new ServerAddress("metaservice.org"), Arrays.asList(credential));
            db = client.getDB("crawlcache");
            db.authenticate("nilo","alokin".toCharArray());
            collection = db.getCollection("debiansnapshot");
            fileCollection = db.getCollection("files");
            collection.ensureIndex("uri");
            fileCollection.ensureIndex("md5");

            DBObject query = new BasicDBObject();
            query.put("md5",new BasicDBObject("$exists",true));
            query.put("content",new BasicDBObject("$type",5));
            BasicDBObject group = new BasicDBObject();
            group.put("_id", "$md5");
            group.put("total", new BasicDBObject("$sum", 1));

            System.err.println(fileCollection.aggregate(new BasicDBObject("$match", query), new BasicDBObject("$group", group), new BasicDBObject("$sort",new BasicDBObject("total",-1))));


            /*
   while(true){

       DBObject query = new BasicDBObject();
       query.put("md5",new BasicDBObject("$exists",false));
       query.put("content",new BasicDBObject("$type",5));

       DBObject object = collection.findOne(query);
        if(object == null)
           break;


       System.err.println(object.get("uri"));

       byte[] x = (byte[]) object.get("content");

       object.put("md5",DigestUtils.md5Hex(x));
       collection.save(object,WriteConcern.SAFE);
   }


   while (true){
       DBObject query = new BasicDBObject();
       query.put("md5",new BasicDBObject("$exists",true));
       query.put("content",new BasicDBObject("$type",5));
       DBObject object = collection.findOne(query);


       String md5  = (String) object.get("md5");
       object.removeField("md5");
       collection.save(object,WriteConcern.SAFE);
       ObjectId id = (ObjectId) object.get("_id");

       object.put("md5",md5);
       object.removeField("_id");
       fileCollection.insert(object,WriteConcern.SAFE);
       System.err.println("INSERTED " + object.get("_id"));
       query = new BasicDBObject();
       query.put("md5",md5);

       // update self
       DBObject x  =collection.findOne(id);
       x.put("contentRef",object.get("_id"));
       x.removeField("content");
       collection.save(x,WriteConcern.SAFE);

       // updatePackage others
       DBObject updatePackage = new BasicDBObject();
       updatePackage.put("$set",new BasicDBObject("contentRef",object.get("_id")));
       updatePackage.put("$unset",new BasicDBObject("content",1));
       WriteResult result = collection.updatePackage(query,updatePackage);
       System.err.println("MD5 " + md5 + " existed  " + result.getN() + " times");

   }
            */
                    } catch (UnknownHostException e) {
                        throw new RuntimeException(e);
                    }

                }

            }

