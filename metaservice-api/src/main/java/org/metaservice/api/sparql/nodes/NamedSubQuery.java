package org.metaservice.api.sparql.nodes;

import org.metaservice.api.sparql.builders.SelectQueryBuilder;

/**
 * Created by ilo on 05.03.14.
 */
public class NamedSubQuery {

    private final String name;
    private final SelectQueryBuilder subQuery;

    public NamedSubQuery(String name, SelectQueryBuilder queryBuilder) {
        this.name = name;
        this.subQuery = queryBuilder;
    }

    public String getName() {
        return name;
    }

    public SelectQueryBuilder getSubQuery() {
        return subQuery;
    }
}
