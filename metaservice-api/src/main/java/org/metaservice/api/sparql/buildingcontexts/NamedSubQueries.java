/*
 * Copyright 2015 Nikola Ilo
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

package org.metaservice.api.sparql.buildingcontexts;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.sparql.builders.SelectQueryBuilder;
import org.metaservice.api.sparql.nodes.NamedSubQuery;
import org.metaservice.api.sparql.builders.QueryBuilder;

import org.metaservice.api.sparql.nodes.Pattern;

/**
 * Created by ilo on 05.03.14.
 */
public interface NamedSubQueries {

    @NotNull
    public NamedSubQuery namedSubQuery(@NotNull String name,@NotNull SelectQueryBuilder queryBuilder);

    @NotNull
    public Pattern include(@NotNull String name);

}
