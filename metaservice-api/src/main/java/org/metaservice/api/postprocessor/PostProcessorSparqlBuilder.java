package org.metaservice.api.postprocessor;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.rdf.vocabulary.BIGDATA;
import org.metaservice.api.rdf.vocabulary.METASERVICE;
import org.metaservice.api.sparql.builders.QueryBuilder;
import org.metaservice.api.sparql.builders.SelectQueryBuilder;
import org.metaservice.api.sparql.buildingcontexts.BigdataSparqlQuery;
import org.metaservice.api.sparql.buildingcontexts.SparqlQuery;
import org.metaservice.api.sparql.nodes.*;
import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.metaservice.api.sparql.buildingcontexts.SparqlQuery.DistinctEnum.ALL;
import static org.metaservice.api.sparql.buildingcontexts.SparqlQuery.DistinctEnum.DISTINCT;

/**
 * Created by ilo on 05.03.14.
 */
public class PostProcessorSparqlBuilder extends AbstractDeferredQueryBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostProcessorSparqlBuilder.class);


    public static BoundVariable getDateVariable(){
        return new BoundVariable("selectedDate");
    }

    @NotNull
    @Override
    public QueryBuilder select(SelectTerm... selectTerms) {
        return select(ALL,selectTerms);
    }

    //todo support greater sparql subset (subselects, ...)
    //todo detect and substitute redundant namedqueries
    @Override
    @NotNull
    public String build(final boolean pretty) {
        SparqlQuery sparqlQuery = new BigdataSparqlQuery(){
            private int statementContextCounter = 0;

            private void handleSubQuery(List<GraphPatternValue> patterns, ArrayList<GraphPatternValue> heuristics, ArrayList<GraphPatternValue> results, SelectQueryBuilder globalQuery){
                HashMap<Value,String> nameMap = new HashMap<>();
                HashMap<Value,Variable> timeMap = new HashMap<>();
                HashMap<Value,Variable> subjectMap= new HashMap<>();
                HashMap<Value,List<QuadPattern>>  contextMap = new HashMap<>();
                HashSet<Variable> artificialContextSet = new HashSet<>();
                for(GraphPatternValue pattern : patterns){
                    if(pattern instanceof QuadPattern){
                        QuadPattern quadPattern = (QuadPattern) pattern;
                        heuristics.add(quadPattern);
                        if(quadPattern.getS() instanceof BIGDATA.QueryHint)
                            continue;
                        if(!contextMap.containsKey(quadPattern.getC())){
                            contextMap.put(quadPattern.getC(),new ArrayList<QuadPattern>());
                            String name = "mmm"+ statementContextCounter;
                            nameMap.put(quadPattern.getC(),name);
                            timeMap.put(quadPattern.getC(),new Variable("time" + name));
                            subjectMap.put(quadPattern.getC(),new Variable("subject" + name));
                            statementContextCounter++;
                        }
                        contextMap.get(quadPattern.getC()).add(quadPattern);
                    }else if (pattern instanceof TriplePattern){
                        TriplePattern triplePattern = (TriplePattern) pattern;
                        heuristics.add(triplePattern);
                        if(triplePattern.getS() instanceof BIGDATA.QueryHint)
                            continue;
                        String name = "mmm"+ statementContextCounter;
                        Variable c = new Variable(name);
                        artificialContextSet.add(c);
                        contextMap.put(c, new ArrayList<QuadPattern>());
                        contextMap.get(c).add(quadPattern(triplePattern.getS(), triplePattern.getP(), triplePattern.getO(), c));
                        nameMap.put(c, name);
                        timeMap.put(c,new Variable("time" + name));
                        subjectMap.put(c, new Variable("subject" + name));
                        statementContextCounter++;
                    }else if (pattern instanceof Filter){
                        heuristics.add(pattern);
                    }else if (pattern instanceof Union){
                        Union union = (Union) pattern;
                        ArrayList<GraphPattern> graphPatterns = new ArrayList<>();
                        ArrayList<GraphPattern> graphPatternsResults = new ArrayList<>();
                        for(GraphPattern graphPattern : union.getGraphPatterns()){
                            ArrayList<GraphPatternValue> arrayList = new ArrayList<>();
                            ArrayList<GraphPatternValue> resultsArrayList = new ArrayList<>();
                            handleSubQuery(Arrays.asList(graphPattern.getGraphPatternValues()), arrayList, resultsArrayList, globalQuery);
                            graphPatterns.add(graphPattern(arrayList.toArray(new GraphPatternValue[arrayList.size()])));
                            graphPatternsResults.add(graphPattern(resultsArrayList.toArray(new GraphPatternValue[resultsArrayList.size()])));
                        }
                        heuristics.add(union(graphPatterns.toArray(new GraphPattern[graphPatterns.size()])));
                        results.add(union(graphPatternsResults.toArray(new GraphPattern[graphPatternsResults.size()])));
                    }else {
                        throw new UnsupportedOperationException();
                    }
                }

                for(Value c: contextMap.keySet()){
                    Variable context2 = new Variable("mmmmmcontext2");
                    Variable processableSubject = subjectMap.get(c);
                    String name = nameMap.get(c);
                    String nameCommon = name +"Common"; //used to check boundness of variables in  heuristic
                    String nameContinuous = name +"Continuous";
                    Variable type = new Variable(name+"Action");
                    Variable time = timeMap.get(c);
                    Variable generator = new Variable(name+"generator");
                    Variable path = new Variable(name+"path");
                    HashSet<Variable> groupByList = new HashSet<>();
                    for(QuadPattern quadPattern : contextMap.get(c)) {
                        if(quadPattern.getS() instanceof Variable && !(quadPattern.getS() instanceof BoundVariable)) {
                            groupByList.add((Variable) quadPattern.getS());
                        }
                        if(quadPattern.getP() instanceof Variable && !(quadPattern.getP() instanceof BoundVariable)) {
                            groupByList.add((Variable) quadPattern.getP());
                        }
                        if(quadPattern.getO() instanceof Variable && !(quadPattern.getO() instanceof BoundVariable)) {
                            groupByList.add((Variable) quadPattern.getO());
                        }
                    }
                    AggregateSelectTerm maxTime = aggregate("MAX", time, time);
                    SelectQueryBuilder filteredQuery = select(all()
                            ).where(
                            triplePattern(BIGDATA.SUB_QUERY,BIGDATA.OPTIMIZE,BIGDATA.NONE),
                            include("heuristic")//,
                    );
                    HashSet<SelectTerm> selectList = new HashSet<>();
                    for(Variable variable : groupByList){
                        selectList.add(var(variable));
                    }
                    selectList.add(maxTime);
                    SelectQueryBuilder maxQuery = select(DISTINCT,
                            selectList.toArray(new SelectTerm[selectList.size()])
                    )
                            .where(
                                    include(nameCommon)
                            );
                    selectList.remove(maxTime);
                    if(c instanceof Variable && !(c instanceof BoundVariable)) {
                        selectList.add(var((Variable) c));
                    }
                    selectList.add(var(time));
                    selectList.add(var(generator));
                    SelectQueryBuilder maxQueryContinuous = select(DISTINCT,
                            selectList.toArray(new SelectTerm[selectList.size()])
                    )
                            .where(
                                    triplePattern(BIGDATA.SUB_QUERY,BIGDATA.OPTIMIZE,BIGDATA.NONE),
                                    include(nameContinuous+"1"),
                                    triplePattern(c,METASERVICE.AUTHORITIVE_SUBJECT,processableSubject),
                                    triplePattern(c,METASERVICE.GENERATOR,generator),
                                    triplePattern(c,METASERVICE.DATA_TIME,time)
                            );
                    SelectQueryBuilder maxQueryContinuous0 = select(DISTINCT,var(processableSubject),var(generator))
                            .where(
                                 include(nameCommon)
                            );
                    SelectQueryBuilder maxQueryContinuous1 = select(DISTINCT,var(processableSubject),var(generator),aggregate("MAX", time, time))
                            .where(
                                 include(nameContinuous+"0"),
                                    triplePattern(BIGDATA.SUB_QUERY,BIGDATA.OPTIMIZE,BIGDATA.NONE),
                                    filter(lessOrEqual(val(time), val(getDateVariable()))),
                                    triplePattern(context2,METASERVICE.AUTHORITIVE_SUBJECT,processableSubject),
                                    triplePattern(context2,METASERVICE.GENERATOR,generator),
               //                   triplePattern(context2,METASERVICE.ACTION,METASERVICE.ACTION_CONTINUOUS),
                                    triplePattern(context2,METASERVICE.DATA_TIME, time)
                            ).groupBy(processableSubject).groupBy(generator);

                    for(QuadPattern quadPattern : contextMap.get(c)){
                        maxQuery.where(quadPattern);
                        maxQueryContinuous.where(quadPattern);
                        maxQueryContinuous0.where(quadPattern);
                    }
                    for(Variable var : groupByList){
                        if(!artificialContextSet.contains(var)) {
                            filteredQuery.where(filter(bound(val(var))));
                        }
                        maxQuery.groupBy(var);
                    }
                    maxQuery.where(
                                    triplePattern(BIGDATA.SUB_QUERY, BIGDATA.OPTIMIZE, BIGDATA.NONE),
                                    filter(unequal(val(type),val(METASERVICE.CONTINUOUS_OBSERVATION))),
                                    filter(lessOrEqual(val(time), val(getDateVariable()))),
                                    triplePattern(c,METASERVICE.DATA_TIME, time),
                                    triplePattern(c, RDF.TYPE,type),
                                    triplePattern(c,METASERVICE.PATH,path)
                            );
                    maxQuery.groupBy(path);
                    maxQueryContinuous0.where(
                            triplePattern(BIGDATA.SUB_QUERY, BIGDATA.OPTIMIZE, BIGDATA.NONE),
                            triplePattern(c, RDF.TYPE, METASERVICE.CONTINUOUS_OBSERVATION),
                            triplePattern(c, METASERVICE.GENERATOR, generator),
                            triplePattern(c, METASERVICE.AUTHORITIVE_SUBJECT, processableSubject)
                    );
                    globalQuery.with(
                            namedSubQuery(nameCommon, filteredQuery),
                            namedSubQuery(name, maxQuery),
                            namedSubQuery(nameContinuous + "0", maxQueryContinuous0),
                            namedSubQuery(nameContinuous + "1", maxQueryContinuous1),
                            namedSubQuery(nameContinuous, maxQueryContinuous)
                    );
                    ArrayList<GraphPatternValue> unionList = new ArrayList<>();
                    unionList.add(include(name));
                    for(QuadPattern quadPattern : contextMap.get(c)){
                        unionList.add(quadPattern);
                    }
                    results.add(
                            union(
                                    graphPattern(unionList.toArray(new GraphPatternValue[unionList.size()])),
                                    graphPattern(include(nameContinuous))
                            ));
                    results.add(triplePattern(c, METASERVICE.DATA_TIME, time));
                    results.add(filter(unequal(val(type),val(METASERVICE.REMOVE_OBSERVATION))));
                    results.add(triplePattern(c, RDF.TYPE,type));

                }
            }

            @Override
            public String build() {
                SelectQueryBuilder selectQueryBuilder = select(distinct,selectTerms);
                selectQueryBuilder
                        .where(
                                triplePattern(BIGDATA.SUB_QUERY,BIGDATA.OPTIMIZE,BIGDATA.NONE),
                                include("heuristic")
                        );
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
                SelectQueryBuilder heuristic = select(all());
                selectQueryBuilder.with(namedSubQuery("heuristic",heuristic));
                ArrayList<GraphPatternValue> heuristics = new ArrayList<>();
                ArrayList<GraphPatternValue> results = new ArrayList<>();
                handleSubQuery(wherePatterns, heuristics, results, selectQueryBuilder);
                for(GraphPatternValue h: heuristics){
                    heuristic.where(h);
                }
                for(GraphPatternValue r: results){
                    selectQueryBuilder.where(r);
                }

                return selectQueryBuilder
                        .build(pretty);
            }
        };
        return sparqlQuery.toString();
    }

}
