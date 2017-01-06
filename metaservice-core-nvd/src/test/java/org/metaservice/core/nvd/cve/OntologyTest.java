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

package org.metaservice.core.nvd.cve;

import org.junit.Test;
import org.metaservice.api.MetaserviceException;
import org.metaservice.api.rdf.vocabulary.CVE;
import org.metaservice.core.AbstractDispatcher;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFParseException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by ilo on 14.06.2014.
 */
public class OntologyTest {

    @Test
    public void testInference() throws RepositoryException, IOException, RDFParseException, MetaserviceException {
        Repository repository = AbstractDispatcher.createTempRepository(true);
        RepositoryConnection connection = repository.getConnection();
        ValueFactory valueFactory = connection.getValueFactory();
        URI cve = valueFactory.createURI("http://example.org/cve");
        connection.add(new URL("http://metaservice.org/ns/cve.rdf"),null,null,new Resource[0]);
        connection.add(cve, CVE.CVE_ID,valueFactory.createLiteral("title"));
        System.err.println(connection.getStatements(null, RDFS.LABEL,null,true).next().toString());
    }

}
