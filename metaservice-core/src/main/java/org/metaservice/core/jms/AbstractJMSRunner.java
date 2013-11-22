package org.metaservice.core.jms;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.metaservice.core.Config;

import javax.jms.*;

public abstract class AbstractJMSRunner implements MessageListener {
    private Session session;
    private final String topicName;

    protected AbstractJMSRunner(String topicName, Config config) throws JMSException {
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory(config.getJmsBroker());
        Connection connection = connectionFactory.createConnection();
        connection.setClientID(this.getClass().getName() +"con");
        connection.start();
        session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);

        this.topicName = topicName;
    }

    public void run() throws JMSException {
        Topic t  =session.createTopic(topicName);
        TopicSubscriber subscriber= session.createDurableSubscriber(t,this.getClass().getName()+"sub");
        MessageListener listener = subscriber.getMessageListener();
        subscriber.setMessageListener(this);
    }


}
