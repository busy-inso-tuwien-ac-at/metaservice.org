package org.metaservice.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.metaservice.core.deb.utils.GitUtil;
import org.metaservice.core.deb.utils.MetaserviceHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

public class PeriodicalDebCrawler {
    private final static Logger LOGGER = LoggerFactory.getLogger(PeriodicalDebCrawler.class);
    private boolean dryRun = true;
    private static  String[] archs = {"amd64", "armel","armhf","i386","ia64","mipsel","mips","powerpc","s390x","s390","sparc","alpha","arm","AVR32","hppa","m32","m68k","sh"};

    public static void main(String[] args) throws IOException, InterruptedException {
        PeriodicalDebCrawler periodicalDebCrawler = new PeriodicalDebCrawler();
        periodicalDebCrawler.run("http://archive.ubuntu.com/ubuntu/dists/",new String[]{"multiverse","main","universe","restricted"});
    }
    MetaserviceHttpClient metaserviceHttpClient = new MetaserviceHttpClient();

    private void run(String startString,String [] licenses) {
        HashSet<String> parsed = new HashSet<String>();
        LinkedList<String> toParse = new LinkedList<String>();
        HashSet<String> dists = new HashSet<String>();
        toParse.add(startString);
        while(toParse.size() >0){
            String uri =  toParse.pop();
            try{
                String s = metaserviceHttpClient.get(uri);
                if(s==null){
                    LOGGER.error("Couldn't load " + uri + " skipping.");
                    continue;
                }
                Document document = Jsoup.parse(s, uri);
                parsed.add(uri);
                for(Element e : document.select("a[href$=/]")){
                    String absHref= e.attr("abs:href");
                    String href = e.attr("href");
                    if(!dists.contains(href) && !href.startsWith("/")&& !href.startsWith(".")){
                        if(uri.endsWith("dists/") /*&& !href.contains("sid") && !href.contains("experimental")*/){
                            dists.add(href);
                            LOGGER.info(href);
                            for(String license : licenses){
                                String url =  absHref+license +"/";
                                LOGGER.info("adding (lic) {}",url);
                                toParse.add(url);
                            }
                        }
                        for(String license : licenses){
                            if(uri.endsWith(license +"/")){
                                if(href.startsWith("binary-")){
                                    for(String arch :archs)
                                    {
                                        if(href.contains(arch)){
                                            LOGGER.info("adding (archdir) {}",absHref);
                                            toParse.add(absHref);
                                        }
                                    }
                                }
                                if(href.startsWith("source")){
                                    LOGGER.info("adding (archdir) {}",absHref);
                                    toParse.add(absHref);
                                }
                            }
                        }

                    }


                }

                for(Element e: document.select("a[abs:href$=Packages.gz] , a[abs:href$=Sources.gz]")){
                    String href= e.attr("abs:href");
                    //only if this seems to be a non duplicate
                    if(document.select("a:contains(prev change)").size() == 0 ||document.select("a:contains(prev change)").get(0).attr("abs:href").equals(document.select("a:contains(prev):not(:contains(change))").get(0).attr("abs:href")))
                    {
                        LOGGER.info("RESULT processing ... {} {} ",i++ ,href);
                        if(!dryRun){
                            processFileToParse(href);
                        }
                    }
                }
            } catch (RuntimeException exception){
                LOGGER.error("RUNTIME EXCEPTION ",exception);
                throw exception;
            }
        }
    }

    private int i =0;

    private void processFileToParse(String href) {
        //To change body of created methods use File | Settings | File Templates.
    }


    GitUtil gitUtil;

    public PeriodicalDebCrawler() throws IOException, InterruptedException {
       // gitUtil = new GitUtil(new File(""));
    }

}
