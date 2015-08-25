package org.metaservice.api.messaging;

import org.jetbrains.annotations.NotNull;

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

    @NotNull
    String getDefaultProviderOpts();

    @NotNull
    String getDefaultPostProcessorOpts();
}
