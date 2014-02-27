package org.metaservice.core.deb.postprocessor;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.api.rdf.vocabulary.DOAP;
import org.metaservice.api.rdf.vocabulary.PACKAGE_DEB;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.query.*;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Created by ilo on 17.02.14.
 */
public class UbuntuProjectPostProcessor  extends AbstractProjectPostProcessor
{
    public static final Logger LOGGER = LoggerFactory.getLogger(UbuntuProjectPostProcessor.class);

    @Inject
    protected UbuntuProjectPostProcessor(ValueFactory valueFactory, RepositoryConnection repositoryConnection) throws MalformedQueryException, RepositoryException {
        super(valueFactory, repositoryConnection);
    }

    @Override
    protected String getDistributionName() {
        return "ubuntu";
    }

    @Override
    public void processProject(
            @NotNull RepositoryConnection resultConnection,
            @NotNull URI projectURI,
            @NotNull String projectName
    ) throws RepositoryException {
        //resultConnection.add(uri, DOAP.BUG_DATABASE,valueFactory.createLiteral("http://bugs.debian.org/cgi-bin/pkgreport.cgi?pkg=" + name));
        resultConnection.add(projectURI, DOAP.BUG_DATABASE,valueFactory.createLiteral("https://bugs.launchpad.net/ubuntu/+source/" +projectName));
        resultConnection.add(projectURI, OWL.SAMEAS,valueFactory.createURI("https://launchpad.net/"+projectName+"/+rdf"));
        resultConnection.add(projectURI, PACKAGE_DEB.META_DISTRIBUTION,valueFactory.createLiteral("ubuntu"));
    }

    @Override
    public void processRelease(@NotNull RepositoryConnection resultConnection, @NotNull URI projectURI, @NotNull URI releaseURI) {

    }
}
