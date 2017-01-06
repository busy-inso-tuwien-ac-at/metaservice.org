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

import com.google.common.base.Optional;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.api.rdf.vocabulary.METASERVICE;
import org.metaservice.core.AbstractDispatcher;
import org.metaservice.core.dispatcher.MetaserviceSimplePipe;
import org.metaservice.core.postprocessor.PostProcessorDispatcher;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
* Created by ilo on 23.07.2014.
*/
public class DetermineGeneratedStatementsPipe extends MetaserviceSimplePipe<PostProcessorDispatcher.Context,PostProcessorDispatcher.Context> {
    private final PostProcessor postProcessor;
    private final ValueFactory valueFactory;
    private final LoadedStatements loadedStatements;

    @Inject
    public DetermineGeneratedStatementsPipe(
            PostProcessor postProcessor,
            ValueFactory valueFactory,
            Logger logger,
            LoadedStatements loadedStatements) {
        super(logger);
        this.postProcessor = postProcessor;
        this.valueFactory = valueFactory;
        this.loadedStatements = loadedStatements;
    }

    @Override
    public Optional<PostProcessorDispatcher.Context> process(PostProcessorDispatcher.Context context) throws Exception {
        context.generatedStatements  = AbstractDispatcher.getGeneratedStatements(context.resultConnection, loadedStatements.getStatements());
        context.subjects = AbstractDispatcher.getSubjects(context.generatedStatements);
        context.processableSubjects = getProcessableSubjects(context.subjects);
        context.objects = AbstractDispatcher.getURIObject(context.generatedStatements);
        if(context.generatedStatements.size() == 0){
            LOGGER.info("NO STATEMENTS GENERATED! -> adding empty statement");
            context.generatedStatements.add(valueFactory.createStatement(context.task.getChangedURI(), METASERVICE.DUMMY,METASERVICE.DUMMY,null));
            context.processableSubjects.add(context.task.getChangedURI());
        }
        return Optional.of(context);
    }
    private Set<URI> getProcessableSubjects(Set<URI> subjects) {
        Set<URI> resultSet = new HashSet<>();
        for(URI subject: subjects){
            try {
                if(!postProcessor.abortEarly(subject)){
                    resultSet.add(subject);
                }
            } catch (PostProcessorException ignored) {
            }
        }
        return resultSet;
    }
}
