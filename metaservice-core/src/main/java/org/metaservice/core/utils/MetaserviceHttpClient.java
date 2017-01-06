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

package org.metaservice.core.utils;

import org.apache.http.client.fluent.Request;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MetaserviceHttpClient {
    private static Logger LOGGER = LoggerFactory.getLogger(MetaserviceHttpClient.class);

    private static final String USER_AGENT = "MetaserviceOrgBot/0.02 (+http://metaservice.org/bot.html)";

    public boolean isCached(String uri) {
        return mongoCache.isCached(uri);
    }

    public enum CachingInstruction {
        CACHE,
        CACHE_READ_ONLY,
        NO_CACHE,
        ONLY_CACHE
    }

    @NotNull
    MongoCache mongoCache = new MongoCache();

    public MetaserviceHttpClient(){

    }

    @Nullable
    public String get(@NotNull String path,@NotNull CachingInstruction cachingInstruction){
        LOGGER.debug("get(\"{}\")", path);
        String result = null;
        if(cachingInstruction == CachingInstruction.CACHE || cachingInstruction == CachingInstruction.CACHE_READ_ONLY){
            LOGGER.debug("Cache lookup");
            result = mongoCache.get("get",path);
            if(result == null){
                LOGGER.debug("\"{}\" not found in cache",path);
            }else{
                LOGGER.debug("\"{}\" found in cache",path);
            }
        }
        if(result == null && cachingInstruction != CachingInstruction.ONLY_CACHE){
            result = httpGet(path);
            if(result!= null && cachingInstruction == CachingInstruction.CACHE){
                mongoCache.put("get",path,result);
            }
        }
        return result;
    }

    @Nullable
    public String get(@NotNull String path) {
        return get(path,CachingInstruction.CACHE);
    }

    @Nullable
    public byte[] getBinary(@NotNull String path) {
       return getBinary(path,CachingInstruction.CACHE);
    }

    @Nullable
    public byte[] getBinary(@NotNull String path,@NotNull CachingInstruction cachingInstruction){
        LOGGER.debug("getBinary(\"{}\")", path);
        byte[] result = null;
        if(cachingInstruction == CachingInstruction.CACHE || cachingInstruction == CachingInstruction.CACHE_READ_ONLY){
            LOGGER.debug("Cache lookup");
            result = mongoCache.getByte("get",path);
            if(result == null){
                LOGGER.debug("\"{}\" not found in cache",path);
            }else{
                LOGGER.debug("\"{}\" found in cache",path);
            }
        }
        if(result == null && cachingInstruction != CachingInstruction.ONLY_CACHE){
            result = httpGetBinary(path);
            if(result!= null && cachingInstruction == CachingInstruction.CACHE){
                mongoCache.putByte("get",path,result);
            }
        }
        return result;
    }



    private @Nullable String httpGet(@NotNull String path) {
        try {
            String result  =  Request
                    .Get(path)
                    .connectTimeout(10000)
                    .socketTimeout(600000)
                    .setHeader("User-Agent", USER_AGENT)
                    .setHeader("Accept", "text/html")
                    .execute()
                    .returnContent()
                    .asString();
            return result;
        } catch (IOException e) {
            LOGGER.error("error retrieving \"{}\"",path,e);
            return null;
        }

    }

    private @Nullable byte[] httpGetBinary(@NotNull String path) {
        try {
            byte[] result  =  Request
                    .Get(path)
                    .connectTimeout(10000)
                    .socketTimeout(600000)
                    .setHeader("User-Agent", USER_AGENT)
                    .setHeader("Accept", "*/*")
                    .execute()
                    .returnContent()
                    .asBytes();
            return result;
        } catch (IOException e) {
            LOGGER.error("error retrieving \"{}\"",path,e);
            return null;
        }
    }
}
