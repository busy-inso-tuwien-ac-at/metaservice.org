package org.metaservice.core.nvd.cve;

import org.junit.Test;
import org.metaservice.api.MetaserviceException;
import org.metaservice.api.rdf.vocabulary.CVE;
import org.metaservice.core.AbstractDispatcher;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFParseException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by ilo on 14.06.2014.
 */
public class OntologyTest {

    @Test
    public void testInference() throws RepositoryException, IOException, RDFParseException, MetaserviceException {
        Repository repository = AbstractDispatcher.createTempRepository(true);
        RepositoryConnection connection = repository.getConnection();
        ValueFactory valueFactory = connection.getValueFactory();
        URI cve = valueFactory.createURI("http://example.org/cve");
        connection.add(new URL("http://metaservice.org/ns/cve.rdf"),null,null,new Resource[0]);
        connection.add(cve, CVE.CVE_ID,valueFactory.createLiteral("title"));
        System.err.println(connection.getStatements(null, RDFS.LABEL,null,true).next().toString());
    }

}