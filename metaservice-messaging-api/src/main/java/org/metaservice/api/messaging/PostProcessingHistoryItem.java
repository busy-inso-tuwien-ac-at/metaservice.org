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

import org.openrdf.model.URI;

import java.io.Serializable;

/**
 * Created by ilo on 17.01.14.
 */
public class PostProcessingHistoryItem implements Serializable{
    private static final long serialVersionUID = -5274232640077302343L;

    private PostProcessingHistoryItem(){}
    public PostProcessingHistoryItem(String postprocessorId, URI[] resources) {
        this.postprocessorId = postprocessorId;
        this.resources = resources;
    }

    public URI[] getResources() {
        return resources;
    }

    public String getPostprocessorId() {
        return postprocessorId;
    }

    private String postprocessorId;
    private URI[] resources;
}
