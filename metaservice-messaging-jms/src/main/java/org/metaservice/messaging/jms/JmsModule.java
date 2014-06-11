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
