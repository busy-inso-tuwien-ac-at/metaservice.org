package org.metaservice.core.crawler.actions;

import org.metaservice.core.crawler.CommonCrawlerData;
import org.metaservice.core.crawler.Crawler;
import org.metaservice.core.crawler.CrawlerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* Created by ilo on 06.01.14.
*/
public class FollowAction implements CrawlerAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(FollowAction.class);

    private final String selector;
    private final CommonCrawlerData c;
    private final Crawler next;

    public FollowAction(String selector, CommonCrawlerData c, Crawler next) {
        this.selector = selector;
        this.c = c;
        this.next = next;
    }

    @Override
    public void execute(org.jsoup.nodes.Document document) {
        for(org.jsoup.nodes.Element e: document.select(selector)){
            String href= e.attr("abs:href");
            if(!href.startsWith(c.startString)){
                LOGGER.debug("Not following " + href + " because out of scope");
                continue;
            }
            LOGGER.debug(href);
            next.setUrl(href);
            next.execute();
        }
    }
}
