package org.metaservice.api.rdf.vocabulary;

import org.openrdf.model.*;
import org.openrdf.model.impl.*;



/**
 * This is an automatically generated class
 * Generator: org.metaservice.core.OntologyToJavaConverter
 * @see <a href="http://spdx.org/rdf/terms#">spdx</a>
 */
public class SPDX{

    public static final String NAMESPACE = "http://spdx.org/rdf/terms#";

    public static final String PREFIX = "spdx";

    public static final Namespace NS = new NamespaceImpl(PREFIX, NAMESPACE);

////////////////////////
// CLASSES
////////////////////////


    /**
     * http://spdx.org/rdf/terms#AnyLicenseInfo<br>
     * The <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">AnyLicenseInfo</code> class includes all resources that represent licensing information.<br>
     */
    public static final URI ANY_LICENSE_INFO;


    /**
     * http://spdx.org/rdf/terms#PackageVerificationCode<br>
     * A manifest based verification code (the algorithm is defined in section 4.7 of the full specification) of the package. This allows consumers of this data and/or database to determine if a package they have in hand is identical to the package from which the data was produced. This algorithm works even if the SPDX document is included in the package.<br>
     */
    public static final URI PACKAGE_VERIFICATION_CODE;


    /**
     * http://spdx.org/rdf/terms#ConjunctiveLicenseSet<br>
     * A <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">ConjunctiveLicenseSet</code> represents a set of <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#AnyLicenseInfo">licensing information</a> all of which apply.<br>
     */
    public static final URI CONJUNCTIVE_LICENSE_SET;


    /**
     * http://spdx.org/rdf/terms#Package<br>
     * A <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">Package</code> represents a collection of software files that are delivered as a single functional component.<br>
     */
    public static final URI PACKAGE;


    /**
     * http://spdx.org/rdf/terms#Review<br>
     * A <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">Review</code> represents an audit and signoff by an individual, organization or tool on the information in an <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#SpdxDocument"><code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">SpdxDocument</code></a>.<br>
     */
    public static final URI REVIEW;


    /**
     * http://spdx.org/rdf/terms#DisjunctiveLicenseSet<br>
     * A <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">DisjunctiveLicenseSet</code> represents a set of <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#AnyLicenseInfo">licensing information</a> where only one license applies at a time.  This class implies that the recipient gets to choose one of these licenses they would prefer to use.<br>
     */
    public static final URI DISJUNCTIVE_LICENSE_SET;


    /**
     * http://spdx.org/rdf/terms#ExtractedLicensingInfo<br>
     * An <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">ExtractedLicensingInfo</code> represents a license or licensing notice that was found in the package.  Any license text that is recognized as a license may be represented as a <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#License"><code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">License</code></a> rather than an <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">ExtractedLicensingInfo</code>.<br>
     */
    public static final URI EXTRACTED_LICENSING_INFO;


    /**
     * http://spdx.org/rdf/terms#Checksum<br>
     * A <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">Checksum</code> is value that allows the contents of a file to be authenticated.  Even small changes to the content of the file will change it's checksum.  This class allows the results of a variety of checksum and cryptographic message digest algorithms to be represented.<br>
     */
    public static final URI CHECKSUM;


    /**
     * http://spdx.org/rdf/terms#CreationInfo<br>
     * A <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">CreationInfo</code> provides information about the individuals, organizations and tools involved in the creation of an <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#SpdxDocument"><code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">SpdxDocument</code></a>.<br>
     */
    public static final URI CREATION_INFO;


    /**
     * http://spdx.org/rdf/terms#SpdxDocument<br>
     * An <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">SpdxDocument</code> is a summary of the contents, provenance, ownership and licensing analysis of a specific software package.  This is, effectively, the top level of SPDX information.<br>
     */
    public static final URI SPDX_DOCUMENT;


    /**
     * http://spdx.org/rdf/terms#File<br>
     * A <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">File</code> represents a named sequence of information that is contained in a software package.<br>
     */
    public static final URI FILE;


    /**
     * http://spdx.org/rdf/terms#License<br>
     * A <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">License</code> represents a copyright license. The <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="http://spdx.org/licenses">SPDX license list website</a> is annotated with these properties (using <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="http://www.w3.org/TR/2008/REC-rdfa-syntax-20081014/">RDFa</a>) to allow license data published there to be easily processed.<br>
     */
    public static final URI LICENSE;


////////////////////////
// OBJECT PROPERTIES
////////////////////////


