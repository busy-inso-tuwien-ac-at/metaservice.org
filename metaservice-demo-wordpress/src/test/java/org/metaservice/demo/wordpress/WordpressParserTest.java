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

package org.metaservice.demo.wordpress;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.metaservice.api.archive.ArchiveAddress;
import org.mockito.Matchers;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class WordpressParserTest {

    @Test
    public void testParse() throws Exception {
        WordpressParser wordpressParser = new WordpressParser();
        List<VersionEntry> result = wordpressParser.parse(new InputStreamReader(WordpressParserTest.class.getResourceAsStream("/release-archive.html")),new ArchiveAddress("","",null,"",""));
      //  assertThat(result,is(Matchers.anyListOf(VersionEntry.class)));
        assertThat(result.size(),is(262));
        System.err.println(result);
    }
}