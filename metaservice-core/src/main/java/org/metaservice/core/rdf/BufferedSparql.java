package org.metaservice.core.rdf;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.metaservice.core.Config;
import org.openrdf.model.*;
import org.openrdf.model.impl.TreeModel;
import org.openrdf.query.*;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sparql.SPARQLRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Date;

/**
 * TODO replace with something better...
 * TODO BigData Sail?
 * TODO intelligent/comparing updater?
 */
@Singleton
public class BufferedSparql {
    private final Logger LOGGER = LoggerFactory.getLogger(BufferedSparql.class);
    @NotNull
    private TreeModel model = new TreeModel();
    private int statementcount = 0;

    @Inject
    public BufferedSparql(Config config) throws RepositoryException {
        SPARQLRepository repository = new SPARQLRepository(config.getSparqlEndpoint());
        repository.initialize();
        con = repository.getConnection();
        f = repository.getValueFactory();
    }

    private final ValueFactory f;
    private final RepositoryConnection con;
    public void flushModel(){
        try{
            LOGGER.info("Flushing Model to Repository");
            con.add(model);
            model = new TreeModel();
            statementcount =0;
        }catch (RepositoryException e){
            e.printStackTrace();
        }
    }

    public boolean hasStatement(Resource subj, URI pred, Value obj, boolean includeInferred, Resource... contexts) throws RepositoryException {
        return con.hasStatement(subj, pred, obj, includeInferred, contexts);
    }

    public boolean hasStatement(Statement st, boolean includeInferred, Resource... contexts) throws RepositoryException {
        return con.hasStatement(st, includeInferred, contexts);
    }

    public Update prepareUpdate(QueryLanguage ql, String update, String baseURI) throws RepositoryException, MalformedQueryException {
        return con.prepareUpdate(ql, update, baseURI);
    }

    public Query prepareQuery(QueryLanguage ql, String query) throws RepositoryException, MalformedQueryException {
        return con.prepareQuery(ql, query);
    }

    public Query prepareQuery(QueryLanguage ql, String query, String baseURI) throws RepositoryException, MalformedQueryException {
        return con.prepareQuery(ql, query, baseURI);
    }

    public void addStatement(Resource s, URI p, Value o) throws RepositoryException {
        if(statementcount++ == 6000)
        {
            LOGGER.info("Flushing because of statement count");
            flushModel();
        }
        model.add(f.createStatement(s,p,o));
    }


    public void addStatement(Resource s, URI p, Value o,Resource context) throws RepositoryException {
        if(statementcount++ == 6000)
        {
            LOGGER.info("Flushing because of statement count");
            flushModel();
        }
        model.add(f.createStatement(s,p,o,context));
    }

    public URI createURI(String namespace, String localName) {
        return this.f.createURI(namespace,localName);
    }

    public BNode createBNode() {
        return f.createBNode();
    }

    public Value createLiteral(String literal) {
        return f.createLiteral(literal);
    }
    public Value createLiteral(int literal) {
        return f.createLiteral(literal);
    }
    public Value createLiteral(Date literal) {
        return f.createLiteral(literal);
    }
    public Value createLiteral(boolean literal) {
        return f.createLiteral(literal);
    }
    public Value createLiteral(double literal) {
        return f.createLiteral(literal);
    }
    public Value createLiteral(float literal) {
        return f.createLiteral(literal);
    }
    public Value createLiteral(long literal) {
        return f.createLiteral(literal);
    }


    public URI createURI(String uri) {
        return this.f.createURI(uri);
    }

    public void close() throws RepositoryException {
        this.flushModel();
        this.con.close();
    }

    public TupleQuery prepareTupleQuery(QueryLanguage sparql, String s) throws RepositoryException, MalformedQueryException {
        return con.prepareTupleQuery(sparql,s);
    }

    public TupleQuery prepareTupleQuery(QueryLanguage ql, String query, String baseURI) throws RepositoryException, MalformedQueryException {
        return con.prepareTupleQuery(ql, query, baseURI);
    }

    public GraphQuery prepareGraphQuery(QueryLanguage ql, String query) throws RepositoryException, MalformedQueryException {
        return con.prepareGraphQuery(ql, query);
    }

    public GraphQuery prepareGraphQuery(QueryLanguage ql, String query, String baseURI) throws RepositoryException, MalformedQueryException {
        return con.prepareGraphQuery(ql, query, baseURI);
    }

    public BooleanQuery prepareBooleanQuery(QueryLanguage ql, String query) throws RepositoryException, MalformedQueryException {
        return con.prepareBooleanQuery(ql, query);
    }

    public BooleanQuery prepareBooleanQuery(QueryLanguage ql, String query, String baseURI) throws RepositoryException, MalformedQueryException {
        return con.prepareBooleanQuery(ql, query, baseURI);
    }

    public Update prepareUpdate(QueryLanguage sparql, String s) throws RepositoryException, MalformedQueryException {
        return this.con.prepareUpdate(sparql,s);
    }
}
