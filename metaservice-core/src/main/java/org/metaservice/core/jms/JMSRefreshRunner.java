package org.metaservice.core.jms;

import javax.inject.Inject;
import org.metaservice.core.provider.ProviderDispatcher;
import org.metaservice.core.injection.InjectorFactory;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;


public class JMSRefreshRunner extends AbstractJMSRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(JMSRefreshRunner.class);
    private final ProviderDispatcher providerDispatcher;
    private final ValueFactory valueFactory;

    public static void main(String[] args) throws JMSException {
        if(args.length != 1)
        {
            LOGGER.error("Need provider id");
            System.exit(-1);
        }
        String id=  args[0];
        AbstractJMSRunner runner = InjectorFactory.getInjectorForProvider(id).getInstance(JMSRefreshRunner.class);
        runner.run();
    }

    @Inject
    private JMSRefreshRunner(
            ValueFactory valueFactory,
            ConnectionFactory connectionFactory,
            ProviderDispatcher providerDispatcher) throws JMSException, RepositoryException {
        super(connectionFactory);
        initQueue("Consumer." + getClass().getName().replaceAll("\\.","_") + ".VirtualTopic.Refresh");
        this.valueFactory = valueFactory;
        this.providerDispatcher = providerDispatcher;
    }

    @Override
    public void onMessage(Message message) {
        try {
            if(!(message instanceof TextMessage)){
                LOGGER.warn("ATTENTION: Message is not a TextMessage -> Ignoring");
                return;
            }
            TextMessage m = (TextMessage) message;
            providerDispatcher.refresh(valueFactory.createURI(m.getText()));
         //TODO think about
         //   bufferedSparql.flushModel();
        } catch (JMSException e) {
            LOGGER.error("JMS Exception",e);
        }
    }
}
