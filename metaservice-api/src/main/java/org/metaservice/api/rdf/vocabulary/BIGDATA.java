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

package org.metaservice.api.rdf.vocabulary;

import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by ilo on 20.07.2014.
 */
public class BIGDATA {
    public static final QueryHint QUERY;
    public static final QueryHint SUB_QUERY;
    public static final QueryHint OPTIMIZE;
    public static final QueryHint RUN_ONCE;

    public static final Literal NONE;
    public static final Literal STATIC;
    public static final Literal RUNTIME;

    public static final Literal TRUE;
    public static final Literal FALSE;

    static {
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();
        QUERY = new QueryHint("hint:Query");
        SUB_QUERY = new QueryHint("hint:SubQuery");
        OPTIMIZE = new QueryHint("hint:optimizer");
        RUN_ONCE = new QueryHint("hint:runOnce");
        NONE = valueFactory.createLiteral("None");
        STATIC = valueFactory.createLiteral("Static");
        RUNTIME = valueFactory.createLiteral("Runtime");
        TRUE = valueFactory.createLiteral(true);
        FALSE = valueFactory.createLiteral(false);
    }

    public static class QueryHint implements Literal{
        private final String s;

        QueryHint(String s) {
            this.s = s;
        }

        @Override
        public String getLabel() {
            return null;
        }

        @Override
        public String getLanguage() {
            return null;
        }

        @Override
        public URI getDatatype() {
            return null;
        }

        @Override
        public byte byteValue() {
            return 0;
        }

        @Override
        public short shortValue() {
            return 0;
        }

        @Override
        public int intValue() {
            return 0;
        }

        @Override
        public long longValue() {
            return 0;
        }

        @Override
        public BigInteger integerValue() {
            return null;
        }

        @Override
        public BigDecimal decimalValue() {
            return null;
        }

        @Override
        public float floatValue() {
            return 0;
        }

        @Override
        public double doubleValue() {
            return 0;
        }

        @Override
        public boolean booleanValue() {
            return false;
        }

        @Override
        public XMLGregorianCalendar calendarValue() {
            return null;
        }

        @Override
        public String stringValue() {
            return s;
        }

        @Override
        public String toString(){
            return s;
        }
    }
}
