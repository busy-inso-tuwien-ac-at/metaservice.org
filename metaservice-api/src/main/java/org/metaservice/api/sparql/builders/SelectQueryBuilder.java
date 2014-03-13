package org.metaservice.api.sparql.builders;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.sparql.nodes.GraphPatternValue;
import org.metaservice.api.sparql.nodes.NamedSubQuery;
import org.metaservice.api.sparql.nodes.SelectTerm;
import org.metaservice.api.sparql.nodes.Variable;

/**
 * Created by ilo on 04.03.14.
 */
public interface SelectQueryBuilder extends NamedSubQueriesBuilder,GraphPatternValue{

    @NotNull
    public SelectQueryBuilder select(boolean distinct,SelectTerm... selectTerms);
    @NotNull
    public SelectQueryBuilder orderByAsc(@NotNull Variable variable);
    @NotNull
    public SelectQueryBuilder orderByDesc(@NotNull Variable variable);
    @NotNull
    public SelectQueryBuilder groupBy(@NotNull Variable variable);
    @NotNull
    public SelectQueryBuilder limit(int i);

    @NotNull
    public String build();

    @NotNull
    public String build(boolean pretty);

    @NotNull
    public SelectQueryBuilder where(GraphPatternValue... graphPatternValue);

}


