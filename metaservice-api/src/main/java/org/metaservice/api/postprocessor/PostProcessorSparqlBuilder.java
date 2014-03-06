package org.metaservice.api.postprocessor;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.rdf.vocabulary.METASERVICE;
import org.metaservice.api.sparql.builders.SelectQueryBuilder;
import org.metaservice.api.sparql.buildingcontexts.BigdataSparqlQuery;
import org.metaservice.api.sparql.buildingcontexts.SparqlQuery;
import org.metaservice.api.sparql.nodes.*;
import org.metaservice.api.sparql.builders.QueryBuilder;
import org.openrdf.model.Literal;
import org.openrdf.model.Value;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by ilo on 05.03.14.
 */
public class PostProcessorSparqlBuilder implements QueryBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostProcessorSparqlBuilder.class);
    private Integer limit = null;
    private SelectTerm[] selectTerms = null;

    private final ArrayList<Variable> orderByAscList = new ArrayList<>();
    private final ArrayList<Variable> orderByDescList = new ArrayList<>();
    private final ArrayList<Variable> groupByList = new ArrayList<>();
    private ArrayList<GraphPatternValue> wherePatterns = new ArrayList<>();
    private ArrayList<NamedSubQuery> originalNamedSubQueries =new ArrayList<>();
    private boolean distinct=false;


    public PostProcessorSparqlBuilder(){
    }

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

    public static Variable getDateVariable(){
        return new Variable("selectedDate");
    }

    @Override
    @NotNull
    public String build(final boolean pretty) {
        SparqlQuery sparqlQuery = new BigdataSparqlQuery(){

            private final ValueFactoryImpl valueFactory = ValueFactoryImpl.getInstance();
            @Override
            public String build() {


                SelectQueryBuilder selectQueryBuilder = select(distinct,selectTerms);
                for(Variable v : groupByList){
                    selectQueryBuilder.groupBy(v);
                }
                for(Variable v :orderByAscList){
                    selectQueryBuilder.orderByAsc(v);
                }
                for(Variable v :orderByDescList){
                    selectQueryBuilder.orderByDesc(v);
                }
                if(limit != null)
                    selectQueryBuilder.limit(limit);
                SelectQueryBuilder heuristic = select(false,all());
                selectQueryBuilder.with(namedSubQuery("heuristic",heuristic));

                HashMap<Value,String> nameMap = new HashMap<>();
                HashMap<Value,Variable> timeMap = new HashMap<>();
                HashMap<Value,List<QuadPattern>>  contextMap = new HashMap<>();
                int i = 0;
                for(GraphPatternValue pattern : wherePatterns){
                    if(pattern instanceof QuadPattern){
                        QuadPattern quadPattern = (QuadPattern) pattern;
                        heuristic.where(quadPattern);
                        if(!contextMap.containsKey(quadPattern.getC())){
                            contextMap.put(quadPattern.getC(),new ArrayList<QuadPattern>());
                            String name = "mmm"+i;
                            nameMap.put(quadPattern.getC(),name);
                            timeMap.put(quadPattern.getC(),new Variable("time" + name));
                            i++;
                        }
                        contextMap.get(quadPattern.getC()).add(quadPattern);
                    }else if (pattern instanceof TriplePattern){
                        TriplePattern triplePattern = (TriplePattern) pattern;
                        String name = "mmm"+i;
                        Variable c = new Variable(name);
                        contextMap.put(c,new ArrayList<QuadPattern>());
                        contextMap.get(c).add(quadPattern(triplePattern.getS(),triplePattern.getP(),triplePattern.getO(),c));
                        nameMap.put(c,name);
                        timeMap.put(c,new Variable("time" + name));
                        heuristic.where(triplePattern);
                        i++;
                    }else if (pattern instanceof Filter){
                        heuristic.where(pattern);
                    }else if (pattern instanceof Union){
                        //todo fix it such that each unionpart is correctly treated.
                        heuristic.where(pattern);
                        LOGGER.warn("UNION found - will default to global time unrelated view");
                    }else {
                        throw new UnsupportedOperationException();
                    }
                }

                for(Value c: contextMap.keySet()){

                    String name = nameMap.get(c);
                    Variable time = timeMap.get(c);
                    SelectQueryBuilder maxQuery = select(false,
                            aggregate("MAX", time, time)
                    )
                            .where(
                                    include("heuristic"),
                                    filter(lessOrEqual(val(time), val(getDateVariable()))),
                                    triplePattern(c, METASERVICE.TIME, time)

                            );
                    for(QuadPattern quadPattern : contextMap.get(c)){
                        maxQuery.where(quadPattern);
                        selectQueryBuilder.where(quadPattern);
                    }
                    selectQueryBuilder.with(
                            namedSubQuery(name,maxQuery)
                    );
                    selectQueryBuilder.where(
                            include(name),
                            triplePattern(c, METASERVICE.TIME, time),
                            triplePattern(c, METASERVICE.ACTION, ValueFactoryImpl.getInstance().createLiteral("add"))
                    );
                }
                selectQueryBuilder
                        .where(
                                include("heuristic")
                        );

                return selectQueryBuilder
                                .build(pretty);
            }
        };
        return sparqlQuery.toString();
    }

    @Override
    @NotNull
    public QueryBuilder where(GraphPatternValue... graphPatternValues) {
        this.wherePatterns.addAll(Arrays.asList(graphPatternValues));
        return this;
    }
}
