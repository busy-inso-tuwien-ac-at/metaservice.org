package org.metaservice;

import org.openrdf.model.Literal;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sparql.SPARQLRepository;

public class SesameTest {


    public static void main(String[] args) throws RepositoryException {
        String serverUrl = "http://metaservice.org:8080/sparql";
        SPARQLRepository repository = new SPARQLRepository(serverUrl);
        repository.initialize();
        RepositoryConnection connection = repository.getConnection();
        ValueFactory factory = repository.getValueFactory();
        URI s = factory.createURI("http://google.at");
        URI p = factory.createURI("http://metaservice.org/sucks");
        Literal o = factory.createLiteral("hard");

        Statement x = factory.createStatement(s, p, o);
        connection.add(x);


    }
}
