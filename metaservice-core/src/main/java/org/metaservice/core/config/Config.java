package org.metaservice.core.config;

import java.util.List;

/**
 * Scope Global
 */
public interface Config {
    String getSparqlEndpoint();

    String getJmsBroker();
    List<String> getArchivesForProvider(String provider);
    String getArchiveBasePath();
    String getHttpdDataDirectory();

    int getBatchSize();

    boolean getDumpRDFBeforeLoad();
    String  getDumpRDFDirectory();
}
