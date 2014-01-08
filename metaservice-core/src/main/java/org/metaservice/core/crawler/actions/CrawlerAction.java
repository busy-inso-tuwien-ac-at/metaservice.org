package org.metaservice.core.crawler.actions;

/**
* Created by ilo on 06.01.14.
*/
public interface CrawlerAction {
    public void execute(org.jsoup.nodes.Document document);
}
