package org.metaservice.api.postprocessor;

import org.metaservice.api.sparql.builders.QueryBuilder;
import org.metaservice.api.sparql.builders.QueryBuilderFactory;
import org.metaservice.api.sparql.buildingcontexts.DefaultSparqlQuery;
import org.metaservice.api.sparql.impl.SparqlQuerBuilderFactory;

import java.util.Date;

/**
 * Created by ilo on 06.03.14.
 */
public abstract class PostProcessorSparqlQuery extends DefaultSparqlQuery {
    public PostProcessorSparqlQuery(){
        super(new QueryBuilderFactory(){

            @Override
            public QueryBuilder create() {
                return new PostProcessorSparqlBuilder();
            }
        });
    }
}
