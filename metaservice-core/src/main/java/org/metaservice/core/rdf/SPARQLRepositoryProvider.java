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

package org.metaservice.core.rdf;

import com.google.inject.Provider;
import org.metaservice.api.messaging.Config;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sparql.SPARQLRepository;

import javax.inject.Inject;

public class SPARQLRepositoryProvider implements Provider<SPARQLRepository> {
    private final Config config;
    @Inject
    public SPARQLRepositoryProvider(Config config){
        this.config = config;
    }
    @Override
    public SPARQLRepository get() {
        SPARQLRepository sparqlRepository = new SPARQLRepository(config.getSparqlEndpoint());
        try {
            sparqlRepository.initialize();
            return sparqlRepository;

        } catch (RepositoryException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }
    }
}
