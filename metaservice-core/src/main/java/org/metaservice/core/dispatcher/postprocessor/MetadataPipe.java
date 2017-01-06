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
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.descriptors.DescriptorHelper;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.rdf.vocabulary.METASERVICE;
import org.metaservice.core.AbstractDispatcher;
import org.metaservice.core.dispatcher.MetaserviceSimplePipe;
import org.metaservice.core.postprocessor.PostProcessorDispatcher;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.RepositoryConnection;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Date;

/**
* Created by ilo on 23.07.2014.
*/
public class MetadataPipe extends MetaserviceSimplePipe<PostProcessorDispatcher.Context,PostProcessorDispatcher.Context> {
    private final RepositoryConnection repositoryConnection;
    private final PostProcessor postProcessor;
    private final ValueFactory valueFactory;
    private final DescriptorHelper descriptorHelper;
    private final MetaserviceDescriptor metaserviceDescriptor;
    private final MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor;

    @Inject
    public MetadataPipe(RepositoryConnection repositoryConnection, PostProcessor postProcessor, ValueFactory valueFactory, DescriptorHelper descriptorHelper, MetaserviceDescriptor metaserviceDescriptor, MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor, Logger logger) {
        super(logger);
        this.repositoryConnection = repositoryConnection;
        this.postProcessor = postProcessor;
        this.valueFactory = valueFactory;
        this.descriptorHelper = descriptorHelper;
        this.metaserviceDescriptor = metaserviceDescriptor;
        this.postProcessorDescriptor = postProcessorDescriptor;
    }

    @Override
    public Optional<PostProcessorDispatcher.Context> process(PostProcessorDispatcher.Context context) throws Exception {
        AbstractDispatcher.recoverSparqlConnection(repositoryConnection);
        Date now = new Date();
        //todo uniqueness in uri necessary
        URI metadata = valueFactory.createURI("http://metaservice.org/m/" + postProcessor.getClass().getSimpleName() + "/" + System.currentTimeMillis());
        repositoryConnection.begin();
        repositoryConnection.add(metadata, RDF.TYPE, METASERVICE.OBSERVATION, metadata);
        repositoryConnection.add(metadata, RDF.TYPE, METASERVICE.CONTINUOUS_OBSERVATION, metadata);
        repositoryConnection.add(metadata, METASERVICE.DATA_TIME, valueFactory.createLiteral(context.task.getTime()),metadata); //todo fix it to be based on the used data
        repositoryConnection.add(metadata, METASERVICE.CREATION_TIME, valueFactory.createLiteral(now),metadata);
        repositoryConnection.add(metadata, METASERVICE.LAST_CHECKED_TIME, valueFactory.createLiteral(now),metadata);
        repositoryConnection.add(metadata, METASERVICE.GENERATOR, valueFactory.createLiteral(descriptorHelper.getStringFromPostProcessor(metaserviceDescriptor.getModuleInfo(), postProcessorDescriptor)), metadata);
        for(URI processableSubject : context.processableSubjects) {
            repositoryConnection.add(metadata, METASERVICE.AUTHORITIVE_SUBJECT, processableSubject,metadata);
        }
        repositoryConnection.commit();
        context.metadata =  metadata;
        LOGGER.debug("generated and stored metadata URI {}", context.metadata);
        return Optional.of(context);
    }
}
