package org.metaservice.core.jms;

import javax.jms.*;

public abstract class AbstractJMSRunner implements MessageListener {
    private Session session;
    private final String topicName;

    protected AbstractJMSRunner(
            String topicName,
            ConnectionFactory connectionFactory
    ) throws JMSException {

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
