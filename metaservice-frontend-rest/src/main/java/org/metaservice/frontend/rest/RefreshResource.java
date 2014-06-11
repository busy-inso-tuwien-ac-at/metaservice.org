package org.metaservice.frontend.rest;

import com.sun.jersey.spi.resource.Singleton;

import org.metaservice.api.messaging.MessageHandler;
import org.metaservice.api.messaging.MessagingException;
import org.metaservice.kryo.MongoKryoMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;


@Singleton
@Path("/refresh")
public class RefreshResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshResource.class);

    public RefreshResource() {
        messageHandler = new MongoKryoMessageHandler();
        try {
            messageHandler.init();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    //todo init
    private MessageHandler messageHandler;
    @Produces("application/json")
    @POST
    public Response postRefresh(RefreshBean bean) {
        try {
            messageHandler.send(bean.getUrl());
            LOGGER.info("Sent message '{}'",bean.getUrl());
        } catch (MessagingException e) {
            LOGGER.error("Messaging Exception ",e);
            return Response.serverError().build();
        }
        return Response.ok("{status:200}").build();
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
