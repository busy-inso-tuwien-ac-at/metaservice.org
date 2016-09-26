/*
 * Copyright 2015 Nikola Ilo
 * Research Group for Industrial Software, Vienna University of Technology, Austria
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaservice.api.rdf.vocabulary;

import org.openrdf.model.*;
import org.openrdf.model.impl.*;



/**
 * This is an automatically generated class
 * Generator: org.metaservice.core.OntologyToJavaConverter
 * @see <a href="http://purl.org/dc/terms/">dcterms</a>
 */
public class DCTERMS{

    public static final String NAMESPACE = "http://purl.org/dc/terms/";

    public static final String PREFIX = "dcterms";

    public static final Namespace NS = new NamespaceImpl(PREFIX, NAMESPACE);

////////////////////////
// CLASSES
////////////////////////


    /**
     * http://purl.org/dc/terms/URI<br>
     * "URI"<br>
     * The set of identifiers constructed according to the generic syntax for Uniform Resource Identifiers as specified by the Internet Engineering Task Force.<br>
     */
    public static final URI URI;


    /**
     * http://purl.org/dc/terms/PeriodOfTime<br>
     * "Period of Time"<br>
     * An interval of time that is named or defined by its start and end dates.<br>
     */
    public static final URI PERIOD_OF_TIME;


    /**
     * http://purl.org/dc/terms/MediaTypeOrExtent<br>
     * "Media Type or Extent"<br>
     * A media type or extent.<br>
     */
    public static final URI MEDIA_TYPE_OR_EXTENT;


    /**
     * http://purl.org/dc/terms/ProvenanceStatement<br>
     * "Provenance Statement"<br>
     * A statement of any changes in ownership and custody of a resource since its creation that are significant for its authenticity, integrity, and interpretation.<br>
     */
    public static final URI PROVENANCE_STATEMENT;


    /**
     * http://purl.org/dc/terms/AgentClass<br>
     * "Agent Class"<br>
     * A group of agents.<br>
     */
    public static final URI AGENT_CLASS;


    /**
     * http://purl.org/dc/terms/Point<br>
     * "DCMI Point"<br>
     * The set of points in space defined by their geographic coordinates according to the DCMI Point Encoding Scheme.<br>
     */
    public static final URI POINT;


    /**
     * http://purl.org/dc/terms/Frequency<br>
     * "Frequency"<br>
     * A rate at which something recurs.<br>
     */
    public static final URI FREQUENCY;


    /**
     * http://purl.org/dc/terms/SizeOrDuration<br>
     * "Size or Duration"<br>
     * A dimension or extent, or a time taken to play or execute.<br>
     */
    public static final URI SIZE_OR_DURATION;


    /**
     * http://purl.org/dc/terms/W3CDTF<br>
     * "W3C-DTF"<br>
     * The set of dates and times constructed according to the W3C Date and Time Formats Specification.<br>
     */
    public static final URI W3_CDTF;


    /**
     * http://purl.org/dc/terms/PhysicalResource<br>
     * "Physical Resource"<br>
     * A material thing.<br>
     */
    public static final URI PHYSICAL_RESOURCE;


    /**
     * http://purl.org/dc/terms/ISO639-3<br>
     * "ISO 639-3"<br>
     * The set of three-letter codes listed in ISO 639-3 for the representation of names of languages.<br>
     */
    public static final URI ISO639_3;


    /**
     * http://purl.org/dc/terms/ISO639-2<br>
     * "ISO 639-2"<br>
     * The three-letter alphabetic codes listed in ISO639-2 for the representation of names of languages.<br>
     */
    public static final URI ISO639_2;


    /**
     * http://purl.org/dc/terms/Box<br>
     * "DCMI Box"<br>
     * The set of regions in space defined by their geographic coordinates according to the DCMI Box Encoding Scheme.<br>
     */
    public static final URI BOX;


    /**
     * http://purl.org/dc/terms/Standard<br>
     * "Standard"<br>
     * A basis for comparison; a reference point against which other things can be evaluated.<br>
     */
    public static final URI STANDARD;


