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

package org.metaservice.api.rdf.vocabulary;

import org.openrdf.model.*;
import org.openrdf.model.impl.*;



/**
 * This is an automatically generated class
 * Generator: org.metaservice.core.OntologyToJavaConverter
 * @see <a href="http://purl.org/vocab/vann/">vann</a>
 */
public class VANN{

    public static final String NAMESPACE = "http://purl.org/vocab/vann/";

    public static final String PREFIX = "vann";

    public static final Namespace NS = new NamespaceImpl(PREFIX, NAMESPACE);

////////////////////////
// ANNOTATION PROPERTIES
////////////////////////


    /**
     * http://purl.org/vocab/vann/termGroup<br>
     * "Term Group"<br>
     * A group of related terms in a vocabulary.<br>
     */
    public static final URI TERM_GROUP;


    /**
     * http://purl.org/vocab/vann/example<br>
     * "Example"<br>
     * A reference to a resource that provides an example of how this resource can be used.<br>
     */
    public static final URI EXAMPLE;


    /**
     * http://purl.org/vocab/vann/usageNote<br>
     * "Usage Note"<br>
     * A reference to a resource that provides information on how this resource is to be used.<br>
     */
    public static final URI USAGE_NOTE;


    /**
     * http://purl.org/vocab/vann/preferredNamespaceUri<br>
     * "Preferred Namespace Uri"<br>
     * The preferred namespace URI to use when using terms from this vocabulary in an XML document.<br>
     */
    public static final URI PREFERRED_NAMESPACE_URI;


    /**
     * http://purl.org/vocab/vann/changes<br>
     * "Changes"<br>
     * A reference to a resource that describes changes between this version of a vocabulary and the previous.<br>
     */
    public static final URI CHANGES;


    /**
     * http://purl.org/vocab/vann/preferredNamespacePrefix<br>
     * "Preferred Namespace Prefix"<br>
     * The preferred namespace prefix to use when using terms from this vocabulary in an XML document.<br>
     */
    public static final URI PREFERRED_NAMESPACE_PREFIX;


    static{
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();

        TERM_GROUP = valueFactory.createURI(NAMESPACE,"termGroup");
        EXAMPLE = valueFactory.createURI(NAMESPACE,"example");
        USAGE_NOTE = valueFactory.createURI(NAMESPACE,"usageNote");
        PREFERRED_NAMESPACE_URI = valueFactory.createURI(NAMESPACE,"preferredNamespaceUri");
        CHANGES = valueFactory.createURI(NAMESPACE,"changes");
        PREFERRED_NAMESPACE_PREFIX = valueFactory.createURI(NAMESPACE,"preferredNamespacePrefix");
    }
}
