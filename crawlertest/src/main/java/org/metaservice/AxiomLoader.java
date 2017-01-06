/*
 * Copyright 2015 Nikola Ilo
 * Research Group for Industrial Software, Vienna University of Technology, Austria
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaservice;

import org.metaservice.api.rdf.vocabulary.ADMSSW;
import org.metaservice.api.rdf.vocabulary.XHV;
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
        //TODO add NS/grammars usw.
        if(!bufferedSparql.hasStatement(XHV.NEXT,OWL.INVERSEOF,XHV.PREV,true)){
            bufferedSparql.add(XHV.NEXT, OWL.INVERSEOF, XHV.PREV);
        }
    }

    private void close() throws RepositoryException {
       // bufferedSparql.flushModel();
        bufferedSparql.close();
    }

}
