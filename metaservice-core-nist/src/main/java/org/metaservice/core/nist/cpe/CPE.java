package org.metaservice.core.nist.cpe;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 * Created by ilo on 25.02.14.
 */
public class CPE {

    public static final String NS ="http://metaservice.org/ns/cpe#";
    public static final URI CPE;
    public static final URI DEPRECATED;
    public static final URI NOTE;
    public static final URI CHECK;
    public static final URI CHECK_VALUE;
    public static final URI CHECK_SYSTEM;
    public static final URI CHECK_HREF;
    public static final URI NAME;

    static {
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();
        CPE = valueFactory.createURI(NS,"CPE");
        DEPRECATED = valueFactory.createURI(NS,"deprecated");
        NOTE = valueFactory.createURI(NS,"note");
        CHECK = valueFactory.createURI(NS,"check");
        CHECK_VALUE = valueFactory.createURI(NS,"checkValue");
        CHECK_SYSTEM = valueFactory.createURI(NS,"checkSystem");
        CHECK_HREF = valueFactory.createURI(NS,"checkHref");
        NAME = valueFactory.createURI(NS,"name");
    }


}
