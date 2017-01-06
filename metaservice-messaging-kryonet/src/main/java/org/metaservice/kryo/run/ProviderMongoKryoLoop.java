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

import com.esotericsoftware.kryonet.Client;
import org.metaservice.api.messaging.MessagingException;
import org.metaservice.api.messaging.ProviderMessagingLoop;
import org.metaservice.kryo.KryoClientUtil;
import org.metaservice.kryo.ProviderListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by ilo on 08.06.2014.
 */
public class ProviderMongoKryoLoop implements ProviderMessagingLoop{
    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderMongoKryoLoop.class);


    private final ProviderListener providerListener;

    @Inject
    public ProviderMongoKryoLoop(ProviderListener providerListener) {
        this.providerListener = providerListener;
    }


    @Override
    public void run() throws MessagingException {
        try {
            LOGGER.error("STARTING CLIENT");
            Client client =new KryoClientUtil().startClient(providerListener);
            client.getUpdateThread().setPriority(Thread.MAX_PRIORITY);
            LOGGER.error("Update thread is alive " + client.getUpdateThread().isAlive());
            client.getUpdateThread().join();
            LOGGER.error("Client Update Thread joined");
        } catch (IOException | InterruptedException e) {
            LOGGER.error("Error during running ", e);
        }
    }
}
