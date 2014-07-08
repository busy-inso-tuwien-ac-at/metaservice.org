package org.metaservice.core;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.sail.NotifyingSail;
import org.openrdf.sail.SailException;
import org.openrdf.sail.inferencer.fc.CustomGraphQueryInferencer;

/**
 * Created by ilo on 04.07.2014.
 * todo buggy
 */
public class PropertyReificationInferencer extends CustomGraphQueryInferencer {
private final static String SHORTCUT_RELATION_RULE = "prefix prv:  <http://purl.org/ontology/prv/core#> \n" +
        "CONSTRUCT  { ?s ?sc ?o }" +
        " WHERE{" +
        " ?pr a prv:PropertyReification ;\n" +
        "        prv:shortcut ?sc ;\n" +
        "        prv:reification_class ?rc ;\n" +
        "        prv:shortcut_property ?scp ;\n" +
        "        prv:subject_property ?sp ;\n" +
        "        prv:object_property ?op .\n" +
        "\n" +
        "    ?r a ?rc ;\n" +
        "        ?scp ?sc ;\n" +
        "        ?sp ?s ;\n" +
        "        ?op ?o ."+
        "}";
    private final static String PROPERTY_REIFICATION_RULE = "prefix prv:  <http://purl.org/ontology/prv/core#> \n" +
            "CONSTRUCT  {" +
            " ?r a ?rc ;\n" +
            "      ?scp ?sc ;\n" +
            "      ?sp ?s ;\n" +
            "      ?op ?o .\n" +
            "}" +
            " WHERE{" +
            " ?pr a prv:PropertyReification ;\n" +
            "        prv:shortcut ?sc ;\n" +
            "        prv:reification_class ?rc ;\n" +
            "        prv:shortcut_property ?scp ;\n" +
            "        prv:subject_property ?sp ;\n" +
            "        prv:object_property ?op .\n" +
            "\n" +
            "     ?s ?sc ?o ."+
            "}";

    public PropertyReificationInferencer(NotifyingSail baseSail) throws SailException, MalformedQueryException {
        super(
                new CustomGraphQueryInferencer(
                        baseSail,
                        QueryLanguage.SPARQL,
                        PROPERTY_REIFICATION_RULE,
                        ""),
                QueryLanguage.SPARQL,
                SHORTCUT_RELATION_RULE,
                "");
    }

}
