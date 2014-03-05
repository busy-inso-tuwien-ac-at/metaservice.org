package org.metaservice.api.sparql;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.Value;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ilo on 05.03.14.
 */
public class SparqlQueryBuilderImpl implements SPARQLQueryBuilder{
    public static class TriplePattern implements Pattern{
        private final Value s;
        private final Value p;
        private final Value o;

        public TriplePattern(Value s, Value p, Value o) {
            this.s = s;
            this.p = p;
            this.o = o;
        }

        public Value getS() {
            return s;
        }

        public Value getP() {
            return p;
        }

        public Value getO() {
            return o;
        }
    }
    public static class QuadPattern implements Pattern{
        private final Value s;
        private final Value p;
        private final Value o;
        private final Value c;

        public QuadPattern(Value s, Value p, Value o,Value c) {
            this.s = s;
            this.p = p;
            this.o = o;
            this.c = c;

        }

        public Value getS() {
            return s;
        }

        public Value getP() {
            return p;
        }

        public Value getO() {
            return o;
        }

        public Value getC() {
            return c;
        }

    }
    public static class FilterExists implements Filter{
        private final GraphPattern graphPattern;
        public FilterExists(GraphPattern graphPattern) {
            this.graphPattern = graphPattern;
        }
    }

    public static class AggregateSelectTermImpl implements AggregateSelectTerm {
        private final Variable variable;
        private final Variable as;
        private final String aggregate;

        public AggregateSelectTermImpl(Variable variable, Variable as, String aggregate) {
            this.variable = variable;
            this.as = as;
            this.aggregate = aggregate;
        }


        @Override
        public String getAggregate() {
            return aggregate;
        }

        @Override
        public Variable getVariable() {
            return variable;
        }

        public Variable getAs() {
            return as;
        }
    }
    public static class DirectSelectTermImpl implements DirectSelectTerm {
        private final Variable variable;
        private final Variable as;

        public DirectSelectTermImpl(Variable value, Variable as) {
            this.variable = value;
            this.as = as;
        }

        public Variable getVariable() {
            return variable;
        }

        public Variable getAs() {
            return as;
        }
    }
    public static class FilterNotExists implements Filter{
        private final GraphPattern graphPattern;
        public FilterNotExists(GraphPattern graphPattern) {
            this.graphPattern = graphPattern;
        }
    }
    public static class Union implements GraphPatternValue{
        private final GraphPattern[] graphPatterns;
        public Union(GraphPattern[] graphPatterns) {
            this.graphPatterns = graphPatterns;
        }

        public GraphPattern[] getGraphPatterns() {
            return graphPatterns;
        }
    }

    public static FilterExists filterExists(GraphPatternValue... graphPatternValues){
        return new FilterExists(new GraphPatternImpl(graphPatternValues));
    }
    public static FilterExists filterNotExists(GraphPatternValue... graphPatternValues){
        return new FilterExists(new GraphPatternImpl(graphPatternValues));
    }
    public static class BinaryFunction<X,Y,T> implements BinaryTerm<X,Y,T>{
        private final String  name;
        private final Y t1;
        private final T t2;


        public BinaryFunction(String name, Y t1, T t2) {
            this.name = name;
            this.t1 = t1;
            this.t2 = t2;
        }
    }
    public static class BinaryOperator<Y,T> implements BinaryTerm<Boolean,Y,T>{
        private final String  name;
        private final Y t1;
        private final T t2;


        public BinaryOperator(String name, Y t1, T t2) {
            this.name = name;
            this.t1 = t1;
            this.t2 = t2;
        }
    }
    public static class UnaryOperator<Y> implements UnaryTerm<Boolean,Y>{
        private final String  name;
        private final Y t1;

        public UnaryOperator(String name, Y t1) {
            this.name = name;
            this.t1 = t1;
        }
    }

    public static class SimpleTerm<T> implements Term<T>{
        private final T t;

        public SimpleTerm(T t) {
            this.t = t;
        }

        @Override
        public String toString(boolean pretty) {
            return format(T);
        }
    }

