package org.metaservice.core;

import org.junit.Before;
import org.junit.Test;
import org.metaservice.api.MetaserviceException;
import org.metaservice.api.rdf.vocabulary.METASERVICE_SWDEP;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.*;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;
import org.openrdf.sail.SailException;

import java.io.IOException;

import static org.junit.Assert.*;

public class PropertyReificationInferencerTest {
    private final static String NS = "http://example.org/";
    Repository repo;
    RepositoryConnection con;
    ValueFactory valueFactory;

    @Before
    public void setUp() throws RepositoryException, MalformedQueryException, SailException, MetaserviceException {
        repo = AbstractDispatcher.createTempRepository(true);
        con = repo.getConnection();
        valueFactory = con.getValueFactory();
    }
    @Test
    public void propertyReificationInferenceTest() throws RepositoryException, MalformedQueryException, SailException, IOException, RDFParseException, QueryEvaluationException {

        URI package1 = valueFactory.createURI(NS,"package1");
        URI package2 = valueFactory.createURI(NS,"package2");
        con.add(package1, METASERVICE_SWDEP.LINKS,package2);
        con.add(PropertyReificationInferencerTest.class.getResourceAsStream("/ontologies/propertyreification.rdf"), "", RDFFormat.RDFXML);
        con.add(PropertyReificationInferencerTest.class.getResourceAsStream("/ontologies/metaservice-swrel.rdf"), "",RDFFormat.RDFXML);
        System.out.println(con.getStatements(package1, null, null, true).asList());
        System.out.println(con.getStatements(null, null, METASERVICE_SWDEP.LINKS, true).asList());

        assertTrue(con.hasStatement(package1, METASERVICE_SWDEP.DEPENDS, null, true));
    }

}