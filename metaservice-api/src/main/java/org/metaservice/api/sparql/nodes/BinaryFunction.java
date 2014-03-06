package org.metaservice.api.sparql.nodes;

/**
* Created by ilo on 05.03.14.
*/
public class BinaryFunction<X, Y extends Term, T extends Term> implements BinaryTerm<X, Y, T> {
    private final String name;
    private final Y t1;
    private final T t2;

    public BinaryFunction(String name, Y t1, T t2) {
        this.name = name;
        this.t1 = t1;
        this.t2 = t2;
    }

    public String getName() {
        return name;
    }

    public Y getT1() {
        return t1;
    }

    public T getT2() {
        return t2;
    }
}
