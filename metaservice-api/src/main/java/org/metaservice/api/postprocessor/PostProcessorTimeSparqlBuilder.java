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

package org.metaservice.api.postprocessor;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.rdf.vocabulary.METASERVICE;
import org.metaservice.api.sparql.builders.QueryBuilder;
import org.metaservice.api.sparql.builders.SelectQueryBuilder;
import org.metaservice.api.sparql.buildingcontexts.BigdataSparqlQuery;
import org.metaservice.api.sparql.buildingcontexts.SparqlQuery;
import org.metaservice.api.sparql.nodes.*;
import org.openrdf.model.Value;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.model.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ilo on 05.03.14.
 */
public class PostProcessorTimeSparqlBuilder extends AbstractDeferredQueryBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostProcessorTimeSparqlBuilder.class);

    public PostProcessorTimeSparqlBuilder(){
    }



    public static Variable getDateVariable(){
        return new Variable("selectedDate");
    }

    @NotNull
    @Override
    public QueryBuilder select(SelectTerm... selectTerms) {
        return select(SparqlQuery.DistinctEnum.ALL,selectTerms);
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
                SelectQueryBuilder heuristic = select(all());
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
                    SelectQueryBuilder maxQuery = select(
                            aggregate("MAX", time, time)
                    )
                            .where(
                                    include("heuristic"),
                                    filter(lessOrEqual(val(time), val(getDateVariable()))),
                                    triplePattern(c, METASERVICE.DATA_TIME, time)

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
                            triplePattern(c, METASERVICE.DATA_TIME, time),
                            triplePattern(c, RDF.TYPE,METASERVICE.ADD_OBSERVATION)
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
