package org.metaservice.core.jms;


import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.core.Dispatcher;
import org.metaservice.core.injection.InjectorFactory;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
/**
 * Created by ilo on 05.01.14.
 */
public class JMSCreateRunner extends AbstractJMSRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(JMSRefreshRunner.class);
    private final Dispatcher dispatcher;

    public static void main(String[] args) throws JMSException {
        if(args.length != 1)
        {
            LOGGER.error("Need provider id");
            System.exit(-1);
        }
        String id=  args[0];
        AbstractJMSRunner runner = InjectorFactory.getInjectorForProvider(id).getInstance(JMSCreateRunner.class);
        runner.run();
    }


    @Inject
    public JMSCreateRunner(
            ConnectionFactory connectionFactory,
            Dispatcher dispatcher) throws JMSException, RepositoryException {
        super(connectionFactory);
        initQueue("Consumer." + getClass().getName() + ".VirtualTopic.Create");
        this.dispatcher = dispatcher;
    }

    @Override
    public void onMessage(Message message) {
        try {

            ObjectMessage m = (ObjectMessage) message;
            ArchiveAddress archiveAddress = (ArchiveAddress) m.getObject();
            LOGGER.info("processing " + archiveAddress.getPath());
            dispatcher.create(archiveAddress);
            //TODO think about
            //   bufferedSparql.flushModel();
        } catch (JMSException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
