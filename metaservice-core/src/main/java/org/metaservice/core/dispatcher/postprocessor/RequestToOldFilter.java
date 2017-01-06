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

import org.metaservice.api.MetaserviceException;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.descriptors.DescriptorHelper;
import org.metaservice.api.rdf.vocabulary.METASERVICE;
import org.metaservice.core.AbstractDispatcher;
import org.metaservice.core.dispatcher.MetaserviceFilterPipe;
import org.metaservice.core.postprocessor.PostProcessorDispatcher;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.*;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
* Created by ilo on 23.07.2014.
*/
public class RequestToOldFilter extends MetaserviceFilterPipe<PostProcessorDispatcher.Context> {
    private final TupleQuery graphSelect;
    private final RepositoryConnection repositoryConnection;
    private final ValueFactory valueFactory;
    private final DescriptorHelper descriptorHelper;
    private final MetaserviceDescriptor metaserviceDescriptor;
    private final MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor;

    @Inject
    public RequestToOldFilter(Logger logger, RepositoryConnection repositoryConnection, ValueFactory valueFactory, DescriptorHelper descriptorHelper, MetaserviceDescriptor metaserviceDescriptor, MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor) throws MalformedQueryException, RepositoryException {
        super(logger);
        this.repositoryConnection = repositoryConnection;
        this.valueFactory = valueFactory;
        this.descriptorHelper = descriptorHelper;
        this.metaserviceDescriptor = metaserviceDescriptor;
        this.postProcessorDescriptor = postProcessorDescriptor;
        this.graphSelect = this.repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL, "SELECT DISTINCT ?metadata ?lastchecked { graph ?metadata {?resource ?p ?o}.  ?metadata a <"+ METASERVICE.OBSERVATION+">;  <" + METASERVICE.GENERATOR + "> ?postprocessor; <"+METASERVICE.DATA_TIME+"> ?time; <" + METASERVICE.LAST_CHECKED_TIME+"> ?lastchecked. }");
        LOGGER.trace(graphSelect.toString());
    }

    @Override
    public boolean accept(PostProcessorDispatcher.Context context) {
        try {
            AbstractDispatcher.recoverSparqlConnection(repositoryConnection);

            URI resource = context.task.getChangedURI();
            graphSelect.setBinding("postprocessor",valueFactory.createLiteral(descriptorHelper.getStringFromPostProcessor(metaserviceDescriptor.getModuleInfo(), postProcessorDescriptor)));
            graphSelect.setBinding("resource",resource);
            graphSelect.setBinding("time",valueFactory.createLiteral(context.task.getTime()));
            TupleQueryResult queryResult = graphSelect.evaluate();
            XMLGregorianCalendar newestTime = null;
            while(queryResult.hasNext()){
                BindingSet ser = queryResult.next();
                LOGGER.trace("existing metadata for uri: " + ser.getBinding("metadata"));
                Binding binding =ser.getBinding("lastchecked");
                if(binding == null || binding.getValue() == null){
                    LOGGER.warn("lastchecked was null, ignoring request to old check for {} {}", context.task.getChangedURI(),context.task.getTime());
                    return true;
                }
                LOGGER.trace("data type{}", ((Literal) binding.getValue()).getDatatype());
                XMLGregorianCalendar creationTime =   ((Literal)binding.getValue()).calendarValue();
                if(newestTime == null || creationTime.compare(newestTime) == DatatypeConstants.GREATER)
                    newestTime = creationTime;
            }

            if(newestTime != null){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
                LOGGER.trace("Comparing " + simpleDateFormat.format(newestTime.toGregorianCalendar().getTime()) + " with " + simpleDateFormat.format(new Date(context.messagingTimestamp)));
                if(newestTime.toGregorianCalendar().getTimeInMillis() > context.messagingTimestamp){
                    LOGGER.debug("Ignoring - because request is too old");
                    return false;
                }
            }
        } catch (QueryEvaluationException e) {
            LOGGER.error("Could not check if too old {}", context.task.getChangedURI(), e);
            return true;
        } catch (MetaserviceException e) {
            //todo check this
            LOGGER.warn("check me",e);
            return true;
        }
        return true;
    }
}
