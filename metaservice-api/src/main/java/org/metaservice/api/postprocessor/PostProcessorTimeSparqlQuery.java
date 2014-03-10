package org.metaservice.api.postprocessor;

import org.metaservice.api.sparql.builders.QueryBuilder;
import org.metaservice.api.sparql.builders.QueryBuilderFactory;
import org.metaservice.api.sparql.buildingcontexts.DefaultSparqlQuery;

/**
 * Created by ilo on 06.03.14.
 */
public abstract class PostProcessorTimeSparqlQuery extends DefaultSparqlQuery {
    public PostProcessorTimeSparqlQuery(){
        super(new QueryBuilderFactory(){

            @Override
            public QueryBuilder create() {
                return new PostProcessorSparqlBuilder();
            }
        });
    }
}
