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

import org.junit.Test;
import org.metaservice.api.MetaserviceException;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.api.postprocessor.PostProcessorSparqlBuilder;
import org.metaservice.api.postprocessor.PostProcessorSparqlQuery;
import org.metaservice.api.rdf.vocabulary.BIGDATA;
import org.metaservice.api.rdf.vocabulary.DOAP;
import org.metaservice.api.rdf.vocabulary.METASERVICE_SWDEP;
import org.metaservice.api.rdf.vocabulary.SPDX;
import org.metaservice.api.sparql.nodes.BoundVariable;
import org.metaservice.api.sparql.nodes.Variable;
import org.metaservice.core.AbstractDispatcher;
import org.metaservice.demo.license.SPDX_LICENSE;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sparql.query.QueryStringUtil;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.Rio;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by ilo on 08.07.2014.
 */
public class LicenseCheckTest {
    @Test
    public void licenseTest() throws MetaserviceException, RepositoryException {
        Repository repository = AbstractDispatcher.createTempRepository(true);
        RepositoryConnection repositoryConnection = repository.getConnection();
        //rule 1: links to gpl -> also gpl
        //rule 2: links to lgpl ->
    }

    @Test
    public void licenseGenerator() throws IOException, RDFParseException {
        Model model =  Rio.parse(new URL("http://spdx.org/licenses/").openStream(),"http://spdx.org/licenses/", RDFFormat.RDFA);
        for(Statement statement : model){
            System.err.println(statement);
        }
    }
}
