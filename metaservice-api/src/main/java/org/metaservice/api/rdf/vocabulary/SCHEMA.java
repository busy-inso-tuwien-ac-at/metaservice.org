package org.metaservice.api.rdf.vocabulary;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 * Created by ilo on 24.06.2014.
 */
public class SCHEMA {
    public static final String NAMESPACE ="http://schema.org/";


    public static final URI CONTRIBUTOR;
    public static final URI DOWNLOAD_URL;
    public static final URI FILE_SIZE;

    static{
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();

        CONTRIBUTOR = valueFactory.createURI(NAMESPACE,"contributor");
        DOWNLOAD_URL = valueFactory.createURI(NAMESPACE,"downloadUrl");
        FILE_SIZE = valueFactory.createURI(NAMESPACE,"fileSize");

    }
}




