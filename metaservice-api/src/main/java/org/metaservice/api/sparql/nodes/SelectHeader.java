package org.metaservice.api.sparql.nodes;

/**
* Created by ilo on 06.03.14.
*/
public class SelectHeader implements QueryHeader {

    private final boolean distinct;
    private final SelectTerm[] terms;
    public SelectHeader(SelectTerm[] selectTerms) {
        this(false,selectTerms);
    }

    public SelectHeader(boolean distinct, SelectTerm[] selectTerms) {
        this.distinct = distinct;
        this.terms = selectTerms;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public SelectTerm[] getTerms() {
        return terms;
    }
}
