package org.metaservice.core.injection;

import com.google.inject.*;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.core.config.Config;
import org.metaservice.core.config.ManagerConfig;
import org.metaservice.core.injection.providers.ConnectionFactoryProvider;
import org.metaservice.core.injection.providers.JAXBMetaserviceDescriptorProvider;
import org.metaservice.core.rdf.RepositoryConnectionProvider;
import org.metaservice.core.rdf.SPARQLRepositoryProvider;
import org.metaservice.core.rdf.ValueFactoryProvider;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;

import javax.jms.ConnectionFactory;

public class MetaserviceModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(ManagerConfig.class).toProvider(ManagerConfigProvider.class).asEagerSingleton();
        binder.bind(Config.class).toProvider(ConfigProvider.class).asEagerSingleton();
        binder.bind(MetaserviceDescriptor.class).toProvider(new JAXBMetaserviceDescriptorProvider(MetaserviceModule.this.getClass().getResourceAsStream("/metaservice.xml"))).asEagerSingleton();
        binder.bind(ValueFactory.class).toProvider(ValueFactoryProvider.class).in(Scopes.SINGLETON);
        binder.bind(Repository.class).toProvider(SPARQLRepositoryProvider.class);
        binder.bind(RepositoryConnection.class).toProvider(RepositoryConnectionProvider.class);
        binder.bind(ConnectionFactory.class).toProvider(ConnectionFactoryProvider.class).in(Scopes.SINGLETON);
    }
}
