package org.metaservice.demo.license;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 * Created by ilo on 13.07.2014.
 */
public class SPDX_LICENSE {
    public static final String NS = "http://spdx.org/licenses/";

    public static final URI GPL_2_0;
    public static final URI LGPL_2_0;

    static {
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();
        GPL_2_0 = valueFactory.createURI(NS,"GPL-2.0");
        LGPL_2_0 = valueFactory.createURI(NS,"LGPL-2.0");
    }
}
