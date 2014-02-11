package org.metaservice.core.jms;

import com.google.inject.Inject;

import javax.jms.*;

/**
 * Created by ilo on 11.02.14.
 */
public class JMSProducerUtil {
    private final ConnectionFactory connectionFactory;

    @Inject
    public JMSProducerUtil(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public <T extends Throwable> void  executeTopicProducerTask(String topicName ,ProducerTask<T> task) throws JMSException,T {
        Connection jmsConnection = connectionFactory.createConnection();
        jmsConnection.setClientID(this.getClass().getName() +"con");
        jmsConnection.start();
        Session session = jmsConnection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic(topicName);
        MessageProducer producer = session.createProducer(destination);
        task.execute(session,producer);
        producer.close();
        session.close();
        jmsConnection.close();
    }


    public abstract static class ProducerTask<T extends Throwable> {
        public abstract void execute(Session session, MessageProducer producer) throws JMSException,T;
    }
}
