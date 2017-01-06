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

package org.metaservice.core.dispatcher.postprocessor;

import com.google.common.base.Optional;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.core.AbstractDispatcher;
import org.metaservice.core.dispatcher.MetaserviceSimplePipe;
import org.metaservice.core.postprocessor.PostProcessorDispatcher;
import org.openrdf.model.URI;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.Update;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryConnection;
import org.slf4j.Logger;

import javax.inject.Inject;

/**
* Created by ilo on 23.07.2014.
*/
public class DropExistingGraphsPipe extends MetaserviceSimplePipe<PostProcessorDispatcher.Context,PostProcessorDispatcher.Context> {
    private final RepositoryConnection repositoryConnection;

    @Inject
    public DropExistingGraphsPipe(RepositoryConnection repositoryConnection, Logger logger) {
        super(logger);
        this.repositoryConnection = repositoryConnection;
    }

    @Override
    public Optional<PostProcessorDispatcher.Context> process(PostProcessorDispatcher.Context c) throws Exception {
        //todo deleted subjects should also be notified, an additional query is needed
        AbstractDispatcher.recoverSparqlConnection(repositoryConnection);
        for(URI context : c.existingGraphs){
            try {
                LOGGER.warn("Dropping Graph {}", context);
                Update dropGraphUpdate = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL,"DROP GRAPH <"+context.toString()+">");
                //  dropGraphUpdate.setBinding("graph", context);
                dropGraphUpdate.execute();
            } catch (MalformedQueryException e) {
                LOGGER.error("Could not parse drop query", e);
            }catch (UpdateExecutionException e) {
                LOGGER.error("Couldn't drop graph {}", e);
                throw new PostProcessorException(e);
            }
        }
        return Optional.of(c);
    }
}
