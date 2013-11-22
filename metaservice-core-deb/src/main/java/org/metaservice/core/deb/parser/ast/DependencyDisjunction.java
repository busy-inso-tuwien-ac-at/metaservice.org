package org.metaservice.core.deb.parser.ast;

import org.jsoup.helper.StringUtil;

/**
 * Created with IntelliJ IDEA.
 * User: ilo
 * Date: 15.10.13
 * Time: 02:28
 * To change this template use File | Settings | File Templates.
 */
public class DependencyDisjunction extends SuperNode {
    @Override
    public String toString() {
        return StringUtil.join(this.getChildren()," | ");
    }
}
