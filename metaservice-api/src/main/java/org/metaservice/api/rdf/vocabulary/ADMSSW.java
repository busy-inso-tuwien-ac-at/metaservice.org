package org.metaservice.api.rdf.vocabulary;

import org.openrdf.model.*;
import org.openrdf.model.impl.*;



/**
 * This is an automatically generated class
 * Generator: org.metaservice.core.OntologyToJavaConverter
 * @see <a href="http://purl.org/adms/sw/">admssw</a>
 */
public class ADMSSW{

    public static final String NAMESPACE = "http://purl.org/adms/sw/";

    public static final String PREFIX = "admssw";

    public static final Namespace NS = new NamespaceImpl(PREFIX, NAMESPACE);

////////////////////////
// CLASSES
////////////////////////


    /**
     * http://purl.org/adms/sw/SoftwareRepository<br>
     * "Software Repository"<br>
     * A Software Repository is a system or service that provides facilities for storage and maintenance of descriptions of Software Projects, Software Releases and Software Distributions, and functionality that allows users to search and access these descriptions. A Software Repository will typically contain descriptions of several Software Projects, Software Releases and related Software Distributions.<br>
     */
    public static final URI SOFTWARE_REPOSITORY;


    /**
     * http://purl.org/adms/sw/SoftwareProject<br>
     * "Software Project"<br>
     * A Software Project is a time-delimited undertaking with the objective to produce one or more software releases, materialised as software packages. Some projects are long-running undertakings, and do not have a clear time-delimited nature or project organisation. In this case, the term 'software project' can be interpreted as the result of the work: a collection of related software releases that serve a common purpose.<br>
     */
    public static final URI SOFTWARE_PROJECT;


    /**
     * http://purl.org/adms/sw/SoftwareRelease<br>
     * "Software Release"<br>
     * A Software Release is an abstract entity that reflects the intellectual content of the software and represents those characteristics of the software that are independent of its physical embodiment. This abstract entity corresponds to the FRBR entity expression (the intellectual or artistic realization of a work). An example of a Software Release is the Apache HTTP Server 2.22.22 (httpd) release.<br>
     */
    public static final URI SOFTWARE_RELEASE;


    /**
     * http://purl.org/adms/sw/SoftwarePackage<br>
     * "Software Package"<br>
     * A Software Package represents a particular physical embodiment of a Software Release, which is an example of the FRBR entity manifestation (the physical embodiment of an expression of a work). A Software Package is typically a downloadable computer file (but in principle it could also be a paper document) that implements the intellectual content of a Software Release. A particular Software Package is associated with one and only one Software Release, while all Packages of a Release share the same intellectual content in different physical formats. An example of a Software Package is httpd-2.2.22.tar.gz , which represents the Unix Source of the Apache HTTP Server 2.22.22 (httpd) software release.<br>
     */
    public static final URI SOFTWARE_PACKAGE;


////////////////////////
// PROPERTIES
////////////////////////


    /**
     * http://purl.org/adms/sw/supportsFormat<br>
     * "Software Project - supports format"<br>
     * Data format that is supported by the software.<br>
     * Usage Note <br>
     * Ideally, the range of this property is dcterms:FileFormat.<br>
     */
    public static final URI SUPPORTS_FORMAT;


    /**
     * http://purl.org/adms/sw/status<br>
     * "Software Project - status"<br>
     * The status of the software project, asset, or package.<br>
     * Usage Note <br>
     * Ideally, the range of this property is skos:Concept.<br>
     */
    public static final URI STATUS;


    /**
     * http://purl.org/adms/sw/metrics<br>
     * "Software Project - metrics"<br>
     * A data set of metrics about the software project or software release.<br>
     * Usage Note <br>
     * The range of this property is not fixed by the ADMS.SW specification. It is recommended to relate this property to an instance of the class 'qb:DataSet'.<br>
     */
    public static final URI METRICS;


    /**
     * http://purl.org/adms/sw/assessment<br>
     * "Software Release - assessment"<br>
     * Assessment of the asset.<br>
     * Usage Note <br>
     * The range of this property is foaf:Document.<br>
     */
    public static final URI ASSESSMENT;


    /**
     * http://purl.org/adms/sw/downloadNumber<br>
     * "measure: number of downloads"<br>
     * The number of times a software package has been downloaded.<br>
     */
    public static final URI DOWNLOAD_NUMBER;


    /**
     * http://purl.org/adms/sw/fundedBy<br>
     * "Software Project - funded by"<br>
     * contributing person or organisation for the project.<br>
     * Usage Note <br>
     * Used in ADMS.SW to associate a project with an organisation or person who funds it.<br>
     */
    public static final URI FUNDED_BY;


