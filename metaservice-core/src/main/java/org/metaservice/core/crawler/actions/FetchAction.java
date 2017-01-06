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

package org.metaservice.core.crawler.actions;

import org.jsoup.nodes.Document;
import org.metaservice.api.archive.ArchiveException;
import org.metaservice.core.crawler.CommonCrawlerData;
import org.metaservice.core.utils.MetaserviceHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.zip.GZIPInputStream;

/**
 * Created by ilo on 06.01.14.
 */
public class FetchAction implements CrawlerAction {
    private final Logger LOGGER = LoggerFactory.getLogger(FetchAction.class);


    private final String selector;
    private final CommonCrawlerData c;
    private final boolean gz;
    private final MetaserviceHttpClient metaserviceHttpClient;

    public FetchAction(String selector, CommonCrawlerData c, boolean gz, MetaserviceHttpClient metaserviceHttpClient) {
        this.selector = selector;
        this.c = c;
        this.gz = gz;
        this.metaserviceHttpClient = metaserviceHttpClient;
    }

    @Override
    public void execute(HashSet<String> alreadyProcessed, Document document) {
        LOGGER.info("FETCH {} gz:{}",selector,gz);
        boolean found = false;
        for(org.jsoup.nodes.Element e: document.select(selector)){
            found = true;
            String href= e.attr("abs:href");
            LOGGER.debug(href);

            byte[] data = metaserviceHttpClient.getBinary(href, MetaserviceHttpClient.CachingInstruction.NO_CACHE);
            if(data == null){
                LOGGER.error("could not retrieve "+ href + " -> SKIPPING");
                return;
            }
            if(!href.startsWith(c.baseUri)){
                LOGGER.warn("fetched uri not starting with baseUri");
            }
            String localPath = href.replaceFirst(c.baseUri,"");
            if(gz){
                localPath = localPath.replace(".gz","");
            }

            try (
                    InputStream fileStream =
                            (gz)
                                    ?
                                    new GZIPInputStream(new ByteArrayInputStream(data))
                                    :
                                    new ByteArrayInputStream(data)
            ) {
                c.archive.addContent(localPath,fileStream);
            } catch (IOException | ArchiveException e1) {
                e1.printStackTrace();
            }
        }
        if(!found){
            LOGGER.warn("Selector {} did not match",selector);
        }
    }
}
