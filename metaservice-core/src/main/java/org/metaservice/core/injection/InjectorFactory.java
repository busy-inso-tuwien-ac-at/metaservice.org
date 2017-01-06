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
        List<MetaserviceDescriptor.ParserDescriptor> selectedParsers = new ArrayList<>();
        List<MetaserviceDescriptor.RepositoryDescriptor> selectedRepositories = new ArrayList<>();


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


        for(MetaserviceDescriptor.ParserDescriptor parserDescriptor : descriptor.getParserList()){
            if(selectedProvider.getModel().equals(parserDescriptor.getModel())){
                selectedParsers.add(parserDescriptor);
            }
        }

        if(selectedParsers.isEmpty()){
            LOGGER.error("FATAL Error: No compatible parser of model '{}' found, choose one of {}", selectedProvider.getModel(),descriptor.getParserList());
            System.exit(-1);
        }
        LOGGER.info("Found {} parsers ", selectedParsers.size());

        for(MetaserviceDescriptor.ParserDescriptor selectedParser : selectedParsers) {
            for (MetaserviceDescriptor.RepositoryDescriptor repositoryDescriptor : descriptor.getRepositoryList()) {
                if (repositoryDescriptor.getType().equals(selectedParser.getType())) {
                    if (repositoryDescriptor.getActive()) {
                        selectedRepositories.add(repositoryDescriptor);
                    }
                }
            }
        }

        if(selectedRepositories.isEmpty()){
            LOGGER.error("FATAL Error: No compatible repository of type '{}' found, choose one of {}", selectedParsers, descriptor.getRepositoryList());
            System.exit(-1);
        }
        LOGGER.info("Found {} repositories ", selectedRepositories.size());

        injector = Guice.createInjector(
                new MetaserviceModule(),
                new ProviderModule(selectedProvider,selectedParsers,selectedRepositories, injector.getInstance(Config.class)));
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
