package org.metaservice.core.runner;

import com.google.inject.Injector;
import org.metaservice.api.messaging.MessagingException;
import org.metaservice.core.injection.InjectorFactory;
import org.metaservice.api.messaging.PostProcessorMessagingLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ilo on 08.06.2014.
 */
public class PostProcessorRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostProcessorRunner.class);

    public static void main(String[] args) throws MessagingException {
        if(args.length != 1)
        {
            LOGGER.error("Need postprocessor id");
            System.exit(-1);
        }
        String id = args[0];
        Injector injector = InjectorFactory.getInjectorForPostProcessor(id);
        PostProcessorMessagingLoop runner = injector.getInstance(PostProcessorMessagingLoop.class);
        try {
            runner.run();
        }catch (MessagingException e){
            LOGGER.error("failed ",e);
        }
    }

}
