package org.metaservice.api.sparql.impl;

import org.metaservice.api.sparql.builders.QueryBuilder;
import org.metaservice.api.sparql.builders.QueryBuilderFactory;

/**
 * Created by ilo on 06.03.14.
 */
public class SparqlQuerBuilderFactory implements QueryBuilderFactory {

    @Override
    public QueryBuilder create() {
        return SparqlQueryBuilderImpl.getInstance();
    }
}
