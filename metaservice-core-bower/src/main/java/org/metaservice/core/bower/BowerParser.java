package org.metaservice.core.bower;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.parser.Parser;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by ilo on 01.06.2014.
 */
public class BowerParser implements Parser<BowerPackage> {
    @Override
    public List<BowerPackage> parse(String s, ArchiveAddress archiveParameters) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<BowerPackage>>(){}.getType();
        return gson.fromJson(s,collectionType);
    }
}
