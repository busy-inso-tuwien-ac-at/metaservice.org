package org.metaservice.core.bower;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.parser.Parser;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by ilo on 01.06.2014.
 */
public class BowerIndexParser  implements Parser<BowerIndexEntry> {
    @Override
    public List<BowerIndexEntry> parse(Reader s, ArchiveAddress archiveParameters) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<BowerIndexEntry>>(){}.getType();
        return gson.fromJson(s,collectionType);
    }
}
