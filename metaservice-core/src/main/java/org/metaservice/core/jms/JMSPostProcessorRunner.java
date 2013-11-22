package org.metaservice.core.jms;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.core.Config;
import org.metaservice.core.MetaserviceModule;
import org.metaservice.core.rdf.BufferedSparql;
import org.openrdf.model.URI;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

public class JMSPostProcessorRunner extends AbstractJMSRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(JMSPostProcessorRunner.class);
    final PostProcessor postProcessor;
    final BufferedSparql bufferedSparql;

    public static void main(String[] args) throws JMSException {
        Injector injector = Guice.createInjector(new MetaserviceModule());
        JMSPostProcessorRunner runner = injector.getInstance(JMSPostProcessorRunner.class);
        runner.run();
    }


    @Inject
    private JMSPostProcessorRunner(
            PostProcessor postProcessor,
            BufferedSparql bufferedSparql,
            Config config
    ) throws JMSException, RepositoryException {
        super("PostProcessResource",config);
        this.postProcessor = postProcessor;
        this.bufferedSparql = bufferedSparql;
        LOGGER.info("DONE");
    }

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage m = (TextMessage) message;
            URI uri = bufferedSparql.createURI(m.getText());
            if(postProcessor.abortEarly(uri)){
                postProcessor.process(uri);
            }
        } catch (JMSException|PostProcessorException e) {
            LOGGER.error("",e);
        }
    }
}
