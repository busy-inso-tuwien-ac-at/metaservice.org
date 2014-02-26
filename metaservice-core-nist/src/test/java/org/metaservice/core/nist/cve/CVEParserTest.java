package org.metaservice.core.nist.cve;

import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.metaservice.nist.cve.jaxb.VulnerabilityType;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Created by ilo on 25.02.14.
 */
public class CVEParserTest {
    @Test
    public void testParse() throws Exception {
        InputStream fileInputStream = CVEParserTest.class.getResourceAsStream("/nvdcve-2.0-2009.xml.gz");
        GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
        InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream);
        StringWriter stringWriter = new StringWriter();
        IOUtils.copy(inputStreamReader, stringWriter);
        List<VulnerabilityType> list = new CVEParser().parse(stringWriter.toString(), null);
        Assert.assertNotNull(list);
        System.err.println("SIZE: " + list.size());
        Assert.assertThat(list.size(), Matchers.greaterThan(1));
    }
}