    /**
     * http://purl.org/dc/terms/Period<br>
     * "DCMI Period"<br>
     * The set of time intervals defined by their limits according to the DCMI Period Encoding Scheme.<br>
     */
    public static final URI PERIOD;


    /**
     * http://purl.org/dc/terms/Policy<br>
     * "Policy"<br>
     * A plan or course of action by an authority, intended to influence and determine decisions, actions, and other matters.<br>
     */
    public static final URI POLICY;


    /**
     * http://purl.org/dc/terms/Location<br>
     * "Location"<br>
     * A spatial region or named place.<br>
     */
    public static final URI LOCATION;


    /**
     * http://purl.org/dc/terms/RFC3066<br>
     * "RFC 3066"<br>
     * The set of tags constructed according to RFC 3066 for the identification of languages.<br>
     */
    public static final URI RFC3066;


    /**
     * http://purl.org/dc/terms/Agent<br>
     * "Agent"<br>
     * A resource that acts or has the power to act.<br>
     */
    public static final URI AGENT;


    /**
     * http://purl.org/dc/terms/LinguisticSystem<br>
     * "Linguistic System"<br>
     * A system of signs, symbols, sounds, gestures, or rules used in communication.<br>
     */
    public static final URI LINGUISTIC_SYSTEM;


    /**
     * http://purl.org/dc/terms/RightsStatement<br>
     * "Rights Statement"<br>
     * A statement about the intellectual property rights (IPR) held in or over a Resource, a legal document giving official permission to do something with a resource, or a statement about access rights.<br>
     */
    public static final URI RIGHTS_STATEMENT;


    /**
     * http://purl.org/dc/terms/BibliographicResource<br>
     * "Bibliographic Resource"<br>
     * A book, article, or other documentary resource.<br>
     */
    public static final URI BIBLIOGRAPHIC_RESOURCE;


    /**
     * http://purl.org/dc/terms/FileFormat<br>
     * "File Format"<br>
     * A digital resource format.<br>
     */
    public static final URI FILE_FORMAT;


    /**
     * http://purl.org/dc/terms/LicenseDocument<br>
     * "License Document"<br>
     * A legal document giving official permission to do something with a Resource.<br>
     */
    public static final URI LICENSE_DOCUMENT;


    /**
     * http://purl.org/dc/terms/ISO3166<br>
     * "ISO 3166"<br>
     * The set of codes listed in ISO 3166-1 for the representation of names of countries.<br>
     */
    public static final URI ISO3166;


    /**
     * http://purl.org/dc/terms/PhysicalMedium<br>
     * "Physical Medium"<br>
     * A physical material or carrier.<br>
     */
    public static final URI PHYSICAL_MEDIUM;


    /**
     * http://purl.org/dc/terms/RFC1766<br>
     * "RFC 1766"<br>
     * The set of tags, constructed according to RFC 1766, for the identification of languages.<br>
     */
    public static final URI RFC1766;


    /**
     * http://purl.org/dc/terms/LocationPeriodOrJurisdiction<br>
     * "Location, Period, or Jurisdiction"<br>
     * A location, period of time, or jurisdiction.<br>
     */
    public static final URI LOCATION_PERIOD_OR_JURISDICTION;


    /**
     * http://purl.org/dc/terms/Jurisdiction<br>
     * "Jurisdiction"<br>
     * The extent or range of judicial, law enforcement, or other authority.<br>
     */
    public static final URI JURISDICTION;


    /**
     * http://purl.org/dc/terms/MethodOfInstruction<br>
     * "Method of Instruction"<br>
     * A process that is used to engender knowledge, attitudes, and skills.<br>
     */
    public static final URI METHOD_OF_INSTRUCTION;


    /**
     * http://purl.org/dc/terms/MethodOfAccrual<br>
     * "Method of Accrual"<br>
     * A method by which resources are added to a collection.<br>
     */
    public static final URI METHOD_OF_ACCRUAL;


