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

package org.metaservice.api.messaging;

import org.jetbrains.annotations.NotNull;
import org.openrdf.model.URI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ilo on 17.01.14.
 */
public class PostProcessingTask implements Serializable {
    private static final long serialVersionUID = -13895398349827644l;

    private PostProcessingTask(){}
    public PostProcessingTask(@NotNull URI changedURI,@NotNull Date time) {
        this.changedURI = changedURI;
        this.time = time;
    }

    @NotNull
    public URI getChangedURI() {
        return changedURI;
    }

    @NotNull
    public Date getTime() {return  time;}
    @NotNull
    public ArrayList<PostProcessingHistoryItem> getHistory() {
        return history;
    }

    private  URI changedURI;
    private  Date time;
    private  ArrayList<PostProcessingHistoryItem> history = new ArrayList<>();

    @Override
    public String toString() {
        return "PostProcessingTask{" +
                "changedURI=" + changedURI +
                ", time=" + time +
                '}';
    }
}
