package org.metaservice.demo.license;

import org.openrdf.model.*;
import org.openrdf.model.impl.*;



/**
 * This is an automatically generated class
 * Generator: org.metaservice.core.OntologyToJavaConverter
 * @see <a href="http://metaservice.org/ns/licensing#">lic</a>
 */
public class LIC{

    public static final String NAMESPACE = "http://metaservice.org/ns/licensing#";

    public static final String PREFIX = "lic";

    public static final Namespace NS = new NamespaceImpl(PREFIX, NAMESPACE);

////////////////////////
// CLASSES
////////////////////////


    /**
     * http://metaservice.org/ns/licensing#SuspectedViolation<br>
     */
    public static final URI SUSPECTED_VIOLATION;


////////////////////////
// OBJECT PROPERTIES
////////////////////////


    /**
     * http://metaservice.org/ns/licensing#licensingViolation<br>
     * "has violation"<br>
     */
    public static final URI LICENSING_VIOLATION;


    /**
     * http://metaservice.org/ns/licensing#conflicting<br>
     * "conflicting Dependency"<br>
     */
    public static final URI CONFLICTING;


////////////////////////
// DATA PROPERTIES
////////////////////////


    /**
     * http://metaservice.org/ns/licensing#description<br>
     * "explanation"<br>
     */
    public static final URI DESCRIPTION;


    static{
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();

        SUSPECTED_VIOLATION = valueFactory.createURI(NAMESPACE,"SuspectedViolation");
        LICENSING_VIOLATION = valueFactory.createURI(NAMESPACE,"licensingViolation");
        CONFLICTING = valueFactory.createURI(NAMESPACE,"conflicting");
        DESCRIPTION = valueFactory.createURI(NAMESPACE,"description");
    }
}
