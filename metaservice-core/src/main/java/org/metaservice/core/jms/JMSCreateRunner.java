package org.metaservice.core.jms;


import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.core.provider.ProviderDispatcher;
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
    private final ProviderDispatcher providerDispatcher;

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
            ProviderDispatcher providerDispatcher) throws JMSException, RepositoryException {
        super(connectionFactory);
        initQueue("Consumer." + getClass().getName().replaceAll("\\.","_") + ".VirtualTopic.Create");
        this.providerDispatcher = providerDispatcher;
    }

    @Override
    public void onMessage(Message message) {
        try {
            ObjectMessage m = (ObjectMessage) message;
            ArchiveAddress archiveAddress = (ArchiveAddress) m.getObject();
            LOGGER.info("processing " + archiveAddress.getPath());
            providerDispatcher.create(archiveAddress);
        } catch (JMSException e) {
            LOGGER.error("JMS Exception",e);
        }
    }
}
