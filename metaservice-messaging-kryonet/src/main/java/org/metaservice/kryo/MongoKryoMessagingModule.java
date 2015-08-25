package org.metaservice.kryo;

import com.google.inject.AbstractModule;
import org.metaservice.api.messaging.MessageHandler;
import org.metaservice.api.messaging.PostProcessorMessagingLoop;
import org.metaservice.api.messaging.ProviderMessagingLoop;
import org.metaservice.api.messaging.dispatcher.ProviderDispatcher;
import org.metaservice.kryo.run.PostProcessorMongoKryoLoop;
import org.metaservice.kryo.run.ProviderMongoKryoLoop;

/**
 * Created by ilo on 08.06.2014.
 */
public class MongoKryoMessagingModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MessageHandler.class).to(MongoKryoMessageHandler.class);
        bind(ProviderMessagingLoop.class).to(ProviderMongoKryoLoop.class);
        bind(PostProcessorMessagingLoop.class).to(PostProcessorMongoKryoLoop.class);
    }
}
