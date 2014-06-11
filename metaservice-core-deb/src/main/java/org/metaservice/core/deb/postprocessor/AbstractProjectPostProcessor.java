package org.metaservice.core.deb.postprocessor;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.api.postprocessor.PostProcessorSparqlBuilder;
import org.metaservice.api.postprocessor.PostProcessorSparqlQuery;
import org.metaservice.api.rdf.vocabulary.ADMSSW;
import org.metaservice.api.rdf.vocabulary.DOAP;
import org.metaservice.api.rdf.vocabulary.PACKAGE_DEB;
import org.metaservice.api.sparql.nodes.Variable;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.*;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sparql.query.QueryStringUtil;
import org.openrdf.repository.sparql.query.SPARQLTupleQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by ilo on 24.02.14.
 */
public abstract class AbstractProjectPostProcessor implements PostProcessor {
    public static final Logger LOGGER = LoggerFactory.getLogger(AbstractProjectPostProcessor.class);

    protected final ValueFactory valueFactory;
    protected final RepositoryConnection repositoryConnection;
    private final TupleQuery releaseQuery;
    private final TupleQuery projectQuery;

    private final Variable resource = new Variable("resource");
    private final Variable release = new Variable("release");
    private final Variable packageName = new Variable("packageName");
    private final Variable metaDistribution = new Variable("metaDistribution");

    protected AbstractProjectPostProcessor(ValueFactory valueFactory, RepositoryConnection repositoryConnection) throws MalformedQueryException, RepositoryException {
        this.valueFactory = valueFactory;
        this.repositoryConnection = repositoryConnection;
        this.releaseQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL, new PostProcessorSparqlQuery(){
            @Override
            public String build() {
                return select(
                        true,
                        var(release),var(packageName),var(metaDistribution))
                        .where(
                                triplePattern(resource, PACKAGE_DEB.PACKAGE_NAME, packageName),
                                triplePattern(resource, PACKAGE_DEB.META_DISTRIBUTION,metaDistribution),
                                triplePattern(resource, RDF.TYPE,PACKAGE_DEB.RELEASE),
                                triplePattern(release,PACKAGE_DEB.PACKAGE_NAME,packageName),
                                triplePattern(release, PACKAGE_DEB.META_DISTRIBUTION,metaDistribution),
                                triplePattern(release, RDF.TYPE,PACKAGE_DEB.RELEASE)
                        )
                        .build();
            }
        }.toString());
        projectQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,   new PostProcessorSparqlQuery(){
            @Override
            public String build() {
                return select(
                        true,
                        var(release),var(packageName),var(metaDistribution))
                        .where(
                                triplePattern(resource, DOAP.RELEASE, release),
                                triplePattern(release, PACKAGE_DEB.PACKAGE_NAME, packageName),
                                triplePattern(release, PACKAGE_DEB.META_DISTRIBUTION, metaDistribution),
                                triplePattern(release, RDF.TYPE, PACKAGE_DEB.RELEASE)
                        )
                        .build();
            }
        }.toString());
        LOGGER.info(projectQuery.toString());
        LOGGER.info(releaseQuery.toString());
    }

    private String getUriRegex(){
        return "^http://metaservice.org/d/(releases|projects)/"+getDistributionName()+"/([^/#\n\r]+)(/[^/#\n\r]+)?$";
    }

    protected abstract String getDistributionName();

    @Override
    public void process(@NotNull URI uri, @NotNull RepositoryConnection resultConnection, Date time) throws PostProcessorException {
        try {
            releaseQuery.setBinding(PostProcessorSparqlBuilder.getDateVariable().toString(),valueFactory.createLiteral(time));
            projectQuery.setBinding(PostProcessorSparqlBuilder.getDateVariable().toString(),valueFactory.createLiteral(time));

            String projectName = uri.toString().replaceAll(getUriRegex(),"$2");
            URI projectURI = valueFactory.createURI("http://metaservice.org/d/projects/"+getDistributionName()+"/"+projectName);
            LOGGER.debug(projectURI.toString());
            resultConnection.add(projectURI, RDF.TYPE, PACKAGE_DEB.PROJECT);
            processProject(resultConnection,projectURI,projectName);
            TupleQueryResult tupleQueryResult;
            if(uri.toString().startsWith("http://metaservice.org/d/releases")){
                LOGGER.debug("releaseQuery");
                releaseQuery.setBinding(resource.toString(),uri);
                if(projectURI.toString().contains("openssl")) {
                    System.err.println(Arrays.asList(((SPARQLTupleQuery) releaseQuery).getBindingsArray()));
                    System.err.println(QueryStringUtil.getQueryString(releaseQuery.toString(), releaseQuery.getBindings()));
                    try {
                        Thread.sleep(120000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                tupleQueryResult = releaseQuery.evaluate();
            }else if(uri.toString().startsWith("http://metaservice.org/d/projects")){
                LOGGER.debug("projectQuery");
                projectQuery.setBinding(resource.toString(),uri);
                tupleQueryResult = projectQuery.evaluate();
            }else
            {
                throw new PostProcessorException("invalid uri " + uri);
            }
            while (tupleQueryResult.hasNext()){
                BindingSet bindingSet = tupleQueryResult.next();
                URI releaseURI = (URI) bindingSet.getBinding(release.toString()).getValue();
                LOGGER.info("ADDING " + releaseURI);
                resultConnection.add(releaseURI, ADMSSW.PROJECT,projectURI);
                resultConnection.add(projectURI, DOAP.RELEASE, releaseURI);
                processRelease(resultConnection,projectURI,releaseURI);
            }
        } catch (RepositoryException | QueryEvaluationException e) {
            throw new PostProcessorException(e);
        }
    }

    @Override
    public boolean abortEarly(@NotNull URI uri) throws PostProcessorException {
        return !uri.stringValue().matches(getUriRegex());
    }

    public abstract void processProject(
            @NotNull RepositoryConnection resultConnection,
            @NotNull URI projectURI,
            @NotNull String projectName
    ) throws RepositoryException;

    public abstract void processRelease(
            @NotNull RepositoryConnection resultConnection,
            @NotNull URI projectURI,
            @NotNull URI releaseURI
    ) throws RepositoryException;

}
