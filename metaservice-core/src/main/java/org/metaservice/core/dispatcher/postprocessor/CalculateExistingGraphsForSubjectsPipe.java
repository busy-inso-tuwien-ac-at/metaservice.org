package org.metaservice.core.dispatcher.postprocessor;

import com.google.common.base.Optional;
import org.apache.commons.lang3.StringUtils;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.descriptors.DescriptorHelper;
import org.metaservice.api.rdf.vocabulary.METASERVICE;
import org.metaservice.core.dispatcher.MetaserviceSimplePipe;
import org.metaservice.core.postprocessor.PostProcessorDispatcher;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.*;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;

/**
* Created by ilo on 23.07.2014.
*/
public class CalculateExistingGraphsForSubjectsPipe extends MetaserviceSimplePipe<PostProcessorDispatcher.Context,PostProcessorDispatcher.Context> {
    private final RepositoryConnection repositoryConnection;
    private final ValueFactory valueFactory;
    private final DescriptorHelper descriptorHelper;
    private final MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor;
    private final MetaserviceDescriptor metaserviceDescriptor;

    @Inject
    public CalculateExistingGraphsForSubjectsPipe(RepositoryConnection repositoryConnection, ValueFactory valueFactory, Logger logger, DescriptorHelper descriptorHelper, MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor, MetaserviceDescriptor metaserviceDescriptor) {
        super(logger);
        this.repositoryConnection = repositoryConnection;
        this.valueFactory = valueFactory;
        this.descriptorHelper = descriptorHelper;
        this.postProcessorDescriptor = postProcessorDescriptor;
        this.metaserviceDescriptor = metaserviceDescriptor;
    }

    @Override
    public Optional<PostProcessorDispatcher.Context> process(PostProcessorDispatcher.Context context) throws Exception {
        context.existingGraphs = new HashSet<>();
        if (context.processableSubjects.size() == 0)
            return Optional.of(context);
        try {
            StringBuilder builder = new StringBuilder();
            builder
                    .append("SELECT DISTINCT ?metadata {");
            ArrayList<String> uris = new ArrayList<>();

            for(URI uri : context.processableSubjects){
                uris.add("{ ?metadata a <" + METASERVICE.METADATA + ">;"+
                        "  <"+METASERVICE.SOURCE_SUBJECT +"><" + uri.toString() + ">;"+
                        "  <"+METASERVICE.GENERATOR + "> ?postprocessor;"+
                        "  <" + METASERVICE.TIME+"> ?time.}");
            }
            builder.append(StringUtils.join(uris, " UNION")).append("}");

            String query = builder.toString();
            LOGGER.trace(query);
            TupleQuery tupleQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,query);
            tupleQuery.setBinding("postprocessor", valueFactory.createLiteral(descriptorHelper.getStringFromPostProcessor(metaserviceDescriptor.getModuleInfo(), postProcessorDescriptor)));
            tupleQuery.setBinding("time",valueFactory.createLiteral( context.task.getTime()));
            TupleQueryResult result  = tupleQuery.evaluate();


            while(result.hasNext()){
                BindingSet bindings = result.next();
                URI value = (URI) bindings.
                        getValue("metadata");
                if(value==null){
                    LOGGER.error("Couldn't find binding in {}",bindings.getBindingNames());
                }
                context.existingGraphs.add(value);
            }
            LOGGER.debug("Existing Graphs {}", context.existingGraphs);
        } catch (RepositoryException | QueryEvaluationException | MalformedQueryException e) {
            LOGGER.error("ERROR evaluation ", e);
        }
        return Optional.of(context);
    }
}
