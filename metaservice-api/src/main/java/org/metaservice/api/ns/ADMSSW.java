package org.metaservice.api.ns;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

public class ADMSSW {
    public static final String NAMESPACE = "http://purl.org/adms/sw/";

    /**
     * CLASS
     */
    public static final URI SOFTWARE_PROJECT;
    public static final URI SOFTWARE_RELEASE;
    public static final URI SOFTWARE_PACKAGE;
    public static final URI SOFTWARE_REPOSITORY;


    /**
     * PROPERTY
     */
    public static final URI FUNDED_BY;
    public static final URI FORK_OF;
    public static final URI INTENDED_AUDIENCE;
    public static final URI LOCALE;
    public static final URI USER_INTERFACE_TYPE;
    public static final URI PROGRAMMING_LANGUAGE;
    public static final URI SUPPORTS_FORMAT;
    public static final URI STATUS;
    public static final URI IDENTIFIER;
    public static final URI ASSESSMENT;
    public static final URI INCLUDED_ASSET;
    public static final URI METRICS;
    public static final URI PROJECT;
    public static final URI PACKAGE;
    public static final URI USED_BY;
    public static final URI TAG_URL;
    public static final URI RELEASE;
    public static final URI DOWNLOAD_NUMBER;
    public static final URI INSTALLATION_NUMBER;
    public static final URI USER_NUMBER;
    public static final URI COMMIT_NUMBER;
    public static final URI LINES_OF_CODE_NUMBER;
    public static final URI TICKET_LEAD_TIME;
    public static final URI EFFORT;
    public static final URI CUMULATIVE_EFFORT;

    public static final URI RELEASE_DIMENSION;
    public static final URI PACKAGE_DIMENSION;
    public static final URI PROGRAMMING_LANGUAGE_DIMENSION;


    static {
        ValueFactory factory = ValueFactoryImpl.getInstance();

        SOFTWARE_PROJECT = factory.createURI(ADMSSW.NAMESPACE,"SoftwareProject");
        SOFTWARE_RELEASE = factory.createURI(ADMSSW.NAMESPACE,"SoftwareRelease");
        SOFTWARE_PACKAGE = factory.createURI(ADMSSW.NAMESPACE,"SoftwarePackage");
        SOFTWARE_REPOSITORY = factory.createURI(ADMSSW.NAMESPACE,"SoftwareRepository");
        FUNDED_BY = factory.createURI(ADMSSW.NAMESPACE,"fundedBy");
        FORK_OF = factory.createURI(ADMSSW.NAMESPACE,"forkOf");
        INTENDED_AUDIENCE = factory.createURI(ADMSSW.NAMESPACE,"intendedAudience");
        LOCALE = factory.createURI(ADMSSW.NAMESPACE,"locale");
        USER_INTERFACE_TYPE = factory.createURI(ADMSSW.NAMESPACE,"userInterfaceType");
        PROGRAMMING_LANGUAGE = factory.createURI(ADMSSW.NAMESPACE,"programmingLanguage");
        SUPPORTS_FORMAT = factory.createURI(ADMSSW.NAMESPACE,"supportsFormat");
        STATUS = factory.createURI(ADMSSW.NAMESPACE,"status");
        IDENTIFIER = factory.createURI(ADMSSW.NAMESPACE,"identifier");
        ASSESSMENT = factory.createURI(ADMSSW.NAMESPACE,"assessment");
        INCLUDED_ASSET = factory.createURI(ADMSSW.NAMESPACE,"includedAsset");
        METRICS = factory.createURI(ADMSSW.NAMESPACE,"metrics");
        PROJECT = factory.createURI(ADMSSW.NAMESPACE,"project");
        PACKAGE = factory.createURI(ADMSSW.NAMESPACE,"package");
        USED_BY = factory.createURI(ADMSSW.NAMESPACE,"usedBy");
        TAG_URL = factory.createURI(ADMSSW.NAMESPACE,"tagURL");
        RELEASE = factory.createURI(ADMSSW.NAMESPACE,"release");
        DOWNLOAD_NUMBER = factory.createURI(ADMSSW.NAMESPACE,"downloadNumber");
        INSTALLATION_NUMBER = factory.createURI(ADMSSW.NAMESPACE,"installationNumber");
        USER_NUMBER = factory.createURI(ADMSSW.NAMESPACE,"userNumber");
        COMMIT_NUMBER = factory.createURI(ADMSSW.NAMESPACE,"commitNumber");
        LINES_OF_CODE_NUMBER = factory.createURI(ADMSSW.NAMESPACE,"linesOfCodeNumber");
        TICKET_LEAD_TIME = factory.createURI(ADMSSW.NAMESPACE,"ticketLeadTime");
        EFFORT = factory.createURI(ADMSSW.NAMESPACE,"effort");
        CUMULATIVE_EFFORT = factory.createURI(ADMSSW.NAMESPACE,"cumulativeEffort");

        RELEASE_DIMENSION = factory.createURI(ADMSSW.NAMESPACE,"releaseDimension");
        PACKAGE_DIMENSION = factory.createURI(ADMSSW.NAMESPACE,"packageDimension");
        PROGRAMMING_LANGUAGE_DIMENSION = factory.createURI(ADMSSW.NAMESPACE,"programmingLanguageDimension");
    }


    /**
     * TODO not really admssw
     */
    public final static URI NEXT;
    public static final URI PREV;

    static {
        ValueFactory factory = ValueFactoryImpl.getInstance();

        String ns ="http://www.w3.org/1999/xhtml/vocab#";
        NEXT = factory.createURI(ns,"next");
        PREV = factory.createURI(ns,"prev");
    }

}
