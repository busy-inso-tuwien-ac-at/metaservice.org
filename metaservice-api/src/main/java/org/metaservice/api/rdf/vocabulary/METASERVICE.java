package org.metaservice.api.rdf.vocabulary;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

public class METASERVICE {
    public static final String NAMESPACE = "http://metaservice.org/ns/metaservice#";

    public static final URI METADATA;

    public static final URI REPOSITORY_ID;
    public static final URI SOURCE;
    public static final URI TIME;
    public static final URI PATH;
    public static final URI VIEW;
    public static final URI GENERATOR;
    public static final URI CREATION_TIME;
    public static final URI LAST_CHECKED_TIME;
    public static final URI SCOPE;
    public static final URI DUMMY;


    static {
        ValueFactory factory = ValueFactoryImpl.getInstance();
        REPOSITORY_ID = factory.createURI(NAMESPACE,"repositoryId");
        SOURCE = factory.createURI(NAMESPACE,"source");
        TIME = factory.createURI(NAMESPACE,"time");
        PATH = factory.createURI(NAMESPACE,"path");
        METADATA = factory.createURI(NAMESPACE,"metadata");
        VIEW = factory.createURI(NAMESPACE,"view");
        GENERATOR = factory.createURI(NAMESPACE,"generator");
        CREATION_TIME = factory.createURI(NAMESPACE,"creation_time");
        LAST_CHECKED_TIME = factory.createURI(NAMESPACE,"last_checked_time");
        SCOPE = factory.createURI(NAMESPACE,"scope");
        DUMMY = factory.createURI( NAMESPACE,"dummy" );
    }


}
