package org.metaservice.core.deb.postprocessor;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.api.rdf.vocabulary.ADMSSW;
import org.metaservice.api.rdf.vocabulary.DOAP;
import org.metaservice.api.rdf.vocabulary.PACKAGE_DEB;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.*;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ilo on 24.02.14.
 */
public abstract class AbstractProjectPostProcessor implements PostProcessor {
    public static final Logger LOGGER = LoggerFactory.getLogger(AbstractProjectPostProcessor.class);

    protected final ValueFactory valueFactory;
    protected final RepositoryConnection repositoryConnection;
    private final TupleQuery releaseQuery;
    private final TupleQuery projectQuery;

    protected AbstractProjectPostProcessor(ValueFactory valueFactory, RepositoryConnection repositoryConnection) throws MalformedQueryException, RepositoryException {
        this.valueFactory = valueFactory;
        this.repositoryConnection = repositoryConnection;
        this.releaseQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,

        "SELECT DISTINCT ?release ?packageName ?metaDistribution " +
                "{ ?resource <"+PACKAGE_DEB.PACKAGE_NAME+"> ?packageName; <"+PACKAGE_DEB.META_DISTRIBUTION+"> ?metaDistribution." +
                "  ?release  <"+PACKAGE_DEB.PACKAGE_NAME+"> ?packageName; <"+PACKAGE_DEB.META_DISTRIBUTION+"> ?metaDistribution;  a  <"+PACKAGE_DEB.RELEASE+">. }");
        projectQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,
                "SELECT DISTINCT ?release ?packageName ?metaDistribution " +
                        "{ ?resource <"+DOAP.RELEASE+"> ?release. " +
                        "  ?release   <"+PACKAGE_DEB.PACKAGE_NAME+"> ?packageName; <"+PACKAGE_DEB.META_DISTRIBUTION+"> ?metaDistribution;  a  <"+PACKAGE_DEB.RELEASE+">. }");
        LOGGER.error(projectQuery.toString());
        LOGGER.error(releaseQuery.toString());
    }

    private String getUriRegex(){
        return "^http://metaservice.org/d/(releases|projects)/"+getDistributionName()+"/([^/#]+)(/[^/#]+)?$";
    }

    protected abstract String getDistributionName();

    @Override
    public void process(@NotNull URI uri, @NotNull RepositoryConnection resultConnection) throws PostProcessorException {
        try {
            String projectName = uri.toString().replaceAll(getUriRegex(),"$2");
            URI projectURI = valueFactory.createURI("http://metaservice.org/d/projects/"+getDistributionName()+"/"+projectName);
            LOGGER.debug(projectURI.toString());
            resultConnection.add(projectURI, RDF.TYPE, PACKAGE_DEB.PROJECT);
            processProject(resultConnection,uri,projectName);
            TupleQueryResult tupleQueryResult;
            if(uri.toString().startsWith("http://metaservice.org/d/releases")){
                LOGGER.debug("releaseQuery");
                releaseQuery.setBinding("resource",uri);
                tupleQueryResult = releaseQuery.evaluate();
            }else if(uri.toString().startsWith("http://metaservice.org/d/projects")){
                LOGGER.debug("projectQuery");
                projectQuery.setBinding("resource",uri);
                tupleQueryResult = projectQuery.evaluate();
            }else
            {
                throw new PostProcessorException("invalid uri " + uri);
            }
            while (tupleQueryResult.hasNext()){
                BindingSet bindingSet = tupleQueryResult.next();
                URI releaseURI = (URI) bindingSet.getBinding("release").getValue();
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
