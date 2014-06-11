package org.metaservice.core.crawler;

import com.google.inject.Injector;
import org.metaservice.api.archive.Archive;
import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.archive.ArchiveException;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.core.injection.InjectorFactory;
import org.metaservice.api.messaging.MessageHandler;
import org.metaservice.api.messaging.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.inject.Inject;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by ilo on 11.12.13.
 */
public class CrawlerRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerRunner.class);

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, InterruptedException, ArchiveException {
        if(args.length != 1)
        {
            LOGGER.error("Need crawler id");
            System.exit(-1);
        }
        String id=  args[0];

        Injector injector = InjectorFactory.getInjectorForCrawler(id);


        CrawlerRunner crawlerRunner = injector.getInstance(CrawlerRunner.class);
        crawlerRunner.run();
    }


    private final Crawler crawler;
    private final Archive archive;
    private final MessageHandler producer;
    private final MetaserviceDescriptor.RepositoryDescriptor repositoryDescriptor;
    private final MetaserviceDescriptor metaserviceDescriptor;

    @Inject
    public CrawlerRunner(
            final Crawler crawler,
            final Archive archive,
            final MessageHandler producer,
            final MetaserviceDescriptor.RepositoryDescriptor repositoryDescriptor,
            final MetaserviceDescriptor metaserviceDescriptor
    ) {
        this.crawler = crawler;
        this.archive = archive;
        this.repositoryDescriptor = repositoryDescriptor;
        this.metaserviceDescriptor = metaserviceDescriptor;
        this.producer = producer;
        try {
            producer.init();
        } catch (MessagingException e) {
            LOGGER.error("messaging", e);
        }
    }

    public  void run(){
        crawler.execute(new HashSet<String>());
        try {
            if(archive.commitContent()){
                Date commitTime = archive.getLastCommitTime();
                String sourceBaseUri =  archive.getSourceBaseUri();
                for(String s : archive.getLastChangedPaths()){
                    try{
                        LOGGER.info("Sending " + commitTime +" s " + s);
                        ArchiveAddress archiveAddress = new ArchiveAddress(
                                repositoryDescriptor.getId(),
                                sourceBaseUri,
                                commitTime,
                                s);
                        archiveAddress.setParameters(repositoryDescriptor.getProperties());
                        producer.send(archiveAddress);
                    }  catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            } else{
                LOGGER.info("Nothing changed");
            }
            producer.close();
        } catch (ArchiveException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
