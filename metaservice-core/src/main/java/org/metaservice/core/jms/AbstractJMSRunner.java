package org.metaservice.core.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractJMSRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(JMSPostProcessorRunner.class);
    private Session session;

    private List<ListenerBean> listenerBeanList = new ArrayList<>();

    public void add(ListenerBean listenerBean){
        listenerBeanList.add(listenerBean);
    }

    protected AbstractJMSRunner(
            ConnectionFactory connectionFactory
    ) throws JMSException {

        Connection connection = connectionFactory.createConnection();
        //connection.setClientID(this.getClass().getName() +"con");
        connection.start();
        session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);
    }



    public void run() throws JMSException {
        for(ListenerBean listenerBean : listenerBeanList){
            MessageConsumer consumer = null;
            if(listenerBean.getType() == Type.TOPIC){
                Topic t  =session.createTopic(listenerBean.getName());
                consumer= session.createDurableSubscriber(t,this.getClass().getName()+"sub");
            }else if(listenerBean.getType() == Type.QUEUE){
                Queue q = session.createQueue(listenerBean.getName());
                consumer = session.createConsumer(q);
            }
            if(consumer==null){
                LOGGER.error("run() was called before initTopic or initQueue");
                return;
            }else{
                consumer.setMessageListener(listenerBean);
            }
        }
    }

    public enum Type {
        QUEUE,TOPIC
    }

    public abstract static class ListenerBean implements MessageListener{
        public abstract String getName();
        public abstract Type getType();
    }
}
