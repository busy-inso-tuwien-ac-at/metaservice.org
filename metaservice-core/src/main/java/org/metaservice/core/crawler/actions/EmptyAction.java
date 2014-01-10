package org.metaservice.core.crawler.actions;

import org.jsoup.nodes.Document;

import java.util.HashSet;

/**
* Created by ilo on 06.01.14.
*/
public class EmptyAction implements CrawlerAction {
    @Override
    public void execute(HashSet<String> alreadyProcessed, Document document) {

    }
}
