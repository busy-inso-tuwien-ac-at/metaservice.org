/*
 * Copyright 2015 Nikola Ilo
 * Research Group for Industrial Software, Vienna University of Technology, Austria
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaservice.api.sparql.impl;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaservice.api.sparql.builders.SelectQueryBuilder;
import org.metaservice.api.sparql.buildingcontexts.SparqlQuery;
import org.metaservice.api.sparql.nodes.Variable;
import org.metaservice.api.sparql.builders.QueryBuilder;
import org.metaservice.api.sparql.nodes.*;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by ilo on 05.03.14.
 */
public class SparqlQueryBuilderImpl implements QueryBuilder {
    private static final String PRETTY_INTENDATION = "    ";
    private final List<String> orderBys = new ArrayList<>();
    private final List<String> groupBys = new ArrayList<>();
    Integer limit = null;
    private QueryHeader queryHeader;
    private List<NamedSubQuery> subQueries = new ArrayList<>();

    private SparqlQueryBuilderImpl() {
        queryHeader = null;
    }

    public static String format(Value value) {
        if (value instanceof Variable) {
            return "?" + value;
        }
        if (value instanceof URI) {
            return "<" + value.stringValue() + ">";
        }
        if (value instanceof Literal) {
            return value.toString();
        }
        throw new IllegalArgumentException();
    }

    public static QueryBuilder getInstance() {
        return new SparqlQueryBuilderImpl();
    }
    @NotNull
    @Override
    public QueryBuilder with(NamedSubQuery... namedSubQueries) {
        subQueries.addAll(Arrays.asList(namedSubQueries));
        return this;
    }
    @NotNull
    public QueryBuilder select(@NotNull SparqlQuery.DistinctEnum distinct,SelectTerm... selectTerms) {
        if (queryHeader != null && !(queryHeader instanceof SelectHeader)) {
            throw new UnsupportedOperationException("no multiple headers");
        }
        if (queryHeader == null)
            queryHeader = new SelectHeader(distinct,selectTerms);
        return this;
    }

    @NotNull
    public QueryBuilder select(SelectTerm... selectTerms) {
        return select(SparqlQuery.DistinctEnum.ALL,selectTerms);
    }

    @NotNull
    @Override
    public QueryBuilder ask() {
        if (queryHeader != null && !(queryHeader instanceof AskHeader)) {
            throw new UnsupportedOperationException("no multiple headers");
        }
        if (queryHeader == null)
            queryHeader = new AskHeader();
        return this;
    }

    @NotNull
    @Override
    public QueryBuilder construct(Pattern... patterns) {
        if (queryHeader != null && !(queryHeader instanceof ConstructHeader)) {
            throw new UnsupportedOperationException("no multiple headers");
        }
        if (queryHeader == null)
            queryHeader = new ConstructHeader(patterns);
        return this;
    }

    @NotNull
    @Override
    public QueryBuilder orderByAsc(@NotNull Variable variable) {
        orderBys.add("ASC(" + format(variable) + ")");
        return this;
    }

    @NotNull
    @Override
    public QueryBuilder orderByDesc(@NotNull Variable variable) {
        orderBys.add("DESC(" + format(variable) + ")");
        return this;
    }

    @NotNull
    @Override
    public QueryBuilder limit(int i) {
        this.limit = i;
        return this;
    }



    @NotNull
    @Override
    public String build() {
        return build(false);
    }

