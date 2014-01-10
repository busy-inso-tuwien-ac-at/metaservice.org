package org.metaservice.api.ns;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

public class METASERVICE {
    public static final String NAMESPACE = "http://metaservice.org/ns/metaservice#";

    public static final URI METADATA;

    public static final URI SOURCE;
    public static final URI TIME;
    public static final URI PATH;
    public static final URI VIEW;



    static {
        ValueFactory factory = ValueFactoryImpl.getInstance();
        SOURCE = factory.createURI(NAMESPACE,"source");
        TIME = factory.createURI(NAMESPACE,"time");
        PATH = factory.createURI(NAMESPACE,"path");
        METADATA = factory.createURI(NAMESPACE,"metadata");
        VIEW = factory.createURI(NAMESPACE,"view");
    }

}
