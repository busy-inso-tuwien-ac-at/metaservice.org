/*
 * Copyright 2015 Nikola Ilo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaservice.core.crawler.actions;

import org.jsoup.nodes.Document;
import org.metaservice.core.crawler.CommonCrawlerData;
import org.metaservice.core.crawler.Crawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;

/**
* Created by ilo on 06.01.14.
*/
public class FollowAction implements CrawlerAction {
    private final Logger LOGGER = LoggerFactory.getLogger(FollowAction.class);

    private final String selector;
    private final CommonCrawlerData c;
    private final Crawler next;
    private final Crawler current;
    private final boolean skippable;

    public FollowAction(Crawler current,boolean skippable,String selector, CommonCrawlerData c, Crawler next) {
        this.selector = selector;
        this.c = c;
        this.next = next;
        this.skippable = skippable;
        this.current= current;
    }

    @Override
    public void execute(HashSet<String> alreadyProcessed, Document document) {
        LOGGER.info("FOLLOW");
        for(org.jsoup.nodes.Element e: document.select(selector)){
            String href= e.attr("abs:href");
            if(!href.startsWith(c.startString)){
                LOGGER.info("Not following " + href + " because out of scope");
                continue;
            }
            LOGGER.debug(href);
            next.setUrl(href);
            next.execute(new HashSet<>(alreadyProcessed));
        }
        if(skippable){
            LOGGER.info("skipping directly to next crawler");
            next.setUrl(current.getUrl());

            //remove current url such that skipping doesn't abort immediately
            alreadyProcessed = new HashSet<>(alreadyProcessed);
            alreadyProcessed.remove(current.getUrl());
            next.execute(alreadyProcessed);
        }
    }
}
