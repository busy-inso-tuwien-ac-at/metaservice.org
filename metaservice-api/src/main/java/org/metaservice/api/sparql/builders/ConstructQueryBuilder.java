package org.metaservice.api.sparql.builders;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.sparql.nodes.GraphPatternValue;
import org.metaservice.api.sparql.nodes.NamedSubQuery;
import org.metaservice.api.sparql.nodes.Pattern;

/**
 * Created by ilo on 05.03.14.
 */
public interface ConstructQueryBuilder {

    @NotNull
    public ConstructQueryBuilder construct(Pattern... patterns);

    @NotNull
    public ConstructQueryBuilder limit(int i);

    @NotNull
    public String build();

    @NotNull
    public String build(boolean pretty);

    @NotNull
    public ConstructQueryBuilder where(GraphPatternValue... graphPatternValue);

}
