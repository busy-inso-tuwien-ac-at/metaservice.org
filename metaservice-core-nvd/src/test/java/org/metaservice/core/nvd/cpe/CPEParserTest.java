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

package org.metaservice.core.nvd.cpe;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.metaservice.nist.cpe.jaxb.ItemType;
import org.mitre.cpe.common.WellFormedName;
import org.mitre.cpe.naming.util.CPEFactory;

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
        List<ItemType> list = new CPEParser().parse(inputStreamReader, null);
        Assert.assertNotNull(list);
        System.err.println("SIZE: " + list.size());
        Assert.assertThat(list.size(), Matchers.greaterThan(1));
    }

    @Test
    public void testWellFormed()throws Exception{
        WellFormedName wellFormedName = CPEFactory.newCPEName("cpe:/a:wordpress:wordpress:3.8.1").getWellFormedName();
        System.err.println(wellFormedName);
        System.err.println(wellFormedName.get(WellFormedName.Attribute.VERSION));
        System.err.println(wellFormedName.get(WellFormedName.Attribute.VERSION).toString().replaceAll("\\\\[.]","."));
    }
}
