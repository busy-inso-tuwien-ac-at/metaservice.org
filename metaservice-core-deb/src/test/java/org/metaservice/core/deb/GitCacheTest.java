package org.metaservice.core.deb;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.metaservice.core.utils.MetaserviceHttpClient;

/**
 * Created with IntelliJ IDEA.
 * User: ilo
 * Date: 21.10.13
 * Time: 01:59
 * To change this template use File | Settings | File Templates.
 */
public class GitCacheTest {
    @NotNull
    private MetaserviceHttpClient clientMetaservice = new MetaserviceHttpClient();

    @Test
    public void testRunDiscovery() throws Exception {
        String uri = "http://snapshot.debian.org/archive/debian/20090409T161602Z/dists/lenny-proposed-updates/main/binary-amd64/";
        uri = "http://snapshot.debian.org/archive/debian/20090227T213913Z/dists/squeeze/non-free/binary-amd64/";
        Document document = Jsoup.parse(clientMetaservice.get(uri), uri);
        System.err.println("SIZE " + document.select("a:contains(prev change)").size());
        if(document.select("a:contains(prev change)").size() == 0 ||document.select("a:contains(prev change)").get(0).attr("abs:href").equals(document.select("a:contains(prev):not(:contains(change))").get(0).attr("abs:href")))
        {
            System.err.println("TRUE");
        }       else {
            System.err.println("FALSE");

        }

    }
}
