package org.metaservice.core.file.archives;

import com.google.common.io.Files;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.api.rdf.vocabulary.DCTERMS;
import org.metaservice.api.rdf.vocabulary.SPDX;
import org.metaservice.api.sparql.buildingcontexts.SparqlQuery;
import org.metaservice.core.file.FileIdentifier;
import org.metaservice.core.file.FileProcessingException;
import org.metaservice.core.file.FileRetrievalProcessor;
import org.metaservice.core.file.FileUriUtils;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.tukaani.xz.XZInputStream;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Created by ilo on 29.05.2014.
 */
public class ArchiveExtractionProcessor implements PostProcessor {

    @Override
    public void process(@NotNull org.openrdf.model.URI uri, @NotNull RepositoryConnection resultConnection, Date time) throws PostProcessorException {
        String mimeType = retrievMimeType(uri);

        //todo check if mimetype is archive
        try( FileInputStream fileInputStream = new FileInputStream(FileUriUtils.getFile(uri).toFile())) {
            if (mimeType.equals("xz")) {
                XZInputStream xzInputStream = new XZInputStream(fileInputStream);
                processExtration(xzInputStream, uri, resultConnection);
            }else if (mimeType.equals("gz")){
                GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
                processExtration(gzipInputStream,uri,resultConnection);
            }else if (mimeType.equals("bz2")){
                BZip2CompressorInputStream bZip2CompressorInputStream = new BZip2CompressorInputStream(fileInputStream);
                processExtration(bZip2CompressorInputStream,uri,resultConnection);
            }else if (mimeType.equals("zip")){
                //todo also process as archive
                ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(fileInputStream);
                processArchiveExtraction(zipInputStream, uri, resultConnection);
            } else if (mimeType.equals("tar")){
                TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(fileInputStream);
                processArchiveExtraction(tarArchiveInputStream,uri,resultConnection);
            }

        } catch (IOException | RepositoryException | FileProcessingException e) {
            throw new PostProcessorException(e);
        }

    }

    private void processArchiveExtraction(ArchiveInputStream archiveInputStream, URI uri, RepositoryConnection repositoryConnection) throws IOException, FileProcessingException, RepositoryException {

        ValueFactory valueFactory  = repositoryConnection.getValueFactory();
        ArchiveEntry archiveEntry;
       while(( archiveEntry = archiveInputStream.getNextEntry()) != null){
           //ignore directories
           if (archiveEntry.isDirectory()) {
               continue;
           }
           FileIdentifier identifier = FileUriUtils.storeFile(archiveInputStream);
           URI entryURI = valueFactory.createURI(uri.toString()+"/" + archiveEntry.getName());
           FileRetrievalProcessor.addFile(repositoryConnection,identifier);
           URI contentURI = identifier.getUri();
           repositoryConnection.add(contentURI,DCTERMS.IS_PART_OF,uri);
           repositoryConnection.add(entryURI,DCTERMS.IS_FORMAT_OF,contentURI);
        }
    }

    private void processExtration(InputStream inputStream, URI uri, RepositoryConnection resultConnection) throws FileProcessingException, RepositoryException {
        FileIdentifier identifier = FileUriUtils.storeFile(inputStream);
        URI fileUri = identifier.getUri();
        resultConnection.add(fileUri, DCTERMS.IS_FORMAT_OF,uri);
        FileRetrievalProcessor.addFile(resultConnection,identifier);
    }

    private String retrievMimeType(URI uri) {
        return null;
    }

    @Override
    public boolean abortEarly(@NotNull org.openrdf.model.URI uri) throws PostProcessorException {
        return FileUriUtils.isFileUri(uri);
    }

}
