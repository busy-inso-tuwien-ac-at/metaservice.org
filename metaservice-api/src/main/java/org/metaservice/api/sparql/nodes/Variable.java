package org.metaservice.api.sparql.nodes;

import org.openrdf.model.Value;

/**
 * Created by ilo on 05.03.14.
 */
public class Variable implements Value {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public String stringValue() {
        return name;
    }

    @Override
    public String toString() {
        return stringValue();
    }
}
