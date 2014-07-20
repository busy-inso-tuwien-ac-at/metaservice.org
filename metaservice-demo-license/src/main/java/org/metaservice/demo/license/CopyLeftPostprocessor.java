package org.metaservice.demo.license;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.api.postprocessor.PostProcessorSparqlBuilder;
import org.metaservice.api.postprocessor.PostProcessorSparqlQuery;
import org.metaservice.api.rdf.vocabulary.DOAP;
import org.metaservice.api.rdf.vocabulary.LIC;
import org.metaservice.api.rdf.vocabulary.METASERVICE_SWDEP;
import org.metaservice.api.sparql.nodes.BoundVariable;
import org.metaservice.api.sparql.nodes.Variable;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.*;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sparql.query.QueryStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Date;

/**
 * Created by ilo on 13.07.2014.
 */
public class CopyLeftPostprocessor implements PostProcessor {
    public static final Logger LOGGER = LoggerFactory.getLogger(CopyLeftPostprocessor.class);
    final BoundVariable resourceVariable = new BoundVariable("resource");
    final Variable resourceLicense = new Variable("resourceLicense");
    final Variable dependency = new Variable("dependency");
    final Variable dependencySame = new Variable("dependencySame");
    final BoundVariable dependencyLicense = new BoundVariable("dependencyLicense");
    final BoundVariable dependencyRelation = new BoundVariable("dependencyRelation");
    final Variable collection = new Variable("collection");


    final TupleQuery directQuery;

    @Inject
    public CopyLeftPostprocessor(RepositoryConnection repositoryConnection) throws PostProcessorException {
        try {

            PostProcessorSparqlQuery postProcessorSparqlQuery = new PostProcessorSparqlQuery() {
                @Override
                public String build() {
                    return select(true, var(resourceLicense), var(dependency), var(dependencyLicense))
                            .where(
                                    filter(unequal(val(resourceLicense), val(dependencyLicense))),
                                    triplePattern(resourceVariable, DOAP.LICENSE, resourceLicense),
                                    union(graphPattern(
                                            triplePattern(resourceVariable, METASERVICE_SWDEP.LINKS, dependency),
                                            union(
                                                    graphPattern(triplePattern(dependency, DOAP.LICENSE, dependencyLicense)),
                                                    graphPattern(triplePattern(dependency, OWL.SAMEAS, dependencySame), triplePattern(dependencySame, DOAP.LICENSE, dependencyLicense))
                                            )
                                    ), graphPattern(
                                            triplePattern(resourceVariable, METASERVICE_SWDEP.LINKS, collection),
                                            triplePattern(collection, RDF.TYPE, METASERVICE_SWDEP.ANY_ONE_OF_SOFTWARE),
                                            triplePattern(collection, RDF.LI, dependency),
                                            union(
                                                    graphPattern(triplePattern(dependency, DOAP.LICENSE, dependencyLicense)),
                                                    graphPattern(triplePattern(dependency, OWL.SAMEAS, dependencySame), triplePattern(dependencySame, DOAP.LICENSE, dependencyLicense))
                                            )
                                    ))
                            )
                            .build();
                }
            };
            directQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,postProcessorSparqlQuery.build());
        } catch (RepositoryException | MalformedQueryException e) {
            throw new PostProcessorException(e);
        }

    }

    @Override
    public void process(@NotNull URI uri, @NotNull RepositoryConnection repositoryConnection, Date time) throws PostProcessorException {
        ValueFactory valueFactory = repositoryConnection.getValueFactory();
        directQuery.setBinding(PostProcessorSparqlBuilder.getDateVariable().toString(), valueFactory.createLiteral(time));
        checkCopyLeft(uri,SPDX_LICENSE.GPL_2_0,METASERVICE_SWDEP.LINKS,repositoryConnection,"Linking with Software which is licensed under the GPLv2, but is not licensed by GPLv2");
    }


    public void checkCopyLeft(URI resource,URI license, URI relation,RepositoryConnection repositoryConnection,String description) throws PostProcessorException {
        try {
            ValueFactory valueFactory = repositoryConnection.getValueFactory();
            directQuery.setBinding(resourceVariable.stringValue(),resource);
            directQuery.setBinding(dependencyLicense.stringValue(),license);
            directQuery.setBinding(dependencyRelation.stringValue(),relation);
            System.err.println(QueryStringUtil.getQueryString(directQuery.toString(), directQuery.getBindings()));
            TupleQueryResult result = directQuery.evaluate();
            while(result.hasNext()){
                BindingSet bindingSet = result.next();
                URI dep = (URI)bindingSet.getValue(dependency.toString());
                LOGGER.info(dep.toString());
                URI uri = valueFactory.createURI(resource.toString() + "#LicensingViolation" + dep.getLocalName());
                repositoryConnection.add(resource, LIC.LICENSING_VIOLATION,uri);
                repositoryConnection.add(uri,RDF.TYPE,LIC.SUSPECTED_VIOLATION);
                repositoryConnection.add(uri,LIC.CONFLICTING,dep);
                repositoryConnection.add(uri,LIC.DESCRIPTION,valueFactory.createLiteral(description));
            }
        } catch (QueryEvaluationException|RepositoryException e) {
            throw new PostProcessorException(e);
        }
    }

    @Override
    public boolean abortEarly(@NotNull URI uri) throws PostProcessorException {
        return !uri.toString().startsWith("http://metaservice.org/d/");
    }
}
