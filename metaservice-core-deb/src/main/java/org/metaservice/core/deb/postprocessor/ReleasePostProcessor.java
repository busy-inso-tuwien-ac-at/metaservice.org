package org.metaservice.core.deb.postprocessor;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.api.rdf.vocabulary.ADMSSW;
import org.metaservice.api.rdf.vocabulary.DC;
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

import javax.inject.Inject;

/**
 * Created by ilo on 24.02.14.
 */
public class ReleasePostProcessor implements PostProcessor {
    public static final Logger LOGGER = LoggerFactory.getLogger(ReleasePostProcessor.class);

    final static String URI_REGEX = "^http://metaservice.org/d/(packages|releases)/([^/#]+)/([^/#]+)/([^/#]+)(/[^/#]+)?$";

    private final ValueFactory valueFactory;
    private final TupleQuery packageQuery;
    private final TupleQuery releaseQuery;

    @Inject
    public ReleasePostProcessor(
            ValueFactory valueFactory,
            RepositoryConnection repositoryConnection
    ) throws MalformedQueryException, RepositoryException {
        this.valueFactory = valueFactory;
        packageQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,
                "SELECT DISTINCT ?package ?version ?packageName ?metaDistribution " +
                        "{ ?resource <"+PACKAGE_DEB.VERSION+"> ?version; <"+PACKAGE_DEB.PACKAGE_NAME+"> ?packageName; <"+PACKAGE_DEB.META_DISTRIBUTION+"> ?metaDistribution." +
                        "  ?package  <"+PACKAGE_DEB.VERSION+"> ?version; <"+PACKAGE_DEB.PACKAGE_NAME+"> ?packageName; <"+PACKAGE_DEB.META_DISTRIBUTION+"> ?metaDistribution; <"+RDF.TYPE+"> <"+PACKAGE_DEB.PACKAGE+">. }");
        releaseQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,
                "SELECT DISTINCT ?package ?version ?packageName ?metaDistribution " +
                        "{ ?resource <"+ADMSSW.PACKAGE+"> ?package. " +
                        "  ?package  <"+PACKAGE_DEB.VERSION+"> ?version; <"+PACKAGE_DEB.PACKAGE_NAME+"> ?packageName; <"+PACKAGE_DEB.META_DISTRIBUTION+"> ?metaDistribution; <"+RDF.TYPE+"> <"+PACKAGE_DEB.PACKAGE+">. }");

    }

    @Override
    public void process(@NotNull URI uri, @NotNull RepositoryConnection resultConnection) throws PostProcessorException {
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

            }else{
                releaseQuery.setBinding("resource",uri);
                tupleQueryResult = packageQuery.evaluate();
            }

            while (tupleQueryResult.hasNext()){
                BindingSet bindingSet = tupleQueryResult.next();
                URI packageURI = (URI) bindingSet.getBinding("package").getValue();
                resultConnection.add(packageURI, ADMSSW.RELEASE, releaseURI);
                resultConnection.add(releaseURI, ADMSSW.PACKAGE, packageURI);
            }
            resultConnection.add(releaseURI, RDF.TYPE, PACKAGE_DEB.RELEASE);
            resultConnection.add(releaseURI,PACKAGE_DEB.META_DISTRIBUTION,valueFactory.createLiteral(distribution));
            resultConnection.add(releaseURI,PACKAGE_DEB.VERSION,valueFactory.createLiteral(version));
            resultConnection.add(releaseURI, PACKAGE_DEB.PACKAGE_NAME, valueFactory.createLiteral(project));
            resultConnection.add(releaseURI, DC.TITLE, valueFactory.createLiteral(project +" " + version));
        } catch (RepositoryException | QueryEvaluationException e) {
            throw new PostProcessorException(e);
        }
    }

    @Override
    public boolean abortEarly(@NotNull URI uri) throws PostProcessorException {
        return !uri.stringValue().matches(URI_REGEX);
    }
}
