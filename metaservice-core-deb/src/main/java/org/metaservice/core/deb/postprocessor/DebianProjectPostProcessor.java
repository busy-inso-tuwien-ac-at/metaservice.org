package org.metaservice.core.deb.postprocessor;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.api.rdf.vocabulary.DOAP;
import org.metaservice.api.sparql.buildingcontexts.SparqlQuery;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by ilo on 17.02.14.
 */
public class DebianProjectPostProcessor extends AbstractProjectPostProcessor {
    public static final Logger LOGGER = LoggerFactory.getLogger(DebianProjectPostProcessor.class);

    @Inject
    protected DebianProjectPostProcessor(ValueFactory valueFactory, RepositoryConnection repositoryConnection) throws MalformedQueryException, RepositoryException {
        super(valueFactory, repositoryConnection);
    }

    @Override
    protected String getDistributionName() {
        return "debian";
    }

    @Override
    public void processProject(
            @NotNull RepositoryConnection resultConnection,
            @NotNull URI projectURI,
            @NotNull String projectName
    ) throws RepositoryException {
        resultConnection.add(projectURI, DOAP.BUG_DATABASE,valueFactory.createLiteral("http://bugs.debian.org/cgi-bin/pkgreport.cgi?pkg=" + projectName));
        resultConnection.add(projectURI, DOAP.BUG_DATABASE,valueFactory.createLiteral("http://bugs.debian.org/cgi-bin/pkgreport.cgi?src=" +projectName));
        resultConnection.add(projectURI, OWL.SAMEAS,valueFactory.createURI("http://packages.qa.debian.org/"+projectName+"#project"));
    }

    @Override
    public void processRelease(
            @NotNull RepositoryConnection resultConnection,
            @NotNull URI projectURI,
            @NotNull URI releaseURI) {

    }

    @Override
    public List<SparqlQuery> getQueries() {
        return null;
    }
}