    /**
     * http://spdx.org/rdf/terms#supplier<br>
     * <br>
     *           <p xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">The name and, optionally, contact information of the person or organization who was the immediate supplier of this package to the recipient.  The supplier may be different than <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#originator"><code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">originator</code></a> when the software has been repackaged.</p><br>
     * <br>
     *           <p xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">Values of this property must conform to the <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#agent-syntax">agent and tool syntax</a>.</p><br>
     *           <br>
     */
    public static final URI SUPPLIER;


    /**
     * http://spdx.org/rdf/terms#copyrightText<br>
     * The text of copyright declarations recited in the <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#Package"><code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">Package</code></a> or <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#File"><code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">File</code></a>.<br>
     */
    public static final URI COPYRIGHT_TEXT;


    /**
     * http://spdx.org/rdf/terms#creationInfo<br>
     * The <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">creationInfo</code> property relates an <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#SpdxDocument"><code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">SpdxDocument</code></a> to a set of information about the creation of the <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#SpdxDocument"><code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">SpdxDocument</code></a>.<br>
     */
    public static final URI CREATION_INFO_PROPERTY;


    /**
     * http://spdx.org/rdf/terms#describesPackage<br>
     * The <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">describesPackage</code> property relates an <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">SpdxDocument</code> to the package which it describes.<br>
     */
    public static final URI DESCRIBES_PACKAGE;


    /**
     * http://spdx.org/rdf/terms#algorithm<br>
     * Identifies the algorithm used to produce the subject <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#Checksum"><code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">Checksum</code></a>.<br>
     */
    public static final URI ALGORITHM;


    /**
     * http://spdx.org/rdf/terms#checksum<br>
     * The <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">checksum</code> property provides a mechanism that can be used to verify that the contents of a <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#File"><code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">File</code></a> or <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#Package"><code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">Package</code></a> have not changed.<br>
     */
    public static final URI CHECKSUM_PROPERTY;


    /**
     * http://spdx.org/rdf/terms#licenseInfoFromFiles<br>
     * The licensing information that was discovered directly within the package.  There will be an instance of this property for each distinct value of all <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#licenseInfoInFile"><code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">licenseInfoInFile</code></a> properties of all files contained in the package.<br>
     */
    public static final URI LICENSE_INFO_FROM_FILES;


    /**
     * http://spdx.org/rdf/terms#packageVerificationCode<br>
     * <br>
     *             <p xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">A manifest based authentication code for the package.  This allows consumers of this data to determine if a package they have in hand is identical to the package from which the data was produced.  This algorithm works even if the SPDX document is included in the package.  This algorithm is described in detail in the SPDX specification.</p><br>
     * <br>
     *             <p xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">The package verification code algorithm is defined in section 4.7 of the full specification.</p><br>
     *           <br>
     */
    public static final URI PACKAGE_VERIFICATION_CODE_PROPERTY;


    /**
     * http://spdx.org/rdf/terms#originator<br>
     * <br>
     *           <p xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">The name and, optionally, contact information of the person or organization that originally created the package.</p><br>
     * <br>
     *           <p xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">Values of this property must conform to the <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#agent-syntax">agent and tool syntax</a>.</p><br>
     *           <br>
     */
    public static final URI ORIGINATOR;


    /**
     * http://spdx.org/rdf/terms#member<br>
     * A <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#License">license</a>, or other licensing information, that is a member of the subject license set.<br>
     */
    public static final URI MEMBER;


    /**
     * http://spdx.org/rdf/terms#hasExtractedLicensingInfo<br>
     * Indicates that a particular <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#ExtractedLicensingInfo"><code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">ExtractedLicensingInfo</code></a> was defined in the subject <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#SpdxDocument"><code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">SpdxDocument</code></a>.<br>
     */
    public static final URI HAS_EXTRACTED_LICENSING_INFO;


    /**
     * http://spdx.org/rdf/terms#licenseInfoInFile<br>
     * Licensing information that was discovered directly in the subject file.<br>
     */
    public static final URI LICENSE_INFO_IN_FILE;


    /**
     * http://spdx.org/rdf/terms#licenseDeclared<br>
     * The licensing that the creators of the software in the package, or the packager, have declared.  Declarations by the original software creator should be preferred, if they exist.<br>
     */
    public static final URI LICENSE_DECLARED;


