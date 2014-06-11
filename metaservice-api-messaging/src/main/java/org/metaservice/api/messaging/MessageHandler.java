package org.metaservice.api.messaging;

import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.messaging.statistics.QueueStatistics;

import java.util.List;

/**
 * Created by ilo on 07.06.2014.
 */
public interface MessageHandler {
    public void send(String s) throws MessagingException;

    void bulkSend(Iterable<PostProcessingTask> postProcessingTasks) throws MessagingException;

    public void send(ArchiveAddress archiveAddress) throws MessagingException;
    public void send(PostProcessingTask postProcessingTask) throws MessagingException;
    public void commit() throws MessagingException;

    public List<QueueStatistics> getStatistics() throws MessagingException;

    void close() throws MessagingException;

    void init() throws MessagingException;
}
