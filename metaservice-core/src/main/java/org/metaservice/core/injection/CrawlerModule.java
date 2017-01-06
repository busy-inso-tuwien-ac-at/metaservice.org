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
import org.metaservice.api.archive.Archive;
import org.metaservice.api.archive.ArchiveException;
import org.metaservice.api.archive.ArchiveParameters;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.Config;
import org.metaservice.core.archive.ArchiveParametersImpl;
import org.metaservice.core.crawler.Crawler;
import org.metaservice.core.crawler.CrawlerParameters;
import org.metaservice.core.crawler.CrawlerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by ilo on 06.01.14.
 */
public class CrawlerModule extends AbstractModule{
    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerModule.class);
    private final MetaserviceDescriptor.RepositoryDescriptor repositoryDescriptor;
    private final MetaserviceDescriptor.CrawlerDescriptor crawlerDescriptor;
    private final Config config;

    public CrawlerModule(MetaserviceDescriptor.RepositoryDescriptor repositoryDescriptor, MetaserviceDescriptor.CrawlerDescriptor crawlerDescriptor, Config config) {
        this.repositoryDescriptor = repositoryDescriptor;
        this.crawlerDescriptor = crawlerDescriptor;
        this.config = config;
    }

    @Override
    protected void configure() {
        try {
            LOGGER.debug("crawlerdescriptor {}",crawlerDescriptor);
            LOGGER.debug("repodescriptor {}",repositoryDescriptor);
            ArchiveParameters archiveParameters = new ArchiveParametersImpl(
                    repositoryDescriptor.getBaseUri(),
                    new File(config.getArchiveBasePath() + repositoryDescriptor.getId()) //todo retrieve from cmdb?
            );

            String archiveClass;
            if(crawlerDescriptor.getArchiveClassName() != null){
                archiveClass =crawlerDescriptor.getArchiveClassName();
            }else {
                archiveClass = "org.metaservice.core.archive.GitArchive";
            }
            final Class<? extends Archive> archiveClazz =
                    (Class<? extends Archive>) Class.forName(archiveClass);
            Archive archive = archiveClazz.getConstructor(ArchiveParameters.class).newInstance(archiveParameters);
            bind(Archive.class).toInstance(archive);


            CrawlerParameters parameters = new CrawlerParameters();
            parameters.setName(repositoryDescriptor.getCrawler());
            parameters.setStarturi(repositoryDescriptor.getStartUri());
            parameters.setArchive(archive);
            bind(MetaserviceDescriptor.RepositoryDescriptor.class).toInstance(repositoryDescriptor);
            bind(MetaserviceDescriptor.CrawlerDescriptor.class).toInstance(crawlerDescriptor);
            bind(CrawlerParameters.class).toInstance(parameters);
            bind(Crawler.class).toProvider(CrawlerProvider.class);
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            if(e.getTargetException() instanceof ArchiveException){
                ArchiveException e2 = (ArchiveException) e.getTargetException();
                e.printStackTrace();
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
