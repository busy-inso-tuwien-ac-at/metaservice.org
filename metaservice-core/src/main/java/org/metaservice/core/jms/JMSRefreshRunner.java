package org.metaservice.core.jms;

import com.google.inject.*;
import org.metaservice.core.Config;
import org.metaservice.core.Dispatcher;
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
    private final Dispatcher dispatcher;
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
            Dispatcher dispatcher) throws JMSException, RepositoryException {
        super("Refresh",connectionFactory);
        this.valueFactory = valueFactory;
        this.dispatcher = dispatcher;
    }

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage m = (TextMessage) message;
            dispatcher.refresh(valueFactory.createURI(m.getText()));
         //TODO think about
         //   bufferedSparql.flushModel();
        } catch (JMSException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
