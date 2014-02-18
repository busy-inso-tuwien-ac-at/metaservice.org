package org.metaservice.manager.maven;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import org.apache.maven.repository.internal.MavenAetherModule;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.internal.impl.Slf4jLoggerFactory;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ilo on 18.02.14.
 */
public class MavenGuiceModule extends AbstractModule{
    @Override
    protected void configure()
    {
        install(new MavenAetherModule());
       // alternatively, use the Guice Multibindings extensions
        bind( RepositoryConnectorFactory.class ).annotatedWith( Names.named( "basic" ) ).to( BasicRepositoryConnectorFactory.class );
        bind( TransporterFactory.class ).annotatedWith( Names.named( "file" ) ).to( FileTransporterFactory.class );
        bind( TransporterFactory.class ).annotatedWith( Names.named("http") ).to( HttpTransporterFactory.class );
        bind(org.eclipse.aether.spi.log.LoggerFactory.class).to(Slf4jLoggerFactory.class);
    }

    @Provides
    @Singleton
    Set<RepositoryConnectorFactory> provideRepositoryConnectorFactories( @Named( "basic" ) RepositoryConnectorFactory basic )
    {
        Set<RepositoryConnectorFactory> factories = new HashSet<RepositoryConnectorFactory>();
        factories.add( basic );
        return Collections.unmodifiableSet( factories );
    }

    @Provides
    @Singleton
    Set<TransporterFactory> provideTransporterFactories( @Named( "file" ) TransporterFactory file,
                                                         @Named( "http" ) TransporterFactory http )
    {
        Set<TransporterFactory> factories = new HashSet<TransporterFactory>();
        factories.add( file );
        factories.add( http );
        return Collections.unmodifiableSet(factories);
    }
}
