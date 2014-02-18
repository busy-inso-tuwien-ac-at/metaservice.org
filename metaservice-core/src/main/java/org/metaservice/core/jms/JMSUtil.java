package org.metaservice.core.jms;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

/**
 * Created by ilo on 11.02.14.
 */
public class JMSUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JMSUtil.class);

    private final ConnectionFactory connectionFactory;

    @Inject
    public JMSUtil(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public <T extends Throwable> void executeProducerTask(Type type,String name, ProducerTask<T> task) throws JMSException,T {
        Connection jmsConnection = connectionFactory.createConnection();
        //  jmsConnection.setClientID(this.getClass().getName() +"con");
        jmsConnection.start();
        Session session = jmsConnection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);
        Destination destination;
        if(type == Type.TOPIC)
        {
            destination = session.createTopic(name);
        }
        else
        {
            destination = session.createQueue(name);
        }
        MessageProducer producer = session.createProducer(destination);
        task.execute(session,producer);
        producer.close();
        session.close();
        jmsConnection.close();
    }

    public ShutDownHandler runListener(ListenerBean listenerBean) throws JMSException {
        Connection jmsConnection = connectionFactory.createConnection();
      //  jmsConnection.setClientID(this.getClass().getName() +"con");
        jmsConnection.start();
        Session session = jmsConnection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);
        MessageConsumer consumer;
        if(listenerBean.getType() == Type.TOPIC){
            Topic t  =session.createTopic(listenerBean.getName());
            consumer= session.createDurableSubscriber(t,this.getClass().getName()+"sub");
        }else /* if(listenerBean.getType() == Type.QUEUE)*/{
            Queue q = session.createQueue(listenerBean.getName());
            consumer = session.createConsumer(q);
        }
        consumer.setMessageListener(listenerBean);
        return new ShutDownHandler(jmsConnection,session);
    }


    public abstract static class ProducerTask<T extends Throwable> {
        public abstract void execute(Session session, MessageProducer producer) throws JMSException,T;
    }

    public enum Type {
        QUEUE,TOPIC
    }


    public static class ShutDownHandler{
        private final Connection connection;
        private final Session session;

        public ShutDownHandler(Connection connection, Session session) {
            this.connection = connection;
            this.session = session;
        }

        public void shutdown(){
            try {
                connection.close();
                session.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    public abstract static class ListenerBean implements MessageListener{
        public abstract String getName();
        public abstract Type getType();
    }
}
