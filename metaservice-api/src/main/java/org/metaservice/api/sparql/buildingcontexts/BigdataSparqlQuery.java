package org.metaservice.api.sparql.buildingcontexts;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.sparql.builders.SelectQueryBuilder;
import org.metaservice.api.sparql.nodes.Include;
import org.metaservice.api.sparql.nodes.NamedSubQuery;
import org.metaservice.api.sparql.builders.QueryBuilder;
import org.metaservice.api.sparql.nodes.Pattern;

/**
 * Created by ilo on 05.03.14.
 */
public abstract class BigdataSparqlQuery extends DefaultSparqlQuery implements NamedSubQueries {


    @NotNull
    @Override
    public final NamedSubQuery namedSubQuery(String name,SelectQueryBuilder queryBuilder){
        return new NamedSubQuery(name,queryBuilder);
    }
    @NotNull
    @Override
    public Pattern include(@NotNull String name){
        return new Include(name);
    }

}