    @NotNull
    public String build(boolean pretty) {
        StringBuilder result = new StringBuilder();
        if(queryHeader instanceof ConstructHeader){
            result.append(constructHeader(pretty, (ConstructHeader) queryHeader));
        }else if(queryHeader instanceof  AskHeader){
            result.append(askHeader(pretty, (AskHeader) queryHeader));
        }else if(queryHeader instanceof SelectHeader){
            result.append(selectHeader(pretty, (SelectHeader) queryHeader));
        }else{
            throw new UnsupportedOperationException();
        }
        for (NamedSubQuery subQuery : subQueries) {
            result.append(namedSubQuery(pretty,subQuery));
        }
        result.append(whereClause(pretty));
        if (groupBys.size() > 0) {
            result.append("GROUP BY");
            if (pretty) {
                result.append("\n");
            } else {
                result.append(" ");
            }
            for (String groupBy : groupBys) {
                if (pretty) {
                    result.append(PRETTY_INTENDATION);
                }
                result.append(groupBy);
                if (pretty) {
                    result.append("\n");
                } else {
                    result.append(" ");
                }
            }
        }
        if (orderBys.size() > 0) {
            result.append("ORDER BY");
            if (pretty) {
                result.append("\n");
            } else {
                result.append(" ");
            }
            for (String orderBy : orderBys) {
                if (pretty) {
                    result.append(PRETTY_INTENDATION);
                }
                result.append(orderBy);
                if (pretty) {
                    result.append("\n");
                } else {
                    result.append(" ");
                }
            }
        }
        if (limit != null) {
            result.append("LIMIT ")
                    .append(limit);
            if (pretty) {
                result.append("\n");
            } else {
                result.append(" ");
            }
        }
        return result.toString();
    }

