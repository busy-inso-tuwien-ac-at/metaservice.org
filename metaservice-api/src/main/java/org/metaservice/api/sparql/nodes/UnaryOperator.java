package org.metaservice.api.sparql.nodes;

/**
* Created by ilo on 05.03.14.
*/
public class UnaryOperator<Y extends Term> implements UnaryTerm<Boolean, Y> {
    private final String name;
    private final Y t1;

    public UnaryOperator(String name, Y t1) {
        this.name = name;
        this.t1 = t1;
    }

    public String getName() {
        return name;
    }

    public Y getT1() {
        return t1;
    }
}
