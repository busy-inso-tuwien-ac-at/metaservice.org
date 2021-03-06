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

import com.google.inject.*;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.Config;
import org.metaservice.api.messaging.config.ManagerConfig;
import org.metaservice.api.messaging.descriptors.DescriptorHelper;
import org.metaservice.api.messaging.dispatcher.PostProcessorDispatcher;
import org.metaservice.api.messaging.dispatcher.ProviderDispatcher;
import org.metaservice.core.descriptor.DescriptorHelperImpl;
import org.metaservice.core.injection.providers.JAXBMetaserviceDescriptorProvider;
import org.metaservice.core.rdf.RepositoryConnectionProvider;
import org.metaservice.core.rdf.SPARQLRepositoryProvider;
import org.metaservice.core.rdf.ValueFactoryProvider;
import org.metaservice.kryo.MongoKryoMessagingModule;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;

public class MetaserviceModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(ManagerConfig.class).toProvider(ManagerConfigProvider.class).in(Scopes.SINGLETON);
        binder.bind(Config.class).toProvider(ConfigProvider.class).in(Scopes.SINGLETON);
        binder.bind(MetaserviceDescriptor.class).toProvider(new JAXBMetaserviceDescriptorProvider(MetaserviceModule.this.getClass().getResourceAsStream("/metaservice.xml"))).in(Scopes.SINGLETON);
        binder.bind(Repository.class).toProvider(SPARQLRepositoryProvider.class).in(Scopes.SINGLETON);
        binder.bind(RepositoryConnection.class).toProvider(RepositoryConnectionProvider.class);
        binder.bind(ValueFactory.class).toProvider(ValueFactoryProvider.class);
        binder.bind(DescriptorHelper.class).to(DescriptorHelperImpl.class);
        new MongoKryoMessagingModule().configure(binder);
        //new JmsModule().configure(binder);
    }
}
