package org.metaservice.api.sparql.nodes;

/**
* Created by ilo on 06.03.14.
*/
public class DirectSelectTermImpl implements DirectSelectTerm {
    private final Variable variable;
    private final Variable as;

    public DirectSelectTermImpl(Variable value, Variable as) {
        this.variable = value;
        this.as = as;
    }

    public Variable getVariable() {
        return variable;
    }

    public Variable getAs() {
        return as;
    }
}
