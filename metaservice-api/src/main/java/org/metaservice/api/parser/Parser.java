package org.metaservice.api.parser;

import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.archive.ArchiveParameters;

import java.io.Reader;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ilo
 * Date: 05.12.13
 * Time: 23:55
 * To change this template use File | Settings | File Templates.
 */
public interface Parser<T> {
    public List<T> parse(Reader s, ArchiveAddress archiveParameters) throws ParserException;
}
