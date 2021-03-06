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

package org.metaservice.messaging.jms;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.metaservice.api.messaging.MessageHandler;
import org.metaservice.api.messaging.PostProcessorMessagingLoop;
import org.metaservice.api.messaging.ProviderMessagingLoop;

import javax.jms.ConnectionFactory;

/**
 * Created by ilo on 08.06.2014.
 */
public class JmsModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ConnectionFactory.class).toProvider(ConnectionFactoryProvider.class).in(Scopes.SINGLETON);
        bind(MessageHandler.class).to(JmsMessageHandler.class);
        bind(PostProcessorMessagingLoop.class).to(PostProcessorJMSLoop.class);
        bind(ProviderMessagingLoop.class).to(ProviderJMSLoop.class);
    }
}
