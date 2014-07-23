package org.metaservice.core.dispatcher.postprocessor;

import com.google.common.base.Optional;
import org.metaservice.api.postprocessor.PostProcessorException;
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
