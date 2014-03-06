package org.metaservice.api.sparql.nodes;

/**
* Created by ilo on 06.03.14.
*/
public class Union implements GraphPatternValue {
    private final GraphPattern[] graphPatterns;

    public Union(GraphPattern[] graphPatterns) {
        this.graphPatterns = graphPatterns;
    }

    public GraphPattern[] getGraphPatterns() {
        return graphPatterns;
    }
}
