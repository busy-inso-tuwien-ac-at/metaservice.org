package org.metaservice.api.sparql.nodes;

/**
* Created by ilo on 06.03.14.
*/
public class FilterNotExists implements Filter {
    private final GraphPattern graphPattern;

    public FilterNotExists(GraphPattern graphPattern) {
        this.graphPattern = graphPattern;
    }

    public GraphPattern getGraphPattern() {
        return graphPattern;
    }
}
