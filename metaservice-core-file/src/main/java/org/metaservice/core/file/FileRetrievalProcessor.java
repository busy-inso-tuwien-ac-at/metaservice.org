package org.metaservice.core.file;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.api.rdf.vocabulary.DCTERMS;
import org.metaservice.api.rdf.vocabulary.SPDX;
import org.metaservice.api.sparql.buildingcontexts.SparqlQuery;
import org.openrdf.model.BNode;
import org.openrdf.model.URI;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

/**
 * Created by ilo on 29.05.2014.
 */
public class FileRetrievalProcessor implements PostProcessor {
    @Override
    public void process(@NotNull URI uri, @NotNull RepositoryConnection resultConnection, Date time) throws PostProcessorException {

        //todo make check if this is a referenced file - maybe because of type?
        org.openrdf.model.ValueFactory valueFactory = resultConnection.getValueFactory();
        try {
            FileIdentifier fileIdentifier = FileUriUtils.storeFile(new java.net.URI(uri.toString()));
            resultConnection.add(uri, OWL.SAMEAS,fileIdentifier.getUri());
            addFile(resultConnection,fileIdentifier);
        } catch (FileProcessingException | URISyntaxException | RepositoryException e) {
            throw new PostProcessorException(e);
        }
    }

    public static void addFile(RepositoryConnection resultConnection, FileIdentifier fileIdentifier) throws RepositoryException {
        org.openrdf.model.ValueFactory valueFactory = resultConnection.getValueFactory();
        URI fileURI = fileIdentifier.getUri();
        resultConnection.add(fileURI,RDF.TYPE,SPDX.FILE_CLASS);
        resultConnection.add(fileURI, DCTERMS.EXTENT,valueFactory.createLiteral(fileIdentifier.getSize()));
        BNode bNode = valueFactory.createBNode();
        resultConnection.add(bNode, RDF.TYPE, SPDX.CHECKSUM_CLASS);
        resultConnection.add(bNode, SPDX.CHECKSUM_CLASS,SPDX.CHECKSUMALGORITHM_SHA1);
        resultConnection.add(bNode,SPDX.CHECKSUMVALUE,valueFactory.createLiteral(fileIdentifier.getSha1sum()));
        resultConnection.add(fileURI,SPDX.CHECKSUM,bNode);
    }

    @Override
    public boolean abortEarly(@NotNull URI uri) throws PostProcessorException {
        return (uri.toString().startsWith("http://")||uri.toString().startsWith("https://"));
    }

    @Override
    public List<SparqlQuery> getQueries() {
        return null;
    }
}
