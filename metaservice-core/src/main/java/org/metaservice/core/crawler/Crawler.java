/*
 * Copyright 2015 Nikola Ilo
 * Research Group for Industrial Software, Vienna University of Technology, Austria
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

        String s = null;
        try{
            s = metaserviceHttpClient.get(url, MetaserviceHttpClient.CachingInstruction.NO_CACHE);
        }catch (Exception e){
            LOGGER.error("error getting {}", url,e);
        }
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