package org.metaservice.core.nist.cve;

import org.jetbrains.annotations.NotNull;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 * Created by ilo on 26.02.14.
 */
public class CVE {
    public static final String NS ="http://metaservice.org/ns/cve#";
    public static final URI CVE;
    public static final URI CVE_ID;
    public static final URI CCE_ID;
    public static final URI SUMMARY;
    public static final URI PUBLISHED_DATETIME;
    public static final URI LAST_MODIFIED_DATETIME;
    public static final URI EXPLOIT_PUBLISHED_DATETIME;
    public static final URI DISCOVERED_DATETIME;
    public static final URI DISCLOSURE_DATETIME;
    public static final URI VULNERABLE_SOFTWARE;

    public static final URI OSVDB_LOCATION_DIALUP;
    public static final URI OSVDB_LOCATION_PHYSICAL_ACCESS;
    public static final URI OSVDB_LOCATION_UNKNOWN;
    public static final URI OSVDB_LOCATION_VOLUNTARILY_INTERACT;

    public static final URI SECURITY_PROTECTION;
    public static final URI ATTACK_SCENARIO;
    public static final URI ASSESSMENT_CHECK;
    public static final URI CWE;
    static {
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();
        CVE =valueFactory.createURI(NS,"CVE");
        CVE_ID = valueFactory.createURI(NS,"cveId");
        CCE_ID = valueFactory.createURI(NS,"cceID");
        SUMMARY = valueFactory.createURI(NS,"summary");
        PUBLISHED_DATETIME = valueFactory.createURI(NS,"publishedDatetime");
        LAST_MODIFIED_DATETIME = valueFactory.createURI(NS,"lastModifiedDatetime");
        EXPLOIT_PUBLISHED_DATETIME = valueFactory.createURI(NS,"exploitPublishedDatetime");
        DISCOVERED_DATETIME = valueFactory.createURI(NS,"discoveredDatetime");
        DISCLOSURE_DATETIME = valueFactory.createURI(NS,"disclosureDatetime");
        VULNERABLE_SOFTWARE = valueFactory.createURI(NS,"vulnerableSoftware");

        OSVDB_LOCATION_DIALUP = valueFactory.createURI(NS,"osvdbLocationDialup");
        OSVDB_LOCATION_PHYSICAL_ACCESS = valueFactory.createURI(NS,"osvdbLocationPhysicalAccess");
        OSVDB_LOCATION_UNKNOWN = valueFactory.createURI(NS,"osvdbLocationUnknown");
        OSVDB_LOCATION_VOLUNTARILY_INTERACT = valueFactory.createURI(NS,"osvdbLocationVoluntarilyInteract");

        SECURITY_PROTECTION = valueFactory.createURI(NS,"securityProtection");
        ATTACK_SCENARIO = valueFactory.createURI(NS,"attackScenario");
        ASSESSMENT_CHECK = valueFactory.createURI(NS,"assessmentCheck");
        CWE = valueFactory.createURI(NS,"cwe");
    }

    private static final String LOCAL_NS ="http://metaservice.org/d/report/cve/";



    @NotNull
    public static URI getById(@NotNull String id){
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();
        return valueFactory.createURI(LOCAL_NS,id);
    }

}
