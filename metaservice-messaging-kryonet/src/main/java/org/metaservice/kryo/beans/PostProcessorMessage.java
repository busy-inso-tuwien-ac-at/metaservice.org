package org.metaservice.kryo.beans;

import org.metaservice.api.messaging.PostProcessingTask;

/**
 * Created by ilo on 07.06.2014.
 */
public class PostProcessorMessage extends AbstractMessage{
    private PostProcessingTask task;

    public PostProcessingTask getTask() {
        return task;
    }

    public void setTask(PostProcessingTask task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "PostProcessorMessage{" +
                "task=" + task +
                '}';
    }
}
