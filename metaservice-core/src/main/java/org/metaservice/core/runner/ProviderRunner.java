package org.metaservice.core.runner;

import org.metaservice.api.messaging.MessagingException;
import org.metaservice.core.injection.InjectorFactory;
import org.metaservice.api.messaging.ProviderMessagingLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ilo on 08.06.2014.
 */
public class ProviderRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderRunner.class);

    public static void main(String[] args) {
        if(args.length != 1)
        {
            LOGGER.error("Need provider id");
            System.exit(-1);
        }
        String id=  args[0];
        ProviderMessagingLoop runner = InjectorFactory.getInjectorForProvider(id).getInstance(ProviderMessagingLoop.class);
        try {
            runner.run();
        } catch (MessagingException e) {
            LOGGER.error("failed",e);
        }
    }
}
