package org.metaservice.api.sparql.nodes;

import org.openrdf.model.Value;

/**
* Created by ilo on 05.03.14.
*/
public class SimpleTerm implements Term<Value> {
    private final Value t;

    public SimpleTerm(Value t) {
        this.t = t;
    }

    public Value getT() {
        return t;
    }
}