    /**
     * http://purl.org/dc/terms/MediaType<br>
     * "Media Type"<br>
     * A file format or physical medium.<br>
     */
    public static final URI MEDIA_TYPE;


    /**
     * http://purl.org/dc/terms/RFC5646<br>
     * "RFC 5646"<br>
     * The set of tags constructed according to RFC 5646 for the identification of languages.<br>
     */
    public static final URI RFC5646;


    /**
     * http://purl.org/dc/terms/RFC4646<br>
     * "RFC 4646"<br>
     * The set of tags constructed according to RFC 4646 for the identification of languages.<br>
     */
    public static final URI RFC4646;


////////////////////////
// PROPERTIES
////////////////////////


    /**
     * http://purl.org/dc/terms/language<br>
     * "Language"<br>
     * A language of the resource.<br>
     */
    public static final URI LANGUAGE;


    /**
     * http://purl.org/dc/terms/medium<br>
     * "Medium"<br>
     * The material or physical carrier of the resource.<br>
     */
    public static final URI MEDIUM;


    /**
     * http://purl.org/dc/terms/dateCopyrighted<br>
     * "Date Copyrighted"<br>
     * Date of copyright.<br>
     */
    public static final URI DATE_COPYRIGHTED;


    /**
     * http://purl.org/dc/terms/mediator<br>
     * "Mediator"<br>
     * An entity that mediates access to the resource and for whom the resource is intended or useful.<br>
     */
    public static final URI MEDIATOR;


    /**
     * http://purl.org/dc/terms/accessRights<br>
     * "Access Rights"<br>
     * Information about who can access the resource or an indication of its security status.<br>
     */
    public static final URI ACCESS_RIGHTS;


    /**
     * http://purl.org/dc/terms/hasVersion<br>
     * "Has Version"<br>
     * A related resource that is a version, edition, or adaptation of the described resource.<br>
     */
    public static final URI HAS_VERSION;


    /**
     * http://purl.org/dc/terms/date<br>
     * "Date"<br>
     * A point or period of time associated with an event in the lifecycle of the resource.<br>
     */
    public static final URI DATE;


    /**
     * http://purl.org/dc/terms/relation<br>
     * "Relation"<br>
     * A related resource.<br>
     */
    public static final URI RELATION;


    /**
     * http://purl.org/dc/terms/requires<br>
     * "Requires"<br>
     * A related resource that is required by the described resource to support its function, delivery, or coherence.<br>
     */
    public static final URI REQUIRES;


    /**
     * http://purl.org/dc/terms/instructionalMethod<br>
     * "Instructional Method"<br>
     * A process, used to engender knowledge, attitudes and skills, that the described resource is designed to support.<br>
     */
    public static final URI INSTRUCTIONAL_METHOD;


    /**
     * http://purl.org/dc/terms/available<br>
     * "Date Available"<br>
     * Date (often a range) that the resource became or will become available.<br>
     */
    public static final URI AVAILABLE;


    /**
     * http://purl.org/dc/terms/references<br>
     * "References"<br>
     * A related resource that is referenced, cited, or otherwise pointed to by the described resource.<br>
     */
    public static final URI REFERENCES;


    /**
     * http://purl.org/dc/terms/created<br>
     * "Date Created"<br>
     * Date of creation of the resource.<br>
     */
    public static final URI CREATED;


    /**
     * http://purl.org/dc/terms/isFormatOf<br>
     * "Is Format Of"<br>
     * A related resource that is substantially the same as the described resource, but in another format.<br>
     */
    public static final URI IS_FORMAT_OF;


    /**
     * http://purl.org/dc/terms/identifier<br>
     * "Identifier"<br>
     * An unambiguous reference to the resource within a given context.<br>
     */
    public static final URI IDENTIFIER;


    /**
     * http://purl.org/dc/terms/accrualMethod<br>
     * "Accrual Method"<br>
     * The method by which items are added to a collection.<br>
     */
    public static final URI ACCRUAL_METHOD;


    /**
     * http://purl.org/dc/terms/replaces<br>
     * "Replaces"<br>
     * A related resource that is supplanted, displaced, or superseded by the described resource.<br>
     */
    public static final URI REPLACES;