    /**
     * http://purl.org/adms/sw/tagURL<br>
     * "Software Package - tag URL"<br>
     * URL from which a software tag file can be obtained for the Software Package.<br>
     * Usage Note <br>
     * Several software tag standards exist such as the ISO19770-2:2009, the ISO19770-3 and the SPDX specification. These standards do not require the software tags to be available on via the Web.<br>
     */
    public static final URI TAG_URL;


    /**
     * http://purl.org/adms/sw/identifier<br>
     * "Software Release - identifier"<br>
     * Any identifier for the AssetRelease.<br>
     * Usage Note <br>
     * ADMS.SW uses this to include a local identifier for the Software Release. The range of this property is adms:Identifier. This allows specifying the identifying system and ensures that the identifier stays meaningful in a situation with multiple identification domains.<br>
     */
    public static final URI IDENTIFIER;


    /**
     * http://purl.org/adms/sw/releaseDimension<br>
     * "dimension: release dimension"<br>
     * The release category to which a measured observation applies.<br>
     */
    public static final URI RELEASE_DIMENSION;


    /**
     * http://purl.org/adms/sw/intendedAudience<br>
     * "Software Project - intended audience"<br>
     * Intended audience of the software.<br>
     * Usage Note <br>
     * Ideally, the range of this property is skos:Concept.<br>
     */
    public static final URI INTENDED_AUDIENCE;


    /**
     * http://purl.org/adms/sw/usedBy<br>
     * "Software Release - used by"<br>
     * Agent that uses the Asset.<br>
     */
    public static final URI USED_BY;


    /**
     * http://purl.org/adms/sw/userInterfaceType<br>
     * "Software Project - user interface type"<br>
     * User interface type of the software.<br>
     * Usage Note <br>
     * Ideally, the range of this property is skos:Concept.<br>
     */
    public static final URI USER_INTERFACE_TYPE;


    /**
     * http://purl.org/adms/sw/linesOfCodeNumber<br>
     * "measure: number of lines of code"<br>
     * The number of lines of code within the asset (not including any dependencies).<br>
     * Usage Note <br>
     * Observations for the measure 'number of lines of code' should include the dimension 'programming language'.<br>
     */
    public static final URI LINES_OF_CODE_NUMBER;


    /**
     * http://purl.org/adms/sw/commitNumber<br>
     * "measure: number of commits"<br>
     * The number of times code for the asset has been committed to the forge.<br>
     */
    public static final URI COMMIT_NUMBER;


    /**
     * http://purl.org/adms/sw/installationNumber<br>
     * "measure: number of installations"<br>
     * The number known instances of software package that have been installed.<br>
     */
    public static final URI INSTALLATION_NUMBER;


    /**
     * http://purl.org/adms/sw/effort<br>
     * "measure: effort"<br>
     * The effort spent (expressed in personhours) on producing a new software release, not counting the effort spent on previous software releases.<br>
     */
    public static final URI EFFORT;


    /**
     * http://purl.org/adms/sw/programmingLanguage<br>
     * "Software Project - programming language"<br>
     * Programming language of the software.<br>
     * Usage Note <br>
     * Ideally, the range of this property is skos:Concept.<br>
     */
    public static final URI PROGRAMMING_LANGUAGE;


    /**
     * http://purl.org/adms/sw/agentDimension<br>
     * "dimension: agent dimension"<br>
     * The agent category to which a measured observation applies.<br>
     */
    public static final URI AGENT_DIMENSION;


    /**
     * http://purl.org/adms/sw/forkOf<br>
     * "Software Project - fork of"<br>
     * software project of which the current project is a descendant .<br>
     */
    public static final URI FORK_OF;


    /**
     * http://purl.org/adms/sw/package<br>
     * "Software Release - package"<br>
     * The package relationship associates a Software Release with a Software Package.<br>
     */
    public static final URI PACKAGE;


    /**
     * http://purl.org/adms/sw/ticketLeadTime<br>
     * "measure: ticket lead time"<br>
     * The time between opening and closing a support ticket in milliseconds.<br>
     */
    public static final URI TICKET_LEAD_TIME;


    /**
     * http://purl.org/adms/sw/includedAsset<br>
     * "Software Release - included asset"<br>
     * An Asset  that is contained in the Software Release being described. Example: a Linux distribution is being described, that contains other libraries and submodules.<br>
     */
    public static final URI INCLUDED_ASSET;


    /**
     * http://purl.org/adms/sw/programmingLanguageDimension<br>
     * "dimension: programming language dimension"<br>
     * The programming language dimension to which a measured observation applies.<br>
     */
    public static final URI PROGRAMMING_LANGUAGE_DIMENSION;


