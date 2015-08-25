package org.metaservice.kryo;

import org.metaservice.api.messaging.statistics.QueueStatistics;

import java.util.List;

/**
 * Created by ilo on 09.06.2014.
 */
public interface StatisticsProvider {
    List<QueueStatistics> getQueueStatistics();
}
