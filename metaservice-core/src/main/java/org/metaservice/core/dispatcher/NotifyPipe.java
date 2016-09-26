/*
 * Copyright 2015 Nikola Ilo
 * Research Group for Industrial Software, Vienna University of Technology, Austria
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

package org.metaservice.core.dispatcher;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.MessageHandler;
import org.metaservice.api.messaging.MessagingException;
import org.metaservice.api.messaging.PostProcessingHistoryItem;
import org.metaservice.api.messaging.PostProcessingTask;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.core.postprocessor.PostProcessorDispatcher;
import org.openrdf.model.URI;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
* Created by ilo on 23.07.2014.
*/
public class NotifyPipe extends MetaserviceSimplePipe<PostProcessorDispatcher.Context,PostProcessorDispatcher.Context> {
    private final MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor;
    private final MessageHandler messageHandler;

    @Inject
    public NotifyPipe(MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor, Logger logger, MessageHandler messageHandler) throws MessagingException {
        super(logger);
        this.postProcessorDescriptor = postProcessorDescriptor;
        this.messageHandler = messageHandler;
        this.messageHandler.init();
    }

    @Override
    public Optional<PostProcessorDispatcher.Context> process(PostProcessorDispatcher.Context context) throws PostProcessorException {
        Set<URI> resourcesThatChanged = Sets.union(context.subjects, context.objects);
        List<PostProcessingHistoryItem> oldHistory = context.task.getHistory();
        Date time = context.task.getTime();
        Set<URI> affectedProcessableSubjects = context.processableSubjects;
        notifyPostProcessors(resourcesThatChanged, oldHistory, time, postProcessorDescriptor, affectedProcessableSubjects);
        return Optional.of(context);
    }

    public void notifyPostProcessors(@NotNull Set<URI> resourcesThatChanged, @NotNull List<PostProcessingHistoryItem> oldHistory, @NotNull Date time, @Nullable MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor, @Nullable Set<URI> affectedProcessableSubjects) {
        LOGGER.debug("START NOTIFICATION OF POSTPROCESSORS");
        //todo only let postprocessing happen for date = or >
        // i think it would be enough to query for ech subject the next existing date and let them recalculate
        ArrayList<PostProcessingHistoryItem> history = new ArrayList<>();
        history.addAll(oldHistory);

        if (affectedProcessableSubjects != null) {
            if (postProcessorDescriptor != null) {
                PostProcessingHistoryItem now = new PostProcessingHistoryItem(postProcessorDescriptor.getId(), affectedProcessableSubjects.toArray(new URI[affectedProcessableSubjects.size()]));
                history.add(now);
            }
        }
        try {
            if (resourcesThatChanged.size() == 0) {
                LOGGER.info("nothing to notify");
            }
            if (resourcesThatChanged.size() < 10) {
                for (URI uri : resourcesThatChanged) {
                    LOGGER.info("changed {}", uri);
                    PostProcessingTask postProcessingTask = new PostProcessingTask(uri, time);
                    postProcessingTask.getHistory().addAll(history);
                    messageHandler.send(postProcessingTask);
                }
            } else {
                LOGGER.debug("bulk change of {} objects ", resourcesThatChanged.size());
                ArrayList<PostProcessingTask> postProcessingTasks = new ArrayList<>();
                for (URI uri : resourcesThatChanged) {
                    LOGGER.trace("changed {}", uri);
                    PostProcessingTask postProcessingTask = new PostProcessingTask(uri, time);
                    postProcessingTask.getHistory().addAll(history);
                    postProcessingTasks.add(postProcessingTask);
                }
                messageHandler.bulkSend(postProcessingTasks);

            }
        } catch (MessagingException e) {
            LOGGER.error("Couldn't notify PostProcessors", e);
        }
        LOGGER.debug("STOP NOTIFICATION OF POSTPROCESSORS");
    }
}