    /**
     * http://spdx.org/rdf/terms#dataLicense<br>
     * <br>
     *             <p xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">The licensing under which the <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#creator"><code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">creator</code></a> of this SPDX document allows related data to be reproduced.</p><br>
     * <br>
     *             <p xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">The only valid value for this property is <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">http://spdx.org/licenses/CC0-1.0</code>. This is to alleviate any concern that content (the data) in an SPDX file is subject to any form of intellectual property right that could restrict the re-use of the information or the creation of another SPDX file for the same project(s). This approach avoids intellectual property and related restrictions over the SPDX file, however individuals can still contract one to one to restrict release of specific collections of SPDX files (which map to software bill of materials) and the identification of the supplier of SPDX files.</p><br>
     *           <br>
     */
    public static final URI DATA_LICENSE;


    /**
     * http://spdx.org/rdf/terms#artifactOf<br>
     * <br>
     *             <p xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">Indicates the project in which the file originated.</p><br>
     *             <br>
     *             <p xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">Tools must preserve <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">doap:hompage</code> and <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">doap:name</code> properties and the URI (if one is known) of <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">doap:Project</code> resources that are values of this property.  All other properties of <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">doap:Projects</code> are not directly supported by SPDX and may be dropped when translating to or from some SPDX formats.</p><br>
     * <br>
     *           <br>
     */
    public static final URI ARTIFACT_OF;


    /**
     * http://spdx.org/rdf/terms#referencesFile<br>
     * Indicates that a particular file belongs as part of the set of analyzed files in the <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#SpdxDocument"><code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">SpdxDocument</code></a>.<br>
     */
    public static final URI REFERENCES_FILE;


    /**
     * http://spdx.org/rdf/terms#fileType<br>
     * The type of the file.<br>
     */
    public static final URI FILE_TYPE;


    /**
     * http://spdx.org/rdf/terms#hasFile<br>
     * Indicates that a particular <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#File">file</a> belongs to a <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#Package">package</a>.<br>
     */
    public static final URI HAS_FILE;


    /**
     * http://spdx.org/rdf/terms#licenseConcluded<br>
     * The licensing that the preparer of this SPDX document has concluded, based on the evidence, actually applies to the package.<br>
     */
    public static final URI LICENSE_CONCLUDED;


    /**
     * http://spdx.org/rdf/terms#reviewed<br>
     * The <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">review</code> property relates a <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">SpdxDocument</code> to the review history.<br>
     */
    public static final URI REVIEWED;


////////////////////////
// DATA PROPERTIES
////////////////////////


    /**
     * http://spdx.org/rdf/terms#licenseText<br>
     * The full text of the license.<br>
     */
    public static final URI LICENSE_TEXT;


    /**
     * http://spdx.org/rdf/terms#standardLicenseHeader<br>
     * Text specifically delineated by the license, or license appendix, as the preferred way to indicate that a source, or other, file is copyable under the license.<br>
     */
    public static final URI STANDARD_LICENSE_HEADER;


    /**
     * http://spdx.org/rdf/terms#downloadLocation<br>
     * The URI at which this package is available for download.  Private (i.e., not publicly reachable) URIs are acceptable as values of this property.<br>
     */
    public static final URI DOWNLOAD_LOCATION;


    /**
     * http://spdx.org/rdf/terms#name<br>
     * The full human readable name of the item. This should include version information when applicable.<br>
     */
    public static final URI NAME;


    /**
     * http://spdx.org/rdf/terms#packageVerificationCodeExcludedFile<br>
     * A file that was excluded when calculating the <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#packageVerificationCode">package verification code</a>.  This is usually a file containing SPDX data regarding the package.  If a package contains more than one SPDX file all SPDX files must be excluded from the package verification code.  If this is not done it would be impossible to correctly calculate the verification codes in both files.<br>
     */
    public static final URI PACKAGE_VERIFICATION_CODE_EXCLUDED_FILE;


    /**
     * http://spdx.org/rdf/terms#packageFileName<br>
     * The base name of the package file name.  For example, <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">zlib-1.2.5.tar.gz</code>.<br>
     */
    public static final URI PACKAGE_FILE_NAME;


    /**
     * http://spdx.org/rdf/terms#licenseListVersion<br>
     * An optional field for creators of the SPDX file to provide the version of the SPDX License  List used when the SPDX file was created.<br>
     */
    public static final URI LICENSE_LIST_VERSION;


