package org.metaservice.core.crawler.actions;

import org.jsoup.nodes.Document;

import java.util.HashSet;

/**
* Created by ilo on 06.01.14.
*/
public interface CrawlerAction {
    public void execute(HashSet<String> alreadyProcessed, Document document);
}
