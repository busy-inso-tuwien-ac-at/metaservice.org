import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;
import org.metaservice.api.rdf.vocabulary.ADMSSW;
import org.metaservice.core.injection.MetaserviceModule;
import org.openrdf.model.*;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sparql.SPARQLConnection;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;
import org.openrdf.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.openrdf.sail.memory.MemoryStore;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashSet;
import java.util.Properties;

/**
 * Created by ilo on 11.01.14.
 */
public class TempTests {
    @Test
    public void test() throws NoSuchMethodException {
        Method m = SPARQLConnection.class.getDeclaredMethod("createInsertDataCommand", Iterable.class, Resource[].class);
    }

    @Test
    public void sendByRDFtest() throws IOException, RDFHandlerException {

        Injector injector = Guice.createInjector(new MetaserviceModule());

        ValueFactory valueFactory= injector.getInstance(ValueFactory.class);
        HashSet<Statement> model= new HashSet<>();

        model.add(valueFactory.createStatement(valueFactory.createURI("http://e"), ADMSSW.EFFORT,valueFactory.createURI("http://b"),valueFactory.createBNode()));

        RDFBigdataPrettyWriter writer = new RDFBigdataPrettyWriter(new FileWriter("test.xml"));

        writer.startRDF();
        for(Statement s :model)
            writer.handleStatement(s);

        writer.close();


        RepositoryConnection connection = null;
    }

       /*
    @Test
    public void bigdataSailTest() throws RepositoryException, IOException, RDFParseException, RDFHandlerException {
        // use one of our pre-configured option-sets or "modes"
        Properties properties =
                new Properties();
        properties.put("com.bigdata.rdf.store.AbstractTripleStore.statementIdentifiers","false");
        properties.put("com.bigdata.rdf.store.AbstractTripleStore.textIndex","false");
        TemporaryStore temporaryStore = new TemporaryStore();
        TempTripleStore tempTripleStore = new TempTripleStore(temporaryStore,properties,null);

// instantiate a sail and a Sesame repository
        BigdataSail sail = new BigdataSail(tempTripleStore);
        BigdataSailRepository repo = new BigdataSailRepository(sail);
        repo.initialize();

        ValueFactory valueFactory = repo.getValueFactory();
        BigdataSailRepositoryConnection con =  repo.getConnection();
        con.setAutoCommit(false);
        URL url = new URL("http://metaservice.org/ns/adms_sw.rdf");
        con.add(url,null,null,new Resource[0]);
        con.commit();

        con.add(valueFactory.createURI("http://meta"),RDF.TYPE,ADMSSW.SOFTWARE_PROJECT);


        con.exportStatements(null, null, null, true, new RDFXMLPrettyWriter(new FileWriter("foo.xml")), new Resource[0]);
    }    */

    @Test
    public void sesameSailTest() throws RepositoryException, IOException, RDFParseException, RDFHandlerException {
        Repository repo = new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore()));
        repo.initialize();
        ValueFactory valueFactory = repo.getValueFactory();
        RepositoryConnection con =  repo.getConnection();
        con.setAutoCommit(false);
        URL url = new URL("http://metaservice.org/ns/adms_sw.rdf");
        con.add(url,null,null);
        con.commit();

        con.add(valueFactory.createURI("http://meta"),RDF.TYPE,ADMSSW.SOFTWARE_PROJECT);


        con.exportStatements(null, null, null, true, new RDFXMLPrettyWriter(new FileWriter("foo-sesame.xml")));
    }
}
