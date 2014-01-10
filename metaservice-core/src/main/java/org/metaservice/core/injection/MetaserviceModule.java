package org.metaservice.core.injection;

import com.google.inject.Binder;
import com.google.inject.Module;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.core.Config;
import org.metaservice.core.ProductionConfig;
import org.metaservice.core.descriptor.MetaserviceDescriptorImpl;
import org.metaservice.core.injection.providers.ConnectionFactoryProvider;
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
        binder.bind(Config.class).to(ProductionConfig.class);
        binder.bind(MetaserviceDescriptor.class).to(MetaserviceDescriptorImpl.class);
        binder.bind(ValueFactory.class).toProvider(ValueFactoryProvider.class);
        binder.bind(Repository.class).toProvider(SPARQLRepositoryProvider.class);
        binder.bind(RepositoryConnection.class).toProvider(RepositoryConnectionProvider.class);
        binder.bind(ConnectionFactory.class).toProvider(ConnectionFactoryProvider.class);
    }
}
