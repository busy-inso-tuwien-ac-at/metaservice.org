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

package org.metaservice.api.sparql.nodes;

import org.metaservice.api.sparql.buildingcontexts.SparqlQuery;

/**
* Created by ilo on 06.03.14.
*/
public class SelectHeader implements QueryHeader {

    private SparqlQuery.DistinctEnum distinct;
    private final SelectTerm[] terms;
    public SelectHeader(SelectTerm[] selectTerms) {
        this(SparqlQuery.DistinctEnum.ALL,selectTerms);
    }

    public SelectHeader(SparqlQuery.DistinctEnum distinct, SelectTerm[] selectTerms) {
        this.distinct = distinct;
        this.terms = selectTerms;
    }

    public boolean isDistinct() {
        return distinct == SparqlQuery.DistinctEnum.DISTINCT;
    }

    public SelectTerm[] getTerms() {
        return terms;
    }
}
