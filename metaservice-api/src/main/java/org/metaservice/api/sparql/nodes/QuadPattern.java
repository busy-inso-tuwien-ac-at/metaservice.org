package org.metaservice.api.sparql.nodes;

import org.openrdf.model.Value;

/**
* Created by ilo on 05.03.14.
*/
public class QuadPattern implements Pattern {
    private final Value s;
    private final Value p;
    private final Value o;
    private final Value c;

    public QuadPattern(Value s, Value p, Value o, Value c) {
        this.s = s;
        this.p = p;
        this.o = o;
        this.c = c;

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

    public Value getC() {
        return c;
    }

}
