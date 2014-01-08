package org.metaservice.core.injection.providers;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.metaservice.core.Config;

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
        return new ActiveMQConnectionFactory(config.getJmsBroker());
    }
}
