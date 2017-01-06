/*
 * Copyright 2015 Nikola Ilo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaservice.kryo;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.google.inject.Inject;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.MessagingException;
import org.metaservice.api.messaging.descriptors.DescriptorHelper;
import org.metaservice.api.messaging.dispatcher.Callback;
import org.metaservice.api.messaging.dispatcher.PostProcessorDispatcher;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.api.messaging.PostProcessingTask;

import org.metaservice.kryo.beans.PostProcessorMessage;
import org.metaservice.kryo.beans.RegisterClientMessage;
import org.metaservice.kryo.beans.ResponseMessage;
import org.metaservice.kryo.run.PostProcessorMongoKryoLoop;
import org.metaservice.kryo.run.ProviderMongoKryoLoop;
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
    private final boolean async = true;
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
        registerClientMessage.setMessageCount(25);
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
            Callback callback = new Callback(responseMessage,connection);
            if(async) {
                postProcessorDispatcher.processAsync(task, message.getTimestamp(), callback);
            }else{
                try {
                    postProcessorDispatcher.process(task,message.getTimestamp());
                    callback.handleOk();
                } catch (PostProcessorException e) {
                    callback.handleException(e);
                }
            }
        }
    }

    public static class Callback implements org.metaservice.api.messaging.dispatcher.Callback{
        private final ResponseMessage responseMessage;
        private Connection connection;
        public Callback(ResponseMessage responseMessage, Connection connection) {
            this.responseMessage = responseMessage;
            this.connection = connection;
        }

        @Override
        public void handleException(Exception e){
            if(e instanceof PostProcessorException) {
                LOGGER.error("PostProcessorException received",e);
                responseMessage.setErrorMessage(e.toString());
                responseMessage.setStatus(ResponseMessage.Status.FAILED);
                e.printStackTrace();
            } else {
                LOGGER.error("fatal ", e);
                responseMessage.setErrorMessage(e.toString());
                responseMessage.setStatus(ResponseMessage.Status.FAILED);
            }
            sendMessage();
        }
        @Override
        public void handleOk(){
            responseMessage.setStatus(ResponseMessage.Status.OK);
            sendMessage();
        }
        private void sendMessage(){
            if(connection==null){
                LOGGER.error("Already sent response - doing nothing");
                return;
            }
            connection.sendTCP(responseMessage);
            connection = null;
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

