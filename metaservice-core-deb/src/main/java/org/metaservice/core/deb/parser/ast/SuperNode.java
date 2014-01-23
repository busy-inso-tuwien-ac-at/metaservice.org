package org.metaservice.core.deb.parser.ast;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.parboiled.trees.MutableTreeNodeImpl;

public class SuperNode extends MutableTreeNodeImpl<SuperNode> {

    @Nullable
    @Override
    public String toString() {
        return StringUtils.join(this.getChildren(), "\n");
    }
}
