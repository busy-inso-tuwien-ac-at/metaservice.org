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

package org.metaservice.core.dispatcher;

import com.google.common.base.Optional;
import org.metaservice.api.messaging.Config;
import org.metaservice.core.AbstractDispatcher;
import org.metaservice.core.postprocessor.PostProcessorDispatcher;
import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
* Created by ilo on 23.07.2014.
*/
public class SendDataPipe extends MetaserviceSimplePipe<PostProcessorDispatcher.Context,PostProcessorDispatcher.Context> {

    private final RepositoryConnection repositoryConnection;
    private final ValueFactory valueFactory;
    private final Config config;

    @Inject
    public SendDataPipe(Logger logger, RepositoryConnection repositoryConnection, ValueFactory valueFactory, Config config) {
        super(logger);
        this.repositoryConnection = repositoryConnection;
        this.valueFactory = valueFactory;
        this.config = config;
    }

    @Override
    public Optional<PostProcessorDispatcher.Context> process(PostProcessorDispatcher.Context input) throws Exception {
        AbstractDispatcher.recoverSparqlConnection(repositoryConnection);
        LOGGER.info("starting to send data");
        RDFXMLPrettyWriter writer = null;
        String writerFile = null;
        if(config.getDumpRDFBeforeLoad()){
            try {
                writerFile =config.getDumpRDFDirectory()+ "/"+ /*target.getClass().getSimpleName()+*/"_" + System.currentTimeMillis() +".rdf";
                writer = new RDFXMLPrettyWriter(new FileWriter(writerFile));
            } catch (IOException e) {
                LOGGER.error("Couldn't dump rdf data to {}", writerFile, e);
            }
        }
        RepositoryResult<Namespace> ns = input.resultConnection.getNamespaces();
        while(ns.hasNext()){
            Namespace n = ns.next();
            repositoryConnection.setNamespace(n.getPrefix(), n.getName());
            if(writer != null){
                writer.handleNamespace(n.getPrefix(),n.getName());
            }
            LOGGER.debug("Using namespace "+ n.getPrefix() + " , " +n.getName() );
        }
        if(writer != null){
            try {
                input.resultConnection.exportStatements(null, null, null, true, writer);
            } catch (RDFHandlerException e) {
                LOGGER.error("Couldn't dump rdf data to {}",writerFile,e);
            }
        }
        input.resultConnection.close();
        ArrayList<ArrayList<Statement>> result = split(input.generatedStatements);
        int j = 0;
        for(ArrayList<Statement> r  : result ){
            LOGGER.info("Sending BATCH " + j++);
            repositoryConnection.begin();
            repositoryConnection.add(r, input.metadata);
            repositoryConnection.commit();
        }
        LOGGER.info("finished to send data");
        return Optional.of(input);
    }
    private ArrayList<ArrayList<Statement>> split(List<Statement> statements){
        ArrayList<ArrayList<Statement>> result = new ArrayList<>();
        ArrayList<Statement> currentResult = new ArrayList<>();

        int i = config.getBatchSize()+1;

        LOGGER.info("SPLITTING : ");

        for(Statement statement : statements){
            if(i >config.getBatchSize()){
                i = 0;
                currentResult = new ArrayList<>();
                result.add(currentResult);
            }
            i++;
            currentResult.add(valueFactory.createStatement(statement.getSubject(), statement.getPredicate(), statement.getObject()));
        }
        LOGGER.info(""+result.size());
        return result;
    }
}
