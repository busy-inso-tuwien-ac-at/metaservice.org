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

package org.metaservice.core.nvd.cpe;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.api.postprocessor.PostProcessorSparqlBuilder;
import org.metaservice.api.postprocessor.PostProcessorSparqlQuery;
import org.metaservice.api.rdf.vocabulary.ADMSSW;
import org.metaservice.api.rdf.vocabulary.CPE;
import org.metaservice.api.rdf.vocabulary.DOAP;
import org.metaservice.api.sparql.nodes.BoundVariable;
import org.metaservice.api.sparql.nodes.Variable;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.*;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Date;

/**
 * Created by ilo on 22.06.2014.
 */

public class ReleaseCPELinkPostProcessor implements PostProcessor {
    private final static Logger LOGGER = LoggerFactory.getLogger(ReleaseCPELinkPostProcessor.class);

    /**
     * todo splitting into two postprocessors
     * the problem with this postprocessor is:
     * one cpe may link multiple releases and one release may match multiple cpes
     * therefore the postprocessor subject partitioning rule is either broken or ridiculously big results are given
     *
     * Workaround: create only one direction and let a second postprocessor generate the other one.
     *
     * workaround problem -> links are only created on update of the cpe
     */
    TupleQuery reverseQuery;
    TupleQuery releaseQuery;

    private final Variable cpe  =new Variable("cpe");
    private final BoundVariable boundRelease = new BoundVariable("boundRelease");
    private final Variable revision = new Variable("revision");
    private final Variable productName = new Variable("productName");
    private final Variable project = new Variable("project");

    @Inject
    public ReleaseCPELinkPostProcessor(RepositoryConnection repositoryConnection) throws PostProcessorException {
        try {
            reverseQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,
                    new PostProcessorSparqlQuery(){
                        @Override
                        public String build() {
                            return select(DISTINCT,var(cpe))
                                    .where(
                                            triplePattern(cpe,CPE.IS_ABOUT,boundRelease)
                                    ).build();
                        }
                    }.build());
            releaseQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,
                    new PostProcessorSparqlQuery(){

                        @Override
                        public String build() {
                            return select(DISTINCT,var(cpe))
                                    .where(
                                            triplePattern(boundRelease, RDF.TYPE, ADMSSW.SOFTWARE_RELEASE),
                                            triplePattern(boundRelease, ADMSSW.PROJECT, project),
                                            triplePattern(project, DOAP.NAME, productName),
                                            triplePattern(boundRelease, DOAP.REVISION, revision),
                                            triplePattern(cpe, RDF.TYPE, CPE.CPE),
                                            triplePattern(cpe, CPE.VERSION, revision),
                                            triplePattern(cpe, CPE.PRODUCT, productName)
                                    ).build();
                        }
                    }.build());

        } catch (RepositoryException | MalformedQueryException e) {
            throw new PostProcessorException(e);
        }
    }

    @Override
    public void process(@NotNull URI uri, @NotNull RepositoryConnection resultConnection, Date time) throws PostProcessorException {
        ValueFactory valueFactory = resultConnection.getValueFactory();
        releaseQuery.setBinding(PostProcessorSparqlBuilder.getDateVariable().stringValue(), valueFactory.createLiteral(time));
        releaseQuery.setBinding(boundRelease.stringValue(),uri);
       // System.err.println(QueryStringUtil.getQueryString(releaseQuery.toString(), releaseQuery.getBindings()));
        try {
            TupleQueryResult result = releaseQuery.evaluate();
            while(result.hasNext()){
                BindingSet bindingSet = result.next();
                LOGGER.info("adding -> {}" , bindingSet.getValue(cpe.stringValue()));
                LOGGER.error(bindingSet.getValue(cpe.stringValue()).getClass().getSimpleName());
                resultConnection.add(uri,CPE.IS_ABOUT_REVERSE,bindingSet.getValue(cpe.stringValue()));
            }
        } catch (RepositoryException| QueryEvaluationException e) {
            throw new PostProcessorException(e);
        }
        reverseQuery.setBinding(PostProcessorSparqlBuilder.getDateVariable().stringValue(), valueFactory.createLiteral(time));
        reverseQuery.setBinding(boundRelease.stringValue(),uri);
        try {
            TupleQueryResult result = reverseQuery.evaluate();
            while(result.hasNext()){
                BindingSet bindingSet = result.next();
                LOGGER.info("adding -> {}" , bindingSet.getValue(cpe.stringValue()));
                LOGGER.error(bindingSet.getValue(cpe.stringValue()).getClass().getSimpleName());
                resultConnection.add(uri,CPE.IS_ABOUT_REVERSE,bindingSet.getValue(cpe.stringValue()));
            }
        } catch (RepositoryException| QueryEvaluationException e) {
            throw new PostProcessorException(e);
        }


    }

    @Override
    public boolean abortEarly(@NotNull URI uri) throws PostProcessorException {
        return !uri.toString().contains("wordpress") || !uri.stringValue().startsWith("http://metaservice.org/d/releases/")|| uri.stringValue().contains("/cpe/");
    }


}
