package org.metaservice.api.sparql.builders;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.sparql.nodes.NamedSubQuery;

/**
 * Created by ilo on 06.03.14.
 */
public interface NamedSubQueriesBuilder {
    @NotNull
    public QueryBuilder with(NamedSubQuery... namedSubQueries);
}
