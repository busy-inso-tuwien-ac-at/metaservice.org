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

package org.metaservice.core.deb;

import ch.qos.logback.classic.LoggerContext;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import org.metaservice.api.postprocessor.PostProcessorSparqlQuery;
import org.metaservice.api.postprocessor.PostProcessorTimeSparqlQuery;
import org.metaservice.api.rdf.vocabulary.ADMSSW;
import org.metaservice.api.rdf.vocabulary.DC;
import org.metaservice.api.rdf.vocabulary.DEB;
import org.metaservice.api.rdf.vocabulary.SKOS;
import org.metaservice.api.sparql.buildingcontexts.DefaultSparqlQuery;
import org.metaservice.api.sparql.nodes.BoundVariable;
import org.metaservice.api.sparql.nodes.Variable;
import org.metaservice.core.injection.MetaserviceTestModule;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.query.*;
import org.openrdf.repository.RepositoryConnection;
import org.slf4j.LoggerFactory;

/**
 * Created by ilo on 13.03.14.
 */
public class QueryTest {
    private RepositoryConnection repositoryConnection;
    private final BoundVariable resource = new BoundVariable("resource");
    private final Variable version = new Variable("version");
    private final Variable _package = new Variable("package");
    private final Variable packageName = new Variable("packageName");
    private final Variable metaDistribution = new Variable("metaDistribution");
    private final  Variable graph = new Variable("g");
    private final  Variable graph2 = new Variable("g2");

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new MetaserviceTestModule());
        repositoryConnection = injector.getInstance(RepositoryConnection.class);

    }

    @Test
    public void daExample() throws Exception{
        final Variable a     = new Variable("a"),
                title = new Variable("title"),
                alt   = new Variable("alt");
        DefaultSparqlQuery query = new PostProcessorTimeSparqlQuery(){public String build() {
            return

                    select(DISTINCT,var(a),var(title))
                            .where(
                                    triplePattern(a, RDF.TYPE, ADMSSW.SOFTWARE_PROJECT),
                                    triplePattern(a, SKOS.ALT_LABEL, alt),
                                    union(
                                            graphPattern(
                                                    triplePattern(a, DC.TITLE, title)
                                            ),
                                            graphPattern(
                                                    triplePattern(a, RDFS.LABEL, title)
                                            ),
                                            graphPattern(
                                                    triplePattern(a, SKOS.PREF_LABEL,title)
                                            )
                                    ),
                                    filter(unequal(val(alt),val(title)))
                            )

                            .build(true);
        }};
        System.err.println(query.build());
    }

    @Test
    public void testFoo() throws Exception {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.stop();
        TupleQuery packageQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,
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

        TupleQuery releaseQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,
                new DefaultSparqlQuery(){
                    @Override
                    public String build() {
                        return select(
                                DISTINCT,
                                var(_package),var(version),var(packageName),var(metaDistribution))
                                .where(
                                        triplePattern(resource, ADMSSW.PACKAGE, _package),
                                        triplePattern(resource, RDF.TYPE, ADMSSW.SOFTWARE_RELEASE),
                                        quadPattern(_package, DEB.VERSION, version, graph),
                                        quadPattern(_package, DEB.PACKAGE_NAME, packageName, graph),
                                        quadPattern(_package, DEB.META_DISTRIBUTION, metaDistribution, graph),
                                        quadPattern(_package, RDF.TYPE, DEB.PACKAGE, graph)
                                )
                                .build();
                    }
                }.toString());


        packageQuery.setBinding("resource",repositoryConnection.getValueFactory().createURI("http://metaservice.org/d/packages/ubuntu/libc6/2.13-20ubuntu5/amd64"));
        releaseQuery.setBinding("resource", repositoryConnection.getValueFactory().createURI("http://metaservice.org/d/releases/ubuntu/libc6/2.13-20ubuntu5"));

        packageQuery.evaluate(new TupleQueryResultHandlerBase() {
            @Override
            public void handleSolution(BindingSet bindingSet) throws TupleQueryResultHandlerException {
                System.err.println(bindingSet);
            }
        });

        releaseQuery.evaluate(new TupleQueryResultHandlerBase() {
            @Override
            public void handleSolution(BindingSet bindingSet) throws TupleQueryResultHandlerException {
                System.err.println(bindingSet);
            }
        });

        TupleQuery combined = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,
                new DefaultSparqlQuery(){
                    @Override
                    public String build() {
                        return select(
                                DISTINCT,
                                var(_package),var(version),var(packageName),var(metaDistribution))
                                .where(
                                        union(
                                                graphPattern(
                                                        select(DISTINCT,var(_package),var(packageName), var(metaDistribution), var(version)
                                                        ).where(
                                                                triplePattern(resource, ADMSSW.PACKAGE, _package),
                                                                triplePattern(resource, RDF.TYPE, ADMSSW.SOFTWARE_RELEASE),
                                                                quadPattern(_package, DEB.VERSION, version, graph2),
                                                                quadPattern(_package, DEB.PACKAGE_NAME, packageName, graph2),
                                                                quadPattern(_package, DEB.META_DISTRIBUTION, metaDistribution, graph2),
                                                                quadPattern(_package, RDF.TYPE, DEB.PACKAGE, graph2),
                                                                quadPattern(_package, DEB.VERSION, version, graph),
                                                                quadPattern(_package, DEB.PACKAGE_NAME, packageName, graph),
                                                                quadPattern(_package, DEB.META_DISTRIBUTION, metaDistribution, graph),
                                                                quadPattern(_package, RDF.TYPE, DEB.PACKAGE, graph)
                                                        )
                                                ),
                                                graphPattern(
                                                        select(DISTINCT,var(_package), var(packageName), var(metaDistribution), var(version)
                                                        ).where(
                                                                quadPattern(resource, DEB.VERSION, version, graph2),
                                                                quadPattern(resource, DEB.PACKAGE_NAME, packageName, graph2),
                                                                quadPattern(resource, DEB.META_DISTRIBUTION, metaDistribution, graph2),
                                                                quadPattern(resource, RDF.TYPE, DEB.PACKAGE, graph2),
                                                                quadPattern(_package, DEB.VERSION, version, graph),
                                                                quadPattern(_package, DEB.PACKAGE_NAME, packageName, graph),
                                                                quadPattern(_package, DEB.META_DISTRIBUTION, metaDistribution, graph),
                                                                quadPattern(_package, RDF.TYPE, DEB.PACKAGE, graph)
                                                        )
                                                ))

                                            )
                                .build();
                    }
                }.toString());

        combined.setBinding("resource",repositoryConnection.getValueFactory().createURI("http://metaservice.org/d/packages/ubuntu/libc6/2.13-20ubuntu5/amd64"));
        System.err.println(combined);
        combined.evaluate(new TupleQueryResultHandlerBase() {
            @Override
            public void handleSolution(BindingSet bindingSet) throws TupleQueryResultHandlerException {
                System.err.println(bindingSet);
            }
        });
        combined.setBinding("resource",repositoryConnection.getValueFactory().createURI("http://metaservice.org/d/releases/ubuntu/libc6/2.13-20ubuntu5"));

        combined.evaluate(new TupleQueryResultHandlerBase() {
            @Override
            public void handleSolution(BindingSet bindingSet) throws TupleQueryResultHandlerException {
                System.err.println(bindingSet);
            }
        });


        System.err.println(combined);

    }
}
