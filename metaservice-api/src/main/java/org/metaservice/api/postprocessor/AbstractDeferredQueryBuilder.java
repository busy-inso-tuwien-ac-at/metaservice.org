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
import org.metaservice.api.sparql.builders.QueryBuilder;
import org.metaservice.api.sparql.buildingcontexts.SparqlQuery;
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
    protected SparqlQuery.DistinctEnum distinct= SparqlQuery.DistinctEnum.ALL;

    @Override
    @NotNull
    public QueryBuilder select(@NotNull SparqlQuery.DistinctEnum distinct,SelectTerm... selectTerms) {
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
