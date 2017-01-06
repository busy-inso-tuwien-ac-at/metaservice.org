/*
 * Copyright 2015 Nikola Ilo
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

package org.metaservice.core;

import com.google.common.base.CaseFormat;
import org.junit.Test;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

import static org.junit.Assert.*;

public class OntologyToJavaConverterTest {

    @Test
    public void testToJavaName() throws Exception {
        ValueFactory valueFactory = new ValueFactoryImpl();
        CaseFormat caseFormat = CaseFormat.UPPER_CAMEL;

        OntologyToJavaConverter converter = new OntologyToJavaConverter(caseFormat);
        assertEquals("URI",converter.toJavaName(valueFactory.createURI("http://example.org/URI"),""));
        assertEquals("ISO639_3",converter.toJavaName(valueFactory.createURI("http://example.org/ISO639-3"),""));
     }
}