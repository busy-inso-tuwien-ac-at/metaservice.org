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

package org.metaservice.messaging.jms;

import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.messaging.MessagingException;
import org.metaservice.api.messaging.statistics.QueueStatistics;
import org.metaservice.api.messaging.statistics.QueueStatisticsImpl;
import org.metaservice.api.messaging.PostProcessingTask;
import org.metaservice.api.messaging.MessageHandler;
import org.quartz.*;

import javax.inject.Inject;
import javax.jms.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ilo on 07.06.2014.
 */
public class JmsMessageHandler implements MessageHandler {
    private static final String STATISTICSQUEUE = "metaservice.manager.statistics";

    private final MessageProducer postProcessorProducer;
    private final MessageProducer refreshProducer;
    private final Scheduler scheduler;
    private final JMSUtil jmsUtil;
    private JMSUtil.ShutDownHandler activeMQStatisticsShutdownHandler;

    private final MessageProducer crawlerProducer;
    private final Session session;
    private int count =0;
    private final Map<String,QueueStatistics> currentActiveMQStatistics;


    @Inject
    public JmsMessageHandler(
            ConnectionFactory connectionFactory,
            Scheduler scheduler
    ) throws JMSException {
        Connection jmsConnection = connectionFactory.createConnection();
        session = jmsConnection.createSession(true,Session.AUTO_ACKNOWLEDGE);
        this.postProcessorProducer = session.createProducer(session.createTopic("VirtualTopic.PostProcess"));
        this.crawlerProducer  = session.createProducer(session.createTopic("VirtualTopic.Create"));
        this.refreshProducer = session.createProducer(session.createTopic("VirtualTopic.Refresh"));
        this.scheduler = scheduler;
        this.currentActiveMQStatistics = Collections.synchronizedMap(new HashMap<String, QueueStatistics>());
        jmsUtil= new JMSUtil(connectionFactory);
    }

    @Override
    public void send(String s) throws MessagingException {
        sendSerializable(refreshProducer,s);
    }

    @Override
    public void bulkSend(Iterable<PostProcessingTask> postProcessingTasks) throws MessagingException {
        for(PostProcessingTask postProcessingTask : postProcessingTasks){
            send(postProcessingTask);
        }
    }

    @Override
    public void send(ArchiveAddress archiveAddress) throws MessagingException {
        sendSerializable(crawlerProducer,archiveAddress);
    }

    @Override
    public void send(PostProcessingTask postProcessingTask) throws MessagingException {
        sendSerializable(postProcessorProducer,postProcessingTask);
    }

    private void sendSerializable(MessageProducer producer,Serializable object) throws MessagingException {
        try {
            count++;
            ObjectMessage objectMessage = session.createObjectMessage(object);
            //objectMessage.setJMSExpiration(1000*60*15); // 15 min
            producer.send(objectMessage);
            if(count > 100){
                count=0;
                commit();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void commit() throws MessagingException {
        try{
            session.commit();
        }catch (JMSException e){
            throw new MessagingException(e);
        }
    }

    @Override
    public List<QueueStatistics> getStatistics() throws MessagingException {

        try {
            activeMQStatisticsShutdownHandler = jmsUtil.runListener(new JMSUtil.ListenerBean() {
                @Override
                public String getName() {
                    return STATISTICSQUEUE;
                }

                @Override
                public JMSUtil.Type getType() {
                    return JMSUtil.Type.QUEUE;
                }

                @Override
                public void onMessage(Message message) {
                    if(message instanceof MapMessage){
                        Map map = JMSMessageConverter.getMap((MapMessage) message);
                        QueueStatistics queueStatistics = new QueueStatisticsImpl(map.get("destinationName").toString(),  (Long) map.get("size"));
                        currentActiveMQStatistics.put(queueStatistics.getName(), queueStatistics);
                    }
                }
            });
        } catch (JMSException e) {
            throw new MessagingException(e);
        }
        return null;
    }

    @Override
    public void close() throws MessagingException {
        try {
            activeMQStatisticsShutdownHandler.shutdown();
            session.commit();
            session.close();
        } catch (JMSException e) {
            throw new MessagingException(e);
        }
    }

    @Override
    public void init() throws MessagingException {
        //start retrieval of activemq statistics
        JobDetail job = JobBuilder
                .newJob()
                .ofType(ActiveMqStatisticsRequestJob.class)
                .withIdentity("ACTIVEMQ_STATISTICS")
                .build();
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .startNow()
                .forJob(job)
                .withSchedule(SimpleScheduleBuilder
                                .simpleSchedule()
                                .repeatForever()
                                .withIntervalInSeconds(5)
                )
                .build();
        try {
            scheduler.scheduleJob(job,trigger);
        } catch (SchedulerException e) {
            throw new MessagingException(e);
        }
    }


    public static class ActiveMqStatisticsRequestJob implements Job {
        private final JMSUtil jmsUtil;

        @Inject
        public ActiveMqStatisticsRequestJob(JMSUtil jmsUtil) {
            this.jmsUtil = jmsUtil;
        }

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            try {
                jmsUtil.executeProducerTask(JMSUtil.Type.QUEUE,"ActiveMQ.Statistics.Destination.>", new JMSUtil.ProducerTask<JMSException>() {
                    @Override
                    public void execute(Session session, MessageProducer producer) throws JMSException {
                        TextMessage message = session.createTextMessage();
                        message.setJMSReplyTo(session.createQueue(STATISTICSQUEUE));
                        producer.send(message);
                    }
                });
            } catch (JMSException e) {
                throw new JobExecutionException(e);
            }
        }
    }

}
