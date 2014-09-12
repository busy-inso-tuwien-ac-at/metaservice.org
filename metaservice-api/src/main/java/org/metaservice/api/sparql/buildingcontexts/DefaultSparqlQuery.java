package org.metaservice.api.sparql.buildingcontexts;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.sparql.builders.AskQueryBuilder;
import org.metaservice.api.sparql.builders.QueryBuilder;
import org.metaservice.api.sparql.builders.QueryBuilderFactory;
import org.metaservice.api.sparql.builders.SelectQueryBuilder;
import org.metaservice.api.sparql.impl.SparqlQuerBuilderFactory;
import org.metaservice.api.sparql.nodes.*;
import org.openrdf.model.Value;

/**
 * Created by ilo on 05.03.14.
 */
public abstract class DefaultSparqlQuery implements SparqlQuery {
    public final DistinctEnum DISTINCT = DistinctEnum.DISTINCT;
    private final QueryBuilderFactory queryBuilderFactory;

    protected DefaultSparqlQuery() {
        queryBuilderFactory =  new SparqlQuerBuilderFactory();
    }
    protected DefaultSparqlQuery(QueryBuilderFactory queryBuilderFactory) {
        this.queryBuilderFactory =  queryBuilderFactory;
    }

    public String toString(){
        return build();
    }
    /**
     * to be implemented by user
     * @return
     */
    public abstract String build();


    @NotNull
    @Override
    public final SelectQueryBuilder select(DistinctEnum distinct,SelectTerm... selectTerms) {
        return queryBuilderFactory.create().select(distinct,selectTerms);
    }


    @NotNull
    @Override
    public final SelectQueryBuilder select(SelectTerm... selectTerms) {
        return queryBuilderFactory.create().select(DistinctEnum.ALL,selectTerms);
    }


    @NotNull
    @Override
    public final AskQueryBuilder ask() {
        return queryBuilderFactory.create().ask();
    }

    @NotNull
    @Override
    public final QueryBuilder construct(Pattern... patterns) {
        return queryBuilderFactory.create().construct(patterns);
    }
    @NotNull
    @Override
    public final FilterExists filterExists(GraphPatternValue... graphPatternValues){
        return new FilterExists(new GraphPattern(graphPatternValues));
    }
    @NotNull
    @Override
    public final FilterNotExists filterNotExists(GraphPatternValue... graphPatternValues){
        return new FilterNotExists(new GraphPattern(graphPatternValues));
    }

    @NotNull
    @Override
    public final BinaryOperator<Term<Value>,Term<Value>> lessOrEqual(Term<Value> t1,Term<Value> t2){
        return new BinaryOperator<>("<=",t1,t2);
    }
    @NotNull
    @Override
    public final BinaryOperator<Term<Value>,Term<Value>> equal(Term<Value> t1,Term<Value> t2){
        return new BinaryOperator<>("=",t1,t2);
    }
    @NotNull
    @Override
    public final BinaryOperator<Term<Value>,Term<Value>> unequal(Term<Value> t1,Term<Value> t2){
        return new BinaryOperator<>("!=",t1,t2);
    }
    @NotNull
    @Override
    public final BinaryOperator<Term<Value>,Term<Value>> less(Term<Value> t1,Term<Value> t2){
        return new BinaryOperator<>("<",t1,t2);
    }
    @NotNull
    @Override
    public final BinaryOperator<Term<Value>,Term<Value>> greaterOrEqual(Term<Value> t1,Term<Value> t2){
        return new BinaryOperator<>(">=",t1,t2);
    }
    @NotNull
    @Override
    public final BinaryOperator<Term<Value>,Term<Value>> greater(Term<Value> t1,Term<Value> t2){
        return new BinaryOperator<>(">",t1,t2);
    }
    @NotNull
    @Override
    public final SelectTerm all(){
        return new SelectTerm() {
            public final String toString(){
                return "*";
            }
        };
    }
    @NotNull
    @Override
    public final BinaryOperator<Term<Boolean>,Term<Boolean>> or(Term<Boolean> t1,Term<Boolean>t2){
        return new BinaryOperator<>("||",t1,t2);
    }
    @NotNull
    @Override
    public final BinaryOperator<Term<Boolean>,Term<Boolean>> and(Term<Boolean> t1,Term<Boolean>t2){
        return new BinaryOperator<>("&&",t1,t2);
    }
    @NotNull
    @Override
    public final UnaryOperator<Term<Boolean>> not(Term<Boolean> t1){
        return new UnaryOperator<>("!",t1);
    }
    @NotNull
    @Override
    public final BinaryFunction<Boolean,Term<Value>,Term<Value>> sameTerm(Term<Value> t1,Term<Value>t2){
        return new BinaryFunction<>("sameTerm",t1,t2);
    }

    @NotNull
    @Override
    public final UnaryFunction<Boolean,Term<Value>> bound(Term<Value> t1){
        return new UnaryFunction<>("bound",t1);
    }

    @NotNull
    @Override
    public final Union union(GraphPattern... graphPatterns){
        return new Union(graphPatterns);
    }
    @NotNull
    @Override
    public final TriplePattern triplePattern(Value s, Value  p, Value o){
        return new TriplePattern(s,p,o);
    }
    @NotNull
    @Override
    public final SimpleTerm val(Value s) {
        return new SimpleTerm(s);
    }
    @NotNull
    @Override
    public final Filter filter(Term<Boolean> term){
        return new FilterImpl(term);
    }
    @NotNull
    @Override
    public final DirectSelectTerm var(Variable value){
        return new DirectSelectTermImpl(value,null);
    }
    @NotNull
    @Override
    public final DirectSelectTerm var(Variable value,Variable as){
        return new DirectSelectTermImpl(value,as);
    }
    @NotNull
    @Override
    public final AggregateSelectTerm aggregate(String aggregate,Variable variable,Variable as){
        return new AggregateSelectTermImpl(variable,as,aggregate);
    }
    @NotNull
    @Override
    public final QuadPattern quadPattern(Value s, Value p, Value o, Value c){
        return new QuadPattern(s,p,o,c);
    }
    @NotNull
    @Override
    public final GraphPattern graphPattern(GraphPatternValue... graphPatternValues){
        return new GraphPattern(graphPatternValues);
    }
}
