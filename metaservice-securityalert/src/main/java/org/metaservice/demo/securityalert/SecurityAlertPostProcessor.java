package org.metaservice.demo.securityalert;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.api.postprocessor.PostProcessorSparqlBuilder;
import org.metaservice.api.postprocessor.PostProcessorSparqlQuery;
import org.metaservice.api.rdf.vocabulary.*;
import org.metaservice.api.sparql.nodes.BoundVariable;
import org.metaservice.api.sparql.nodes.Variable;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.query.*;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sparql.query.QueryStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.management.Notification;
import java.util.Date;

/**
 * Created by ilo on 13.06.2014.
 */
public class SecurityAlertPostProcessor implements PostProcessor {
    private static final Logger LOGGER  = LoggerFactory.getLogger(SecurityAlertPostProcessor.class);

    /**
     * only needs to listen to cves for the following scenarios:
     * - cve gets published
     * - cve gets updated with another cpe
     *
     * this won't catch these cases:
     * - cpe is linked with release
     * -
     */
 private NotificationBackend notificationBackend;
    private TupleQuery tupleQuery;

    private final BoundVariable cve = new BoundVariable("cve");
    private final Variable cpe = new Variable("cpe");
    private final Variable release = new Variable("release");
    private final Variable project = new Variable("project");
    private final Variable projectName = new Variable("projectName");

    @Inject
    SecurityAlertPostProcessor(RepositoryConnection repositoryConnection, NotificationBackend notificationBackend) throws MalformedQueryException, RepositoryException {
        this.notificationBackend = notificationBackend;
        tupleQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,
                new PostProcessorSparqlQuery(){
                    @Override
                    public String build() {
                        return select(true, var(project), var(projectName))
                                .where(
                                        triplePattern(cve, CVE.VULNERABLE_SOFTWARE, cpe),
                                        triplePattern(cpe, CPE.IS_ABOUT, release),
                                        triplePattern(release, ADMSSW.PROJECT, project),
                                        triplePattern(project, RDFS.LABEL, projectName)
                                ).build();
                    }
                }.build());
    }

    @Override
    public void process(@NotNull URI uri, @NotNull RepositoryConnection resultConnection, Date time) throws PostProcessorException {
        ValueFactory valueFactory = resultConnection.getValueFactory();

        //todo what to do with the time here?
        tupleQuery.setBinding(PostProcessorSparqlBuilder.getDateVariable().stringValue(),valueFactory.createLiteral(new Date()));

        try {
            tupleQuery.setBinding(cve.stringValue(),uri);
            System.err.println(QueryStringUtil.getQueryString(tupleQuery.toString(), tupleQuery.getBindings()));
            TupleQueryResult result = tupleQuery.evaluate();
            LOGGER.info("processing");
            while(result.hasNext()){
                BindingSet bindingSet = result.next();
                String name = bindingSet.getBinding(projectName.stringValue()).getValue().stringValue();
                String projectUri = bindingSet.getBinding(project.stringValue()).getValue().stringValue();
                LOGGER.info("processing {} {}",name,projectUri);
                notificationBackend.notify(projectUri,name,uri.toString());
            }
        } catch (QueryEvaluationException e) {
            throw new PostProcessorException(e);
        }
    }

    @Override
    public boolean abortEarly(@NotNull URI uri) throws PostProcessorException {
        return !uri.toString().startsWith("http://metaservice.org/d/reports/cve/");
    }
}
