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

package org.metaservice.core.deb.postprocessor;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.rdf.vocabulary.DEB;
import org.metaservice.api.rdf.vocabulary.DOAP;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.query.*;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Created by ilo on 17.02.14.
 */
public class UbuntuProjectPostProcessor  extends AbstractProjectPostProcessor
{
    public static final Logger LOGGER = LoggerFactory.getLogger(UbuntuProjectPostProcessor.class);

    @Inject
    protected UbuntuProjectPostProcessor(ValueFactory valueFactory, RepositoryConnection repositoryConnection) throws MalformedQueryException, RepositoryException {
        super(valueFactory, repositoryConnection);
    }

    @Override
    protected String getDistributionName() {
        return "ubuntu";
    }

    @Override
    public void processProject(
            @NotNull RepositoryConnection resultConnection,
            @NotNull URI projectURI,
            @NotNull String projectName
    ) throws RepositoryException {
        //resultConnection.add(uri, DOAP.BUG_DATABASE,valueFactory.createLiteral("http://bugs.debian.org/cgi-bin/pkgreport.cgi?pkg=" + name));
        resultConnection.add(projectURI, DOAP.BUG_DATABASE,valueFactory.createLiteral("https://bugs.launchpad.net/ubuntu/+source/" +projectName));
        resultConnection.add(projectURI, OWL.SAMEAS,valueFactory.createURI("https://launchpad.net/"+projectName+"/+rdf"));
        resultConnection.add(projectURI, DEB.META_DISTRIBUTION,valueFactory.createLiteral("ubuntu"));
    }

    @Override
    public void processRelease(@NotNull RepositoryConnection resultConnection, @NotNull URI projectURI, @NotNull URI releaseURI) {

    }
}
