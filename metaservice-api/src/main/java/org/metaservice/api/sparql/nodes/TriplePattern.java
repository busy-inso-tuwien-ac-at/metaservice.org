package org.metaservice.api.sparql.nodes;

import org.openrdf.model.Value;

/**
* Created by ilo on 05.03.14.
*/
public class TriplePattern implements Pattern {
    private final Value s;
    private final Value p;
    private final Value o;

    public TriplePattern(Value s, Value p, Value o) {
        this.s = s;
        this.p = p;
        this.o = o;
    }

    public Value getS() {
        return s;
    }

    public Value getP() {
        return p;
    }

    public Value getO() {
        return o;
    }
}
