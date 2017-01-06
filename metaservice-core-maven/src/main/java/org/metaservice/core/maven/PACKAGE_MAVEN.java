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

package org.metaservice.core.maven;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

public class PACKAGE_MAVEN {
    private final static String NAMESPACE = "http://metaservice.org/ns/package-maven#";

    /**
     * CLASS
     */
    public final static URI PACKAGE;
    public final static URI GROUP_ID;
    public final static URI ARTIFACT_ID;
    public final static URI VERSION;
    public final static URI PACKAGING;
    public final static URI URL;
    public final static URI DEVELOPER;
    public final static URI REPOSITORY;
    public final static URI DESCRIPTION;
    public static final URI ORGANIZATION;
    public static final URI ORGANIZATION_URL;
    public static final URI CONTRIBUTOR;
    public static final URI PARENT;

    public static final URI CI_MANAGEMENT_URL;
    public static final URI CI_MANAGEMENT_SYSTEM;


    public static final URI ISSUE_MANAGEMENT_SYSTEM;
    public static final URI ISSUE_MANAGEMENT_URL;

    public static final URI LICENSE;
    public static final URI LICENSE_NAME;
    public static final URI LICENSE_URL;
    public static final URI DEPENDENCY_COMPILE_OPTIONAL;
    public static final URI DEPENDENCY_SYSTEM_OPTIONAL;
    public static final URI DEPENDENCY_PROVIDED_OPTIONAL;
    public static final URI DEPENDENCY_RUNTIME_OPTIONAL;
    public static final URI DEPENDENCY_TEST_OPTIONAL;
    public static final URI DEPENDENCY_COMPILE;
    public static final URI DEPENDENCY_SYSTEM;
    public static final URI DEPENDENCY_PROVIDED;
    public static final URI DEPENDENCY_RUNTIME;
    public static final URI DEPENDENCY_TEST;
    static {
        ValueFactory factory = ValueFactoryImpl.getInstance();
        PACKAGE = factory.createURI(PACKAGE_MAVEN.NAMESPACE,"Package");


        GROUP_ID = factory.createURI(PACKAGE_MAVEN.NAMESPACE,"groupId");
        ARTIFACT_ID = factory.createURI(PACKAGE_MAVEN.NAMESPACE,"artifactId");
        VERSION = factory.createURI(PACKAGE_MAVEN.NAMESPACE,"version");
        PACKAGING = factory.createURI(PACKAGE_MAVEN.NAMESPACE,"packaging");
        URL = factory.createURI(PACKAGE_MAVEN.NAMESPACE,"url");
        DEVELOPER = factory.createURI(PACKAGE_MAVEN.NAMESPACE,"developer");
        CONTRIBUTOR = factory.createURI(PACKAGE_MAVEN.NAMESPACE,"contributor");
        REPOSITORY = factory.createURI(PACKAGE_MAVEN.NAMESPACE,"repository");
        DESCRIPTION = factory.createURI(NAMESPACE,"description");
        ORGANIZATION = factory.createURI(NAMESPACE,"organization");
        ORGANIZATION_URL = factory.createURI(NAMESPACE,"organizationUrl");
        CI_MANAGEMENT_SYSTEM = factory.createURI(NAMESPACE,"ciManagementSystem");
        CI_MANAGEMENT_URL = factory.createURI(NAMESPACE,"ciManagementUrl");
        PARENT = factory.createURI(NAMESPACE,"parent");
        ISSUE_MANAGEMENT_SYSTEM = factory.createURI(NAMESPACE,"issueManagementSystem");
        ISSUE_MANAGEMENT_URL = factory.createURI(NAMESPACE,"issueManagementUrl");

        LICENSE = factory.createURI(NAMESPACE,"license");
        LICENSE_NAME = factory.createURI(NAMESPACE,"licenseName");
        LICENSE_URL = factory.createURI(NAMESPACE,"licenseUrl");

        DEPENDENCY_COMPILE_OPTIONAL = factory.createURI(NAMESPACE,"dependencyCompileOptional");
        DEPENDENCY_SYSTEM_OPTIONAL = factory.createURI(NAMESPACE,"dependencySystemOptional");
        DEPENDENCY_PROVIDED_OPTIONAL = factory.createURI(NAMESPACE,"dependencyProvidedOptional");
        DEPENDENCY_RUNTIME_OPTIONAL = factory.createURI(NAMESPACE,"dependencyRuntimeOptional");
        DEPENDENCY_TEST_OPTIONAL = factory.createURI(NAMESPACE,"dependencyTestOptional");
        DEPENDENCY_COMPILE = factory.createURI(NAMESPACE,"dependencyCompile");
        DEPENDENCY_SYSTEM = factory.createURI(NAMESPACE,"dependencySystem");
        DEPENDENCY_PROVIDED = factory.createURI(NAMESPACE,"dependencyProvided");
        DEPENDENCY_RUNTIME = factory.createURI(NAMESPACE,"dependencyRuntime");
        DEPENDENCY_TEST = factory.createURI(NAMESPACE,"dependencyTest");
    }

}
