package org.metaservice.api.sparql.builders;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.sparql.nodes.GraphPatternValue;

/**
 * Created by ilo on 05.03.14.
 */
public interface AskQueryBuilder {
    @NotNull
    public AskQueryBuilder ask();

    @NotNull
    public String build();

    @NotNull
    public String build(boolean pretty);

    @NotNull
    public AskQueryBuilder where(GraphPatternValue... graphPatternValue);

}
