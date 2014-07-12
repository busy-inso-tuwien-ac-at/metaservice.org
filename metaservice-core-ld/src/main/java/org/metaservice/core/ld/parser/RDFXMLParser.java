package org.metaservice.core.ld.parser;

import org.openrdf.rio.RDFFormat;

import javax.inject.Inject;

/**
 * Created by ilo on 12.07.2014.
 */
public class RDFXMLParser extends AbstractRioParser{
    @Inject
    public RDFXMLParser() {
        super(RDFFormat.RDFXML);
    }
}
