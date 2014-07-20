package org.metaservice.api.sparql.buildingcontexts;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.sparql.nodes.Variable;
import org.metaservice.api.sparql.builders.AskQueryBuilder;
import org.metaservice.api.sparql.builders.ConstructQueryBuilder;
import org.metaservice.api.sparql.builders.SelectQueryBuilder;
import org.metaservice.api.sparql.nodes.*;
import org.openrdf.model.Value;

/**
 * Created by ilo on 05.03.14.
 */
public interface SparqlQuery {

    //query types
    @NotNull
    public SelectQueryBuilder select(boolean distinct,SelectTerm... selectTerms);
    @NotNull
    public AskQueryBuilder ask();
    @NotNull
    public ConstructQueryBuilder construct(Pattern... patterns);

    //select terms
    public SelectTerm all();
    public DirectSelectTerm var(Variable value);
    public DirectSelectTerm var(Variable value,Variable as);
    public AggregateSelectTerm aggregate(String aggregate,Variable variable,Variable as);

    @NotNull
    UnaryFunction<Boolean,Term<Value>> bound(Term<Value> t1);

    public Union union(GraphPattern... graphPatterns);
    public GraphPattern graphPattern(GraphPatternValue... graphPatternValues);

    //patterns and filters
    public QuadPattern quadPattern(Value s, Value p, Value o, Value c);
    public TriplePattern triplePattern(Value s, Value  p, Value o);
    public Filter filter(Term<Boolean> term);
    public FilterExists filterExists(GraphPatternValue... graphPatternValues);
    public FilterNotExists filterNotExists(GraphPatternValue... graphPatternValues);

    //operators
    public UnaryOperator<Term<Boolean>> not(Term<Boolean> t1);
    public BinaryOperator<Term<Boolean>,Term<Boolean>> or(Term<Boolean> t1,Term<Boolean>t2);
    public BinaryOperator<Term<Boolean>,Term<Boolean>> and(Term<Boolean> t1,Term<Boolean> t2);

    @NotNull
    BinaryOperator<Term<Value>,Term<Value>> unequal(Term<Value> t1, Term<Value> t2);

    public BinaryOperator<Term<Value>,Term<Value>> less(Term<Value> t1,Term<Value> t2);
    public BinaryOperator<Term<Value>,Term<Value>> lessOrEqual(Term<Value> t1,Term<Value> t2);
    public BinaryOperator<Term<Value>,Term<Value>> equal(Term<Value> t1,Term<Value> t2);
    public BinaryOperator<Term<Value>,Term<Value>> greaterOrEqual(Term<Value> t1,Term<Value> t2);
    public BinaryOperator<Term<Value>,Term<Value>> greater(Term<Value> t1,Term<Value> t2);

    //functions
    public BinaryFunction<Boolean,Term<Value>,Term<Value>> sameTerm(Term<Value> t1,Term<Value> t2);

    //wrapper
    public SimpleTerm val(Value s);

}
