package org.metaservice.core.bower;

import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class BowerIndexParserTest {

    @Test
    public void testParse() throws Exception {
        BowerIndexParser bowerParser = new BowerIndexParser();
        List l = bowerParser.parse(new StringReader("[{\"name\":\"ðŸ’©\"," +
                "\"description\":\"It's a bower package. It's named poo.\"," +
                "\"owner\":\"smithclay\"," +
                "\"website\":\"https://github.com/smithclay/poo\"," +
                "\"forks\":0," +
                "\"stars\":0," +
                "\"created\":\"2014-05-24T00:08:31Z\"," +
                "\"updated\":\"2014-05-24T00:16:41Z\"}" +
                "," +
                "{\"name\":\"10digit-geo\"," +
                "\"description\":\"Geo components for AngularJS driven apps\"," +
                "\"owner\":\"10digit\"," +
                "\"website\":\"https://github.com/10digit/geo\"," +
                "\"forks\":0," +
                "\"stars\":0," +
                "\"created\":\"2013-10-23T16:09:24Z\"," +
                "\"updated\":\"2013-10-23T16:28:26Z\"}" +
                "," +
                "{\"name\":\"10digit-invoices\"," +
                "\"description\":\"Used along side services to display past and upcoming invoices\"," +
                "\"owner\":\"10digit\"," +
                "\"website\":\"https://github.com/10digit/invoices\"," +
                "\"forks\":0," +
                "\"stars\":0," +
                "\"created\":\"2013-10-23T17:32:39Z\"," +
                "\"updated\":\"2013-10-28T08:41:50Z\"}" +
                "," +
                "{\"name\":\"10digit-legal\"," +
                "\"description\":\"Legal agreement\"," +
                "\"owner\":\"10digit\"," +
                "\"website\":\"https://github.com/10digit/legal\"," +
                "\"forks\":0," +
                "\"stars\":0," +
                "\"created\":\"2013-10-23T17:22:26Z\"," +
                "\"updated\":\"2013-11-12T03:15:15Z\"}]"),null);
        System.err.println(l);
    }
}