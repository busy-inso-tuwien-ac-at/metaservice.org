package org.metaservice.core.jms;

import com.google.inject.Injector;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.core.descriptor.DescriptorHelper;
import org.metaservice.core.descriptor.JAXBMetaserviceDescriptorImpl;
import org.metaservice.core.injection.InjectorFactory;
import org.metaservice.core.postprocessor.PostProcessingTask;
import org.metaservice.core.postprocessor.PostProcessorDispatcher;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jms.*;

public class JMSPostProcessorRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(JMSPostProcessorRunner.class);

    public static void main(String[] args) throws JMSException {
        if(args.length != 1)
        {
            LOGGER.error("Need postprocessor id");
            System.exit(-1);
        }
        String id = args[0];
        Injector injector = InjectorFactory.getInjectorForPostProcessor(id);
        JMSPostProcessorRunner runner = injector.getInstance(JMSPostProcessorRunner.class);
    }


    @Inject
    private JMSPostProcessorRunner(
            final PostProcessorDispatcher postProcessorDispatcher,
            final MetaserviceDescriptor metaserviceDescriptor,
            final MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor,
            final JMSUtil jmsUtil
    ) throws JMSException, RepositoryException {
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
                    if(postProcessorDispatcher.isOkCheapCheck(task, message.getJMSTimestamp())){
                        jmsUtil.executeProducerTask(
                                JMSUtil.Type.QUEUE,
                                getProcessingQueueName(metaserviceDescriptor, postProcessorDescriptor),
                                new JMSUtil.ProducerTask<JMSException>() {
                                    @Override
                                    public void execute(Session session, MessageProducer producer) throws JMSException{
                                        producer.send(m);
                                    }
                                });
                    }
                } catch (JMSException e) {
                    LOGGER.error("JMS Exception", e);
                } catch (PostProcessorException e) {
                    LOGGER.error("PostProcessorException",e);
                }
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
                }
            }
        });
    }

    private String getProcessingQueueName(MetaserviceDescriptor metaserviceDescriptor, MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor) {
        return getEntryQueueName(metaserviceDescriptor,postProcessorDescriptor) +"2";
    }

    private String getEntryQueueName(MetaserviceDescriptor metaserviceDescriptor,MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor){
        return "Consumer." + DescriptorHelper.getStringFromPostProcessor(metaserviceDescriptor.getModuleInfo(), postProcessorDescriptor).replaceAll("\\.", "_").replaceAll(":","-")+ ".VirtualTopic.PostProcess";
    }
}
