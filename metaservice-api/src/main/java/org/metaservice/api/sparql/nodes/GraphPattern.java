package org.metaservice.api.sparql.nodes;

/**
* Created by ilo on 06.03.14.
*/
public class GraphPattern implements GraphPatternValue{
    private final GraphPatternValue[] graphPatternValues;

    public GraphPattern(GraphPatternValue[] graphPatternValues) {
        this.graphPatternValues = graphPatternValues;
    }

    public GraphPatternValue[] getGraphPatternValues() {
        return graphPatternValues;
    }


}