    public static class FilterImpl implements Filter{
        private  Term<Boolean> term;
        public FilterImpl(Term<Boolean> term) {
            this.term = term;
        }

        public Term<Boolean> getTerm() {
            return term;
        }
    }

    public static BinaryOperator<Term<Boolean>,Term<Boolean>> or(Term<Boolean> t1,Term<Boolean>t2){
        return new BinaryOperator<>("||",t1,t2);
    }
    public static BinaryOperator<Term<Boolean>,Term<Boolean>> and(Term<Boolean> t1,Term<Boolean>t2){
        return new BinaryOperator<>("&&",t1,t2);
    }
    public static UnaryOperator<Term<Boolean>> not(Term<Boolean> t1){
        return new UnaryOperator<>("not",t1);
    }
    public static BinaryFunction<Boolean,Term<Value>,Term<Value>> sameTerm(Term<Value> t1,Term<Value>t2){
        return new BinaryFunction<>("sameTerm",t1,t2);
    }

    public static Union union(GraphPattern... graphPatterns){
        return new Union(graphPatterns);
    }
    public static TriplePattern triplePattern(Value s, Value  p, Value o){
        return new TriplePattern(s,p,o);
    }
    public static SimpleTerm<Value> val(Value s) {
        return new SimpleTerm<>(s);
    }

    public static Filter filter(Term<Boolean> term){
        return new FilterImpl(term);
    }

    public static DirectSelectTerm var(Variable value){
        return new DirectSelectTermImpl(value,null);
    }

    public static DirectSelectTerm var(Variable value,Variable as){
        return new DirectSelectTermImpl(value,as);
    }

    public static AggregateSelectTerm aggregate(String aggregate,Variable variable,Variable as){
        return new AggregateSelectTermImpl(variable,as,aggregate);
    }

    public static QuadPattern triplePattern(Value s, Value  p, Value o,Value c){
        return new QuadPattern(s,p,o,c);
    }
    public static GraphPattern graphPattern(GraphPatternValue... graphPatternValues){
        return new GraphPatternImpl(graphPatternValues);
    }



    public static String format(Value value) {
        if(value instanceof Variable){
            return "?"+value;
        }
        if(value instanceof URI){
            return "<" + value.stringValue() +">";
        }
        if(value instanceof Literal){
            return value.toString();
        }
        throw new IllegalArgumentException();
    }
    private static final String PRETTY_INTENDATION = "    ";
    private QueryHeader queryHeader;
    private WhereClause whereClause;

    private final List<String> orderBys = new ArrayList<>();
    private final List<String> groupBys = new ArrayList<>();
    Integer limit= null;
    private boolean pretty;

    private SparqlQueryBuilderImpl(boolean pretty) {
        this.pretty = pretty;
        queryHeader = null;
        whereClause =null;
    }

    @NotNull
    public SPARQLQueryBuilder select(SelectTerm... selectTerms){
        if(queryHeader != null && !(queryHeader instanceof SelectHeader)){
            throw new UnsupportedOperationException("no multiple headers");
        }
        if(queryHeader == null)
            queryHeader =new SelectHeader(this,selectTerms);
        return this;
    }

    @NotNull
    @Override
    public SPARQLQueryBuilder ask() {
        if(queryHeader != null && !(queryHeader instanceof AskHeader)){
            throw new UnsupportedOperationException("no multiple headers");
        }
        if(queryHeader == null)
            queryHeader =new AskHeader(this);
        return this;
    }

    @NotNull
    @Override
    public ConstructHeader construct(Pattern... patterns) {
        if(queryHeader != null && !(queryHeader instanceof ConstructHeader)){
            throw new UnsupportedOperationException("no multiple headers");
        }
        if(queryHeader == null)
            queryHeader =new ConstructHeader(this,patterns);
        return (ConstructHeader) queryHeader;
    }

    @NotNull
    @Override
    public SPARQLQueryBuilder orderByAsc(@NotNull Variable variable) {
        orderBys.add("ASC(" + format(variable) + ")");
        return this;
    }

