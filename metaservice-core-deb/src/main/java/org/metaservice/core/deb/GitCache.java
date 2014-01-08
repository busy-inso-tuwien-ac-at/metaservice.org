package org.metaservice.core.deb;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.metaservice.core.utils.GitUtil;
import org.metaservice.core.utils.MetaserviceHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class GitCache {
    private static Logger LOGGER = LoggerFactory.getLogger(GitCache.class);

    public static void main(String[] args) throws IOException, InterruptedException, GitUtil.GitException {
        if(args.length == 0 || "debian".equals(args[0])){
            startString =  "http://snapshot.debian.org/archive/debian/20050312T000000Z/dists/";
            workdir = new File("/opt/crawlertest/data/");
            licenses = new String[]{"main","contrib","non-free"};
            dryRun = false;
        }else if("security".equals(args[0])){
            startString =  "http://snapshot.debian.org/archive/debian-security/20050313T000000Z/dists/";
            workdir = new File("/opt/crawlertest/data_security/");
            licenses  = new String[]{"updates/main","updates/contrib","updates/non-free"};
            dryRun = false;
        }else if("backports".equals(args[0])){
            startString =  "http://snapshot.debian.org/archive/debian-backports/20090114T125110Z/dists/";
            workdir = new File("/opt/crawlertest/data_backports/");
            licenses = new String[]{"main","contrib","non-free"};
            dryRun = false;
        }else if("ports".equals(args[0])){
            startString =  "http://snapshot.debian.org/archive/debian-ports/20081002T000000Z/dists/";
            workdir = new File("/opt/crawlertest/data_ports/");
            licenses = new String[]{"main","contrib","non-free"};
            dryRun = false;
        }else if("volatile".equals(args[0])){
            startString =  "http://snapshot.debian.org/archive/debian-volatile/20050313T000000Z/dists/";
            workdir = new File("/opt/crawlertest/data_volatile/");
            licenses = new String[]{"volatile/main","volatile/contrib","volatile/non-free"};
            dryRun = false;
        }else{
            LOGGER.error("NOT FOUND " +args[0]);
            return;
        }
       /* if(workdir.exists())             {
            LOGGER.error("WORKDIR {} EXISTS",workdir);
            return;
        }
        if(!dryRun) {
            workdir.mkdirs();

        } */
        GitCache git = new GitCache();
        git.runDiscovery();
        git.createDirectory();
    }


    // Paramters
    private static  boolean dryRun = true;
    private static  String startString;
    private static  File workdir;
    private static  String[] archs = {"amd64", "armel","armhf","i386","ia64","mipsel","mips","powerpc","s390x","s390","sparc","alpha","arm","AVR32","hppa","m32","m68k","sh","ppc64","x32"};
    private static  String[] licenses;




    private GitUtil gitUtil;

    public GitCache() throws  GitUtil.GitException {
        if(!dryRun)
            gitUtil= new GitUtil(workdir);
    }

    private void createDirectory() throws IOException, InterruptedException, GitUtil.GitException {
        int gcCounter = 0;
       /* if(!dryRun)
            gitUtil.initRepository();
         */
        for(long time : getAccessOrder()){
            if(time <= 20110505052259l)
                continue;

            LOGGER.info("time: {}",toTimeString(time));
            for(String uri : processingMap.get(time) )
            {
                if(uri.startsWith("http://snapshot.debian.org/archive/debian/20110505T101429Z"))
                    continue;

                File f = getResultPath(uri);
                f.getParentFile().mkdirs();
                f.delete();
                if(!dryRun){
                    byte[] data = clientMetaservice.getBinary(uri);
                    if(data == null){
                        LOGGER.error("could not retrieve "+ uri + " -> SKIPPING");
                        continue;
                    }
                    InputStream fileStream = new ByteArrayInputStream(data);
                    InputStream gzipStream = new GZIPInputStream(fileStream);
                    FileOutputStream outputStream = new FileOutputStream(f);
                    IOUtils.copy(gzipStream, outputStream);
                    gzipStream.close();
                    outputStream.close();
                    gzipStream.close();
                }else{
                    if(!clientMetaservice.isCached(uri)){
                        byte[] data = clientMetaservice.getBinary(uri);
                        if(data == null){
                            LOGGER.error("could not retrieve "+ uri + " -> SKIPPING");
                            continue;
                        }
                    }else{
                        LOGGER.info("IS IN CACHE {}",uri);
                    }
                }

                LOGGER.info("retrieving {} to {}",uri,getResultPath(uri));
            }

            if(!dryRun && gitUtil.hasChangesToCommit())
            {
                LOGGER.info("workingcopy dirty - will commit");
                gitUtil.commitChanges(toTimeString(time));
                if(gcCounter++ == 50){
                    gitUtil.gc();

                    gcCounter =0;
                }
            }      else{
                LOGGER.info("workingcopy clean - will not commit");
            }
        }
    }

    @NotNull
    private File getResultPath(@NotNull String url) {
        return new File(url.replaceFirst("http://snapshot.debian.org/archive/[^/]*/........T......Z", workdir.getAbsolutePath()).replace(".gz",""));
    }

    @NotNull
    MetaserviceHttpClient clientMetaservice = new MetaserviceHttpClient();

    int i= 0;
    public void runDiscovery(){
        HashSet<String> parsed = new HashSet<>();
        LinkedList<String> toParse = new LinkedList<>();
        HashSet<String> dists = new HashSet<>();
        toParse.add(startString);
        while(toParse.size() >0){
            String uri =  toParse.pop();
            try{
                String s = clientMetaservice.get(uri);
                if(s==null){
                    LOGGER.error("Couldn't load " + uri + " skipping.");
                    continue;
                }
                Document document = Jsoup.parse(s,uri);
                parsed.add(uri);
                for(Element e : document.select("a:contains(next change)")){
                    String href= e.attr("abs:href");
                    if(!parsed.contains(href) &&!toParse.contains (href)){
                        LOGGER.info("adding (next) ", href);
                        toParse.push(href);
                    }
                }

                for(Element e : document.select("a[href$=/]")){
                    String absHref= e.attr("abs:href");
                    String href = e.attr("href");
                    if(!dists.contains(href) && !href.startsWith("/")&& !href.startsWith(".")  /* &&!toParse.contains (href) */){
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
                        processFileToParse(href);
                    }
                }
            } catch (RuntimeException exception){
                LOGGER.error("RUNTIME EXCEPTION ",exception);
                throw exception;
            }
        }
    }


    @NotNull
    HashMap<Long,Set<String>> processingMap = new HashMap<>();
    private void processFileToParse(@NotNull String href) {
        long timeConstant =getTimeConstant(href);
        if(!processingMap.containsKey(timeConstant)){
            processingMap.put(timeConstant,new HashSet<String>());
        }
        processingMap.get(timeConstant).add(href);
    }

    @NotNull
    private List<Long> getAccessOrder(){
        ArrayList<Long> list = new ArrayList<>();
        list.addAll(processingMap.keySet());
        Collections.sort(list);
        return list;
    }

    private long getTimeConstant(@NotNull String href){
       int start = href.indexOf("/2") +1; ///ATTENTION does only work for 2xxx
       return Long.valueOf(href.substring(start,start+16).replace("T","").replace("Z",""));
    }
    private String toTimeString(long time){
        return Long.toString(time).substring(0,8) +"T" + Long.toString(time).substring(8);
    }
}
