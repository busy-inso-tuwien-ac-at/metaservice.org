/*
 * Copyright 2015 Nikola Ilo
 * Research Group for Industrial Software, Vienna University of Technology, Austria
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

import org.jetbrains.annotations.NotNull;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 * Created by ilo on 25.02.14.
 */
public class CPE {

    public static final String NS ="http://metaservice.org/ns/cpe#";
    public static final URI CPE;
    public static final URI DEPRECATED;
    public static final URI NOTE;
    public static final URI CHECK;
    public static final URI CHECK_VALUE;
    public static final URI CHECK_SYSTEM;
    public static final URI CHECK_HREF;
    public static final URI NAME;
    public static final URI DEPRECATION_DATE;
    public static final URI PART;
    public static final URI VENDOR;
    public static final URI PRODUCT;
    public static final URI VERSION;
    public static final URI UPDATE;
    public static final URI EDITION;
    public static final URI LANGUAGE;
    public static final URI SW_EDITION;
    public static final URI TARGET_SW;
    public static final URI TARGET_HW;
    public static final URI OTHER;
    public static final URI REFERENCE;
    public static final URI REFERENCE_HREF;
    public static final URI REFERENCE_VALUE;


    /* CUSTOM METASERVICE */
    public static final URI REFERENCED_BY;
    public static final URI IS_ABOUT;
    public static final URI IS_ABOUT_REVERSE;


    static {
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();
        CPE = valueFactory.createURI(NS,"CPE");
        DEPRECATED = valueFactory.createURI(NS,"deprecated");
        DEPRECATION_DATE = valueFactory.createURI(NS,"deprecationDate");
        NOTE = valueFactory.createURI(NS,"note");
        CHECK = valueFactory.createURI(NS,"check");
        CHECK_VALUE = valueFactory.createURI(NS,"checkValue");
        CHECK_SYSTEM = valueFactory.createURI(NS,"checkSystem");
        CHECK_HREF = valueFactory.createURI(NS,"checkHref");
        NAME = valueFactory.createURI(NS,"name");
        REFERENCED_BY = valueFactory.createURI(NS,"referencedBy");
        PART          = valueFactory.createURI(NS,"part");
        VENDOR        = valueFactory.createURI(NS,"vendor");
        PRODUCT       = valueFactory.createURI(NS,"product");
        VERSION       = valueFactory.createURI(NS,"version");
        UPDATE = valueFactory.createURI(NS,"update");
        EDITION = valueFactory.createURI(NS,"edition");
        LANGUAGE = valueFactory.createURI(NS,"language");
        SW_EDITION = valueFactory.createURI(NS,"swEdition");
        TARGET_SW = valueFactory.createURI(NS,"targetSw");
        TARGET_HW = valueFactory.createURI(NS,"targetHw");
        OTHER = valueFactory.createURI(NS,"other");
        REFERENCE = valueFactory.createURI(NS,"reference");
        REFERENCE_HREF = valueFactory.createURI(NS,"referenceHref");
        REFERENCE_VALUE = valueFactory.createURI(NS,"referenceValue");

        IS_ABOUT = valueFactory.createURI(NS,"isAbout");
        IS_ABOUT_REVERSE = valueFactory.createURI(NS,"isAboutReverse");
    }

    private static final String LOCAL_NS ="http://metaservice.org/d/releases/cpe/";

    @NotNull
    public static URI getById(@NotNull String id){
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();
        return valueFactory.createURI(LOCAL_NS,id);
    }
}
