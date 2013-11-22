package org.metaservice.core.deb.parser.ast;

import org.jsoup.helper.StringUtil;
import org.parboiled.errors.ParserRuntimeException;

public class DependencyConjunction extends SuperNode {
    @Override
    public void addChild(int i, SuperNode superNode) {
        if(! (superNode instanceof DependencyDisjunction)){
            throw new ParserRuntimeException();
        }
        DependencyDisjunction x = (DependencyDisjunction) superNode;
        if(x.getChildren().size() == 1){
            super.addChild(i, x.getChildren().get(0));
        } else if(x.getChildren().size() > 1){
            super.addChild(i, superNode);
        }
    }

    @Override
    public String toString() {
        return StringUtil.join(this.getChildren(), " , ");
    }
}
