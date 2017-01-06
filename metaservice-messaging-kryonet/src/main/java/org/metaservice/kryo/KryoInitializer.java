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

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.mongodb.util.Hash;
import de.undercouch.bson4jackson.types.ObjectId;
import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.messaging.statistics.QueueStatisticsImpl;
import org.metaservice.api.messaging.PostProcessingHistoryItem;
import org.metaservice.api.messaging.PostProcessingTask;
import org.metaservice.kryo.beans.*;
import org.metaservice.kryo.mongo.MongoConnectionWrapper;
import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;

import java.util.*;

/**
 * Created by ilo on 07.06.2014.
 */
public class KryoInitializer {
    public void initialize(Kryo kryo){
        Registration registration= kryo.register(URI.class,new Serializer() {
            @Override
            public void write(Kryo kryo, Output output, Object object) {
                Serializer<URIImpl> uriSerializer = kryo.getSerializer(URIImpl.class);
                URIImpl uri = new URIImpl(((URI)object).stringValue());
                uriSerializer.write(kryo,output,uri);
            }

            @Override
            public Object read(Kryo kryo, Input input, Class type) {
                Serializer<URIImpl> uriSerializer = kryo.getSerializer(URIImpl.class);
                return uriSerializer.read(kryo,input,URIImpl.class);
            }
        });
        kryo.register(URIImpl.class);
        kryo.register(ArrayList.class);
        kryo.register(MongoConnectionWrapper.JacksonUriImpl.class);
        kryo.register(Date.class);
        kryo.register(PostProcessingHistoryItem.class);
        kryo.register(ArchiveAddress.class);
        kryo.register(PostProcessingTask.class);
        kryo.register(ObjectId.class);
        kryo.register(org.bson.types.ObjectId.class);
        kryo.register(PostProcessorMessage.class);
        kryo.register(ProviderCreateMessage.class);
        kryo.register(ProviderRefreshMessage.class);
        kryo.register(ResponseMessage.class);
        kryo.register(ResponseMessage.Status.class);
        kryo.register(RegisterClientMessage.class);
        kryo.register(RegisterClientMessage.Type.class);
        kryo.register(QueueStatisticsImpl.class);
        kryo.register(ObjectSpace.InvokeMethod.class);
        kryo.register(ObjectSpace.InvokeMethodResult.class);
        kryo.register(StatisticsProvider.class);
        kryo.register(HashMap.class);
        kryo.register(org.openrdf.model.URI[].class);

    }

}
