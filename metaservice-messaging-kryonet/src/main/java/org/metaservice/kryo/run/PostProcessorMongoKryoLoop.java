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

package org.metaservice.kryo.run;

import com.esotericsoftware.kryonet.Client;
import org.metaservice.api.messaging.PostProcessorMessagingLoop;
import org.metaservice.kryo.KryoClientUtil;
import org.metaservice.kryo.PostProcessorListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by ilo on 07.06.2014.
 */
public class PostProcessorMongoKryoLoop implements PostProcessorMessagingLoop {
    public static final Logger LOGGER = LoggerFactory.getLogger(PostProcessorMongoKryoLoop.class);

    private final PostProcessorListener postProcessorListener;

    @Inject
    public PostProcessorMongoKryoLoop(PostProcessorListener postProcessorListener) {
        this.postProcessorListener = postProcessorListener;
    }

    public void run() {
        try {
            Client client =new KryoClientUtil().startClient(postProcessorListener);
            client.getUpdateThread().setPriority(Thread.MAX_PRIORITY);
            client.getUpdateThread().join();
        } catch (IOException | InterruptedException e) {
            LOGGER.error("Error during running ", e);
        }
    }
}
