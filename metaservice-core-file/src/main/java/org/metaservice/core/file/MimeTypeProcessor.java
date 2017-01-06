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

package org.metaservice.core.file;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import org.jetbrains.annotations.NotNull;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.api.rdf.vocabulary.DCTERMS;
import org.metaservice.api.rdf.vocabulary.SPDX;
import org.metaservice.api.sparql.buildingcontexts.SparqlQuery;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;


/**
 * Created by ilo on 29.05.2014.
 */
public class MimeTypeProcessor implements PostProcessor{
    ContentInfoUtil util = new ContentInfoUtil();
    @Override
    public void process(@NotNull URI uri, @NotNull RepositoryConnection resultConnection, Date time) throws PostProcessorException {
        ValueFactory valueFactory = resultConnection.getValueFactory();
        try {
            Path path =FileUriUtils.getFile(uri);
            ContentInfo contentInfo = util.findMatch(path.toFile());
            resultConnection.add(uri, DCTERMS.FORMAT,valueFactory.createLiteral(contentInfo.getMimeType()));
        } catch (FileProcessingException | IOException | RepositoryException e) {
            throw new PostProcessorException(e);
        }

    }

    @Override
    public boolean abortEarly(@NotNull URI uri) throws PostProcessorException {
        return FileUriUtils.isFileUri(uri);
    }

}