    /**
     * http://purl.org/dc/terms/rights<br>
     * "Rights"<br>
     * Information about rights held in and over the resource.<br>
     */
    public static final URI RIGHTS;


    /**
     * http://purl.org/dc/terms/modified<br>
     * "Date Modified"<br>
     * Date on which the resource was changed.<br>
     */
    public static final URI MODIFIED;


    /**
     * http://purl.org/dc/terms/issued<br>
     * "Date Issued"<br>
     * Date of formal issuance (e.g., publication) of the resource.<br>
     */
    public static final URI ISSUED;


    /**
     * http://purl.org/dc/terms/description<br>
     * "Description"<br>
     * An account of the resource.<br>
     */
    public static final URI DESCRIPTION;


    /**
     * http://purl.org/dc/terms/accrualPeriodicity<br>
     * "Accrual Periodicity"<br>
     * The frequency with which items are added to a collection.<br>
     */
    public static final URI ACCRUAL_PERIODICITY;


    /**
     * http://purl.org/dc/terms/type<br>
     * "Type"<br>
     * The nature or genre of the resource.<br>
     */
    public static final URI TYPE;


    /**
     * http://purl.org/dc/terms/isVersionOf<br>
     * "Is Version Of"<br>
     * A related resource of which the described resource is a version, edition, or adaptation.<br>
     */
    public static final URI IS_VERSION_OF;


    /**
     * http://purl.org/dc/terms/audience<br>
     * "Audience"<br>
     * A class of entity for whom the resource is intended or useful.<br>
     */
    public static final URI AUDIENCE;


    /**
     * http://purl.org/dc/terms/title<br>
     * "Title"<br>
     * A name given to the resource.<br>
     */
    public static final URI TITLE;


    /**
     * http://purl.org/dc/terms/bibliographicCitation<br>
     * "Bibliographic Citation"<br>
     * A bibliographic reference for the resource.<br>
     */
    public static final URI BIBLIOGRAPHIC_CITATION;


    /**
     * http://purl.org/dc/terms/dateAccepted<br>
     * "Date Accepted"<br>
     * Date of acceptance of the resource.<br>
     */
    public static final URI DATE_ACCEPTED;


    /**
     * http://purl.org/dc/terms/coverage<br>
     * "Coverage"<br>
     * The spatial or temporal topic of the resource, the spatial applicability of the resource, or the jurisdiction under which the resource is relevant.<br>
     */
    public static final URI COVERAGE;


    /**
     * http://purl.org/dc/terms/educationLevel<br>
     * "Audience Education Level"<br>
     * A class of entity, defined in terms of progression through an educational or training context, for which the described resource is intended.<br>
     */
    public static final URI EDUCATION_LEVEL;


    /**
     * http://purl.org/dc/terms/format<br>
     * "Format"<br>
     * The file format, physical medium, or dimensions of the resource.<br>
     */
    public static final URI FORMAT;


    /**
     * http://purl.org/dc/terms/alternative<br>
     * "Alternative Title"<br>
     * An alternative name for the resource.<br>
     */
    public static final URI ALTERNATIVE;


    /**
     * http://purl.org/dc/terms/valid<br>
     * "Date Valid"<br>
     * Date (often a range) of validity of a resource.<br>
     */
    public static final URI VALID;


    /**
     * http://purl.org/dc/terms/creator<br>
     * "Creator"<br>
     * An entity primarily responsible for making the resource.<br>
     */
    public static final URI CREATOR;


    /**
     * http://purl.org/dc/terms/source<br>
     * "Source"<br>
     * A related resource from which the described resource is derived.<br>
     */
    public static final URI SOURCE;


    /**
     * http://purl.org/dc/terms/hasPart<br>
     * "Has Part"<br>
     * A related resource that is included either physically or logically in the described resource.<br>
     */
    public static final URI HAS_PART;


