package org.metaservice.core.crawler;

import org.metaservice.api.archive.Archive;

import java.util.HashSet;

/**
 * Created by ilo on 11.12.13.
 */
public class CommonCrawlerData {
    public HashSet<String> alreadyProcessed = new HashSet<>();
    public String startString;
    public String baseUri;
    public Archive archive;
}
