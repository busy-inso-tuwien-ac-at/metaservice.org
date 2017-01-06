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
 * @see <a href="http://metaservice.org/ns/deb#">deb</a>
 */
public class DEB{

    public static final String NAMESPACE = "http://metaservice.org/ns/deb#";

    public static final String PREFIX = "deb";

    public static final Namespace NS = new NamespaceImpl(PREFIX, NAMESPACE);

////////////////////////
// CLASSES
////////////////////////


    /**
     * http://metaservice.org/ns/deb#Uploader<br>
     * ""<br>
     * <br>
     */
    public static final URI UPLOADER;


    /**
     * http://metaservice.org/ns/deb#Project<br>
     * ""<br>
     * <br>
     */
    public static final URI PROJECT;


    /**
     * http://metaservice.org/ns/deb#Package<br>
     * ""<br>
     * <br>
     */
    public static final URI PACKAGE;


    /**
     * http://metaservice.org/ns/deb#DebArchive<br>
     * ""<br>
     * <br>
     */
    public static final URI DEB_ARCHIVE;


    /**
     * http://metaservice.org/ns/deb#Release<br>
     * ""<br>
     * <br>
     */
    public static final URI RELEASE;


    /**
     * http://metaservice.org/ns/deb#DebianRepository<br>
     * ""<br>
     * <br>
     */
    public static final URI DEBIAN_REPOSITORY;


    /**
     * http://metaservice.org/ns/deb#Maintainer<br>
     * ""<br>
     * <br>
     */
    public static final URI MAINTAINER;


    /**
     * http://metaservice.org/ns/deb#DebSourceArchive<br>
     * ""<br>
     * <br>
     */
    public static final URI DEB_SOURCE_ARCHIVE;


////////////////////////
// OBJECT PROPERTIES
////////////////////////


    /**
     * http://metaservice.org/ns/deb#builtUsing<br>
     * ""<br>
     * <br>
     */
    public static final URI BUILT_USING;


    /**
     * http://metaservice.org/ns/deb#conflicts<br>
     * "is in Conflict with"<br>
     * <br>
     */
    public static final URI CONFLICTS;


    /**
     * http://metaservice.org/ns/deb#buildDependsIndep<br>
     * ""<br>
     * <br>
     */
    public static final URI BUILD_DEPENDS_INDEP;


    /**
     * http://metaservice.org/ns/deb#md5sum<br>
     * ""<br>
     * <br>
     */
    public static final URI MD5SUM;


    /**
     * http://metaservice.org/ns/deb#source<br>
     * ""<br>
     * <br>
     */
    public static final URI SOURCE;


    /**
     * http://metaservice.org/ns/deb#depends<br>
     * ""<br>
     * <br>
     */
    public static final URI DEPENDS;


    /**
     * http://metaservice.org/ns/deb#recommends<br>
     * ""<br>
     * <br>
     */
    public static final URI RECOMMENDS;


    /**
     * http://metaservice.org/ns/deb#uploader<br>
     * ""<br>
     * <br>
     */
    public static final URI UPLOADER_PROPERTY;


    /**
     * http://metaservice.org/ns/deb#suggests<br>
     * ""<br>
     * <br>
     */
    public static final URI SUGGESTS;


    /**
     * http://metaservice.org/ns/deb#replaces<br>
     * ""<br>
     * <br>
     */
    public static final URI REPLACES;


    /**
     * http://metaservice.org/ns/deb#breaks<br>
     * ""<br>
     * <br>
     */
    public static final URI BREAKS;


    /**
     * http://metaservice.org/ns/deb#sha256sum<br>
     * ""<br>
     * <br>
     */
    public static final URI SHA256SUM;


    /**
     * http://metaservice.org/ns/deb#buildConflicts<br>
     * ""<br>
     * <br>
     */
    public static final URI BUILD_CONFLICTS;


    /**
     * http://metaservice.org/ns/deb#provides<br>
     * ""<br>
     * <br>
     */
    public static final URI PROVIDES;


    /**
     * http://metaservice.org/ns/deb#preDepends<br>
     * ""<br>
     * <br>
     */
    public static final URI PRE_DEPENDS;


    /**
     * http://metaservice.org/ns/deb#homepage<br>
     * ""<br>
     * <br>
     */
    public static final URI HOMEPAGE;


    /**
     * http://metaservice.org/ns/deb#maintainer<br>
     * ""<br>
     * <br>
     */
    public static final URI MAINTAINER_PROPERTY;


    /**
     * http://metaservice.org/ns/deb#buildConflictsIndep<br>
     * ""<br>
     * <br>
     */
    public static final URI BUILD_CONFLICTS_INDEP;


    /**
     * http://metaservice.org/ns/deb#buildDepends<br>
     * ""<br>
     * <br>
     */
    public static final URI BUILD_DEPENDS;


