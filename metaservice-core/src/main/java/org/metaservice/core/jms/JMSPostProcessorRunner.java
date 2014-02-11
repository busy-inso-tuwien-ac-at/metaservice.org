package org.metaservice.core.jms;

import com.google.inject.Injector;
import org.metaservice.core.injection.InjectorFactory;
import org.metaservice.core.postprocessor.PostProcessingTask;
import org.metaservice.core.postprocessor.PostProcessorDispatcher;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jms.*;

public class JMSPostProcessorRunner extends AbstractJMSRunner {
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
        runner.run();
    }


    @Inject
    private JMSPostProcessorRunner(
            ConnectionFactory connectionFactory,
            final PostProcessorDispatcher postProcessorDispatcher
    ) throws JMSException, RepositoryException {
        super(connectionFactory);
        this.add(new ListenerBean() {
            @Override
            public String getName() {
                return "Consumer." + getClass().getName().replaceAll("\\.", "_") + ".VirtualTopic.PostProcess";
            }

            @Override
            public Type getType() {
                return Type.QUEUE;
            }

            @Override
            public void onMessage(Message message) {
                try  {
                    ObjectMessage m = (ObjectMessage) message;
                    PostProcessingTask task = (PostProcessingTask) m.getObject();
                    postProcessorDispatcher.process(task,message.getJMSTimestamp());
                } catch (JMSException e) {
                    LOGGER.error("JMS Exception",e);
                }
            }
        });
    }
}
