package org.metaservice.api.sparql;

import org.jetbrains.annotations.NotNull;

/**
 * Created by ilo on 04.03.14.
 */
public interface SPARQLQueryBuilder {

    @NotNull
    public SPARQLQueryBuilder select(SelectTerm... selectTerms);
    @NotNull
    public SPARQLQueryBuilder ask();
    @NotNull
    public SPARQLQueryBuilder construct(Pattern... patterns);
    @NotNull
    public SPARQLQueryBuilder orderByAsc(@NotNull Variable variable);
    @NotNull
    public SPARQLQueryBuilder orderByDesc(@NotNull Variable variable);
    @NotNull
    public SPARQLQueryBuilder groupBy(@NotNull Variable variable);
    @NotNull
    public SPARQLQueryBuilder limit(int i);
    @NotNull
    public String build();
    @NotNull
    public SparqlQueryBuilderImpl.WhereClause where(GraphPatternValue... graphPatternValue);

    public boolean isPrettyPrint();

    public static interface GraphPattern{
        public String toString(boolean pretty);
    }
    public static interface SelectTerm {

    }
    public static interface DirectSelectTerm extends SelectTerm {
        Variable getAs();
        Variable getVariable();
    }
    public static interface AggregateSelectTerm extends SelectTerm {
        String getAggregate();
        Variable getVariable();
        Variable getAs();
    }
    public static interface GraphPatternValue {}
    public static interface Pattern extends GraphPatternValue {}
    public static interface Filter extends GraphPatternValue {
    }


    public static interface Term<T>{
        String toString(boolean pretty);
    }
    public static interface BinaryTerm<T,U,V> extends Term<T>{}
    public static interface UnaryTerm<T,U> extends Term<T>{}
    public static interface BooleanOperator extends Term<Boolean>{}
}


interface QueryHeader{

}

