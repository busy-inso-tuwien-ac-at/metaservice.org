package org.metaservice.api.postprocessor;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.rdf.vocabulary.METASERVICE;
import org.metaservice.api.sparql.builders.SelectQueryBuilder;
import org.metaservice.api.sparql.buildingcontexts.BigdataSparqlQuery;
import org.metaservice.api.sparql.buildingcontexts.SparqlQuery;
import org.metaservice.api.sparql.nodes.*;
import org.openrdf.model.Value;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by ilo on 05.03.14.
 */
public class PostProcessorSparqlBuilder extends AbstractDeferredQueryBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostProcessorSparqlBuilder.class);



    public static BoundVariable getDateVariable(){
        return new BoundVariable("selectedDate");
    }

    @Override
    @NotNull
    public String build(final boolean pretty) {
        SparqlQuery sparqlQuery = new BigdataSparqlQuery(){

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
                HashMap<Value,Variable> subjectMap= new HashMap<>();
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
                            subjectMap.put(quadPattern.getC(),new Variable("subject" + name));
                            i++;
                        }
                        contextMap.get(quadPattern.getC()).add(quadPattern);
                    }else if (pattern instanceof TriplePattern){
                        TriplePattern triplePattern = (TriplePattern) pattern;
                        String name = "mmm"+i;
                        Variable c = new Variable(name);
                        contextMap.put(c,new ArrayList<QuadPattern>());
                        contextMap.get(c).add(quadPattern(triplePattern.getS(), triplePattern.getP(), triplePattern.getO(), c));
                        nameMap.put(c, name);
                        timeMap.put(c,new Variable("time" + name));
                        subjectMap.put(c,new Variable("subject" + name));
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
                    Variable context2 = new Variable("mmmmmcontext2");
                    Variable processableSubject = subjectMap.get(c);
                    String name = nameMap.get(c);
                    String nameContinuous = name +"Continuous";
                    Variable action = new Variable(name+"Action");
                    Variable time = timeMap.get(c);
                    Variable generator = new Variable(name+"generator");
                    HashSet<Variable> groupByList = new HashSet<>();
                    for(QuadPattern quadPattern : contextMap.get(c)) {
                        if(quadPattern.getC() instanceof Variable && !(quadPattern.getC() instanceof BoundVariable)) {
                            groupByList.add((Variable) quadPattern.getC());
                        }
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
                    HashSet<SelectTerm> selectList = new HashSet<>();
                    for(Variable variable : groupByList){
                        selectList.add(var(variable));
                    }
                    selectList.add(aggregate("MAX", time, time));
                    SelectQueryBuilder maxQuery = select(true,
                            selectList.toArray(new SelectTerm[selectList.size()])
                    )
                            .where(
                                    include("heuristic"),
                                    filter(unequal(val(action),val(METASERVICE.ACTION_CONTINUOUS))),
                                    filter(lessOrEqual(val(time), val(getDateVariable()))),
                                    triplePattern(c,METASERVICE.ACTION,action),
                                    triplePattern(c,METASERVICE.TIME, time)
                            );

                    SelectQueryBuilder maxQueryContinuous = select(true,
                            selectList.toArray(new SelectTerm[selectList.size()])
                    )
                            .where(
                                    include("heuristic"),
                                    filter(lessOrEqual(val(time), val(getDateVariable()))),
                                    triplePattern(c,METASERVICE.ACTION,METASERVICE.ACTION_CONTINUOUS),
                                    triplePattern(c,METASERVICE.XYZ,processableSubject),
                                    triplePattern(c,METASERVICE.GENERATOR,generator),
                                    triplePattern(context2,METASERVICE.ACTION,METASERVICE.ACTION_CONTINUOUS),
                                    triplePattern(context2,METASERVICE.XYZ,processableSubject),
                                    triplePattern(context2,METASERVICE.GENERATOR,generator),
                                    triplePattern(context2,METASERVICE.TIME, time)
                            ).groupBy(processableSubject);
                    for(Variable var : groupByList){
                        maxQuery.groupBy(var);
                        maxQueryContinuous.groupBy(var);
                    }
                    for(QuadPattern quadPattern : contextMap.get(c)){
                        maxQueryContinuous.where(quadPattern);
                        selectQueryBuilder.where(quadPattern);
                    }
                    selectQueryBuilder.with(
                            namedSubQuery(nameContinuous,maxQueryContinuous),
                            namedSubQuery(name,maxQuery)
                    );
                    selectQueryBuilder.where(
                            union(
                                    graphPattern(include(name)),
                                    graphPattern(include(nameContinuous))
                            ),
                            triplePattern(c, METASERVICE.TIME, time),
                            filter(unequal(val(action),val(METASERVICE.ACTION_REMOVE))),
                            triplePattern(c, METASERVICE.ACTION,action)
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

}
