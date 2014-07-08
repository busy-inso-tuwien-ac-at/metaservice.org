package org.metaservice.core.nist.cpe;

import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.parser.Parser;
import org.metaservice.nist.cpe.jaxb.ItemType;
import org.metaservice.nist.cpe.jaxb.ListType;

import javax.inject.Inject;
import javax.xml.bind.JAXB;
import java.io.Reader;
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
    public List<ItemType> parse(Reader reader, ArchiveAddress archiveParameters) {
        ListType listType = JAXB.unmarshal(reader, ListType.class);
        return listType.getCpeItem();
    }
}
