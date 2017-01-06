/*
 * Copyright 2015 Nikola Ilo
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

package org.metaservice.core.vcs.git;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.metaservice.core.file.FileIdentifier;
import org.metaservice.core.file.FileProcessingException;
import org.metaservice.core.file.FileRetrievalProcessor;
import org.metaservice.core.file.FileUriUtils;
import org.metaservice.core.utils.GitUtil;
import org.metaservice.core.vcs.VCS;
import org.metaservice.core.vcs.VcsException;
import org.metaservice.core.vcs.VcsProvider;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by ilo on 01.06.2014.
 */
public class GitProvider implements VcsProvider {
    private final static Logger LOGGER = LoggerFactory.getLogger(GitProvider.class);

    @Override
    public void process(URI uri, RepositoryConnection repositoryConnection) throws VcsException {

        try{
            ValueFactory valueFactory = repositoryConnection.getValueFactory();
            String urihash = Hashing.sha1().hashString(uri.toString(), Charset.forName("UTF-8")).toString();
            File wd = new File("/opt/metaservice_data/vcs/git/"+urihash);
            if(!wd.exists()){
                Files.createDirectories(wd.toPath());
            }
            GitUtil gitUtil = new GitUtil(wd);
            if(!gitUtil.isInitialized()){
                gitUtil.clone(uri.toString());
            }
            gitUtil.fetch();
            LOGGER.info("Will process " + gitUtil.getHashes().length);
            for(String hash : gitUtil.getHashes()){
                LOGGER.info("processing " + hash);
                URI revisionURI = valueFactory.createURI(uri+"#revision/"+hash);
                repositoryConnection.add(revisionURI, RDF.TYPE, VCS.REVISION);
                repositoryConnection.add(uri,VCS.CONTAINS_REVISION,revisionURI);
                repositoryConnection.add(revisionURI,VCS.PART_OF_REPOSITORY,uri);
                gitUtil.checkOut(hash);
                for(File file : FileUtils.listFiles(wd, null, true)){
                    if(file.toString().contains(".git/"))
                        continue;
                    Path relativePath = wd.toPath().relativize(file.toPath());

                    LOGGER.debug("processing " + hash+"/"+ relativePath);


                    String fileHash = com.google.common.io.Files.hash(file,Hashing.sha1()).toString();
                    LOGGER.debug("fileHash " +fileHash);

                    long size = file.length();
                    Path p = FileUriUtils.getFile(fileHash, size);
                    FileIdentifier fileIdentifier = new FileIdentifier(fileHash,size);

                    if(!p.toFile().exists()){
                        FileUriUtils.storeFile(file.toURI());
                        FileRetrievalProcessor.addFile(repositoryConnection, fileIdentifier);
                    }
                    URI fileUri = valueFactory.createURI(revisionURI+"/" +relativePath);
                    repositoryConnection.add(fileUri,RDF.TYPE,VCS.VCS_FILE);
                    repositoryConnection.add(fileUri,VCS.REPOSITORY_PATH,valueFactory.createLiteral(relativePath.toString()));
                    repositoryConnection.add(fileUri, OWL.SAMEAS, fileIdentifier.getUri());
                    repositoryConnection.add(fileUri,VCS.REFERENCES_REVISION,revisionURI);
                    repositoryConnection.add(fileUri,VCS.PART_OF_REPOSITORY,uri);
                    repositoryConnection.add(revisionURI,VCS.CONTAINS_FILE,fileUri);
                }
                String[] parents = gitUtil.getParentHashes(hash);
                for(String parentHash :parents){
                    URI parentUri = valueFactory.createURI(uri+"#revision/"+parentHash);
                    LOGGER.info(parentUri.toString());
                    repositoryConnection.add(revisionURI,VCS.SUCCESSOR_OF,parentUri);
                    repositoryConnection.add(parentUri,VCS.PREDECESSOR_OF,revisionURI);
                }
                if(parents.length>1){
                    repositoryConnection.add(revisionURI,RDF.TYPE,VCS.MERGE_REVISION);
                }
            }
            LOGGER.debug("loop end");

            //todo processTags
            //todo processNextFile
            //todo process Branches
        } catch (GitUtil.GitException | RepositoryException | FileProcessingException | IOException e) {
            throw new VcsException(e);
        }
    }
}
