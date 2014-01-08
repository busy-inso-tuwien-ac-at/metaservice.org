package org.metaservice.core.injection;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.util.Types;
import org.metaservice.api.archive.Archive;
import org.metaservice.api.archive.ArchiveParameters;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.parser.Parser;
import org.metaservice.api.provider.Provider;
import org.metaservice.core.Dispatcher;
import org.metaservice.core.archive.ArchiveParametersImpl;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by ilo on 05.01.14.
 */
public class ProviderModule extends AbstractModule {
    private final MetaserviceDescriptor.ProviderDescriptor providerDescriptor;
    private final MetaserviceDescriptor.ParserDescriptor parserDescriptor;

    private final List<MetaserviceDescriptor.RepositoryDescriptor> repositoryDescriptors;

    public ProviderModule(
            MetaserviceDescriptor.ProviderDescriptor providerDescriptor,
            MetaserviceDescriptor.ParserDescriptor parserDescriptor,
            List<MetaserviceDescriptor.RepositoryDescriptor> repositoryDescriptors
    ) {
        this.providerDescriptor = providerDescriptor;
        this.parserDescriptor = parserDescriptor;
        this.repositoryDescriptors = repositoryDescriptors;
    }


    @SuppressWarnings("unchecked")
    @Override
    public void configure(){
        configure2();
    }

    public  <T> void configure2() {
        try {
            final Class providerClazz =
                     Class.forName(providerDescriptor.getClassName());
            final Class parserClazz   =
                    Class.forName(parserDescriptor.getClassName());
            final  Class modelClazz = Class.forName("org.apache.maven.model.Model"); //Todo read from descriptor

            Type providerType = Types.newParameterizedType(Provider.class,modelClazz);
            Type parserType = Types.newParameterizedType(Parser.class,modelClazz);
            Type dispatcherType = Types.newParameterizedType(Dispatcher.class,modelClazz);

            bind(TypeLiteral.get(providerType)).to(providerClazz);
            bind(TypeLiteral.get(parserType)).to(parserClazz);
            bind(new TypeLiteral<Dispatcher>(){}).to((TypeLiteral<? extends Dispatcher>) TypeLiteral.get(dispatcherType));

            Multibinder<Archive> multibinder = Multibinder.newSetBinder(binder(), Archive.class);

            for(final MetaserviceDescriptor.RepositoryDescriptor repositoryDescriptor : repositoryDescriptors){
                try {
                    final Class<? extends Archive> archiveClazz =
                            (Class<? extends Archive>) Class.forName(repositoryDescriptor.getArchiveClassName());
                    ArchiveParameters archiveParameters = new ArchiveParametersImpl(
                            repositoryDescriptor.getBaseUri(),
                            new File("/opt/metaservice_data/" + repositoryDescriptor.getId()+"/") //todo retrieve from cmdb?
                    );
                    Archive archive = archiveClazz.getConstructor(ArchiveParameters.class).newInstance(archiveParameters);
                    multibinder.addBinding().toInstance(archive);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException| ClassNotFoundException e) {
                    addError("Could not add repository {}", repositoryDescriptor.getId());
                }
            }
        } catch (ClassNotFoundException e) {
            addError(e);
        }
    }
}