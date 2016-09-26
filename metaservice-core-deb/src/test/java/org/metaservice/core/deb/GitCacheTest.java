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
