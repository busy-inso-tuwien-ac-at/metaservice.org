package org.metaservice.demo.wordpress;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.parser.Parser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilo on 21.06.2014.
 */
public class WordpressParser implements Parser<VersionEntry> {
    @Override
    public List<VersionEntry> parse(String s, ArchiveAddress archiveParameters) {
        Document document  =Jsoup.parse(s,"http://wordpress.org/download/release-archive/");
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
    }
}
