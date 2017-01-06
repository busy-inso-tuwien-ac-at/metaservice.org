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
 * @see <a href="http://purl.org/ontology/prv/core#">prv</a>
 */
public class PRV{

    public static final String NAMESPACE = "http://purl.org/ontology/prv/core#";

    public static final String PREFIX = "prv";

    public static final Namespace NS = new NamespaceImpl(PREFIX, NAMESPACE);

////////////////////////
// CLASSES
////////////////////////


    /**
     * http://purl.org/ontology/prv/core#PropertyReification<br>
     * "Property Reification"<br>
     * The class, which describes the relations of a property reification. That means, its "shortcut relation", its <br>
     * reification class, and the properties that are relating to the subject and object of the "shortcut relation".<br>
     */
    public static final URI PROPERTY_REIFICATION;


////////////////////////
// PROPERTIES
////////////////////////


    /**
     * http://purl.org/ontology/prv/core#subject_property<br>
     * "has subject property"<br>
     * Relates to the property of the reification class, which relates to the subject of the "shortcut relation".<br>
     */
    public static final URI SUBJECT_PROPERTY;


    /**
     * http://purl.org/ontology/prv/core#shortcut_property<br>
     * "has shortcut property"<br>
     * Relates to the property of the reification class,  which relates to the predicate of the "shortcut relation". So <br>
     * that the specific property reification can be "verified" (the property of the prv:shortcut relation and this one associated by the <br>
     * referred shortcut property should be equal).<br>
     */
    public static final URI SHORTCUT_PROPERTY;


    /**
     * http://purl.org/ontology/prv/core#reified<br>
     * "reified"<br>
     * This property relates an statement identifier of a shortcut relation with an instance of a reification class. Although, this is at the moment not really applicable, because it requires a notation for statement identifier as optional fourth element of a tuple; that means, triple + statement identifier.<br>
     */
    public static final URI REIFIED;


    /**
     * http://purl.org/ontology/prv/core#shortcut<br>
     * "has shortcut"<br>
     * Relates to the property of the "shortcut relation" (its predicate).<br>
     */
    public static final URI SHORTCUT;


    /**
     * http://purl.org/ontology/prv/core#reification_class<br>
     * "has reification class"<br>
     * Relates to the reification class, which can be related to the object and subject property to be able to associate the <br>
     * object and subject of the "shortcut relation". The reification class should provide detailed descriptions of the relationship that is <br>
     * described in a simple form by the "shortcut relation".<br>
     */
    public static final URI REIFICATION_CLASS;


    /**
     * http://purl.org/ontology/prv/core#object_property<br>
     * "has object property"<br>
     * Relates to the property of the reification class, which relates to the object of the "shortcut relation".<br>
     */
    public static final URI OBJECT_PROPERTY;


    static{
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();

        PROPERTY_REIFICATION = valueFactory.createURI(NAMESPACE,"PropertyReification");
        SUBJECT_PROPERTY = valueFactory.createURI(NAMESPACE,"subject_property");
        SHORTCUT_PROPERTY = valueFactory.createURI(NAMESPACE,"shortcut_property");
        REIFIED = valueFactory.createURI(NAMESPACE,"reified");
        SHORTCUT = valueFactory.createURI(NAMESPACE,"shortcut");
        REIFICATION_CLASS = valueFactory.createURI(NAMESPACE,"reification_class");
        OBJECT_PROPERTY = valueFactory.createURI(NAMESPACE,"object_property");
    }
}
