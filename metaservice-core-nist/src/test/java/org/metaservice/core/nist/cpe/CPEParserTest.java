package org.metaservice.core.nist.cpe;

import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.metaservice.nist.cpe.jaxb.ItemType;

import java.io.*;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Created by ilo on 25.02.14.
 */
public class CPEParserTest {
    @Test
    public void testParse() throws Exception {
        InputStream fileInputStream = CPEParserTest.class.getResourceAsStream("/official-cpe-dictionary_v2.3.xml.gz");
        GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
        InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream);
        StringWriter stringWriter = new StringWriter();
        IOUtils.copy(inputStreamReader,stringWriter);
        List<ItemType> list = new CPEParser().parse(stringWriter.toString(), null);
        Assert.assertNotNull(list);
        System.err.println("SIZE: " + list.size());
        Assert.assertThat(list.size(), Matchers.greaterThan(1));
    }
}
