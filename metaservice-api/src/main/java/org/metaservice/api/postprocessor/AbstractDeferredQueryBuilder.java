package org.metaservice.api.postprocessor;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.sparql.builders.QueryBuilder;
import org.metaservice.api.sparql.nodes.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ilo on 10.03.14.
 */
public abstract class AbstractDeferredQueryBuilder  implements QueryBuilder {
    protected Integer limit = null;
    protected SelectTerm[] selectTerms = null;

    protected final ArrayList<Variable> orderByAscList = new ArrayList<>();
    protected final ArrayList<Variable> orderByDescList = new ArrayList<>();
    protected final ArrayList<Variable> groupByList = new ArrayList<>();
    protected ArrayList<GraphPatternValue> wherePatterns = new ArrayList<>();
    protected ArrayList<NamedSubQuery> originalNamedSubQueries =new ArrayList<>();
    protected boolean distinct=false;

    @Override
    @NotNull
    public QueryBuilder select(boolean distinct,SelectTerm... selectTerms) {
        this.distinct = distinct;
        this.selectTerms = selectTerms;
        return this;
    }


    @Override
    @NotNull
    public QueryBuilder ask() {
        throw  new UnsupportedOperationException();
    }

    @Override
    @NotNull
    public QueryBuilder construct(Pattern... patterns) {
        throw  new UnsupportedOperationException();
    }


    @Override
    @NotNull
    public QueryBuilder orderByAsc(@NotNull Variable variable) {
        orderByAscList.add(variable);
        return this;
    }

    @Override
    @NotNull
    public QueryBuilder orderByDesc(@NotNull Variable variable) {
        orderByDescList.add(variable);
        return this;
    }


    @Override
    @NotNull
    public QueryBuilder groupBy(@NotNull Variable variable) {
        groupByList.add(variable);
        return this;
    }

    @Override
    @NotNull
    public QueryBuilder limit(int i) {
        limit = i;
        return this;
    }

    @Override
    @NotNull
    public QueryBuilder with(NamedSubQuery... namedSubQueries) {
        originalNamedSubQueries.addAll(Arrays.asList(namedSubQueries));
        return this;
    }

    @Override
    @NotNull
    public String build() {
        return build(true);
    }

    @Override
    @NotNull
    public QueryBuilder where(GraphPatternValue... graphPatternValues) {
        this.wherePatterns.addAll(Arrays.asList(graphPatternValues));
        return this;
    }
}