    /**
     * http://spdx.org/rdf/terms#reviewer<br>
     * <br>
     *           <p xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">The name and, optionally, contact information of the person who performed the review.</p><br>
     *           <br>
     *           <p xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">Values of this property must conform to the <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#agent-syntax">agent and tool syntax</a>.</p><br>
     *           <br>
     */
    public static final URI REVIEWER;


    /**
     * http://spdx.org/rdf/terms#created<br>
     * The date and time at which the <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#SpdxDocument"><code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">SpdxDocument</code></a> was created.  This value must in UTC and have 'Z' as its timezone indicator.<br>
     */
    public static final URI CREATED;


    /**
     * http://spdx.org/rdf/terms#specVersion<br>
     * Identifies the version of this specification that was used to produce this SPDX document.  The value for this version of the spec is <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">SPDX-1.2</code>. The values <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">SPDX-1.0</code> and <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">SPDX-1.1</code> may also be supported by SPDX tools for backwards compatibility purposes.<br>
     */
    public static final URI SPEC_VERSION;


    /**
     * http://spdx.org/rdf/terms#versionInfo<br>
     * Provides an indication of the version of the package that is described by this <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#SpdxDocument"><code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">SpdxDocument</code></a>.<br>
     */
    public static final URI VERSION_INFO;


    /**
     * http://spdx.org/rdf/terms#sourceInfo<br>
     * Allows the producer(s) of the SPDX document to describe how the package was acquired and/or changed from the original source.<br>
     */
    public static final URI SOURCE_INFO;


    /**
     * http://spdx.org/rdf/terms#licenseComments<br>
     * The <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">licenseComments</code> property allows the preparer of the SPDX document to describe why the licensing in <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#licenseConcluded"><code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">spdx:licenseConcluded</code></a> was chosen.<br>
     */
    public static final URI LICENSE_COMMENTS;


    /**
     * http://spdx.org/rdf/terms#fileName<br>
     * The name of the file relative to the root of the package.<br>
     */
    public static final URI FILE_NAME;


    /**
     * http://spdx.org/rdf/terms#checksumValue<br>
     * The <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">checksumValue</code> property provides a lower case hexidecimal encoded digest value produced using a specific algorithm.<br>
     */
    public static final URI CHECKSUM_VALUE;


    /**
     * http://spdx.org/rdf/terms#standardLicenseTemplate<br>
     * License template which describes sections of the license which can be varied.  See License Template section of the specification for format information.<br>
     */
    public static final URI STANDARD_LICENSE_TEMPLATE;


    /**
     * http://spdx.org/rdf/terms#description<br>
     * Provides a detailed description of the package.<br>
     */
    public static final URI DESCRIPTION;


    /**
     * http://spdx.org/rdf/terms#reviewDate<br>
     * The date and time at which the <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#SpdxDocument"><code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">SpdxDocument</code></a> was reviewed.  This value must be in UTC and have 'Z' as its timezone indicator.<br>
     */
    public static final URI REVIEW_DATE;


    /**
     * http://spdx.org/rdf/terms#licenseId<br>
     * A human readable short form license identifier for a license not on the SPDX License List.<br>
     *           The license Id must be of the form "LicenseRef-"[idString] where <br>
     *           [idString] is a unique string containing letters, numbers, “.”, “-” or “+”.<br>
     */
    public static final URI LICENSE_ID;


    /**
     * http://spdx.org/rdf/terms#creator<br>
     * <br>
     *           <p xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">The name and, optionally, contact information of a person, organization or tool that created, or was used to create, the <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#SpdxDocument"><code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">SpdxDocument</code></a>.</p><br>
     * <br>
     *           <p xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">Values of this property must conform to the <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#agent-syntax">agent and tool syntax</a>.</p><br>
     *           <br>
     */
    public static final URI CREATOR;


    /**
     * http://spdx.org/rdf/terms#extractedText<br>
     * Verbatim license or licensing notice text that was discovered.<br>
     */
    public static final URI EXTRACTED_TEXT;


    /**
     * http://spdx.org/rdf/terms#summary<br>
     * Provides a short description of the package.<br>
     */
    public static final URI SUMMARY;


    /**
     * http://spdx.org/rdf/terms#packageVerificationCodeValue<br>
     * The actual package verification code as a hex encoded value.<br>
     */
    public static final URI PACKAGE_VERIFICATION_CODE_VALUE;


