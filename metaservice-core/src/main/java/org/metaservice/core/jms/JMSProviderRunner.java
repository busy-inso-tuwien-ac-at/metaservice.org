package org.metaservice.core.jms;

import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.core.injection.InjectorFactory;
import org.metaservice.core.provider.ProviderDispatcher;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jms.*;


public class JMSProviderRunner  {
    private static final Logger LOGGER = LoggerFactory.getLogger(JMSProviderRunner.class);

    public static void main(String[] args) throws JMSException {
        if(args.length != 1)
        {
            LOGGER.error("Need provider id");
            System.exit(-1);
        }
        String id=  args[0];
        JMSProviderRunner runner = InjectorFactory.getInjectorForProvider(id).getInstance(JMSProviderRunner.class);
    }

    @Inject
    private JMSProviderRunner(
            final ValueFactory valueFactory,
            final ProviderDispatcher providerDispatcher,
            JMSUtil jmsUtil
    ) throws JMSException, RepositoryException {
        jmsUtil.runListener(new JMSUtil.ListenerBean() {
            @Override
            public String getName() {
                return "Consumer." + getClass().getName().replaceAll("\\.", "_") + ".VirtualTopic.Refresh";
            }

            @Override
            public JMSUtil.Type getType() {
                return JMSUtil.Type.QUEUE;
            }

            @Override
            public void onMessage(Message message) {
                try {
                    if (!(message instanceof TextMessage)) {
                        LOGGER.warn("ATTENTION: Message is not a TextMessage -> Ignoring");
                        return;
                    }
                    TextMessage m = (TextMessage) message;
                    providerDispatcher.refresh(valueFactory.createURI(m.getText()));
                } catch (JMSException e) {
                    LOGGER.error("JMS Exception", e);
                }
            }
        });

        jmsUtil.runListener(new JMSUtil.ListenerBean() {
            @Override
            public String getName() {
                return "Consumer." + getClass().getName().replaceAll("\\.", "_") + ".VirtualTopic.Create";
            }

            @Override
            public void onMessage(Message message) {
                try {
                    ObjectMessage m = (ObjectMessage) message;
                    ArchiveAddress archiveAddress = (ArchiveAddress) m.getObject();
                    LOGGER.info("processing " + archiveAddress.getPath());
                    providerDispatcher.create(archiveAddress);
                } catch (JMSException e) {
                    LOGGER.error("JMS Exception", e);
                }
            }

            @Override
            public JMSUtil.Type getType() {
                return JMSUtil.Type.QUEUE;
            }
        });
    }
}