    /**
     * http://purl.org/dc/terms/provenance<br>
     * "Provenance"<br>
     * A statement of any changes in ownership and custody of the resource since its creation that are significant for its authenticity, integrity, and interpretation.<br>
     */
    public static final URI PROVENANCE;


    /**
     * http://purl.org/dc/terms/dateSubmitted<br>
     * "Date Submitted"<br>
     * Date of submission of the resource.<br>
     */
    public static final URI DATE_SUBMITTED;


    /**
     * http://purl.org/dc/terms/abstract<br>
     * "Abstract"<br>
     * A summary of the resource.<br>
     */
    public static final URI ABSTRACT;


    /**
     * http://purl.org/dc/terms/isReferencedBy<br>
     * "Is Referenced By"<br>
     * A related resource that references, cites, or otherwise points to the described resource.<br>
     */
    public static final URI IS_REFERENCED_BY;


    /**
     * http://purl.org/dc/terms/license<br>
     * "License"<br>
     * A legal document giving official permission to do something with the resource.<br>
     */
    public static final URI LICENSE;


    /**
     * http://purl.org/dc/terms/accrualPolicy<br>
     * "Accrual Policy"<br>
     * The policy governing the addition of items to a collection.<br>
     */
    public static final URI ACCRUAL_POLICY;


    /**
     * http://purl.org/dc/terms/subject<br>
     * "Subject"<br>
     * The topic of the resource.<br>
     */
    public static final URI SUBJECT;


    /**
     * http://purl.org/dc/terms/conformsTo<br>
     * "Conforms To"<br>
     * An established standard to which the described resource conforms.<br>
     */
    public static final URI CONFORMS_TO;


    /**
     * http://purl.org/dc/terms/spatial<br>
     * "Spatial Coverage"<br>
     * Spatial characteristics of the resource.<br>
     */
    public static final URI SPATIAL;


    /**
     * http://purl.org/dc/terms/isReplacedBy<br>
     * "Is Replaced By"<br>
     * A related resource that supplants, displaces, or supersedes the described resource.<br>
     */
    public static final URI IS_REPLACED_BY;


    /**
     * http://purl.org/dc/terms/publisher<br>
     * "Publisher"<br>
     * An entity responsible for making the resource available.<br>
     */
    public static final URI PUBLISHER;


    /**
     * http://purl.org/dc/terms/tableOfContents<br>
     * "Table Of Contents"<br>
     * A list of subunits of the resource.<br>
     */
    public static final URI TABLE_OF_CONTENTS;


    /**
     * http://purl.org/dc/terms/hasFormat<br>
     * "Has Format"<br>
     * A related resource that is substantially the same as the pre-existing described resource, but in another format.<br>
     */
    public static final URI HAS_FORMAT;


    /**
     * http://purl.org/dc/terms/rightsHolder<br>
     * "Rights Holder"<br>
     * A person or organization owning or managing rights over the resource.<br>
     */
    public static final URI RIGHTS_HOLDER;


    /**
     * http://purl.org/dc/terms/contributor<br>
     * "Contributor"<br>
     * An entity responsible for making contributions to the resource.<br>
     */
    public static final URI CONTRIBUTOR;


    /**
     * http://purl.org/dc/terms/isRequiredBy<br>
     * "Is Required By"<br>
     * A related resource that requires the described resource to support its function, delivery, or coherence.<br>
     */
    public static final URI IS_REQUIRED_BY;


    /**
     * http://purl.org/dc/terms/isPartOf<br>
     * "Is Part Of"<br>
     * A related resource in which the described resource is physically or logically included.<br>
     */
    public static final URI IS_PART_OF;


    /**
     * http://purl.org/dc/terms/extent<br>
     * "Extent"<br>
     * The size or duration of the resource.<br>
     */
    public static final URI EXTENT;


    /**
     * http://purl.org/dc/terms/temporal<br>
     * "Temporal Coverage"<br>
     * Temporal characteristics of the resource.<br>
     */
    public static final URI TEMPORAL;


