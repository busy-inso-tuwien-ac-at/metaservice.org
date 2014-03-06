package org.metaservice.api.sparql.buildingcontexts;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.sparql.builders.SelectQueryBuilder;
import org.metaservice.api.sparql.nodes.NamedSubQuery;
import org.metaservice.api.sparql.builders.QueryBuilder;

import org.metaservice.api.sparql.nodes.Pattern;

/**
 * Created by ilo on 05.03.14.
 */
public interface NamedSubQueries {

    @NotNull
    public NamedSubQuery namedSubQuery(@NotNull String name,@NotNull SelectQueryBuilder queryBuilder);

    @NotNull
    public Pattern include(@NotNull String name);

}
