package org.metaservice.core.deb.parser.ast;

import org.jetbrains.annotations.Nullable;
import org.jsoup.helper.StringUtil;
import org.parboiled.trees.MutableTreeNodeImpl;

public class SuperNode extends MutableTreeNodeImpl<SuperNode> {

    @Nullable
    @Override
    public String toString() {
        return StringUtil.join(this.getChildren(),"\n");
    }
}
