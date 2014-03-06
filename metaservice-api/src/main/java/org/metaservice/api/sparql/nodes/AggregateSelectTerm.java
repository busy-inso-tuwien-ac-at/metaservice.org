package org.metaservice.api.sparql.nodes;

/**
* Created by ilo on 06.03.14.
*/
public interface AggregateSelectTerm extends SelectTerm {
    String getAggregate();
    Variable getVariable();
    Variable getAs();
}
