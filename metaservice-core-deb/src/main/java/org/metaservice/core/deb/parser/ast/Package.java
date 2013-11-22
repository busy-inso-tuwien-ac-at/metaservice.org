package org.metaservice.core.deb.parser.ast;

import org.jetbrains.annotations.NotNull;
import org.jsoup.helper.StringUtil;

/**
 * Created with IntelliJ IDEA.
 * User: ilo
 * Date: 15.10.13
 * Time: 10:42
 * To change this template use File | Settings | File Templates.
 */
public class Package extends SuperNode {
    @NotNull
    @Override
    public String toString() {
        return StringUtil.join(this.getChildren(), "\n")+ "\n\n";
    }
}
