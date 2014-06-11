package org.metaservice.core.injection;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilo on 05.01.14.
 */
public class InjectorFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(InjectorFactory.class);


    public static Injector getInjectorForProvider(String providerId){
        MetaserviceDescriptor.ProviderDescriptor selectedProvider = null;
        MetaserviceDescriptor.ParserDescriptor selectedParser = null;


        Injector injector = Guice.createInjector(new MetaserviceModule());
        MetaserviceDescriptor descriptor = injector.getInstance(MetaserviceDescriptor.class);

        for(MetaserviceDescriptor.ProviderDescriptor providerDescriptor : descriptor.getProviderList()){
            if(providerId.equals(providerDescriptor.getId())){
                selectedProvider = providerDescriptor;
                break;
            }
        }

        if(selectedProvider == null){
            LOGGER.error("FATAL Error: No provider with id {} found, choose one of {}", providerId, descriptor.getProviderList());
            System.exit(-1);
        }


        List<MetaserviceDescriptor.RepositoryDescriptor> selectedRepositories = new ArrayList<>();
        for(MetaserviceDescriptor.RepositoryDescriptor repositoryDescriptor : descriptor.getRepositoryList()){
            if(repositoryDescriptor.getType().equals(selectedProvider.getType())){
                if(repositoryDescriptor.getActive() && selectedProvider.getType().equals(repositoryDescriptor.getType())){
                    selectedRepositories.add(repositoryDescriptor);
                }
            }
        }

        if(selectedRepositories.isEmpty()){
            LOGGER.error("FATAL Error: No compatible repository of type '{}' found, choose one of {}", selectedProvider.getType(),descriptor.getRepositoryList());
            System.exit(-1);
        }

        for(MetaserviceDescriptor.ParserDescriptor parserDescriptor : descriptor.getParserList()){
            if(selectedProvider.getType().equals(parserDescriptor.getType())){
                selectedParser = parserDescriptor;
                break;
            }
        }

        if(selectedParser == null){
            LOGGER.error("FATAL Error: No compatible parser of type '{}' found, choose one of {}", selectedProvider.getType(),descriptor.getParserList());
            System.exit(-1);
        }


        injector = Guice.createInjector(
                new MetaserviceModule(),
                new ProviderModule(selectedProvider,selectedParser,selectedRepositories, injector.getInstance(Config.class)));
        return injector;
    }

    public static Injector getInjectorForPostProcessor(String postProcessorId) {
        MetaserviceDescriptor.PostProcessorDescriptor selectedPostProcessor = null;


        Injector injector = Guice.createInjector(new MetaserviceModule());
        MetaserviceDescriptor descriptor = injector.getInstance(MetaserviceDescriptor.class);

        for(MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor : descriptor.getPostProcessorList()){
            if(postProcessorDescriptor.getId().equals(postProcessorId)){
                selectedPostProcessor = postProcessorDescriptor;
                break;
            }
        }

        if(selectedPostProcessor == null){
            LOGGER.error("FATAL Error: No postprocessor with id {} found", postProcessorId);
            System.exit(-1);
        }

        injector = Guice.createInjector(
                new MetaserviceModule(),
                new PostProcessorModule(selectedPostProcessor));
        return injector;
    }

    public static Injector getInjectorForCrawler(String repositoryId) {
        Injector injector = Guice.createInjector(new MetaserviceModule());
        MetaserviceDescriptor descriptor = injector.getInstance(MetaserviceDescriptor.class);
        MetaserviceDescriptor.RepositoryDescriptor selectedRepository =null;
        for(MetaserviceDescriptor.RepositoryDescriptor repositoryDescriptor : descriptor.getRepositoryList()){
            if(repositoryDescriptor.getId().equals(repositoryId)){
                selectedRepository = repositoryDescriptor;
                break;
            }
        }
        if(selectedRepository == null){

            LOGGER.error("FATAL Error: No repository with id {} found, choose one of {} ", repositoryId,descriptor.getRepositoryList());
            System.exit(-1);
        }
        MetaserviceDescriptor.CrawlerDescriptor selectedCrawler = null;

        for(MetaserviceDescriptor.CrawlerDescriptor crawlerDescriptor : descriptor.getCrawlerList()){
            if(crawlerDescriptor.getId().equals(selectedRepository.getCrawler())){
                selectedCrawler = crawlerDescriptor;
                break;
            }
        }
        if(selectedCrawler == null){
            LOGGER.error("FATAL Error: No crawler with id {} found", selectedRepository.getCrawler());
            System.exit(-1);
        }
        injector = Guice.createInjector(
                new MetaserviceModule(),
                new CrawlerModule(selectedRepository,selectedCrawler, injector.getInstance(Config.class)));
        return injector;
    }

    public static Injector getBasicInjector() {
        return Guice.createInjector(
                new MetaserviceModule());
    }
}
