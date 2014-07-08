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