    @NotNull
    @Override
    public SPARQLQueryBuilder orderByDesc(@NotNull Variable variable) {
        orderBys.add("DESC(" + format(variable) + ")");
        return this;
    }

    @NotNull
    @Override
    public SPARQLQueryBuilder limit(int i) {
        this.limit = i;
        return this;
    }

    @NotNull
    @Override
    public String build() {
        StringBuilder result = new StringBuilder();
        result.append(queryHeader);
        if(whereClause!=null)
            result.append(whereClause);
        if(groupBys.size()>0){
            result.append("GROUP BY");
            if(pretty){
                result.append("\n");
            }else{
                result.append(" ");
            }
            for(String groupBy : groupBys){
                if(pretty){
                    result.append(PRETTY_INTENDATION);
                }
                result.append(groupBy);
                if(pretty){
                    result.append("\n");
                }else{
                    result.append(" ");
                }
            }
        }
        if(orderBys.size() > 0){
            result.append("ORDER BY");
            if(pretty){
                result.append("\n");
            }else{
                result.append(" ");
            }
            for(String orderBy : orderBys){
                if(pretty){
                    result.append(PRETTY_INTENDATION);
                }
                result.append(orderBy);
                if(pretty){
                    result.append("\n");
                }else{
                    result.append(" ");
                }
            }
        }
        if(limit != null){
            result.append("LIMIT ")
                    .append(limit);
            if(pretty){
                result.append("\n");
            }else{
                result.append(" ");
            }
        }
        return result.toString();
    }

    @NotNull
    @Override
    public WhereClause where(GraphPatternValue... graphPatternValue) {
        if(whereClause == null)
            whereClause =new WhereClause(this, graphPatternValue);
        return whereClause;
    }

    @Override
    public boolean isPrettyPrint() {
        return pretty;
    }

    @NotNull
    @Override
    public SPARQLQueryBuilder groupBy(@NotNull Variable variable) {
        groupBys.add(format(variable));
        return this;
    }

    public static SPARQLQueryBuilder getInstance(){
        return new SparqlQueryBuilderImpl(false);
    }

    public static SPARQLQueryBuilder getPrettyInstance(){
        return new SparqlQueryBuilderImpl(true);
    }

    public static class SelectHeader extends AbstractDelegate  implements QueryHeader{

        private String distinct = "";
        private SelectTerm[] terms;


        public SelectHeader(SPARQLQueryBuilder aDefault, SelectTerm[] selectTerms) {
            super(aDefault);
            this.terms = selectTerms;
        }

        public  SelectHeader distinct(){
            distinct = "DISTINCT ";
            return this;
        }
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder
                    .append("SELECT ")
                    .append(distinct);
            if(isPrettyPrint()){
                stringBuilder.append("\n");
            }
            for(SelectTerm term : terms){
                if(isPrettyPrint()){
                    stringBuilder.append(PRETTY_INTENDATION);
                }
                if(term instanceof DirectSelectTerm){
                    DirectSelectTerm directSelectTerm = (DirectSelectTerm) term;
                    if(directSelectTerm.getAs() != null)
                        stringBuilder.append(var(directSelectTerm.getVariable(), directSelectTerm.getAs()));
                    else
                        stringBuilder.append(var(directSelectTerm.getVariable()));
                }else if(term instanceof AggregateSelectTerm){
                    AggregateSelectTerm aggregateSelectTerm = (AggregateSelectTerm) term;
                    stringBuilder.append(aggregate(aggregateSelectTerm.getAggregate(), aggregateSelectTerm.getVariable(), aggregateSelectTerm.getAs()));
                }
                if(isPrettyPrint()){
                    stringBuilder.append("\n");
                }else{
                    stringBuilder.append(" ");
                }
            }
            return stringBuilder.toString();
        }


        @NotNull
        private String aggregate(@NotNull String aggregate,@NotNull Variable variable,@Nullable Variable as){
            if(as == null){
                return  aggregate + "(" + format(variable) + ")";
            }else {
                return  "(" + aggregate + "(" + format(variable) + ") as " + format(as) + ")";
            }
        }

