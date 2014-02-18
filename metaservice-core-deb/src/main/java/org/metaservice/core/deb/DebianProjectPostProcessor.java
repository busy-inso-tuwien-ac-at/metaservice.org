package org.metaservice.core.deb;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.api.rdf.vocabulary.DOAP;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import javax.inject.Inject;

/**
 * Created by ilo on 17.02.14.
 */
public class DebianProjectPostProcessor implements PostProcessor {
    final static String URI_REGEX = "^http://metaservice.org/d/packages/debian/([^/#]+)$";


    private final ValueFactory valueFactory;
    @Inject
    public DebianProjectPostProcessor(ValueFactory valueFactory){
        this.valueFactory = valueFactory;
    }

    @Override
    public void process(@NotNull URI uri, @NotNull RepositoryConnection resultConnection) throws PostProcessorException {
        String name = uri.toString().replaceAll(URI_REGEX,"$1");
        try {
            resultConnection.add(uri, DOAP.BUG_DATABASE,valueFactory.createLiteral("http://bugs.debian.org/cgi-bin/pkgreport.cgi?pkg=" + name));
            resultConnection.add(uri, DOAP.BUG_DATABASE,valueFactory.createLiteral("http://bugs.debian.org/cgi-bin/pkgreport.cgi?src=" +name));
            resultConnection.add(uri, OWL.SAMEAS,valueFactory.createURI("http://packages.qa.debian.org/"+name+"#project"));
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean abortEarly(@NotNull URI uri) throws PostProcessorException {
        return !uri.stringValue().matches(URI_REGEX);
    }
}