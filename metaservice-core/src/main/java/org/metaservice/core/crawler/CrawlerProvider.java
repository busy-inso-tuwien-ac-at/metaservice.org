package org.metaservice.core.crawler;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.jetbrains.annotations.NotNull;
import org.metaservice.core.crawler.actions.CrawlerAction;
import org.metaservice.core.crawler.actions.EmptyAction;
import org.metaservice.core.crawler.actions.FetchAction;
import org.metaservice.core.crawler.actions.FollowAction;
import org.metaservice.core.utils.MetaserviceHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

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
                    crawler.add(createFollow(child,commonCrawlerData));
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
        }
        throw  new RuntimeException("Unknown Selector " +  element.toString() );
    }

    private CrawlerAction createFollow(final Element child, final CommonCrawlerData c) {
        final Crawler next = doIt(child,c);
        final String selector = getSelector(child);

        boolean recursive = false;
        if(child.hasAttribute("recursive") && "true".equals(child.getAttribute("recursive"))){
            recursive = true;
        }

        final CrawlerAction crawlerAction = new FollowAction(selector, c, next);

        if(recursive){
            next.add(crawlerAction);
        }

        return crawlerAction;
    }

    @Override
    public Crawler get() {
        try {
            return createCrawler();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

}
