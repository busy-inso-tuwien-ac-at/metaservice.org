package org.metaservice.core;

import java.util.List;

/**
 * Scope Global
 */
public interface Config {
    String getSparqlEndpoint();

    String getJmsBroker();
    List<String> getArchivesForProvider(String provider);

}
