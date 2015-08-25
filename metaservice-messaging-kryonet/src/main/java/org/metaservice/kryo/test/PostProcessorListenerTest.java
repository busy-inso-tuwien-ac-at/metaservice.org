package org.metaservice.kryo.test;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.PostProcessingTask;
import org.metaservice.api.messaging.dispatcher.PostProcessorDispatcher;
import org.metaservice.kryo.beans.PostProcessorMessage;
import org.metaservice.kryo.beans.RegisterClientMessage;
import org.metaservice.kryo.beans.ResponseMessage;

import javax.inject.Inject;

/**
 * Created by ilo on 07.06.2014.
 */
public class PostProcessorListenerTest extends Listener {

    @Inject
    public PostProcessorListenerTest(
    ) {

    }

    @Override
    public void connected(Connection connection) {
        RegisterClientMessage registerClientMessage = new RegisterClientMessage();
        registerClientMessage.setMessageCount(10);
        registerClientMessage.setName("ffoo");
        registerClientMessage.setType(RegisterClientMessage.Type.POSTPROCESS);
        connection.sendTCP(registerClientMessage);
        System.err.println("registering");
    }

    @Override
    public void disconnected(Connection connection) {
        System.err.println("disconnect");
    }

    @Override
    public void received(Connection connection, Object o) {
        if(o instanceof PostProcessorMessage ){
            PostProcessorMessage message = (PostProcessorMessage) o;
            PostProcessingTask task = message.getTask();
            ResponseMessage responseMessage = new ResponseMessage();
            responseMessage.setTimestamp(System.currentTimeMillis());
            responseMessage.setAboutMessage(message);

            System.err.println(((PostProcessorMessage) o).get_id() +" would process " + task.getChangedURI());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //      postProcessorDispatcher.process(task,message.getTimestamp());
            responseMessage.setStatus(ResponseMessage.Status.OK);
            connection.sendTCP(responseMessage);
        }
    }



}

