package org.metaservice.kryo;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.google.inject.Inject;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.descriptors.DescriptorHelper;
import org.metaservice.api.messaging.dispatcher.PostProcessorDispatcher;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.api.messaging.PostProcessingTask;

import org.metaservice.kryo.beans.PostProcessorMessage;
import org.metaservice.kryo.beans.RegisterClientMessage;
import org.metaservice.kryo.beans.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by ilo on 07.06.2014.
 */
public class PostProcessorListener extends Listener {
    private final static Logger LOGGER = LoggerFactory.getLogger(PostProcessorListener.class);
    @Inject(optional = true)
    public void setPostProcessorDispatcher(PostProcessorDispatcher postProcessorDispatcher) {
        this.postProcessorDispatcher = postProcessorDispatcher;
    }

    private MetaserviceDescriptor metaserviceDescriptor;
    private MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor;

    @Inject(optional = true)
    public void setMetaserviceDescriptor(MetaserviceDescriptor metaserviceDescriptor) {
        this.metaserviceDescriptor = metaserviceDescriptor;
    }

    @Inject(optional = true)
    public void setPostProcessorDescriptor(MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor) {
        this.postProcessorDescriptor = postProcessorDescriptor;
    }

    private PostProcessorDispatcher postProcessorDispatcher;

    private final DescriptorHelper descriptorHelper;
    @Inject
    public PostProcessorListener(
        DescriptorHelper descriptorHelper
    ){
        this.descriptorHelper = descriptorHelper;
    }

    @Override
    public void connected(Connection connection) {
        RegisterClientMessage registerClientMessage = new RegisterClientMessage();
        registerClientMessage.setType(RegisterClientMessage.Type.POSTPROCESS);
        registerClientMessage.setName(descriptorHelper.getStringFromPostProcessor(metaserviceDescriptor.getModuleInfo(),postProcessorDescriptor));
        registerClientMessage.setMessageCount(5);
        connection.sendTCP(registerClientMessage);
    }

    @Override
    public void received(Connection connection, Object o) {
        if(o instanceof PostProcessorMessage) {
            PostProcessorMessage message = (PostProcessorMessage) o;
            PostProcessingTask task = message.getTask();
            ResponseMessage responseMessage = new ResponseMessage();
            responseMessage.setTimestamp(System.currentTimeMillis());
            responseMessage.setAboutMessage(message);
            LOGGER.info("processing {}", message.get_id());
            try {
                if (postProcessorDispatcher.isOkCheapCheck(task, message.getTimestamp())) {
                    postProcessorDispatcher.process(task,message.getTimestamp());
                }
                responseMessage.setStatus(ResponseMessage.Status.OK);
            } catch (PostProcessorException e) {
                responseMessage.setErrorMessage(e.toString());
                responseMessage.setStatus(ResponseMessage.Status.FAILED);
                e.printStackTrace();
            } catch (Exception e){
                LOGGER.error("fatal ", e);
                responseMessage.setErrorMessage(e.toString());
                responseMessage.setStatus(ResponseMessage.Status.FAILED);
                connection.sendTCP(responseMessage);
                connection.close();
                return;
            }
            connection.sendTCP(responseMessage);
        }
    }

    @Override
    public void disconnected(final Connection connection) {
        LOGGER.error("DISCONNECTED!!!!!");
        Thread reconnectThread= new Thread("reconnect"){
            @Override
            public void run() {
                connection.getEndPoint().stop();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignored) {
                }
                LOGGER.info("trying to reconnect");
                PostProcessorMongoKryoLoop postProcessorMongoKryoLoop = new PostProcessorMongoKryoLoop(PostProcessorListener.this);
                postProcessorMongoKryoLoop.run();
            }
        };
        reconnectThread.setDaemon(false);
        reconnectThread.start();
    }
}