        private String var(Value value){
            return format(value);
        }
        private String var(Value value,Variable as) {
            return "(" + format(value) + " as " + format(as);
        }

    }

    public static  class ConstructHeader extends AbstractDelegate implements QueryHeader{
        private String quadmode = " ";
        private Pattern[] patterns;

        public ConstructHeader(SPARQLQueryBuilder aDefault, Pattern[] patterns) {
            super(aDefault);
            this.patterns = patterns;
        }

        private String triple(TriplePattern triplePattern){
            return format(triplePattern.getS())+ " " + format(triplePattern.getP()) + " " + format(triplePattern.getO()) + ". ";
        }

        private String quad(QuadPattern quadPattern) {
            quadmode =  "_QUADMODE_ ";
            return "GRAPH " + format(quadPattern.getC()) + "{" + format(quadPattern.getS()) + " " + format(quadPattern.getP()) + " " + format(quadPattern.getO()) + " }. ";
        }
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("CONSTRUCT{");
            stringBuilder.append(quadmode);
            for(Pattern pattern : patterns){
                if(pattern instanceof TriplePattern){
                    stringBuilder.append(triple((TriplePattern) pattern));
                }else if(pattern instanceof QuadPattern){
                    stringBuilder.append(quad((QuadPattern) pattern));
                }else {
                    throw new UnsupportedOperationException();
                }
                stringBuilder.append(pattern);
            }
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static class GraphPatternImpl implements GraphPattern, GraphPatternValue {
        private final GraphPatternValue[] graphPatternValues;
        public GraphPatternImpl(GraphPatternValue[] graphPatternValues) {
            this.graphPatternValues = graphPatternValues;
        }

        public GraphPatternValue[] getGraphPatternValues() {
            return graphPatternValues;
        }

        private void filter(boolean pretty, StringBuilder builder, Filter filter){
            builder.append("FILTER (");
            builder.append(filter.getTerm().toString(pretty));
            builder.append(")");
        }

        private void filterExists(boolean pretty,StringBuilder builder,FilterExists filterExists){
            builder.append("FILTER  EXISTS ");
            builder.append(filterExists.graphPattern.toString(pretty));
            if(pretty)
                builder.append("\n");
        }
        private void filterNotExists(boolean pretty,StringBuilder builder,FilterNotExists filterNotExists){
            builder.append("FILTER NOT EXISTS");
            builder.append(filterNotExists.graphPattern.toString(pretty));
            if(pretty)
                builder.append("\n");
        }

        private void pattern(boolean pretty,StringBuilder builder,Value s, Value  p, Value o) {
            if(pretty)
                builder.append(PRETTY_INTENDATION);
            builder.append(format(s))
                    .append(" ")
                    .append(format(p))
                    .append(" ")
                    .append(format(o))
                    .append(".");
            if(pretty){
                builder.append("\n");
            }else{
                builder.append(" ");
            }
        }
        private void pattern(boolean pretty,StringBuilder builder,Value s, Value  p, Value o,Value c) {
            if(pretty)
                builder.append(PRETTY_INTENDATION);
            builder.append("GRAPH ")
                    .append(format(c))
                    .append("{")
                    .append(format(s))
                    .append(" ")
                    .append(format(p))
                    .append(" ")
                    .append(format(o))
                    .append("}. ");
            if(pretty){
                builder.append("\n");
            }else{
                builder.append(" ");
            }
        }

        public String toString(boolean pretty){
            StringBuilder builder = new StringBuilder();
            builder.append("{");
            if(pretty)
                builder.append("\n");
            for(GraphPatternValue graphPatternValue : graphPatternValues){
                if(graphPatternValue instanceof TriplePattern){
                    TriplePattern triplePattern = (TriplePattern) graphPatternValue;
                    this.pattern(pretty,builder,triplePattern.getS(),triplePattern.getP(),triplePattern.getO());
                }else if(graphPatternValue instanceof QuadPattern){
                    QuadPattern quadPattern = (QuadPattern) graphPatternValue;
                    this.pattern(pretty,builder,quadPattern.getS(), quadPattern.getP(), quadPattern.getO(), quadPattern.getC());
                }else if(graphPatternValue instanceof FilterExists){
                    FilterExists filterExists = (FilterExists) graphPatternValue;
                    this.filterExists(pretty,builder,filterExists);
                }else if(graphPatternValue instanceof FilterNotExists){
                    FilterNotExists filterNotExists = (FilterNotExists) graphPatternValue;
                    this.filterNotExists(pretty, builder, filterNotExists);
                }else if (graphPatternValue instanceof  Filter){
                    Filter filter = (Filter) graphPatternValue;
                    this.filter(pretty,builder,filter);
                } else if(graphPatternValue instanceof Union){
                    Union union = (Union) graphPatternValue;
                    this.union(pretty, builder, union);
                }  else {
                    throw new UnsupportedOperationException();
                }
            }
            builder.append("}");

            return builder.toString();
        }

        private void union(boolean pretty, StringBuilder builder, Union union) {
            ArrayList<String> list = new ArrayList<>();
            for(GraphPattern graphPattern : union.graphPatterns){
                list.add(graphPattern.toString(pretty));

            }
            builder.append(StringUtils.join(list," UNION "));
            if(pretty){
                builder.append("\n");
            }
        }

        public String toString(){
            return toString(false);
        }

    }

    public static class WhereClause extends AbstractDelegate{
        GraphPatternImpl graphPattern;
        public WhereClause(SPARQLQueryBuilder aDefault, GraphPatternValue[] graphPatternValues) {
            super(aDefault);
            this.graphPattern = new GraphPatternImpl(graphPatternValues);
        }

        public String toString(){
            return  "WHERE " + graphPattern.toString(isPrettyPrint()) + (isPrettyPrint()?"\n":" ");
        }
    }

    public static class AskHeader extends AbstractDelegate implements QueryHeader{

        public AskHeader(SPARQLQueryBuilder aDefault) {
            super(aDefault);
        }
        public String toString(){
            return "ASK";
        }
    }

    static abstract class AbstractDelegate implements SPARQLQueryBuilder{
        private final SPARQLQueryBuilder sparqlQueryBuilder;

        @Override
        public boolean isPrettyPrint() {
            return sparqlQueryBuilder.isPrettyPrint();
        }

        @Override
        @NotNull
        public WhereClause where(GraphPatternValue... graphPatternValue) {
            return sparqlQueryBuilder.where(graphPatternValue);
        }

        protected AbstractDelegate(SPARQLQueryBuilder sparqlQueryBuilder) {
            this.sparqlQueryBuilder = sparqlQueryBuilder;
        }

        @NotNull
        @Override
        public SPARQLQueryBuilder select(SelectTerm... values) {
            return sparqlQueryBuilder.select();
        }

        @NotNull
        @Override
        public SPARQLQueryBuilder ask() {
            return sparqlQueryBuilder.ask();
        }

        @NotNull
        @Override
        public SPARQLQueryBuilder construct(Pattern... patterns) {
            return sparqlQueryBuilder.construct(patterns);
        }

        @NotNull
        @Override
        public SPARQLQueryBuilder orderByAsc(@NotNull Variable variable) {
            return sparqlQueryBuilder.orderByAsc(variable);
        }

        @NotNull
        @Override
        public SPARQLQueryBuilder orderByDesc(@NotNull Variable variable) {
            return sparqlQueryBuilder.orderByDesc(variable);
        }

        @NotNull
        @Override
        public SPARQLQueryBuilder limit(int i) {
            return sparqlQueryBuilder.limit(i);
        }

        @NotNull
        @Override
        public String build() {
            return sparqlQueryBuilder.build();
        }

        @Override
        @NotNull
        public SPARQLQueryBuilder groupBy(@NotNull Variable variable) {
            return sparqlQueryBuilder.groupBy(variable);
        }
    }
}
