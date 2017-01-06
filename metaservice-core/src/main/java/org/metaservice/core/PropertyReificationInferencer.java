/*
 * Copyright 2015 Nikola Ilo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
