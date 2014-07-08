package org.metaservice.core.vcs;

import com.google.inject.Injector;
import org.metaservice.api.MetaserviceException;
import org.metaservice.api.rdf.vocabulary.DOAP;
import org.metaservice.core.AbstractDispatcher;
import org.metaservice.api.messaging.Config;
import org.metaservice.core.injection.InjectorFactory;
import org.metaservice.api.messaging.MessageHandler;
import org.metaservice.core.vcs.git.GitProvider;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.*;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Created by ilo on 01.06.2014.
 */
public class VcsRunner extends AbstractDispatcher<String>{
    private final static Logger LOGGER = LoggerFactory.getLogger(VcsRunner.class);

    @Inject
    protected VcsRunner(RepositoryConnection repositoryConnection, Config config, MessageHandler messageHandler, ValueFactory valueFactory){
        super(repositoryConnection, config, messageHandler, valueFactory, "vcsrunner");
        this.repositoryConnection = repositoryConnection;
    }

    public static void main(String[] args) {
        Injector injector = InjectorFactory.getBasicInjector();
        VcsRunner vcsRunner = injector.getInstance(VcsRunner.class);
        vcsRunner.run();
    }

    private void run() {
        try {
            TupleQuery query = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL, "SELECT ?s {?s <" + RDF.TYPE+"> <" + DOAP.GIT_REPOSITORY + ">.}");
            LOGGER.info(query.toString());
            TupleQueryResult result = query.evaluate();
            while(result.hasNext()){
                BindingSet bindings = result.next();
                URI uri = (URI)bindings.getBinding("s").getValue();
                LOGGER.info("Processing {}",uri);
                Repository repository = createTempRepository(true);
                RepositoryConnection tempConnection  = repository.getConnection();
                try {
                    new GitProvider().process(uri,tempConnection);
                }catch (VcsException e){
                    LOGGER.error("error processing vcs " + uri,e);
                }
                repositoryConnection.add(tempConnection.getStatements(null, null, null, true));
                tempConnection.close();
                repository.shutDown();
            }

            LOGGER.info("finished loop");
        } catch (MetaserviceException | RepositoryException | MalformedQueryException | QueryEvaluationException e) {
            LOGGER.error("error querying db",e);
        }
    }

    private final RepositoryConnection repositoryConnection;
}
