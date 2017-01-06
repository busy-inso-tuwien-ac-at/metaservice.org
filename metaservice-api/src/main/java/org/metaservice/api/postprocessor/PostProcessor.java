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

package org.metaservice.api.postprocessor;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.sparql.buildingcontexts.SparqlQuery;
import org.openrdf.model.URI;
import org.openrdf.repository.RepositoryConnection;

import java.util.Date;
import java.util.List;


public interface PostProcessor {
    public void process(@NotNull final URI uri, @NotNull final RepositoryConnection resultConnection, Date time) throws PostProcessorException;

    /**
     * This method should not do the same as process, but be a lightweight check.
     *
     * It is ok to:
     * * Check if uri is processable
     * * ???
     * @param uri
     * @return
     * @throws PostProcessorException
     */
    public boolean abortEarly(@NotNull final URI uri) throws PostProcessorException;

}