    /**
     * http://spdx.org/rdf/terms#isOsiApproved<br>
     * Indicates that a particular <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#License">license</a> has been approved by the <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="http://opensource.org/">OSI</a> as an open source licenses. If this property is true there <em xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">should</em> be a <code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">seeAlso</code> property linking to the OSI version of the license.<br>
     */
    public static final URI IS_OSI_APPROVED;


////////////////////////
// THINGS
////////////////////////


    /**
     * http://spdx.org/rdf/terms#fileType_archive<br>
     * Indicates the file is an archive file.<br>
     */
    public static final URI FILE_TYPE_ARCHIVE;


    /**
     * http://spdx.org/rdf/terms#noassertion<br>
     * Indicates that the preparer of the SPDX document is not making any assertion<br>
     *             regarding the value of this field.<br>
     */
    public static final URI NOASSERTION;


    /**
     * http://spdx.org/rdf/terms#fileType_binary<br>
     * Indicates the file is not a text file.  <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#fileType_archive"><code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">spdx:filetype_archive</code></a> is preferred for archive files even though they are binary.<br>
     */
    public static final URI FILE_TYPE_BINARY;


    /**
     * http://spdx.org/rdf/terms#none<br>
     * When this value is used as the object of a property it indicates that the preparer of the <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#SpdxDocument"><code xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#">SpdxDocument</code></a> believes that there is no value for the property.  This value should only be used if there is sufficient evidence to support this assertion.<br>
     */
    public static final URI NONE;


    /**
     * http://spdx.org/rdf/terms#fileType_source<br>
     * Indicates the file is a source code file.<br>
     */
    public static final URI FILE_TYPE_SOURCE;


    /**
     * http://spdx.org/rdf/terms#fileType_other<br>
     * Indicates the file is not a <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#fileType_source">source</a>, <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#fileType_archive">archive</a> or <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="#fileType_binary">binary</a> file.<br>
     */
    public static final URI FILE_TYPE_OTHER;


    /**
     * http://spdx.org/rdf/terms#checksumAlgorithm_sha1<br>
     * Indicates the algorithm used was <a xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/terms/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:doap="http://usefulinc.com/ns/doap" xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#" href="http://www.itl.nist.gov/fipspubs/fip180-1.htm">SHA-1</a><br>
     */
    public static final URI CHECKSUM_ALGORITHM_SHA1;


