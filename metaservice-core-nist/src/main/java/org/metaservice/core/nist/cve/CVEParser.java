package org.metaservice.core.nist.cve;

import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.parser.Parser;
import org.metaservice.nist.cve.jaxb.Nvd;
import org.metaservice.nist.cve.jaxb.VulnerabilityType;

import javax.inject.Inject;
import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.util.List;

/**
 * Created by ilo on 25.02.14.
 */
public class CVEParser implements Parser<VulnerabilityType> {

    @Inject
    public CVEParser(){

    }
    @Override
    public List<VulnerabilityType> parse(String s, ArchiveAddress archiveParameters) {
        StringReader stringReader = new StringReader(s);
        Nvd nvd = JAXB.unmarshal(stringReader, Nvd.class);
        return nvd.getEntries();
    }
}
