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
