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
