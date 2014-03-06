package org.metaservice.api.sparql.nodes;

/**
* Created by ilo on 06.03.14.
*/
public interface DirectSelectTerm extends SelectTerm {
    Variable getAs();
    Variable getVariable();
}
