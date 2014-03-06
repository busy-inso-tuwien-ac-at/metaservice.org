package org.metaservice.api.sparql.nodes;

/**
* Created by ilo on 06.03.14.
*/
public class ConstructHeader implements QueryHeader {
    private Pattern[] patterns;

    public ConstructHeader(Pattern[] patterns) {
        this.patterns = patterns;
    }


    public Pattern[] getPatterns() {
        return patterns;
    }
}
