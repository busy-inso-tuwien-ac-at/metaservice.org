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
