package org.metaservice.api.rdf.vocabulary;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 * Created by ilo on 22.06.2014.
 */
public class METASERVICE_FILE {
    public static final String NAMESPACE = "http://metaservice.org/ns/metaservice-file#";
    /**
     * class
     */
    public static final URI FILE;


    static {
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();
        FILE =valueFactory.createURI(NAMESPACE,"file");
    }
}
