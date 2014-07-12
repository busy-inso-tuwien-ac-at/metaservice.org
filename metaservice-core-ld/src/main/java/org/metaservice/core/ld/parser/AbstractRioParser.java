package org.metaservice.core.ld.parser;

import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.parser.Parser;
import org.metaservice.api.parser.ParserException;
import org.openrdf.model.Model;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.Rio;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ilo on 12.07.2014.
 */
public abstract class AbstractRioParser implements Parser<Model> {
    private final RDFFormat rdfFormat;

    protected AbstractRioParser(RDFFormat rdfFormat) {
        this.rdfFormat = rdfFormat;
    }

    @Override
    public List<Model> parse(Reader s, ArchiveAddress archiveParameters) throws ParserException {
        try {
            return Arrays.asList(Rio.parse(s, archiveParameters.getArchiveUri(), rdfFormat));
        } catch (IOException | RDFParseException e) {
            throw new ParserException(e);
        }
    }
}