    private String selectHeader(boolean pretty, SelectHeader selectHeader) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("SELECT ");
        if (selectHeader.isDistinct()) {
            stringBuilder.append("DISTINCT ");
        }
        if (pretty) {
            stringBuilder.append("\n");
        }
        for (SelectTerm term : selectHeader.getTerms()) {
            if (pretty) {
                stringBuilder.append(PRETTY_INTENDATION);
            }
            if (term instanceof DirectSelectTerm) {
                DirectSelectTerm directSelectTerm = (DirectSelectTerm) term;
                if (directSelectTerm.getAs() != null)
                    stringBuilder.append(var(directSelectTerm.getVariable(), directSelectTerm.getAs()));
                else
                    stringBuilder.append(var(directSelectTerm.getVariable()));
            } else if (term instanceof AggregateSelectTerm) {
                AggregateSelectTerm aggregateSelectTerm = (AggregateSelectTerm) term;
                stringBuilder.append(aggregate(aggregateSelectTerm.getAggregate(), aggregateSelectTerm.getVariable(), aggregateSelectTerm.getAs()));
            } else if (term.toString().equals("*")) {
                stringBuilder.append("*");
            } else {
                throw new UnsupportedOperationException();
            }
            if (pretty) {
                stringBuilder.append("\n");
            } else {
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString();
    }

    public String namedSubQuery(boolean pretty, NamedSubQuery namedSubQuery){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("WITH {");
        if(pretty){
            stringBuilder.append("\n");
        }
        stringBuilder.append(namedSubQuery.getSubQuery().build(pretty));
        stringBuilder.append("} as %" +namedSubQuery.getName());
        if(pretty){
            stringBuilder.append("\n\n");
        } else {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    public String unaryFunction(boolean pretty,UnaryFunction unaryFunction){
        return unaryFunction.getName()
                + "("
                + term(pretty, unaryFunction.getT1())
                + ")";
    }

    public String binaryFunction(boolean pretty,BinaryFunction binaryFunction) {
        return binaryFunction.getName()
                + "("
                +
                term(pretty, binaryFunction.getT1())
                + ","
                + term(pretty, binaryFunction.getT2())
                + ")";
    }

    public String term(boolean pretty,Term t){
        if(t instanceof BinaryFunction){
            return binaryFunction(pretty, (BinaryFunction) t);
        } else if(t instanceof UnaryFunction){
            return unaryFunction(pretty, (UnaryFunction) t);
        } else if (t instanceof BinaryOperator){
            return binaryOperator(pretty, (BinaryOperator) t);
        } else if (t instanceof UnaryOperator){
            return unaryOperator(pretty, (UnaryOperator) t);
        } else if(t instanceof SimpleTerm){
            return simpleTerm(pretty, (SimpleTerm) t);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private final ArrayList<GraphPatternValue> where = new ArrayList<>();

    @NotNull
    @Override
    public QueryBuilder where(GraphPatternValue... graphPatternValue) {
        where.addAll(Arrays.asList(graphPatternValue));
        return this;
    }

    @NotNull
    @Override
    public QueryBuilder groupBy(@NotNull Variable variable) {
        groupBys.add(format(variable));
        return this;
    }

    public String binaryOperator(boolean pretty,BinaryOperator binaryOperator) {
        return "(" + term(pretty,binaryOperator.getT1()) + " " + binaryOperator.getName() + " " + term(pretty,binaryOperator.getT2()) + ")";
    }

    public String unaryOperator(boolean pretty,UnaryOperator unaryOperator) {
        return unaryOperator.getName() + term(pretty, unaryOperator.getT1());
    }

    public String simpleTerm(boolean pretty,SimpleTerm simpleTerm) {
        return format(simpleTerm.getT());
    }


    @NotNull
    private String aggregate(@NotNull String aggregate, @NotNull Variable variable, @Nullable Variable as) {
        if (as == null) {
            return aggregate + "(" + format(variable) + ")";
        } else {
            return "(" + aggregate + "(" + format(variable) + ") as " + format(as) + ")";
        }
    }

    private String var(Value value) {
        return format(value);
    }

    private String var(Value value, Variable as) {
        return "(" + format(value) + " as " + format(as);
    }

    public String constructHeader(boolean pretty, ConstructHeader constructHeader) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CONSTRUCT{");
        String quadmode = " ";
        for (Pattern pattern : constructHeader.getPatterns()){
            if(pattern instanceof QuadPattern){
                quadmode = "_QUADMODE_ ";
                break;
            }
        }
            stringBuilder.append(quadmode);
        for (Pattern pattern : constructHeader.getPatterns()) {
            if (pattern instanceof TriplePattern) {
                stringBuilder.append(triple((TriplePattern) pattern));
            } else if (pattern instanceof QuadPattern) {
                stringBuilder.append(quad((QuadPattern) pattern));
            } else {
                throw new UnsupportedOperationException();
            }
            stringBuilder.append(pattern);
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
    private String triple(TriplePattern triplePattern) {
        return format(triplePattern.getS()) + " " + format(triplePattern.getP()) + " " + format(triplePattern.getO()) + ". ";
    }

    private String quad(QuadPattern quadPattern) {
        return "GRAPH " + format(quadPattern.getC()) + "{" + format(quadPattern.getS()) + " " + format(quadPattern.getP()) + " " + format(quadPattern.getO()) + " }. ";
    }

    private void filter(boolean pretty, StringBuilder builder, FilterImpl filter) {
        if (pretty) {
            builder.append(PRETTY_INTENDATION);
        }
        builder.append("FILTER (");
        builder.append(term(pretty, filter.getTerm()));
        builder.append(")");
        if (pretty) {
            builder.append("\n");
        } else {
            builder.append(" ");
        }
    }

    private void filterExists(boolean pretty, StringBuilder builder, FilterExists filterExists) {
        if (pretty) {
            builder.append(PRETTY_INTENDATION);
        }
        builder.append("FILTER  EXISTS ");
        builder.append(graphPattern(pretty,filterExists.getGraphPattern()));
        if (pretty) {
            builder.append("\n");
        }
    }

    private void filterNotExists(boolean pretty, StringBuilder builder, FilterNotExists filterNotExists) {
        if (pretty) {
            builder.append(PRETTY_INTENDATION);
        }
        builder.append("FILTER NOT EXISTS");
        builder.append(graphPattern(pretty,filterNotExists.getGraphPattern()));
        if (pretty) {
            builder.append("\n");
        }
    }

    private void pattern(boolean pretty, StringBuilder builder, Value s, Value p, Value o) {
        if (pretty) {
            builder.append(PRETTY_INTENDATION);
        }
        builder.append(format(s))
                .append(" ")
                .append(format(p))
                .append(" ")
                .append(format(o))
                .append(".");
        if (pretty) {
            builder.append("\n");
        } else {
            builder.append(" ");
        }
    }

    private void pattern(boolean pretty, StringBuilder builder, Value s, Value p, Value o, Value c) {
        if (pretty) {
            builder.append(PRETTY_INTENDATION);
        }
        builder.append("GRAPH ")
                .append(format(c))
                .append("{")
                .append(format(s))
                .append(" ")
                .append(format(p))
                .append(" ")
                .append(format(o))
                .append("}. ");
        if (pretty) {
            builder.append("\n");
        } else {
            builder.append(" ");
        }
    }



    private void include(boolean pretty, StringBuilder builder, Include include) {
        if (pretty) {
            builder.append(PRETTY_INTENDATION);
        }
        builder.append("INCLUDE %").append(include.getName());
        if (pretty) {
            builder.append("\n");
        } else {
            builder.append(" ");
        }
    }

    private void union(boolean pretty, StringBuilder builder, Union union) {
        ArrayList<String> list = new ArrayList<>();
        for (GraphPattern graphPattern : union.getGraphPatterns()) {
            list.add(graphPattern(pretty, graphPattern));

        }
        builder.append(StringUtils.join(list, " UNION "));
        if (pretty) {
            builder.append("\n");
        }
    }
    public String graphPattern(boolean pretty, GraphPattern graphPattern) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        if (pretty) {
            builder.append("\n");
        }
        for (GraphPatternValue graphPatternValue : graphPattern.getGraphPatternValues()) {
            if (graphPatternValue instanceof TriplePattern) {
                TriplePattern triplePattern = (TriplePattern) graphPatternValue;
                this.pattern(pretty, builder, triplePattern.getS(), triplePattern.getP(), triplePattern.getO());
            } else if (graphPatternValue instanceof QuadPattern) {
                QuadPattern quadPattern = (QuadPattern) graphPatternValue;
                this.pattern(pretty, builder, quadPattern.getS(), quadPattern.getP(), quadPattern.getO(), quadPattern.getC());
            } else if (graphPatternValue instanceof FilterExists) {
                FilterExists filterExists = (FilterExists) graphPatternValue;
                this.filterExists(pretty, builder, filterExists);
            } else if (graphPatternValue instanceof FilterNotExists) {
                FilterNotExists filterNotExists = (FilterNotExists) graphPatternValue;
                this.filterNotExists(pretty, builder, filterNotExists);
            } else if (graphPatternValue instanceof FilterImpl) {
                FilterImpl filter = (FilterImpl) graphPatternValue;
                this.filter(pretty, builder, filter);
            } else if (graphPatternValue instanceof Union) {
                Union union = (Union) graphPatternValue;
                this.union(pretty, builder, union);
            } else if (graphPatternValue instanceof Include) {
                this.include(pretty, builder, (Include) graphPatternValue);
            } else if (graphPatternValue instanceof SelectQueryBuilder){
                this.subselect(pretty,builder, (SelectQueryBuilder) graphPatternValue);
            }else {
                throw new UnsupportedOperationException();
            }
        }
        builder.append("}");

        return builder.toString();
    }

    private void subselect(boolean pretty, StringBuilder builder, SelectQueryBuilder graphPatternValue) {
        if(pretty){
            builder.append(PRETTY_INTENDATION);
        }
        builder.append("{");
        if(pretty) {
            builder.append("\n");
        }
        String sub = graphPatternValue.build(pretty);
        if(pretty) {
            sub = sub.replaceAll("^", PRETTY_INTENDATION + PRETTY_INTENDATION)
                    .replaceAll("\n$","")
                    .replaceAll("\n", "\n" + PRETTY_INTENDATION + PRETTY_INTENDATION)
                    .replaceAll("$","\n");
        }
        builder.append(sub);
        if(pretty){
            builder.append(PRETTY_INTENDATION);
        }
        builder.append("}");
        if(pretty) {
            builder.append("\n");
        }

    }

    public String whereClause(boolean pretty) {
        return "WHERE " + graphPattern(pretty, new GraphPattern(where.toArray(new GraphPatternValue[where.size()]))) + (pretty ? "\n" : " ");
    }
    public String askHeader(boolean pretty,AskHeader askHeader){
        return "ASK";
    }


}
