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

package org.metaservice.kryo.test;

import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import org.metaservice.api.messaging.PostProcessingTask;
import org.metaservice.kryo.KryoClientUtil;
import org.metaservice.kryo.StatisticsProvider;
import org.metaservice.kryo.beans.PostProcessorMessage;
import org.openrdf.model.impl.ValueFactoryImpl;

import java.io.IOException;
import java.util.Date;

/**
 * Created by ilo on 08.06.2014.
 */
public class KryoTesMessageSender {

    public static void main(String[] args) throws IOException, InterruptedException {
        KryoClientUtil clientUtil = new KryoClientUtil();
        com.esotericsoftware.kryonet.Client client = clientUtil.startClient(new Listener() {
            @Override
            public void connected(Connection connection) {

try {
    for (int i = 0; i < 400000; i++) {
        PostProcessorMessage postProcessorMessage = new PostProcessorMessage();
        postProcessorMessage.setTimestamp(System.currentTimeMillis());
        PostProcessingTask postProcessingTask = new PostProcessingTask(ValueFactoryImpl.getInstance().createURI("http://" + i), new Date());
        postProcessorMessage.setTask(postProcessingTask);
        connection.sendTCP(postProcessorMessage);
        if(i%10 == 0)
            Thread.sleep(1);
   //     System.err.println("sed");

    }
    System.err.println("sent");
}catch (Throwable e){
    e.printStackTrace();
}
            }

            @Override
            public void disconnected(Connection connection) {
                System.err.println("disconnected");
            }
        });
        StatisticsProvider queueContainer = ObjectSpace.getRemoteObject(client,1, StatisticsProvider.class);
        System.err.println(queueContainer.getQueueStatistics());
        Thread.sleep(1000);
        System.err.println(queueContainer.getQueueStatistics());
        Thread.sleep(1000);
        System.err.println(queueContainer.getQueueStatistics());
        Thread.sleep(1000);
        client.stop();
    }
}
