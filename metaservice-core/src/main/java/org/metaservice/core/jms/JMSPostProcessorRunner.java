package org.metaservice.core.jms;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.core.Config;
import org.metaservice.core.injection.InjectorFactory;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

public class JMSPostProcessorRunner extends AbstractJMSRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(JMSPostProcessorRunner.class);
    final PostProcessor postProcessor;
    final ValueFactory valueFactory;

    public static void main(String[] args) throws JMSException {
        if(args.length != 1)
        {
            LOGGER.error("Need postprocessor id");
            System.exit(-1);
        }
        String id = args[0];
        Injector injector = InjectorFactory.getInjectorForPostProcessor(id);
        JMSPostProcessorRunner runner = injector.getInstance(JMSPostProcessorRunner.class);
        runner.run();
    }


    @Inject
    private JMSPostProcessorRunner(
            PostProcessor postProcessor,
            ValueFactory valueFactory,
            ConnectionFactory connectionFactory
    ) throws JMSException, RepositoryException {
        super("PostProcessResource",connectionFactory);
        this.postProcessor = postProcessor;
        this.valueFactory = valueFactory;
        LOGGER.info("DONE");
    }

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage m = (TextMessage) message;
            URI uri = valueFactory.createURI(m.getText());
            if(postProcessor.abortEarly(uri)){
                postProcessor.process(uri);
            }
        } catch (JMSException|PostProcessorException e) {
            LOGGER.error("",e);
        }
    }
}
