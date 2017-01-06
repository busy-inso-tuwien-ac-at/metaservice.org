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
import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.MessagingException;
import org.metaservice.api.messaging.descriptors.DescriptorHelper;
import org.metaservice.api.messaging.dispatcher.ProviderDispatcher;
import org.metaservice.kryo.beans.*;
import org.metaservice.kryo.run.ProviderMongoKryoLoop;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by ilo on 07.06.2014.
 */
public class ProviderListener extends Listener{
    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderListener.class);
    private ProviderDispatcher providerDispatcher;
    private final ValueFactory valueFactory;
    private MetaserviceDescriptor metaserviceDescriptor;
    private MetaserviceDescriptor.ProviderDescriptor providerDescriptor;
    private final DescriptorHelper descriptorHelper;
    @Inject
    private ProviderListener(

            final ValueFactory valueFactory,
            DescriptorHelper descriptorHelper) throws RepositoryException {
        this.valueFactory = valueFactory;
        this.descriptorHelper = descriptorHelper;
    }


    @Override
    public void connected(Connection connection) {
        LOGGER.info("REGISTERING");
        RegisterClientMessage registerClientMessage = new RegisterClientMessage();
        registerClientMessage.setType(RegisterClientMessage.Type.PROVIDER_REFRESH);
        registerClientMessage.setName(descriptorHelper.getStringFromProvider(metaserviceDescriptor.getModuleInfo(),providerDescriptor)+"_REFRESH");
        registerClientMessage.setMessageCount(5);
        connection.sendTCP(registerClientMessage);
        RegisterClientMessage registerClientMessage2 = new RegisterClientMessage();
        registerClientMessage2.setType(RegisterClientMessage.Type.PROVIDER_CREATE);
        registerClientMessage2.setName(descriptorHelper.getStringFromProvider(metaserviceDescriptor.getModuleInfo(),providerDescriptor)+"_CREATE");
        registerClientMessage2.setMessageCount(5);
        connection.sendTCP(registerClientMessage2);
        LOGGER.info("REGISTERING finished");
    }

    @Inject(optional = true)
    public void setMetaserviceDescriptor(MetaserviceDescriptor metaserviceDescriptor) {
        this.metaserviceDescriptor = metaserviceDescriptor;
    }

    @Inject(optional = true)
    public void setProviderDescriptor(MetaserviceDescriptor.ProviderDescriptor providerDescriptor) {
        this.providerDescriptor = providerDescriptor;
    }

    @Inject(optional = true)
    public void setProviderDispatcher(ProviderDispatcher providerDispatcher) {
        this.providerDispatcher = providerDispatcher;
    }

    @Override
    public void received(Connection connection, Object o) {

        if(o instanceof AbstractMessage){
            ResponseMessage responseMessage = new ResponseMessage();
            responseMessage.setAboutMessage((AbstractMessage) o);
            try{
                if(o instanceof ProviderRefreshMessage){
                    URI uri = valueFactory.createURI(((ProviderRefreshMessage) o).getUri());
                    providerDispatcher.refresh(uri);
                }else if(o instanceof ProviderCreateMessage){
                    ArchiveAddress archiveAddress = ((ProviderCreateMessage) o).getArchiveAddress();
                    providerDispatcher.create(archiveAddress);
                }
                responseMessage.setStatus(ResponseMessage.Status.OK);
            }catch (Exception e){
                responseMessage.setStatus(ResponseMessage.Status.FAILED);
                responseMessage.setErrorMessage(e.getMessage());
                LOGGER.error("Processing failed",e);
            }
            responseMessage.setTimestamp(System.currentTimeMillis());
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
                ProviderMongoKryoLoop providerMongoKryoLoop = new ProviderMongoKryoLoop(ProviderListener.this);
                try {
                    providerMongoKryoLoop.run();
                } catch (MessagingException e) {
                    LOGGER.error("failed to reconnect ",e);
                }
            }
        };
        reconnectThread.setDaemon(false);
        reconnectThread.start();
    }
}