    static{
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();

        URI = valueFactory.createURI(NAMESPACE,"URI");
        PERIOD_OF_TIME = valueFactory.createURI(NAMESPACE,"PeriodOfTime");
        MEDIA_TYPE_OR_EXTENT = valueFactory.createURI(NAMESPACE,"MediaTypeOrExtent");
        PROVENANCE_STATEMENT = valueFactory.createURI(NAMESPACE,"ProvenanceStatement");
        AGENT_CLASS = valueFactory.createURI(NAMESPACE,"AgentClass");
        POINT = valueFactory.createURI(NAMESPACE,"Point");
        FREQUENCY = valueFactory.createURI(NAMESPACE,"Frequency");
        SIZE_OR_DURATION = valueFactory.createURI(NAMESPACE,"SizeOrDuration");
        W3_CDTF = valueFactory.createURI(NAMESPACE,"W3CDTF");
        PHYSICAL_RESOURCE = valueFactory.createURI(NAMESPACE,"PhysicalResource");
        ISO639_3 = valueFactory.createURI(NAMESPACE,"ISO639-3");
        ISO639_2 = valueFactory.createURI(NAMESPACE,"ISO639-2");
        BOX = valueFactory.createURI(NAMESPACE,"Box");
        STANDARD = valueFactory.createURI(NAMESPACE,"Standard");
        PERIOD = valueFactory.createURI(NAMESPACE,"Period");
        POLICY = valueFactory.createURI(NAMESPACE,"Policy");
        LOCATION = valueFactory.createURI(NAMESPACE,"Location");
        RFC3066 = valueFactory.createURI(NAMESPACE,"RFC3066");
        AGENT = valueFactory.createURI(NAMESPACE,"Agent");
        LINGUISTIC_SYSTEM = valueFactory.createURI(NAMESPACE,"LinguisticSystem");
        RIGHTS_STATEMENT = valueFactory.createURI(NAMESPACE,"RightsStatement");
        BIBLIOGRAPHIC_RESOURCE = valueFactory.createURI(NAMESPACE,"BibliographicResource");
        FILE_FORMAT = valueFactory.createURI(NAMESPACE,"FileFormat");
        LICENSE_DOCUMENT = valueFactory.createURI(NAMESPACE,"LicenseDocument");
        ISO3166 = valueFactory.createURI(NAMESPACE,"ISO3166");
        PHYSICAL_MEDIUM = valueFactory.createURI(NAMESPACE,"PhysicalMedium");
        RFC1766 = valueFactory.createURI(NAMESPACE,"RFC1766");
        LOCATION_PERIOD_OR_JURISDICTION = valueFactory.createURI(NAMESPACE,"LocationPeriodOrJurisdiction");
        JURISDICTION = valueFactory.createURI(NAMESPACE,"Jurisdiction");
        METHOD_OF_INSTRUCTION = valueFactory.createURI(NAMESPACE,"MethodOfInstruction");
        METHOD_OF_ACCRUAL = valueFactory.createURI(NAMESPACE,"MethodOfAccrual");
        MEDIA_TYPE = valueFactory.createURI(NAMESPACE,"MediaType");
        RFC5646 = valueFactory.createURI(NAMESPACE,"RFC5646");
        RFC4646 = valueFactory.createURI(NAMESPACE,"RFC4646");
        LANGUAGE = valueFactory.createURI(NAMESPACE,"language");
        MEDIUM = valueFactory.createURI(NAMESPACE,"medium");
        DATE_COPYRIGHTED = valueFactory.createURI(NAMESPACE,"dateCopyrighted");
        MEDIATOR = valueFactory.createURI(NAMESPACE,"mediator");
        ACCESS_RIGHTS = valueFactory.createURI(NAMESPACE,"accessRights");
        HAS_VERSION = valueFactory.createURI(NAMESPACE,"hasVersion");
        DATE = valueFactory.createURI(NAMESPACE,"date");
        RELATION = valueFactory.createURI(NAMESPACE,"relation");
        REQUIRES = valueFactory.createURI(NAMESPACE,"requires");
        INSTRUCTIONAL_METHOD = valueFactory.createURI(NAMESPACE,"instructionalMethod");
        AVAILABLE = valueFactory.createURI(NAMESPACE,"available");
        REFERENCES = valueFactory.createURI(NAMESPACE,"references");
        CREATED = valueFactory.createURI(NAMESPACE,"created");
        IS_FORMAT_OF = valueFactory.createURI(NAMESPACE,"isFormatOf");
        IDENTIFIER = valueFactory.createURI(NAMESPACE,"identifier");
        ACCRUAL_METHOD = valueFactory.createURI(NAMESPACE,"accrualMethod");
        REPLACES = valueFactory.createURI(NAMESPACE,"replaces");
        RIGHTS = valueFactory.createURI(NAMESPACE,"rights");
        MODIFIED = valueFactory.createURI(NAMESPACE,"modified");
        ISSUED = valueFactory.createURI(NAMESPACE,"issued");
        DESCRIPTION = valueFactory.createURI(NAMESPACE,"description");
        ACCRUAL_PERIODICITY = valueFactory.createURI(NAMESPACE,"accrualPeriodicity");
        TYPE = valueFactory.createURI(NAMESPACE,"type");
        IS_VERSION_OF = valueFactory.createURI(NAMESPACE,"isVersionOf");
        AUDIENCE = valueFactory.createURI(NAMESPACE,"audience");
        TITLE = valueFactory.createURI(NAMESPACE,"title");
        BIBLIOGRAPHIC_CITATION = valueFactory.createURI(NAMESPACE,"bibliographicCitation");
        DATE_ACCEPTED = valueFactory.createURI(NAMESPACE,"dateAccepted");
        COVERAGE = valueFactory.createURI(NAMESPACE,"coverage");
        EDUCATION_LEVEL = valueFactory.createURI(NAMESPACE,"educationLevel");
        FORMAT = valueFactory.createURI(NAMESPACE,"format");
        ALTERNATIVE = valueFactory.createURI(NAMESPACE,"alternative");
        VALID = valueFactory.createURI(NAMESPACE,"valid");
        CREATOR = valueFactory.createURI(NAMESPACE,"creator");
        SOURCE = valueFactory.createURI(NAMESPACE,"source");
        HAS_PART = valueFactory.createURI(NAMESPACE,"hasPart");
        PROVENANCE = valueFactory.createURI(NAMESPACE,"provenance");
        DATE_SUBMITTED = valueFactory.createURI(NAMESPACE,"dateSubmitted");
        ABSTRACT = valueFactory.createURI(NAMESPACE,"abstract");
        IS_REFERENCED_BY = valueFactory.createURI(NAMESPACE,"isReferencedBy");
        LICENSE = valueFactory.createURI(NAMESPACE,"license");
        ACCRUAL_POLICY = valueFactory.createURI(NAMESPACE,"accrualPolicy");
        SUBJECT = valueFactory.createURI(NAMESPACE,"subject");
        CONFORMS_TO = valueFactory.createURI(NAMESPACE,"conformsTo");
        SPATIAL = valueFactory.createURI(NAMESPACE,"spatial");
        IS_REPLACED_BY = valueFactory.createURI(NAMESPACE,"isReplacedBy");
        PUBLISHER = valueFactory.createURI(NAMESPACE,"publisher");
        TABLE_OF_CONTENTS = valueFactory.createURI(NAMESPACE,"tableOfContents");
        HAS_FORMAT = valueFactory.createURI(NAMESPACE,"hasFormat");
        RIGHTS_HOLDER = valueFactory.createURI(NAMESPACE,"rightsHolder");
        CONTRIBUTOR = valueFactory.createURI(NAMESPACE,"contributor");
        IS_REQUIRED_BY = valueFactory.createURI(NAMESPACE,"isRequiredBy");
        IS_PART_OF = valueFactory.createURI(NAMESPACE,"isPartOf");
        EXTENT = valueFactory.createURI(NAMESPACE,"extent");
        TEMPORAL = valueFactory.createURI(NAMESPACE,"temporal");
    }
}
