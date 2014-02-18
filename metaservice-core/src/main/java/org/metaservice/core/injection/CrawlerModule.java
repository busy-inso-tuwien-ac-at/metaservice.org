package org.metaservice.core.injection;

import com.google.inject.AbstractModule;
import org.metaservice.api.archive.Archive;
import org.metaservice.api.archive.ArchiveException;
import org.metaservice.api.archive.ArchiveParameters;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.core.config.Config;
import org.metaservice.core.archive.ArchiveParametersImpl;
import org.metaservice.core.crawler.Crawler;
import org.metaservice.core.crawler.CrawlerParameters;
import org.metaservice.core.crawler.CrawlerProvider;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by ilo on 06.01.14.
 */
public class CrawlerModule extends AbstractModule{
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
            System.err.println(crawlerDescriptor);
            System.err.println(repositoryDescriptor);
            ArchiveParameters archiveParameters = new ArchiveParametersImpl(
                    repositoryDescriptor.getBaseUri(),
                    new File(config.getArchiveBasePath() + repositoryDescriptor.getId()) //todo retrieve from cmdb?
            );
            final Class<? extends Archive> archiveClazz =
                    (Class<? extends Archive>) Class.forName(crawlerDescriptor.getArchiveClassName());
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
