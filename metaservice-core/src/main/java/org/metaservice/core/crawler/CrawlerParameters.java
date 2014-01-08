package org.metaservice.core.crawler;

import org.metaservice.api.archive.Archive;

/**
 * Created by ilo on 05.01.14.
 */
public class CrawlerParameters {
    private Archive archive;
    private String starturi;
    private String name;

    public Archive getArchive() {
        return archive;
    }

    public void setArchive(Archive archive) {
        this.archive = archive;
    }

    public String getStarturi() {
        return starturi;
    }

    public void setStarturi(String starturi) {
        this.starturi = starturi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
