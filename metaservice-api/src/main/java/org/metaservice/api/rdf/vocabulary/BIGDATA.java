package org.metaservice.api.rdf.vocabulary;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.sparql.builders.QueryBuilder;
import org.omg.CORBA.TRANSACTION_UNAVAILABLE;
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

    static class QueryHint implements Literal{
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
