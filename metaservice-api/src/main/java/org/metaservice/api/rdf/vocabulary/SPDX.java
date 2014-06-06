package org.metaservice.api.rdf.vocabulary;

import org.openrdf.model.Namespace;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.NamespaceImpl;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 * Created by ilo on 30.05.2014.
 */
public class SPDX {
    /**
     * The SKOS namespace: http://www.w3.org/2004/02/skos/core#
     */
    public static final String NAMESPACE = "http://spdx.org/rdf/terms";

    /**
     * The recommended prefix for the SKOS namespace: "skos"
     */
    public static final String PREFIX = "skos";

    /**
     * An immutable {@link org.openrdf.model.Namespace} constant that represents the SKOS
     * namespace.
     */
    public static final Namespace NS = new NamespaceImpl(PREFIX, NAMESPACE);


    public static final URI CHECKSUM_CLASS;
    public static final URI FILE_CLASS;

    public static final URI CHECKSUM;

    public static final URI ALGORITHM;
    public static final URI CHECKSUMVALUE;

    public static final URI ARTIFACTOF;



    public static final URI CHECKSUMALGORITHM_SHA1;
    static {
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();
        CHECKSUM_CLASS = valueFactory.createURI(NAMESPACE,"Checksum");
        FILE_CLASS = valueFactory.createURI(NAMESPACE,"File");

        ALGORITHM = valueFactory.createURI(NAMESPACE,"algorithm");
        CHECKSUMVALUE = valueFactory.createURI(NAMESPACE,"checksumValue");
        CHECKSUM= valueFactory.createURI(NAMESPACE,"checksum");
        ARTIFACTOF = valueFactory.createURI(NAMESPACE,"artifactOf");
        CHECKSUMALGORITHM_SHA1 =  valueFactory.createURI(NAMESPACE,"checksumAlgorithm_sha1");
    }
}
