package org.metaservice.core.nist.cpe;

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
import org.openrdf.model.impl.ValueFactoryImpl;
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
 * Created by ilo on 22.06.2014.
 */

public class CPEReleaseLinkPostProcessor implements PostProcessor {
    private final static Logger LOGGER = LoggerFactory.getLogger(CPEReleaseLinkPostProcessor.class);

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
    TupleQuery cpeQuery;

    private final BoundVariable boundCpe = new BoundVariable("boundCpe");
    private final Variable release = new Variable("release");
    private final Variable revision = new Variable("revision");
    private final Variable productName = new Variable("productName");
    private final Variable project = new Variable("project");

    @Inject
    public CPEReleaseLinkPostProcessor(RepositoryConnection repositoryConnection) throws PostProcessorException {
        try {
            reverseQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,
                    new PostProcessorSparqlQuery(){
                        @Override
                        public String build() {
                            return select(true,var(release))
                                    .where(
                                            triplePattern(release,CPE.IS_ABOUT_REVERSE,boundCpe)
                                    ).build();
                        }
                    }.build());
            cpeQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,
                    new PostProcessorSparqlQuery(){

                        @Override
                        public String build() {
                            return select(true,var(release))
                                    .where(
                                            triplePattern(release, RDF.TYPE, ADMSSW.SOFTWARE_RELEASE),
                                            triplePattern(release, ADMSSW.PROJECT, project),
                                            triplePattern(project, DOAP.NAME, productName),
                                            triplePattern(release, DOAP.REVISION, revision),
                                            triplePattern(boundCpe, RDF.TYPE, CPE.CPE),
                                            triplePattern(boundCpe, CPE.VERSION, revision),
                                            triplePattern(boundCpe, CPE.PRODUCT, productName)
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
        cpeQuery.setBinding(PostProcessorSparqlBuilder.getDateVariable().stringValue(), valueFactory.createLiteral(time));
        cpeQuery.setBinding(boundCpe.stringValue(),uri);
        System.err.println(QueryStringUtil.getQueryString(cpeQuery.toString(), cpeQuery.getBindings()));
        try {
            TupleQueryResult result = cpeQuery.evaluate();
            while(result.hasNext()){
                BindingSet bindingSet = result.next();
                LOGGER.info("adding -> {}" , bindingSet.getValue(release.stringValue()));
                LOGGER.error(bindingSet.getValue(release.stringValue()).getClass().getSimpleName());
                resultConnection.add(uri,CPE.IS_ABOUT,bindingSet.getValue(release.stringValue()));
            }
        } catch (RepositoryException| QueryEvaluationException e) {
            throw new PostProcessorException(e);
        }
        reverseQuery.setBinding(PostProcessorSparqlBuilder.getDateVariable().stringValue(), valueFactory.createLiteral(time));
        reverseQuery.setBinding(boundCpe.stringValue(),uri);
        try {
            TupleQueryResult result = reverseQuery.evaluate();
            while(result.hasNext()){
                BindingSet bindingSet = result.next();
                LOGGER.info("adding -> {}" , bindingSet.getValue(release.stringValue()));
                LOGGER.error(bindingSet.getValue(release.stringValue()).getClass().getSimpleName());
                resultConnection.add(uri,CPE.IS_ABOUT,bindingSet.getValue(release.stringValue()));
            }
        } catch (RepositoryException| QueryEvaluationException e) {
            throw new PostProcessorException(e);
        }


    }

    @Override
    public boolean abortEarly(@NotNull URI uri) throws PostProcessorException {
        return !uri.toString().contains("wordpress") || !uri.stringValue().startsWith("http://metaservice.org/d/releases/") ||!uri.stringValue().contains("/cpe/");
    }
}
