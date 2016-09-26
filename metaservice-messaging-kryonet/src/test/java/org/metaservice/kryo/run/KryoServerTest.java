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

package org.metaservice.kryo.run;

import org.junit.Test;
import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.messaging.MessageHandler;
import org.metaservice.api.messaging.MessagingException;
import org.metaservice.api.messaging.PostProcessingHistoryItem;
import org.metaservice.api.messaging.PostProcessingTask;
import org.metaservice.kryo.MongoKryoMessageHandler;
import org.metaservice.kryo.beans.PostProcessorMessage;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.*;

public class KryoServerTest {

    public static class FakeURI implements URI{
        @Override
        public String getNamespace() {
            return "http:";
        }

        @Override
        public String getLocalName() {
            return "//history2";
        }

        @Override
        public String stringValue() {
            return "http://history2";
        }
    }
    @Test
    public void postProcessorTest() throws MessagingException, IOException, InterruptedException {
        KryoServer kryoServer = new KryoServer();
        kryoServer.run();
        MessageHandler messageHandler = new MongoKryoMessageHandler();
        ValueFactory valueFactory = new ValueFactoryImpl();
        URI uri = valueFactory.createURI("http://test123");
        PostProcessingTask postProcessingTask = new PostProcessingTask( uri, new Date());
        postProcessingTask.getHistory().add(new PostProcessingHistoryItem("asdfasdf",
                new URI[]{valueFactory.createURI("http://history1234"),
                new FakeURI()}
        ));

        messageHandler.init();
        Thread.sleep(1000);
        messageHandler.send(postProcessingTask);
        Thread.sleep(1000);
        kryoServer.close();
    }

    @Test
    public void privoderCreateTest() throws IOException, MessagingException, InterruptedException {
        KryoServer kryoServer = new KryoServer();
        kryoServer.run();
        MessageHandler messageHandler = new MongoKryoMessageHandler();

        ArchiveAddress archiveAddress =new ArchiveAddress("asdf","asdf",new Date(),"ASDASDF","type");

        messageHandler.init();
        Thread.sleep(1000);
        messageHandler.send(archiveAddress);
        Thread.sleep(1000);
        kryoServer.close();
    }

    @Test
    public void providerRefreshTest() throws MessagingException, InterruptedException, IOException {
        KryoServer kryoServer = new KryoServer();
        kryoServer.run();
        MessageHandler messageHandler = new MongoKryoMessageHandler();
        messageHandler.init();
        Thread.sleep(1000);
        messageHandler.send("ASDFASDFASDF");
        Thread.sleep(1000);
        kryoServer.close();
    }

}