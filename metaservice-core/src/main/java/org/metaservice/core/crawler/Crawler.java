package org.metaservice.core.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.metaservice.core.crawler.actions.CrawlerAction;
import org.metaservice.core.utils.MetaserviceHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by ilo on 11.12.13.
 */
public class Crawler {

    private final  static Logger LOGGER = LoggerFactory.getLogger(Crawler.class);
    private String url;
    private final List<CrawlerAction> actions = new ArrayList<>();
    private final CommonCrawlerData commonCrawlerData;
    private final MetaserviceHttpClient metaserviceHttpClient;

    public Crawler(CommonCrawlerData commonCrawlerData, MetaserviceHttpClient metaserviceHttpClient) {
        this.commonCrawlerData = commonCrawlerData;
        this.metaserviceHttpClient = metaserviceHttpClient;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void add(CrawlerAction follow) {
        actions.add(follow);
    }

    public void execute(HashSet<String> alreadyProcessed) {
        LOGGER.info("Executing: {}",url);
        if(alreadyProcessed.contains(url)){
            LOGGER.info("Already processed {}" , url);
            return;
        }

        String s = metaserviceHttpClient.get(url, MetaserviceHttpClient.CachingInstruction.NO_CACHE);
        if(s==null){
            LOGGER.error("Couldn't load {} skipping.",url);
            return;
        }
        Document document = Jsoup.parse(s, url);
        alreadyProcessed.add(url);
        for(CrawlerAction action : actions){
            action.execute(alreadyProcessed,document);
        }
    }
}