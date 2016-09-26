/*
 * Copyright 2015 Nikola Ilo
 * Research Group for Industrial Software, Vienna University of Technology, Austria
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

package org.metaservice.messaging.jms;

import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.MessagingException;
import org.metaservice.api.messaging.ProviderMessagingLoop;
import org.metaservice.api.messaging.descriptors.DescriptorHelper;
import org.metaservice.api.messaging.dispatcher.ProviderDispatcher;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jms.*;


public class ProviderJMSLoop implements ProviderMessagingLoop{
    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderJMSLoop.class);
    private final JMSUtil jmsUtil;
    private final DescriptorHelper descriptorHelper;
    private final MetaserviceDescriptor metaserviceDescriptor;
    private final MetaserviceDescriptor.ProviderDescriptor providerDescriptor;
    private final ProviderDispatcher providerDispatcher;
    private final ValueFactory valueFactory;
    @Inject
    private ProviderJMSLoop(
            final ValueFactory valueFactory,
            final MetaserviceDescriptor.ProviderDescriptor providerDescriptor,
            final MetaserviceDescriptor metaserviceDescriptor,
            final ProviderDispatcher providerDispatcher,
            final DescriptorHelper descriptorHelper,
            JMSUtil jmsUtil
    ) throws JMSException, RepositoryException {

        this.jmsUtil = jmsUtil;
        this.descriptorHelper =descriptorHelper;
        this.valueFactory =valueFactory;
        this.metaserviceDescriptor = metaserviceDescriptor;
        this.providerDescriptor = providerDescriptor;
        this.providerDispatcher = providerDispatcher;
    }

    @Override
    public void run() throws MessagingException {
        try {

            jmsUtil.runListener(new JMSUtil.ListenerBean() {
                @Override
                public String getName() {
                    return "Consumer." + descriptorHelper.getStringFromProvider(metaserviceDescriptor.getModuleInfo(),providerDescriptor).replaceAll("\\.", "_").replaceAll(":","-") + ".VirtualTopic.Refresh?consumer.prefetchSize=10";
                }

                @Override
                public JMSUtil.Type getType() {
                    return JMSUtil.Type.QUEUE;
                }

                @Override
                public void onMessage(Message message) {
                    try {
                        if (!(message instanceof TextMessage)) {
                            LOGGER.warn("ATTENTION: Message is not a TextMessage -> Ignoring");
                            return;
                        }
                        TextMessage m = (TextMessage) message;
                        providerDispatcher.refresh(valueFactory.createURI(m.getText()));
                    } catch (JMSException e) {
                        LOGGER.error("JMS Exception", e);
                    }
                }
            });

            jmsUtil.runListener(new JMSUtil.ListenerBean() {
                @Override
                public String getName() {
                    return "Consumer." + descriptorHelper.getStringFromProvider(metaserviceDescriptor.getModuleInfo(),providerDescriptor).replaceAll("\\.", "_").replaceAll(":","-") + ".VirtualTopic.Create?consumer.prefetchSize=10";
                }

                @Override
                public void onMessage(Message message) {
                    try {
                        ObjectMessage m = (ObjectMessage) message;
                        ArchiveAddress archiveAddress = (ArchiveAddress) m.getObject();
                        LOGGER.info("processing " + archiveAddress.getPath());
                        providerDispatcher.create(archiveAddress);
                    } catch (JMSException e) {
                        LOGGER.error("JMS Exception", e);
                    }
                }

                @Override
                public JMSUtil.Type getType() {
                    return JMSUtil.Type.QUEUE;
                }
            });
        } catch (JMSException e) {
            throw new MessagingException(e);
        }
    }
}
