package org.metaservice.core.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public abstract class AbstractJMSRunner implements MessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(JMSPostProcessorRunner.class);
    private Session session;
    private String topicName = null;
    private String queueName = null;

    protected AbstractJMSRunner(
            ConnectionFactory connectionFactory
    ) throws JMSException {

        Connection connection = connectionFactory.createConnection();
        //connection.setClientID(this.getClass().getName() +"con");
        connection.start();
        session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);
    }

    public void initTopic(String name){
        this.topicName = name;
    }

    public void initQueue(String name){
        this.queueName = name;
    }

    public void run() throws JMSException {
        MessageConsumer consumer = null;
        if(topicName!= null){
            Topic t  =session.createTopic(topicName);
            consumer= session.createDurableSubscriber(t,this.getClass().getName()+"sub");
        }else if(queueName != null){
            Queue q = session.createQueue(queueName);
            consumer = session.createConsumer(q);
        }
        if(consumer==null){
            LOGGER.error("run() was called before initTopic or initQueue");
            return;
        }else{
            consumer.setMessageListener(this);
        }
    }


}
