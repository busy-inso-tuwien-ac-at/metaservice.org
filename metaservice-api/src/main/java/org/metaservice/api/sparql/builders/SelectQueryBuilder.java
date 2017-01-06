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

package org.metaservice.api.sparql.builders;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.sparql.buildingcontexts.SparqlQuery;
import org.metaservice.api.sparql.nodes.GraphPatternValue;
import org.metaservice.api.sparql.nodes.NamedSubQuery;
import org.metaservice.api.sparql.nodes.SelectTerm;
import org.metaservice.api.sparql.nodes.Variable;

/**
 * Created by ilo on 04.03.14.
 */
public interface SelectQueryBuilder extends NamedSubQueriesBuilder,GraphPatternValue{

    @NotNull
    public SelectQueryBuilder select(SparqlQuery.DistinctEnum distinct,SelectTerm... selectTerms);
    @NotNull
    public SelectQueryBuilder orderByAsc(@NotNull Variable variable);
    @NotNull
    public SelectQueryBuilder orderByDesc(@NotNull Variable variable);
    @NotNull
    public SelectQueryBuilder groupBy(@NotNull Variable variable);
    @NotNull
    public SelectQueryBuilder limit(int i);

    @NotNull
    public String build();

    @NotNull
    public String build(boolean pretty);

    @NotNull
    public SelectQueryBuilder where(GraphPatternValue... graphPatternValue);

}


