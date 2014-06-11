package org.metaservice.api.messaging.statistics;

/**
 * Created by ilo on 08.06.2014.
 */
public class QueueStatisticsImpl implements QueueStatistics{
    private QueueStatisticsImpl(){}
    public QueueStatisticsImpl(String name, long count) {
        this.name = name;
        this.count = count;
    }

    private String name;
    private long count;
    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getCount() {
        return count;
    }

    @Override
    public String toString() {
        return name + " -> " + count;
    }
}
