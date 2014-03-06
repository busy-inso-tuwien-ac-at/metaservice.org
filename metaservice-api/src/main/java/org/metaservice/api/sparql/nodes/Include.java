package org.metaservice.api.sparql.nodes;

/**
* Created by ilo on 05.03.14.
*/
public class Include implements Pattern {
    private final String name;

    public Include(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
