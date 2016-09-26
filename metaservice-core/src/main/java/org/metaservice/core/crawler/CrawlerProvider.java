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

import com.google.inject.Provider;
import org.jetbrains.annotations.NotNull;
import org.metaservice.core.crawler.actions.CrawlerAction;
import org.metaservice.core.crawler.actions.FetchAction;
import org.metaservice.core.crawler.actions.FollowAction;
import org.metaservice.core.utils.MetaserviceHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by ilo on 11.12.13.
 */
public class CrawlerProvider implements Provider<Crawler> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerProvider.class);
    private final MetaserviceHttpClient metaserviceHttpClient;
    private final CrawlerParameters crawlerParameters;

    @Inject
    public CrawlerProvider(
            @NotNull MetaserviceHttpClient metaserviceHttpClient,
            @NotNull CrawlerParameters crawlerParameters){
        this.crawlerParameters = crawlerParameters;
        this.metaserviceHttpClient = metaserviceHttpClient;
    }

    private Crawler createCrawler() throws IOException, SAXException, ParserConfigurationException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(this.getClass().getResourceAsStream("/metaservice.xml"));
//        Document doc =dBuilder.parse(new FileInputStream("metaservice-core-deb/src/main/resources/metaservice.xml"));
        //optional, but recommended
        //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        doc.getDocumentElement().normalize();

        NodeList producers =doc.getElementsByTagName("crawler");

        for(int i = 0; i < producers.getLength();i++){
            Element e = (Element) producers.item(i);
            if(e.getAttribute("id").equals(crawlerParameters.getName())){
                CommonCrawlerData c = new CommonCrawlerData();
                c.startString = crawlerParameters.getStarturi();
                c.baseUri = crawlerParameters.getArchive().getSourceBaseUri();
                c.archive = crawlerParameters.getArchive();
                Crawler crawler = doIt(e,c);
                crawler.setUrl(c.startString);
                return crawler;
            }
        }
        throw new RuntimeException("NOT FOUND CRAWLER");
    }

    private Crawler doIt(Element e,CommonCrawlerData commonCrawlerData) {
        Crawler crawler = new Crawler(commonCrawlerData, metaserviceHttpClient);
        for(int i = 0 ; i < e.getChildNodes().getLength();i++){
            if(! (e.getChildNodes().item(i) instanceof Element))
                continue;
            Element  child = (Element) e.getChildNodes().item(i);
            switch (child.getNodeName()){
                case "follow":
                    crawler.add(createFollow(crawler,child,commonCrawlerData));
                    break;
                case "fetch":
                    crawler.add(createFetch(child,commonCrawlerData));
                    break;
            }
        }


        return crawler;
    }

    private CrawlerAction createFetch(Element child, final CommonCrawlerData c) {
        final String selector = getSelector(child);
        final boolean gz = child.hasAttribute("unpack") &&"gz".equals(child.getAttribute("unpack"));
        return new FetchAction(selector, c, gz, metaserviceHttpClient);
    }

    private String getSelector(final Element element){
        if(element.hasAttribute("endsWith")){
            return "a[abs:href$="+element.getAttribute("endsWith") +"]";
        }else if (element.hasAttribute("matches")){
            return "a[abs:href~="+ element.getAttribute("matches")  +"]";
        } else if (element.hasAttribute("exactly")){
            return "a[href="+ element.getAttribute("exactly") +"]";
        } else if(element.hasAttribute("selector")){
            return element.getAttribute("selector");
        }else if (element.hasAttribute("link")){
            return "link[title=" + element.getAttribute("link")+"]";
        }
        throw  new RuntimeException("Unknown Selector " +  element.toString() );
    }

    private CrawlerAction createFollow(Crawler crawler, final Element child, final CommonCrawlerData c) {
        final Crawler next = doIt(child,c);
        final String selector = getSelector(child);


        boolean recursive = false;
        if(child.hasAttribute("recursive") && "true".equals(child.getAttribute("recursive"))){
            recursive = true;
        }
        boolean skippable = false;
        if(child.hasAttribute("skippable") && "true".equals(child.getAttribute("skippable"))){
            skippable = true;
        }


        final CrawlerAction crawlerAction = new FollowAction(crawler,skippable,selector, c, next);

        if(recursive){
            next.add(crawlerAction);
        }

        return crawlerAction;
    }

    @Override
    public Crawler get() {
        try {
            return createCrawler();
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

}
