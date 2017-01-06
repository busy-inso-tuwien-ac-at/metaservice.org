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

package org.metaservice.api.postprocessor;

import org.metaservice.api.sparql.builders.QueryBuilder;
import org.metaservice.api.sparql.builders.QueryBuilderFactory;
import org.metaservice.api.sparql.buildingcontexts.DefaultSparqlQuery;

/**
 * Created by ilo on 06.03.14.
 */
public abstract class PostProcessorTimeSparqlQuery extends DefaultSparqlQuery {
    public PostProcessorTimeSparqlQuery(){
        super(new QueryBuilderFactory(){

            @Override
            public QueryBuilder create() {
                return new PostProcessorSparqlBuilder();
            }
        });
    }
}
