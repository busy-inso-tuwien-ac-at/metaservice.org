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
