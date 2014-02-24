package org.metaservice.core.deb;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.api.rdf.vocabulary.ADMSSW;
import org.metaservice.api.rdf.vocabulary.DOAP;
import org.metaservice.api.rdf.vocabulary.PACKAGE_DEB;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.*;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import javax.inject.Inject;

/**
 * Created by ilo on 17.02.14.
 */
public class UbuntuProjectPostProcessor  implements PostProcessor
{
    final static String URI_REGEX = "^http://metaservice.org/d/packages/ubuntu/([^/#]+)/([^/#]+)/([^/#]+)$";


    private final ValueFactory valueFactory;
    private final TupleQuery releaseQuery;
    @Inject
    public UbuntuProjectPostProcessor(
            ValueFactory valueFactory,
            RepositoryConnection repositoryConnection
    ) throws MalformedQueryException, RepositoryException {
        this.valueFactory = valueFactory;
        releaseQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,"SELECT DISTINCT ?release {?release <"+ ADMSSW.PROJECT +"> ?project}");
    }

    @Override
    public void process(@NotNull URI uri, @NotNull RepositoryConnection resultConnection) throws PostProcessorException {
        String name = uri.toString().replaceAll(URI_REGEX,"$1");

        URI projectURI = valueFactory.createURI("http://metaservice.org/d/packages/ubuntu/"+name);
        try {
            releaseQuery.setBinding("project",projectURI);
            TupleQueryResult tupleQueryResult = releaseQuery.evaluate();
            while (tupleQueryResult.hasNext()){
                BindingSet bindingSet = tupleQueryResult.next();
                Value releaseURI = bindingSet.getBinding("release").getValue();
                resultConnection.add(projectURI, DOAP.RELEASE, releaseURI);
            }
            resultConnection.add(projectURI, RDF.TYPE, PACKAGE_DEB.PROJECT);
            //resultConnection.add(uri, DOAP.BUG_DATABASE,valueFactory.createLiteral("http://bugs.debian.org/cgi-bin/pkgreport.cgi?pkg=" + name));
            resultConnection.add(projectURI, DOAP.BUG_DATABASE,valueFactory.createLiteral("https://bugs.launchpad.net/ubuntu/+source/" +name));
            resultConnection.add(projectURI, OWL.SAMEAS,valueFactory.createURI("https://launchpad.net/"+name+"/+rdf"));
        } catch (RepositoryException e) {
            e.printStackTrace();
        } catch (QueryEvaluationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean abortEarly(@NotNull URI uri) throws PostProcessorException {
        return !uri.stringValue().matches(URI_REGEX);
    }
}
