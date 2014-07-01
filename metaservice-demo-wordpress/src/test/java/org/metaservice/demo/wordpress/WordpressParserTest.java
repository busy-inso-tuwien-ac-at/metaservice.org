package org.metaservice.demo.wordpress;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.metaservice.api.archive.ArchiveAddress;
import org.mockito.Matchers;

import java.io.StringWriter;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class WordpressParserTest {

    @Test
    public void testParse() throws Exception {
        StringWriter stringWriter = new StringWriter();
        IOUtils.copy(WordpressParserTest.class.getResourceAsStream("/release-archive.html"),stringWriter);
        WordpressParser wordpressParser = new WordpressParser();
        List<VersionEntry> result = wordpressParser.parse(stringWriter.toString(),new ArchiveAddress("","",null,""));
      //  assertThat(result,is(Matchers.anyListOf(VersionEntry.class)));
        assertThat(result.size(),is(262));
        System.err.println(result);
    }
}