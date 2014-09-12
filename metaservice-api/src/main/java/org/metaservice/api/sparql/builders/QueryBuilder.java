package org.metaservice.api.sparql.builders;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.sparql.buildingcontexts.SparqlQuery;
import org.metaservice.api.sparql.nodes.*;


/**
 * Created by ilo on 04.03.14.
 */
public interface QueryBuilder extends SelectQueryBuilder,AskQueryBuilder,ConstructQueryBuilder,NamedSubQueriesBuilder{

    @NotNull
    public QueryBuilder select(@NotNull SparqlQuery.DistinctEnum distinct,SelectTerm... selectTerms);
    @NotNull
    public QueryBuilder select(SelectTerm... selectTerms);
    @NotNull
    public QueryBuilder ask();
    @NotNull
    public QueryBuilder construct(Pattern... patterns);
    @NotNull
    public QueryBuilder orderByAsc(@NotNull Variable variable);
    @NotNull
    public QueryBuilder orderByDesc(@NotNull Variable variable);
    @NotNull
    public QueryBuilder groupBy(@NotNull Variable variable);
    @NotNull
    public QueryBuilder limit(int i);

    @NotNull
    public String build();
    @NotNull
    public String build(boolean pretty);
    @NotNull
    public QueryBuilder where(GraphPatternValue... graphPatternValue);


}

