package org.metaservice.api.sparql.nodes;

/**
* Created by ilo on 05.03.14.
*/
public class UnaryFunction<X, Y extends Term> implements UnaryTerm<X, Y> {
    private final String name;
    private final Y t1;

    public UnaryFunction(String name, Y t1) {
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
