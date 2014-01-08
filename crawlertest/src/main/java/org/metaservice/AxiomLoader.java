package org.metaservice;

import org.metaservice.api.ns.ADMSSW;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AxiomLoader {
       private static final Logger LOGGER = LoggerFactory.getLogger(AxiomLoader.class);
    private final RepositoryConnection bufferedSparql;

    public AxiomLoader(RepositoryConnection bufferedSparql) throws RepositoryException {
        this.bufferedSparql = bufferedSparql;
    }

    public static void main(String[] args) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UpdateExecutionException {
        AxiomLoader axiomLoader = new AxiomLoader(null);
        axiomLoader.run();
        axiomLoader.close();
        LOGGER.info("DONE");
        System.exit(0);
    }

    private void run() throws RepositoryException {
        //TODO add NS/grammers usw.
        if(!bufferedSparql.hasStatement(ADMSSW.NEXT,OWL.INVERSEOF,ADMSSW.PREV,true)){
            bufferedSparql.add(ADMSSW.NEXT, OWL.INVERSEOF, ADMSSW.PREV);
        }
    }

    private void close() throws RepositoryException {
       // bufferedSparql.flushModel();
        bufferedSparql.close();
    }

}