    /**
     * http://purl.org/adms/sw/userNumber<br>
     * "measure: number of users"<br>
     * The number of known users of a software release.<br>
     */
    public static final URI USER_NUMBER;


    /**
     * http://purl.org/adms/sw/cumulativeEffort<br>
     * "measure: cumulative effort"<br>
     * The total effort spent (expressed in personhours) on producing a software release, including the effort spent on previous releases.<br>
     */
    public static final URI CUMULATIVE_EFFORT;


    /**
     * http://purl.org/adms/sw/project<br>
     * "Software Release - project"<br>
     * The project that has produced the Asset.<br>
     * Usage Note <br>
     * The inverse property of 'admsv:project' is 'doap:release'.<br>
     */
    public static final URI PROJECT;


    /**
     * http://purl.org/adms/sw/packageDimension<br>
     * "dimension: package dimension"<br>
     * The software package category to which a measured observation applies.<br>
     */
    public static final URI PACKAGE_DIMENSION;


    /**
     * http://purl.org/adms/sw/release<br>
     * "Software Package - release"<br>
     * The release relationship associates a Software Package with a Software Release.<br>
     */
    public static final URI RELEASE;


    /**
     * http://purl.org/adms/sw/locale<br>
     * "Software Project - locale"<br>
     * Locale of the software.<br>
     * Usage Note <br>
     * Ideally, the range of this property is skos:Concept.<br>
     */
    public static final URI LOCALE;


    static{
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();

        SOFTWARE_REPOSITORY = valueFactory.createURI(NAMESPACE,"SoftwareRepository");
        SOFTWARE_PROJECT = valueFactory.createURI(NAMESPACE,"SoftwareProject");
        SOFTWARE_RELEASE = valueFactory.createURI(NAMESPACE,"SoftwareRelease");
        SOFTWARE_PACKAGE = valueFactory.createURI(NAMESPACE,"SoftwarePackage");
        SUPPORTS_FORMAT = valueFactory.createURI(NAMESPACE,"supportsFormat");
        STATUS = valueFactory.createURI(NAMESPACE,"status");
        METRICS = valueFactory.createURI(NAMESPACE,"metrics");
        ASSESSMENT = valueFactory.createURI(NAMESPACE,"assessment");
        DOWNLOAD_NUMBER = valueFactory.createURI(NAMESPACE,"downloadNumber");
        FUNDED_BY = valueFactory.createURI(NAMESPACE,"fundedBy");
        TAG_URL = valueFactory.createURI(NAMESPACE,"tagURL");
        IDENTIFIER = valueFactory.createURI(NAMESPACE,"identifier");
        RELEASE_DIMENSION = valueFactory.createURI(NAMESPACE,"releaseDimension");
        INTENDED_AUDIENCE = valueFactory.createURI(NAMESPACE,"intendedAudience");
        USED_BY = valueFactory.createURI(NAMESPACE,"usedBy");
        USER_INTERFACE_TYPE = valueFactory.createURI(NAMESPACE,"userInterfaceType");
        LINES_OF_CODE_NUMBER = valueFactory.createURI(NAMESPACE,"linesOfCodeNumber");
        COMMIT_NUMBER = valueFactory.createURI(NAMESPACE,"commitNumber");
        INSTALLATION_NUMBER = valueFactory.createURI(NAMESPACE,"installationNumber");
        EFFORT = valueFactory.createURI(NAMESPACE,"effort");
        PROGRAMMING_LANGUAGE = valueFactory.createURI(NAMESPACE,"programmingLanguage");
        AGENT_DIMENSION = valueFactory.createURI(NAMESPACE,"agentDimension");
        FORK_OF = valueFactory.createURI(NAMESPACE,"forkOf");
        PACKAGE = valueFactory.createURI(NAMESPACE,"package");
        TICKET_LEAD_TIME = valueFactory.createURI(NAMESPACE,"ticketLeadTime");
        INCLUDED_ASSET = valueFactory.createURI(NAMESPACE,"includedAsset");
        PROGRAMMING_LANGUAGE_DIMENSION = valueFactory.createURI(NAMESPACE,"programmingLanguageDimension");
        USER_NUMBER = valueFactory.createURI(NAMESPACE,"userNumber");
        CUMULATIVE_EFFORT = valueFactory.createURI(NAMESPACE,"cumulativeEffort");
        PROJECT = valueFactory.createURI(NAMESPACE,"project");
        PACKAGE_DIMENSION = valueFactory.createURI(NAMESPACE,"packageDimension");
        RELEASE = valueFactory.createURI(NAMESPACE,"release");
        LOCALE = valueFactory.createURI(NAMESPACE,"locale");
    }
}
