package org.metaservice.core.injection;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.util.Types;
import org.metaservice.api.MetaserviceException;
import org.metaservice.api.archive.Archive;
import org.metaservice.api.archive.ArchiveParameters;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.parser.Parser;
import org.metaservice.api.provider.Provider;
import org.metaservice.api.messaging.Config;
import org.metaservice.core.provider.ParserFactory;
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
    private final List<MetaserviceDescriptor.ParserDescriptor> parserDescriptors;

    private final List<MetaserviceDescriptor.RepositoryDescriptor> repositoryDescriptors;
    private final Config config;

    public ProviderModule(
            MetaserviceDescriptor.ProviderDescriptor providerDescriptor,
            List<MetaserviceDescriptor.ParserDescriptor> parserDescriptors,
            List<MetaserviceDescriptor.RepositoryDescriptor> repositoryDescriptors,
            Config config) {
        this.providerDescriptor = providerDescriptor;
        this.parserDescriptors = parserDescriptors;
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
            final  Class modelClazz = Class.forName(providerDescriptor.getModel());

            Type providerType = Types.newParameterizedType(Provider.class,modelClazz);
            Type parserType = Types.newParameterizedType(ParserFactory.class,modelClazz);
            Type dispatcherType = Types.newParameterizedType(ProviderDispatcher.class,modelClazz);

            bind(MetaserviceDescriptor.ProviderDescriptor.class).toInstance(providerDescriptor);
            bind(TypeLiteral.get(providerType)).to(providerClazz);
            bind(new TypeLiteral<org.metaservice.api.messaging.dispatcher.ProviderDispatcher>(){}).to((TypeLiteral<? extends ProviderDispatcher>) TypeLiteral.get(dispatcherType));

            ParserFactory parserFactory = new ParserFactory();
            ((LinkedBindingBuilder<ParserFactory>)bind(TypeLiteral.get(parserType))).toInstance(parserFactory);
            for(final MetaserviceDescriptor.ParserDescriptor parserDescriptor : parserDescriptors){
                try {
                    String parserClass = parserDescriptor.getClassName();
                    final Class<? extends Parser> parserClazz =
                            (Class<? extends Parser>) Class.forName(parserClass);
                    Parser parser = parserClazz.getConstructor().newInstance();
                    parserFactory.addParser(parserDescriptor.getType(), parser);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException| ClassNotFoundException e) {
                    addError("Could not add parser " + parserDescriptor.getId(),e);
                }
            }
            Multibinder<Archive> multibinder = Multibinder.newSetBinder(binder(), Archive.class);
            for(final MetaserviceDescriptor.RepositoryDescriptor repositoryDescriptor : repositoryDescriptors){
                try {
                    String archiveClass;
                    if(repositoryDescriptor.getArchiveClassName() != null){
                        archiveClass =repositoryDescriptor.getArchiveClassName();
                    }else {
                        archiveClass = "org.metaservice.core.archive.GitArchive";
                    }
                    final Class<? extends Archive> archiveClazz =
                            (Class<? extends Archive>) Class.forName(archiveClass);
                    ArchiveParameters archiveParameters = new ArchiveParametersImpl(
                            repositoryDescriptor.getBaseUri(),
                            new File(config.getArchiveBasePath()+ repositoryDescriptor.getId()+"/") //todo retrieve from cmdb?
                    );
                    Archive archive = archiveClazz.getConstructor(ArchiveParameters.class).newInstance(archiveParameters);
                    multibinder.addBinding().toInstance(archive);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException| ClassNotFoundException e) {
                    addError("Could not add repository " + repositoryDescriptor.getId(),e);
                }
            }
        } catch (ClassNotFoundException e) {
            addError(e);
        }
    }
}