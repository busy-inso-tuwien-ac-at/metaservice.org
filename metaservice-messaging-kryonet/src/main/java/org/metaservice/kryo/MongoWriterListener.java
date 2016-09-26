/*
 * Copyright 2015 Nikola Ilo
 * Research Group for Industrial Software, Vienna University of Technology, Austria
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
        if(object instanceof AbstractMessage){
            if (object instanceof PostProcessorMessage) {
                LOGGER.trace("storing {}", object);
                mongo.getPostProcessorMessageCollection().insert((PostProcessorMessage) object);
            }else if(object instanceof ProviderCreateMessage){
                LOGGER.trace("storing {}", object);
                mongo.getProviderCreateMessageCollection().insert((ProviderCreateMessage) object);
            } else if(object instanceof ProviderRefreshMessage){
                LOGGER.trace("storing {}", object);
                mongo.getProviderRefreshMessageCollection().insert((ProviderRefreshMessage) object);
            }
        }
    }

}
