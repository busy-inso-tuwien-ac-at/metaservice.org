package org.metaservice.frontend.rest;

import com.sun.jersey.spi.resource.Singleton;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

    /*
@Singleton
@Path("/refresh")*/
public class RefreshResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshResource.class);

    public RefreshResource() throws JMSException {
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);

        Connection connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);

        Destination destination = session.createTopic("VirtualTopic.Refresh");
        producer = session.createProducer(destination);
    }
    private Session session;

    private MessageProducer producer;
    @POST
    public Response postRefresh(RefreshBean bean) {
        try {
            TextMessage message  = session.createTextMessage(bean.getUrl());
            producer.send(message);
            LOGGER.info("Sent message '{}'",bean.getUrl());
        } catch (JMSException e) {
            LOGGER.error("JMS Exception ",e);
            return Response.serverError().build();
        }
        return Response.ok().build();
    }

    @XmlRootElement
    private static class RefreshBean {
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        private String url;
    }
}
