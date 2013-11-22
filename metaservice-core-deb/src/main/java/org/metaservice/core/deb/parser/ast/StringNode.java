package org.metaservice.core.deb.parser.ast;

/**
 * Created with IntelliJ IDEA.
 * User: ilo
 * Date: 15.10.13
 * Time: 03:15
 * To change this template use File | Settings | File Templates.
 */
public class StringNode extends SuperNode {
    private final String s;
    public StringNode(String s) {
        super();
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }
}
