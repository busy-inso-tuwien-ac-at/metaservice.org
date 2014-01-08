package org.metaservice.api.parser;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ilo
 * Date: 05.12.13
 * Time: 23:55
 * To change this template use File | Settings | File Templates.
 */
public interface Parser<T> {
    public List<T> parse(String s);
}
