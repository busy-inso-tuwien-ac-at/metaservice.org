package org.metaservice.api.sparql.nodes;

/**
* Created by ilo on 05.03.14.
*/
public class FilterExists implements Filter {
    private final GraphPattern graphPattern;

    public FilterExists(GraphPattern graphPattern) {
        this.graphPattern = graphPattern;
    }

    public GraphPattern getGraphPattern() {
        return graphPattern;
    }
}
