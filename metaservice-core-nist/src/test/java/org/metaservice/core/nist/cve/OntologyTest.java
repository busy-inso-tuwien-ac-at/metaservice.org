package org.metaservice.core.nist.cve;

import org.junit.Test;
import org.metaservice.api.rdf.vocabulary.DC;
import org.metaservice.core.AbstractDispatcher;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFParseException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ilo on 14.06.2014.
 */
public class OntologyTest {

    @Test
    public void testInference() throws RepositoryException, IOException, RDFParseException {
        Repository repository = AbstractDispatcher.createTempRepository(true);
        RepositoryConnection connection = repository.getConnection();
        ValueFactory valueFactory = connection.getValueFactory();
        URI cve = valueFactory.createURI("http://example.org/cve");
        connection.add(new URL("http://metaservice.org/ns/cve.rdf"),null,null,new Resource[0]);
        connection.add(cve,CVE.CVE_ID,valueFactory.createLiteral("title"));
        System.err.println(connection.getStatements(null, DC.TITLE,null,true).next().toString());
    }

}
