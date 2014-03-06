package org.metaservice.api.sparql.nodes;

/**
* Created by ilo on 06.03.14.
*/
public class FilterImpl implements Filter {
    private Term<Boolean> term;

    public FilterImpl(Term<Boolean> term) {
        this.term = term;
    }

    public Term<Boolean> getTerm() {
        return term;
    }
}
