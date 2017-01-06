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

package org.metaservice.messaging.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.metaservice.api.messaging.Config;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.jms.ConnectionFactory;

/**
 * Created by ilo on 06.01.14.
 */
public class ConnectionFactoryProvider implements Provider<ConnectionFactory> {
    private final Config config;

    @Inject
    public ConnectionFactoryProvider(Config config) {
        this.config = config;
    }

    @Override
    public ConnectionFactory get() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(config.getJmsBroker());
        activeMQConnectionFactory.setUseAsyncSend(true);
        //activeMQConnectionFactory.setDispatchAsync();
        return activeMQConnectionFactory;
    }
}