    /**
     * http://metaservice.org/ns/deb#sha1sum<br>
     * ""<br>
     * <br>
     */
    public static final URI SHA1SUM;


////////////////////////
// DATA PROPERTIES
////////////////////////


    /**
     * http://metaservice.org/ns/deb#version<br>
     * ""<br>
     * <br>
     */
    public static final URI VERSION;


    /**
     * http://metaservice.org/ns/deb#meta-distribution<br>
     * ""<br>
     * <br>
     */
    public static final URI META_DISTRIBUTION;


    /**
     * http://metaservice.org/ns/deb#description<br>
     * ""<br>
     * <br>
     */
    public static final URI DESCRIPTION;


    /**
     * http://metaservice.org/ns/deb#architecture<br>
     * ""<br>
     * <br>
     */
    public static final URI ARCHITECTURE;


    /**
     * http://metaservice.org/ns/deb#filename<br>
     * ""<br>
     * <br>
     */
    public static final URI FILENAME;


    /**
     * http://metaservice.org/ns/deb#distribution<br>
     * ""<br>
     * <br>
     */
    public static final URI DISTRIBUTION;


    /**
     * http://metaservice.org/ns/deb#distributionVersion<br>
     * ""<br>
     * <br>
     */
    public static final URI DISTRIBUTION_VERSION;


    /**
     * http://metaservice.org/ns/deb#packageName<br>
     * ""<br>
     * <br>
     */
    public static final URI PACKAGE_NAME;


    static{
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();

        UPLOADER = valueFactory.createURI(NAMESPACE,"Uploader");
        PROJECT = valueFactory.createURI(NAMESPACE,"Project");
        PACKAGE = valueFactory.createURI(NAMESPACE,"Package");
        DEB_ARCHIVE = valueFactory.createURI(NAMESPACE,"DebArchive");
        RELEASE = valueFactory.createURI(NAMESPACE,"Release");
        DEBIAN_REPOSITORY = valueFactory.createURI(NAMESPACE,"DebianRepository");
        MAINTAINER = valueFactory.createURI(NAMESPACE,"Maintainer");
        DEB_SOURCE_ARCHIVE = valueFactory.createURI(NAMESPACE,"DebSourceArchive");
        BUILT_USING = valueFactory.createURI(NAMESPACE,"builtUsing");
        CONFLICTS = valueFactory.createURI(NAMESPACE,"conflicts");
        BUILD_DEPENDS_INDEP = valueFactory.createURI(NAMESPACE,"buildDependsIndep");
        MD5SUM = valueFactory.createURI(NAMESPACE,"md5sum");
        SOURCE = valueFactory.createURI(NAMESPACE,"source");
        DEPENDS = valueFactory.createURI(NAMESPACE,"depends");
        RECOMMENDS = valueFactory.createURI(NAMESPACE,"recommends");
        UPLOADER_PROPERTY = valueFactory.createURI(NAMESPACE,"uploader");
        SUGGESTS = valueFactory.createURI(NAMESPACE,"suggests");
        REPLACES = valueFactory.createURI(NAMESPACE,"replaces");
        BREAKS = valueFactory.createURI(NAMESPACE,"breaks");
        SHA256SUM = valueFactory.createURI(NAMESPACE,"sha256sum");
        BUILD_CONFLICTS = valueFactory.createURI(NAMESPACE,"buildConflicts");
        PROVIDES = valueFactory.createURI(NAMESPACE,"provides");
        PRE_DEPENDS = valueFactory.createURI(NAMESPACE,"preDepends");
        HOMEPAGE = valueFactory.createURI(NAMESPACE,"homepage");
        MAINTAINER_PROPERTY = valueFactory.createURI(NAMESPACE,"maintainer");
        BUILD_CONFLICTS_INDEP = valueFactory.createURI(NAMESPACE,"buildConflictsIndep");
        BUILD_DEPENDS = valueFactory.createURI(NAMESPACE,"buildDepends");
        SHA1SUM = valueFactory.createURI(NAMESPACE,"sha1sum");
        VERSION = valueFactory.createURI(NAMESPACE,"version");
        META_DISTRIBUTION = valueFactory.createURI(NAMESPACE,"meta-distribution");
        DESCRIPTION = valueFactory.createURI(NAMESPACE,"description");
        ARCHITECTURE = valueFactory.createURI(NAMESPACE,"architecture");
        FILENAME = valueFactory.createURI(NAMESPACE,"filename");
        DISTRIBUTION = valueFactory.createURI(NAMESPACE,"distribution");
        DISTRIBUTION_VERSION = valueFactory.createURI(NAMESPACE,"distributionVersion");
        PACKAGE_NAME = valueFactory.createURI(NAMESPACE,"packageName");
    }
}