    static{
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();

        ANY_LICENSE_INFO = valueFactory.createURI(NAMESPACE,"AnyLicenseInfo");
        PACKAGE_VERIFICATION_CODE = valueFactory.createURI(NAMESPACE,"PackageVerificationCode");
        CONJUNCTIVE_LICENSE_SET = valueFactory.createURI(NAMESPACE,"ConjunctiveLicenseSet");
        PACKAGE = valueFactory.createURI(NAMESPACE,"Package");
        REVIEW = valueFactory.createURI(NAMESPACE,"Review");
        DISJUNCTIVE_LICENSE_SET = valueFactory.createURI(NAMESPACE,"DisjunctiveLicenseSet");
        EXTRACTED_LICENSING_INFO = valueFactory.createURI(NAMESPACE,"ExtractedLicensingInfo");
        CHECKSUM = valueFactory.createURI(NAMESPACE,"Checksum");
        CREATION_INFO = valueFactory.createURI(NAMESPACE,"CreationInfo");
        SPDX_DOCUMENT = valueFactory.createURI(NAMESPACE,"SpdxDocument");
        FILE = valueFactory.createURI(NAMESPACE,"File");
        LICENSE = valueFactory.createURI(NAMESPACE,"License");
        SUPPLIER = valueFactory.createURI(NAMESPACE,"supplier");
        COPYRIGHT_TEXT = valueFactory.createURI(NAMESPACE,"copyrightText");
        CREATION_INFO_PROPERTY = valueFactory.createURI(NAMESPACE,"creationInfo");
        DESCRIBES_PACKAGE = valueFactory.createURI(NAMESPACE,"describesPackage");
        ALGORITHM = valueFactory.createURI(NAMESPACE,"algorithm");
        CHECKSUM_PROPERTY = valueFactory.createURI(NAMESPACE,"checksum");
        LICENSE_INFO_FROM_FILES = valueFactory.createURI(NAMESPACE,"licenseInfoFromFiles");
        PACKAGE_VERIFICATION_CODE_PROPERTY = valueFactory.createURI(NAMESPACE,"packageVerificationCode");
        ORIGINATOR = valueFactory.createURI(NAMESPACE,"originator");
        MEMBER = valueFactory.createURI(NAMESPACE,"member");
        HAS_EXTRACTED_LICENSING_INFO = valueFactory.createURI(NAMESPACE,"hasExtractedLicensingInfo");
        LICENSE_INFO_IN_FILE = valueFactory.createURI(NAMESPACE,"licenseInfoInFile");
        LICENSE_DECLARED = valueFactory.createURI(NAMESPACE,"licenseDeclared");
        DATA_LICENSE = valueFactory.createURI(NAMESPACE,"dataLicense");
        ARTIFACT_OF = valueFactory.createURI(NAMESPACE,"artifactOf");
        REFERENCES_FILE = valueFactory.createURI(NAMESPACE,"referencesFile");
        FILE_TYPE = valueFactory.createURI(NAMESPACE,"fileType");
        HAS_FILE = valueFactory.createURI(NAMESPACE,"hasFile");
        LICENSE_CONCLUDED = valueFactory.createURI(NAMESPACE,"licenseConcluded");
        REVIEWED = valueFactory.createURI(NAMESPACE,"reviewed");
        LICENSE_TEXT = valueFactory.createURI(NAMESPACE,"licenseText");
        STANDARD_LICENSE_HEADER = valueFactory.createURI(NAMESPACE,"standardLicenseHeader");
        DOWNLOAD_LOCATION = valueFactory.createURI(NAMESPACE,"downloadLocation");
        NAME = valueFactory.createURI(NAMESPACE,"name");
        PACKAGE_VERIFICATION_CODE_EXCLUDED_FILE = valueFactory.createURI(NAMESPACE,"packageVerificationCodeExcludedFile");
        PACKAGE_FILE_NAME = valueFactory.createURI(NAMESPACE,"packageFileName");
        LICENSE_LIST_VERSION = valueFactory.createURI(NAMESPACE,"licenseListVersion");
        REVIEWER = valueFactory.createURI(NAMESPACE,"reviewer");
        CREATED = valueFactory.createURI(NAMESPACE,"created");
        SPEC_VERSION = valueFactory.createURI(NAMESPACE,"specVersion");
        VERSION_INFO = valueFactory.createURI(NAMESPACE,"versionInfo");
        SOURCE_INFO = valueFactory.createURI(NAMESPACE,"sourceInfo");
        LICENSE_COMMENTS = valueFactory.createURI(NAMESPACE,"licenseComments");
        FILE_NAME = valueFactory.createURI(NAMESPACE,"fileName");
        CHECKSUM_VALUE = valueFactory.createURI(NAMESPACE,"checksumValue");
        STANDARD_LICENSE_TEMPLATE = valueFactory.createURI(NAMESPACE,"standardLicenseTemplate");
        DESCRIPTION = valueFactory.createURI(NAMESPACE,"description");
        REVIEW_DATE = valueFactory.createURI(NAMESPACE,"reviewDate");
        LICENSE_ID = valueFactory.createURI(NAMESPACE,"licenseId");
        CREATOR = valueFactory.createURI(NAMESPACE,"creator");
        EXTRACTED_TEXT = valueFactory.createURI(NAMESPACE,"extractedText");
        SUMMARY = valueFactory.createURI(NAMESPACE,"summary");
        PACKAGE_VERIFICATION_CODE_VALUE = valueFactory.createURI(NAMESPACE,"packageVerificationCodeValue");
        IS_OSI_APPROVED = valueFactory.createURI(NAMESPACE,"isOsiApproved");
        FILE_TYPE_ARCHIVE = valueFactory.createURI(NAMESPACE,"fileType_archive");
        NOASSERTION = valueFactory.createURI(NAMESPACE,"noassertion");
        FILE_TYPE_BINARY = valueFactory.createURI(NAMESPACE,"fileType_binary");
        NONE = valueFactory.createURI(NAMESPACE,"none");
        FILE_TYPE_SOURCE = valueFactory.createURI(NAMESPACE,"fileType_source");
        FILE_TYPE_OTHER = valueFactory.createURI(NAMESPACE,"fileType_other");
        CHECKSUM_ALGORITHM_SHA1 = valueFactory.createURI(NAMESPACE,"checksumAlgorithm_sha1");
    }
}
