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
import org.metaservice.core.config.Config;
import org.metaservice.core.provider.ProviderDispatcher;
import org.metaservice.core.archive.ArchiveParametersImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by ilo on 05.01.14.
 */
public class ProviderModule extends AbstractModule {
    public static final Logger LOGGER = LoggerFactory.getLogger(ProviderModule.class);

    private final MetaserviceDescriptor.ProviderDescriptor providerDescriptor;
    private final MetaserviceDescriptor.ParserDescriptor parserDescriptor;

    private final List<MetaserviceDescriptor.RepositoryDescriptor> repositoryDescriptors;
    private final Config config;

    public ProviderModule(
            MetaserviceDescriptor.ProviderDescriptor providerDescriptor,
            MetaserviceDescriptor.ParserDescriptor parserDescriptor,
            List<MetaserviceDescriptor.RepositoryDescriptor> repositoryDescriptors,
            Config config) {
        this.providerDescriptor = providerDescriptor;
        this.parserDescriptor = parserDescriptor;
        this.repositoryDescriptors = repositoryDescriptors;
        this.config = config;
    }


    @SuppressWarnings("unchecked")
    @Override
    public void configure() {
        LOGGER.info(providerDescriptor.toString());
        try {
            final Class providerClazz =
                    Class.forName(providerDescriptor.getClassName());
            final Class parserClazz   =
                    Class.forName(parserDescriptor.getClassName());
            final  Class modelClazz = Class.forName(providerDescriptor.getModel());

            Type providerType = Types.newParameterizedType(Provider.class,modelClazz);
            Type parserType = Types.newParameterizedType(Parser.class,modelClazz);
            Type dispatcherType = Types.newParameterizedType(ProviderDispatcher.class,modelClazz);

            bind(MetaserviceDescriptor.ParserDescriptor.class).toInstance(parserDescriptor);
            bind(MetaserviceDescriptor.ProviderDescriptor.class).toInstance(providerDescriptor);
            bind(TypeLiteral.get(providerType)).to(providerClazz);
            bind(TypeLiteral.get(parserType)).to(parserClazz);
            bind(new TypeLiteral<ProviderDispatcher>(){}).to((TypeLiteral<? extends ProviderDispatcher>) TypeLiteral.get(dispatcherType));

            Multibinder<Archive> multibinder = Multibinder.newSetBinder(binder(), Archive.class);

            for(final MetaserviceDescriptor.RepositoryDescriptor repositoryDescriptor : repositoryDescriptors){
                try {
                    final Class<? extends Archive> archiveClazz =
                            (Class<? extends Archive>) Class.forName(repositoryDescriptor.getArchiveClassName());
                    ArchiveParameters archiveParameters = new ArchiveParametersImpl(
                            repositoryDescriptor.getBaseUri(),
                            new File(config.getArchiveBasePath()+ repositoryDescriptor.getId()+"/") //todo retrieve from cmdb?
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