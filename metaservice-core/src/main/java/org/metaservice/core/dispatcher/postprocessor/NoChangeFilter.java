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

package org.metaservice.core.dispatcher.postprocessor;

import org.metaservice.api.rdf.vocabulary.METASERVICE;
import org.metaservice.core.AbstractDispatcher;
import org.metaservice.core.dispatcher.MetaserviceFilterPipe;
import org.metaservice.core.postprocessor.PostProcessorDispatcher;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* Created by ilo on 23.07.2014.
*/
public class NoChangeFilter extends MetaserviceFilterPipe<PostProcessorDispatcher.Context> {
    private final RepositoryConnection repositoryConnection;
    private final ValueFactory valueFactory;

    @Inject
    public NoChangeFilter(RepositoryConnection repositoryConnection, ValueFactory valueFactory, Logger logger) {
        super(logger);
        this.repositoryConnection = repositoryConnection;
        this.valueFactory = valueFactory;
    }

    @Override
    public boolean accept(PostProcessorDispatcher.Context context) throws Exception {
        AbstractDispatcher.recoverSparqlConnection(repositoryConnection);
        if(context.existingGraphs.size() == 1){
            URI graph = context.existingGraphs.iterator().next();
            if(contentsUnchanged(graph, context.generatedStatements)){
                LOGGER.info("There was no change, only updating last checked time");
                repositoryConnection.remove(graph, METASERVICE.LAST_CHECKED_TIME,null);
                repositoryConnection.add(graph,METASERVICE.LAST_CHECKED_TIME,valueFactory.createLiteral(new Date()));
                return false;
            }
        }
        return true;
    }

    private boolean contentsUnchanged(URI metadataUri, List<Statement> generatedStatements) throws RepositoryException {
        Set<Statement> currentValues = new HashSet<>();
        Set<Statement> newValues = new HashSet<>();
        RepositoryResult<Statement> fullQuery = repositoryConnection.getStatements(null, null, null, false, metadataUri);
        while (fullQuery.hasNext()){
            Statement statement = fullQuery.next();
            if(!statement.getSubject().equals(metadataUri)){
                currentValues.add(statement);
            }
        }
        newValues.addAll(generatedStatements);
        return newValues.equals(currentValues);
    }

}
