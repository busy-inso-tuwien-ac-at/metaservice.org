package org.metaservice.messaging.jms;

import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.descriptors.DescriptorHelper;
import org.metaservice.api.messaging.dispatcher.PostProcessorDispatcher;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.api.messaging.MessagingException;
import org.metaservice.api.messaging.PostProcessorMessagingLoop;
import org.metaservice.api.messaging.PostProcessingTask;

import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jms.*;

public class PostProcessorJMSLoop implements PostProcessorMessagingLoop{
    private static final Logger LOGGER = LoggerFactory.getLogger(PostProcessorJMSLoop.class);

    final PostProcessorDispatcher postProcessorDispatcher;
    final MetaserviceDescriptor metaserviceDescriptor;
    final MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor;
    final JMSUtil jmsUtil;
    final DescriptorHelper descriptorHelper;
    @Inject
    private PostProcessorJMSLoop(
            PostProcessorDispatcher postProcessorDispatcher,
            MetaserviceDescriptor metaserviceDescriptor,
            MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor,
            JMSUtil jmsUtil,
            DescriptorHelper descriptorHelper
    ) throws JMSException, RepositoryException {
        this.postProcessorDispatcher = postProcessorDispatcher;
        this.metaserviceDescriptor = metaserviceDescriptor;
        this.postProcessorDescriptor = postProcessorDescriptor;
        this.jmsUtil = jmsUtil;
        this.descriptorHelper = descriptorHelper;
    }

    private String getProcessingQueueName(MetaserviceDescriptor metaserviceDescriptor, MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor) {
        return getEntryQueueName(metaserviceDescriptor,postProcessorDescriptor) +"2";
    }

    private String getEntryQueueName(MetaserviceDescriptor metaserviceDescriptor,MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor){
        return "Consumer." + descriptorHelper.getStringFromPostProcessor(metaserviceDescriptor.getModuleInfo(), postProcessorDescriptor).replaceAll("\\.", "_").replaceAll(":","-")+ ".VirtualTopic.PostProcess";
    }

    @Override
    public void run() throws MessagingException {
        try {
            jmsUtil.runListener(new JMSUtil.ListenerBean() {
                @Override
                public String getName() {
                    return getEntryQueueName(metaserviceDescriptor, postProcessorDescriptor);
                }

                @Override
                public JMSUtil.Type getType() {
                    return JMSUtil.Type.QUEUE;
                }

                @Override
                public void onMessage(Message message) {
                    try {
                        final ObjectMessage m = (ObjectMessage) message;
                        PostProcessingTask task = (PostProcessingTask) m.getObject();
                       // if(postProcessorDispatcher.isOkCheapCheck(task, message.getJMSTimestamp())){
                            jmsUtil.executeProducerTask(
                                    JMSUtil.Type.QUEUE,
                                    getProcessingQueueName(metaserviceDescriptor, postProcessorDescriptor),
                                    new JMSUtil.ProducerTask<JMSException>() {
                                        @Override
                                        public void execute(Session session, MessageProducer producer) throws JMSException{
                                            producer.send(m);
                                        }
                                    });
                        //}
                    } catch (JMSException e) {
                        LOGGER.error("JMS Exception", e);
                    }/* catch (PostProcessorException e) {
                        LOGGER.error("PostProcessorException",e);
                    }*/
                }
            });
            jmsUtil.runListener(new JMSUtil.ListenerBean() {
                @Override
                public String getName() {
                    return getProcessingQueueName(metaserviceDescriptor,postProcessorDescriptor);
                }

                @Override
                public JMSUtil.Type getType() {
                    return JMSUtil.Type.QUEUE;
                }

                @Override
                public void onMessage(Message message) {
                    try {
                        ObjectMessage m = (ObjectMessage) message;
                        PostProcessingTask task = (PostProcessingTask) m.getObject();
                        postProcessorDispatcher.process(task, message.getJMSTimestamp());
                    } catch (JMSException e) {
                        LOGGER.error("JMS Exception", e);
                    } catch (PostProcessorException e) {
                        LOGGER.error("PostProcessorException", e);
                    }
                }
            });
        } catch (JMSException e) {
            throw new MessagingException(e);
        }

    }
}
