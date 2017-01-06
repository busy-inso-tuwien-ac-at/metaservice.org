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

package org.metaservice.core.deb.postprocessor;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.api.postprocessor.PostProcessorSparqlBuilder;
import org.metaservice.api.postprocessor.PostProcessorSparqlQuery;
import org.metaservice.api.rdf.vocabulary.ADMSSW;
import org.metaservice.api.rdf.vocabulary.DEB;
import org.metaservice.api.sparql.buildingcontexts.DefaultSparqlQuery;
import org.metaservice.api.sparql.nodes.BoundVariable;
import org.metaservice.api.sparql.nodes.Variable;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.query.*;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Date;

/**
 * Created by ilo on 24.02.14.
 */
public class ReleasePostProcessor implements PostProcessor {
    public static final Logger LOGGER = LoggerFactory.getLogger(ReleasePostProcessor.class);

    final static String URI_REGEX = "^http://metaservice.org/d/(packages|releases)/([^/#]+)/([^/#]+)/([^/#]+)(/[^/#]+)?$";

    private final ValueFactory valueFactory;
    private final TupleQuery packageQuery;
    private final TupleQuery releaseQuery;

    private final BoundVariable resource = new BoundVariable("resource");
    private final Variable version = new Variable("version");
    private final Variable _package = new Variable("package");
    private final Variable packageName = new Variable("packageName");
    private final Variable metaDistribution = new Variable("metaDistribution");
    private final  Variable graph = new Variable("g");
    private final  Variable graph2 = new Variable("g2");

    @Inject
    public ReleasePostProcessor(
            ValueFactory valueFactory,
            RepositoryConnection repositoryConnection
    ) throws MalformedQueryException, RepositoryException {
        this.valueFactory = valueFactory;


        //quad because we want to have from single source (this is a first level postprocessor) and it optimized better in timescope
        packageQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,
                new PostProcessorSparqlQuery(){
                    @Override
                    public String build() {
                        return select(
                                DISTINCT,
                                var(_package),var(version),var(packageName),var(metaDistribution))
                                .where(
                                        quadPattern(resource, DEB.VERSION, version, graph2),
                                        quadPattern(resource, DEB.PACKAGE_NAME, packageName, graph2),
                                        quadPattern(resource, DEB.META_DISTRIBUTION, metaDistribution, graph2),
                                        quadPattern(resource, RDF.TYPE, DEB.PACKAGE, graph2),
                                        quadPattern(_package, DEB.VERSION, version, graph),
                                        quadPattern(_package, DEB.PACKAGE_NAME, packageName, graph),
                                        quadPattern(_package, DEB.META_DISTRIBUTION, metaDistribution, graph),
                                        quadPattern(_package, RDF.TYPE, DEB.PACKAGE, graph)
                                )
                                .build();
                    }
                }.toString());

        releaseQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,
                new DefaultSparqlQuery(){
                    @Override
                    public String build() {
                        return select(
                                DISTINCT,
                                var(_package),var(version),var(packageName),var(metaDistribution))
                                .where(
                                        triplePattern(resource, ADMSSW.PACKAGE, _package),
                                        triplePattern(resource, RDF.TYPE, DEB.RELEASE),
                                        quadPattern(_package, DEB.VERSION, version, graph),
                                        quadPattern(_package, DEB.PACKAGE_NAME, packageName, graph),
                                        quadPattern(_package, DEB.META_DISTRIBUTION, metaDistribution, graph),
                                        quadPattern(_package, RDF.TYPE, DEB.PACKAGE, graph)
                                )
                                .build();
                    }
                }.toString());
        LOGGER.info("package Query: "+packageQuery.toString());
        LOGGER.info("release Query: "+releaseQuery.toString());
    }

    @Override
    public void process(@NotNull URI uri, @NotNull RepositoryConnection resultConnection, Date time) throws PostProcessorException {
        releaseQuery.setBinding(PostProcessorSparqlBuilder.getDateVariable().toString(),valueFactory.createLiteral(time));
        packageQuery.setBinding(PostProcessorSparqlBuilder.getDateVariable().toString(),valueFactory.createLiteral(time));
        final String distribution = uri.toString().replaceAll(URI_REGEX,"$2");
        final String project = uri.toString().replaceAll(URI_REGEX,"$3");
        final String version = uri.toString().replaceAll(URI_REGEX,"$4");

        try {
            final URI releaseURI= valueFactory.createURI("http://metaservice.org/d/releases/"+distribution+"/"+project+"/" + version);
            TupleQueryResult tupleQueryResult;
            if(uri.toString().startsWith("http://metaservice.org/d/packages"))
            {
                packageQuery.setBinding("resource",uri);
                tupleQueryResult = packageQuery.evaluate();

            }else if(uri.toString().startsWith("http://metaservice.org/d/releases")){
                releaseQuery.setBinding("resource",uri);
                tupleQueryResult = releaseQuery.evaluate();
            }else{
                throw new PostProcessorException("wrong uri");
            }
            if(!tupleQueryResult.hasNext())
                    return;
            while (tupleQueryResult.hasNext()){
                BindingSet bindingSet = tupleQueryResult.next();
                URI packageURI = (URI) bindingSet.getBinding("package").getValue();
                resultConnection.add(packageURI, ADMSSW.RELEASE, releaseURI);
                LOGGER.info("adding {} -> {}",packageURI,releaseURI);
                resultConnection.add(releaseURI, ADMSSW.PACKAGE, packageURI);
            }
            resultConnection.add(releaseURI, RDF.TYPE, DEB.RELEASE);
            resultConnection.add(releaseURI, DEB.META_DISTRIBUTION,valueFactory.createLiteral(distribution));
            resultConnection.add(releaseURI, DEB.VERSION,valueFactory.createLiteral(version));
            resultConnection.add(releaseURI, DEB.PACKAGE_NAME, valueFactory.createLiteral(project));
            resultConnection.add(releaseURI, RDFS.LABEL, valueFactory.createLiteral(project +" " + version));
        } catch (RepositoryException | QueryEvaluationException e) {
            throw new PostProcessorException(e);
        }
    }

    @Override
    public boolean abortEarly(@NotNull URI uri) throws PostProcessorException {
        return !uri.stringValue().matches(URI_REGEX);
    }
}
