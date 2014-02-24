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

/**
 * Created by ilo on 24.02.14.
 */
public abstract class AbstractProjectPostProcessor implements PostProcessor {
    protected final ValueFactory valueFactory;
    protected final RepositoryConnection repositoryConnection;
    private final TupleQuery releaseQuery;
    private final TupleQuery projectQuery;

    protected AbstractProjectPostProcessor(ValueFactory valueFactory, RepositoryConnection repositoryConnection) throws MalformedQueryException, RepositoryException {
        this.valueFactory = valueFactory;
        this.repositoryConnection = repositoryConnection;
        this.releaseQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,

        "SELECT DISTINCT ?package ?version ?packageName ?metaDistribution " +
                "{ ?resource <"+PACKAGE_DEB.PACKAGE_NAME+"> ?packageName; <"+PACKAGE_DEB.META_DISTRIBUTION+"> ?metaDistribution." +
                "  ?release  <"+PACKAGE_DEB.PACKAGE_NAME+"> ?packageName; <"+PACKAGE_DEB.META_DISTRIBUTION+"> ?metaDistribution;  <"+RDF.TYPE+">  <"+PACKAGE_DEB.RELEASE+">. }");
        projectQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,
                "SELECT DISTINCT ?package ?version ?packageName ?metaDistribution " +
                        "{ ?resource <"+DOAP.RELEASE+"> ?release. " +
                        "  ?release  <"+PACKAGE_DEB.PACKAGE_NAME+"> ?packageName; <"+PACKAGE_DEB.META_DISTRIBUTION+"> ?metaDistribution;  <"+RDF.TYPE+">  <"+PACKAGE_DEB.RELEASE+">. }");

    }

    private String getUriRegex(){
        return "^http://metaservice.org/d/(releases|projects)/"+getDistributionName()+"/([^/#]+)(/[^/#]+)?$";
    }

    protected abstract String getDistributionName();

    @Override
    public void process(@NotNull URI uri, @NotNull RepositoryConnection resultConnection) throws PostProcessorException {
        try {
            String projectName = uri.toString().replaceAll(getUriRegex(),"$1");
            URI projectURI = valueFactory.createURI("http://metaservice.org/d/projects/"+getDistributionName()+"/"+projectName);
            resultConnection.add(projectURI, RDF.TYPE, PACKAGE_DEB.PROJECT);
            processProject(resultConnection,uri,projectName);
            TupleQueryResult tupleQueryResult;
            if(uri.toString().startsWith("http://metaservice.org/d/releases")){
                releaseQuery.setBinding("resource",uri);
                tupleQueryResult = releaseQuery.evaluate();
            }else{
                projectQuery.setBinding("resource",uri);
                tupleQueryResult = projectQuery.evaluate();
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
        return false;
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
