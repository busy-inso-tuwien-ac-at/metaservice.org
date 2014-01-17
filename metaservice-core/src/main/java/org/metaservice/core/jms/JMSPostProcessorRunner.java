package org.metaservice.core.jms;

import com.google.inject.Injector;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.core.injection.InjectorFactory;
import org.metaservice.core.postprocessor.PostProcessingHistoryItem;
import org.metaservice.core.postprocessor.PostProcessingTask;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jms.*;

public class JMSPostProcessorRunner extends AbstractJMSRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(JMSPostProcessorRunner.class);
    private final PostProcessor postProcessor;
    private final MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor;

    public static void main(String[] args) throws JMSException {
        if(args.length != 1)
        {
            LOGGER.error("Need postprocessor id");
            System.exit(-1);
        }
        String id = args[0];
        Injector injector = InjectorFactory.getInjectorForPostProcessor(id);
        JMSPostProcessorRunner runner = injector.getInstance(JMSPostProcessorRunner.class);
        runner.run();
    }


    @Inject
    private JMSPostProcessorRunner(
            PostProcessor postProcessor,
            MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor,
            ConnectionFactory connectionFactory
    ) throws JMSException, RepositoryException {
        super(connectionFactory);
        this.postProcessor = postProcessor;
        this.postProcessorDescriptor = postProcessorDescriptor;
        initQueue("Consumer." + getClass().getName() + ".VirtualTopic.PostProcess");
    }

    @Override
    public void onMessage(Message message) {
        try {
            ObjectMessage m = (ObjectMessage) message;
            PostProcessingTask task = (PostProcessingTask) m.getObject();
            for(PostProcessingHistoryItem item  :task.getHistory()){
                if(item.getPostprocessorId().equals(postProcessorDescriptor.getId()) && item.getResource().equals(task.getChangedURI())){
                    LOGGER.info("Not processing, because did already process");
                    return;
                }
            }
            if(!postProcessor.abortEarly(task.getChangedURI())){
                postProcessor.process(task.getChangedURI());
            }
        } catch (JMSException|PostProcessorException e) {
            LOGGER.error("",e);
        }
    }
}
