/*
 * Copyright 2015 Nikola Ilo
 * Research Group for Industrial Software, Vienna University of Technology, Austria
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaservice.core.nvd.cve;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.metaservice.nist.cve.jaxb.VulnerabilityType;

import java.io.InputStream;
import java.io.InputStreamReader;
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
        List<VulnerabilityType> list = new CVEParser().parse(inputStreamReader, null);
        Assert.assertNotNull(list);
        System.err.println("SIZE: " + list.size());
        Assert.assertThat(list.size(), Matchers.greaterThan(1));
    }
}
