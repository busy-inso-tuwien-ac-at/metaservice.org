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

package org.metaservice.demo.license;

import org.openrdf.model.*;
import org.openrdf.model.impl.*;



/**
 * This is an automatically generated class
 * Generator: org.metaservice.core.OntologyToJavaConverter
 * @see <a href="http://metaservice.org/ns/licensing#">lic</a>
 */
public class LIC{

    public static final String NAMESPACE = "http://metaservice.org/ns/licensing#";

    public static final String PREFIX = "lic";

    public static final Namespace NS = new NamespaceImpl(PREFIX, NAMESPACE);

////////////////////////
// CLASSES
////////////////////////


    /**
     * http://metaservice.org/ns/licensing#SuspectedViolation<br>
     */
    public static final URI SUSPECTED_VIOLATION;


////////////////////////
// OBJECT PROPERTIES
////////////////////////


    /**
     * http://metaservice.org/ns/licensing#licensingViolation<br>
     * "has violation"<br>
     */
    public static final URI LICENSING_VIOLATION;


    /**
     * http://metaservice.org/ns/licensing#conflicting<br>
     * "conflicting Dependency"<br>
     */
    public static final URI CONFLICTING;


////////////////////////
// DATA PROPERTIES
////////////////////////


    /**
     * http://metaservice.org/ns/licensing#description<br>
     * "explanation"<br>
     */
    public static final URI DESCRIPTION;


    static{
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();

        SUSPECTED_VIOLATION = valueFactory.createURI(NAMESPACE,"SuspectedViolation");
        LICENSING_VIOLATION = valueFactory.createURI(NAMESPACE,"licensingViolation");
        CONFLICTING = valueFactory.createURI(NAMESPACE,"conflicting");
        DESCRIPTION = valueFactory.createURI(NAMESPACE,"description");
    }
}
