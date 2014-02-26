package org.metaservice.core.nist.cpe;

import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.parser.Parser;
import org.metaservice.nist.cpe.jaxb.ItemType;
import org.metaservice.nist.cpe.jaxb.ListType;

import javax.inject.Inject;
import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.util.List;


/**
 * Created by ilo on 25.02.14.
 */
public class CPEParser implements Parser<ItemType> {

    @Inject
    public CPEParser(){

    }

    @Override
    public List<ItemType> parse(String s, ArchiveAddress archiveParameters) {
        StringReader stringReader = new StringReader(s);
        ListType listType = JAXB.unmarshal(stringReader, ListType.class);
        return listType.getCpeItem();
    }
}
