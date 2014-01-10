package org.metaservice.core.crawler;

import com.google.inject.Injector;
import org.metaservice.api.archive.Archive;
import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.archive.ArchiveException;
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
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");
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
    private final MessageProducer producer;

    @Inject
    public CrawlerRunner(
            Crawler crawler,
            Archive archive,
            ConnectionFactory connectionFactory) throws JMSException {
        this.crawler = crawler;
        this.archive = archive;

        Connection connection = connectionFactory.createConnection();
        connection.setClientID(this.getClass().getName() +"con");
        connection.start();
        session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);


        Destination destination = session.createTopic("Create");
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
        } catch (ArchiveException e) {
            e.printStackTrace();
        }
    }
}
