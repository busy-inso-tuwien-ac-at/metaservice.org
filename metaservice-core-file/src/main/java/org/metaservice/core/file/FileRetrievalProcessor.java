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

package org.metaservice.core.file;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.api.postprocessor.PostProcessorSparqlBuilder;
import org.metaservice.api.postprocessor.PostProcessorSparqlQuery;
import org.metaservice.api.rdf.vocabulary.DCTERMS;
import org.metaservice.api.rdf.vocabulary.METASERVICE_FILE;
import org.metaservice.api.rdf.vocabulary.SPDX;
import org.metaservice.api.sparql.nodes.BoundVariable;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.*;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import java.net.URISyntaxException;
import java.util.Date;

/**
 * Created by ilo on 29.05.2014.
 */
public class FileRetrievalProcessor implements PostProcessor {
    private final TupleQuery uriTypeCheckQuery;
    public FileRetrievalProcessor(ValueFactory valueFactory, RepositoryConnection repositoryConnection) throws PostProcessorException {
        try {
            uriTypeCheckQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,new PostProcessorSparqlQuery() {
                @Override
                public String build() {
                    return select(
                            true,var(resource)
                    ).where(
                            triplePattern(resource,RDF.TYPE, METASERVICE_FILE.FILE)
                    ).build();
                }
            }.build());
        } catch (RepositoryException | MalformedQueryException e) {
            throw new PostProcessorException(e);
        }
    }
    private final BoundVariable resource = new BoundVariable("resource");

    @Override
    public void process(@NotNull URI uri, @NotNull final RepositoryConnection resultConnection, Date time) throws PostProcessorException {
        org.openrdf.model.ValueFactory valueFactory = resultConnection.getValueFactory();

        uriTypeCheckQuery.setBinding(PostProcessorSparqlBuilder.getDateVariable().toString(), valueFactory.createLiteral(time));
        uriTypeCheckQuery.setBinding(resource.toString(),uri);
        try {

            TupleQueryResult tupleQueryResult = uriTypeCheckQuery.evaluate();
            if(tupleQueryResult.hasNext()) {
                //todo make check if this is a referenced file - maybe because of type?
                FileIdentifier fileIdentifier = FileUriUtils.storeFile(new java.net.URI(uri.toString()));
                resultConnection.add(uri, OWL.SAMEAS, fileIdentifier.getUri());
                addFile(resultConnection, fileIdentifier);

            }
        } catch (FileProcessingException | URISyntaxException | RepositoryException | QueryEvaluationException e) {
            throw new PostProcessorException(e);
        }
    }

    public static void addFile(RepositoryConnection resultConnection, FileIdentifier fileIdentifier) throws RepositoryException {
        ValueFactory valueFactory = resultConnection.getValueFactory();
        URI fileURI = fileIdentifier.getUri();
        resultConnection.add(fileURI,RDF.TYPE,SPDX.FILE);
        resultConnection.add(fileURI, DCTERMS.EXTENT,valueFactory.createLiteral(fileIdentifier.getSize()));
        URI sha1sum = valueFactory.createURI(fileURI+"#sha1");
        resultConnection.add(sha1sum, RDF.TYPE, SPDX.CHECKSUM);
        resultConnection.add(sha1sum, SPDX.ALGORITHM,SPDX.CHECKSUM_ALGORITHM_SHA1);
        resultConnection.add(sha1sum,SPDX.CHECKSUM_VALUE,valueFactory.createLiteral(fileIdentifier.getSha1sum()));
        resultConnection.add(fileURI,SPDX.CHECKSUM,sha1sum);
    }

    @Override
    public boolean abortEarly(@NotNull URI uri) throws PostProcessorException {
        return (uri.toString().startsWith("http://")||uri.toString().startsWith("https://"));
    }

}
