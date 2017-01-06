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

package org.metaservice.demo.wordpress;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.parser.Parser;
import org.metaservice.api.parser.ParserException;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilo on 21.06.2014.
 */
public class WordpressParser implements Parser<VersionEntry> {
    @Override
    public List<VersionEntry> parse(Reader s, ArchiveAddress archiveParameters) throws ParserException {
        try {
            Document document = Jsoup.parse(IOUtils.toString(s), "http://wordpress.org/download/release-archive/");
            ArrayList<VersionEntry> result= new ArrayList<>();
            Elements tables = document.select("table.widefat");
            for(Element table : tables){
                Elements rows = table.select("tr");
                // System.err.println(rows);
                for(Element row : rows){
                    Elements columns = row.select("td");
                    if(columns.size()> 0) {
                        VersionEntry versionEntry = new VersionEntry();
                        versionEntry.setName(columns.get(0).text().trim());
                        versionEntry.setZip(columns.select("a[href$=zip]").attr("href"));
                        versionEntry.setTar(columns.select("a[href$=tar.gz]").attr("href"));
                        versionEntry.setIis(columns.select("a[href$=IIS.zip]").attr("href"));
                        result.add(versionEntry);
                    }
                }
            }
            return result;
        } catch (IOException e) {
            throw new ParserException(e);
        }
    }
}
