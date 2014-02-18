package org.metaservice.core.crawler;

import com.google.inject.Injector;
import org.metaservice.api.archive.Archive;
import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.archive.ArchiveException;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.core.injection.InjectorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.inject.Inject;
import javax.jms.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
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
    private final Session session;
    private final Connection connection;
    private final MessageProducer producer;
    private final MetaserviceDescriptor.RepositoryDescriptor repositoryDescriptor;

    @Inject
    public CrawlerRunner(
            Crawler crawler,
            Archive archive,
            ConnectionFactory connectionFactory,
            MetaserviceDescriptor.RepositoryDescriptor repositoryDescriptor
    ) throws JMSException {
        this.crawler = crawler;
        this.archive = archive;
        this.repositoryDescriptor = repositoryDescriptor;

        connection = connectionFactory.createConnection();
        connection.setClientID(this.getClass().getName() +"con");
        connection.start();
        session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);


        Destination destination = session.createTopic("VirtualTopic.Create");
        producer = session.createProducer(destination);
    }

    public  void run(){
        crawler.execute(new HashSet<String>());
        try {
            if(archive.commitContent()){
                String commitTime = archive.getLastCommitTime();
                String sourceBaseUri =  archive.getSourceBaseUri();
                for(String s : archive.getLastChangedPaths()){
                    try{
                        LOGGER.info("Sending " + commitTime +" s " + s);
                        ArchiveAddress archiveAddress = new ArchiveAddress(sourceBaseUri,commitTime,s);
                        archiveAddress.setParameters(repositoryDescriptor.getProperties());
                        ObjectMessage message = session.createObjectMessage();
                        message.setObject(archiveAddress);
                        producer.send(message);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            } else{
                LOGGER.info("Nothing changed");
            }
            producer.close();
            connection.close();
        } catch (ArchiveException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
