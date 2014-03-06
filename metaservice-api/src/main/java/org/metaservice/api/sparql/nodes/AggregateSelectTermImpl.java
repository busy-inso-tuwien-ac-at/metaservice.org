package org.metaservice.api.sparql.nodes;

/**
* Created by ilo on 05.03.14.
*/
public class AggregateSelectTermImpl implements AggregateSelectTerm {
    private final Variable variable;
    private final Variable as;
    private final String aggregate;

    public AggregateSelectTermImpl(Variable variable, Variable as, String aggregate) {
        this.variable = variable;
        this.as = as;
        this.aggregate = aggregate;
    }


    @Override
    public String getAggregate() {
        return aggregate;
    }

    @Override
    public Variable getVariable() {
        return variable;
    }

    public Variable getAs() {
        return as;
    }
}
