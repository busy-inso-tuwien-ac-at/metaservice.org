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

package org.metaservice.core.injection;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.jetbrains.annotations.NotNull;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.Config;
import org.metaservice.core.injection.providers.JAXBMetaserviceDescriptorProvider;
import org.metaservice.core.rdf.RepositoryConnectionProvider;
import org.metaservice.core.rdf.SPARQLRepositoryProvider;
import org.metaservice.core.rdf.ValueFactoryProvider;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;

import java.util.List;

/**
 * Created by ilo on 13.03.14.
 */
public class MetaserviceTestModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Config.class).toInstance(new Config() {
            @Override
            public String getSparqlEndpoint() {
                return "http://graph.metaservice.org:8080/bigdata/sparql";
            }

            @Override
            public String getJmsBroker() {
                return null;
            }

            @Override
            public List<String> getArchivesForProvider(String provider) {
                return null;
            }

            @Override
            public String getArchiveBasePath() {
                return null;
            }

            @Override
            public String getHttpdDataDirectory() {
                return null;
            }

            @Override
            public int getBatchSize() {
                return 0;
            }

            @Override
            public boolean getDumpRDFBeforeLoad() {
                return false;
            }

            @Override
            public String getDumpRDFDirectory() {
                return null;
            }

            @NotNull
            @Override
            public String getDefaultProviderOpts() {
                return null;
            }

            @NotNull
            @Override
            public String getDefaultPostProcessorOpts() {
                return null;
            }
        });
        bind(MetaserviceDescriptor.class).toProvider(new JAXBMetaserviceDescriptorProvider(MetaserviceTestModule.this.getClass().getResourceAsStream("/metaservice.xml"))).in(Scopes.SINGLETON);
        bind(ValueFactory.class).toProvider(ValueFactoryProvider.class).in(Scopes.SINGLETON);
        bind(Repository.class).toProvider(SPARQLRepositoryProvider.class).in(Scopes.SINGLETON);
        bind(RepositoryConnection.class).toProvider(RepositoryConnectionProvider.class).in(Scopes.SINGLETON);
     //   bind(ConnectionFactory.class).toProvider(ConnectionFactoryProvider.class).in(Scopes.SINGLETON);
    }
}
