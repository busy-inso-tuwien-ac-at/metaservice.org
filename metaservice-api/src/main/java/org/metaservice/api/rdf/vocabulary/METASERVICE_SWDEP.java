package org.metaservice.api.rdf.vocabulary;

import org.openrdf.model.*;
import org.openrdf.model.impl.*;



/**
 * This is an automatically generated class
 * Generator: org.metaservice.core.OntologyToJavaConverter
 * @see <a href="http://metaservice.org/ns/metaservice-swdep#">swdep</a>
 */
public class METASERVICE_SWDEP{

    public static final String NAMESPACE = "http://metaservice.org/ns/metaservice-swdep#";

    public static final String PREFIX = "swdep";

    public static final Namespace NS = new NamespaceImpl(PREFIX, NAMESPACE);

////////////////////////
// CLASSES
////////////////////////


    /**
     * http://metaservice.org/ns/metaservice-swdep#Software<br>
     * "Abstract Software"<br>
     * A very abstract term about Software.<br>
     */
    public static final URI SOFTWARE;


    /**
     * http://metaservice.org/ns/metaservice-swdep#Hardware<br>
     * "Abstract Hardware"<br>
     * A very abstract term about Hardware.<br>
     */
    public static final URI HARDWARE;


    /**
     * http://metaservice.org/ns/metaservice-swdep#AnyOneOfSoftware<br>
     * ""<br>
     * <br>
     */
    public static final URI ANY_ONE_OF_SOFTWARE;


    /**
     * http://metaservice.org/ns/metaservice-swdep#AnyOneOfHardware<br>
     * ""<br>
     * <br>
     */
    public static final URI ANY_ONE_OF_HARDWARE;


    /**
     * http://metaservice.org/ns/metaservice-swdep#AllOfSoftware<br>
     * ""<br>
     * This is probably only useful in combination with Software, because the default semantics is already conjunctive.<br>
     */
    public static final URI ALL_OF_SOFTWARE;


    /**
     * http://metaservice.org/ns/metaservice-swdep#SoftwareRange<br>
     * ""<br>
     * This is used to describe a range of Software, with specific properties.<br>
     */
    public static final URI SOFTWARE_RANGE;


    /**
     * http://metaservice.org/ns/metaservice-swdep#AllOfHardware<br>
     * ""<br>
     * <br>
     */
    public static final URI ALL_OF_HARDWARE;


////////////////////////
// OBJECT PROPERTIES
////////////////////////


    /**
     * http://metaservice.org/ns/metaservice-swdep#depends<br>
     * "depends on"<br>
     * <br>
     */
    public static final URI DEPENDS;


    /**
     * http://metaservice.org/ns/metaservice-swdep#antiDepends<br>
     * "depends on absence of"<br>
     * This is the inverted dependency. To state this it is intentionally not a subproperty of "depends".<br>
     *             Nonetheless this may be used as a superproperty in combination with the other dependency types to denote the negation. In conclusion it is intended that a property is included both as a dependency and antidependency.<br>
     *             The expressiveness of this ontology currently does not allow to detect contradictions which occur when both are intentionally added.<br>
     */
    public static final URI ANTI_DEPENDS;


    /**
     * http://metaservice.org/ns/metaservice-swdep#abstractTypeDependencyProperty<br>
     * An abstract intermediate property, which is only used to structure the ontology.<br>
     */
    public static final URI ABSTRACT_TYPE_DEPENDENCY_PROPERTY;


    /**
     * http://metaservice.org/ns/metaservice-swdep#dependsBuild<br>
     * "depend during built-time on"<br>
     * <br>
     */
    public static final URI DEPENDS_BUILD;


    /**
     * http://metaservice.org/ns/metaservice-swdep#dependsInterpreter<br>
     * "depend on interpreter"<br>
     * <br>
     */
    public static final URI DEPENDS_INTERPRETER;


    /**
     * http://metaservice.org/ns/metaservice-swdep#links<br>
     * "links to"<br>
     * <br>
     */
    public static final URI LINKS;


    /**
     * http://metaservice.org/ns/metaservice-swdep#relatedSoftware<br>
     * "depends on"<br>
     * abstract concept of relation to software<br>
     */
    public static final URI RELATED_SOFTWARE;


    /**
     * http://metaservice.org/ns/metaservice-swdep#dependsStandalone<br>
     * "is independent of"<br>
     * Software runs on its own. If communication happens, than only through weakly typed communication channels, which may be sockets or pipes.<br>
     */
    public static final URI DEPENDS_STANDALONE;


    /**
     * http://metaservice.org/ns/metaservice-swdep#dependsService<br>
     * "uses the service"<br>
     * Software connects and uses a service.<br>
     */
    public static final URI DEPENDS_SERVICE;


    /**
     * http://metaservice.org/ns/metaservice-swdep#dependsTest<br>
     * "depend during built-time on"<br>
     * <br>
     */
    public static final URI DEPENDS_TEST;


    /**
     * http://metaservice.org/ns/metaservice-swdep#implements<br>
     * "implementation of"<br>
     * connects a implementation with its specification or interface<br>
     */
    public static final URI IMPLEMENTS;


    /**
     * http://metaservice.org/ns/metaservice-swdep#dependsRuntime<br>
     * "depends during runtime on"<br>
     * <br>
     */
    public static final URI DEPENDS_RUNTIME;


    /**
     * http://metaservice.org/ns/metaservice-swdep#abstractStageDependencyProperty<br>
     * An abstract intermediate property, which is only used to structure the ontology.<br>
     *             Sub-properties of this state in which stage of a software lifetime the dependency is needed.<br>
     */
    public static final URI ABSTRACT_STAGE_DEPENDENCY_PROPERTY;


    /**
     * http://metaservice.org/ns/metaservice-swdep#releaseConstraint<br>
     * ""<br>
     * <br>
     */
    public static final URI RELEASE_CONSTRAINT;


    /**
     * http://metaservice.org/ns/metaservice-swdep#related<br>
     * "related to"<br>
     * abstract concept of relation<br>
     */
    public static final URI RELATED;


    /**
     * http://metaservice.org/ns/metaservice-swdep#optional<br>
     * "is optionally dependent on"<br>
     * <br>
     */
    public static final URI OPTIONAL;


    /**
     * http://metaservice.org/ns/metaservice-swdep#requires<br>
     * "is necessarily dependent on"<br>
     * <br>
     */
    public static final URI REQUIRES;


    /**
     * http://metaservice.org/ns/metaservice-swdep#abstractUsageDependencyProperty<br>
     * An abstract property, which is only used to structure the ontology.<br>
     */
    public static final URI ABSTRACT_USAGE_DEPENDENCY_PROPERTY;


    /**
     * http://metaservice.org/ns/metaservice-swdep#projectConstraint<br>
     * ""<br>
     * is from Project<br>
     */
    public static final URI PROJECT_CONSTRAINT;


    /**
     * http://metaservice.org/ns/metaservice-swdep#pluginOf<br>
     * "is a plugin of"<br>
     * Software is incorporated in form of a Plugin.<br>
     */
    public static final URI PLUGIN_OF;


    /**
     * http://metaservice.org/ns/metaservice-swdep#dependsCompiler<br>
     * "depend during built-time on"<br>
     * <br>
     */
    public static final URI DEPENDS_COMPILER;


    /**
     * http://metaservice.org/ns/metaservice-swdep#dependsMiddleware<br>
     * "runs using middleware"<br>
     * Software uses the linked middleware.<br>
     */
    public static final URI DEPENDS_MIDDLEWARE;


    /**
     * http://metaservice.org/ns/metaservice-swdep#dependsSoftware<br>
     * "depends on software"<br>
     * <br>
     */
    public static final URI DEPENDS_SOFTWARE;


    /**
     * http://metaservice.org/ns/metaservice-swdep#binary<br>
     * "has binary"<br>
     * Software usually exists either in source or binary format. In case there exists a binary for a source, it may be linked through this.<br>
     */
    public static final URI BINARY;


    /**
     * http://metaservice.org/ns/metaservice-swdep#source<br>
     * "has source"<br>
     * Software usually exists either in source or binary format. In case there exists a source for a binary, it may be linked through this.<br>
     */
    public static final URI SOURCE;


    /**
     * http://metaservice.org/ns/metaservice-swdep#distributionConstraint<br>
     * ""<br>
     * <br>
     */
    public static final URI DISTRIBUTION_CONSTRAINT;


    /**
     * http://metaservice.org/ns/metaservice-swdep#dependsInstallation<br>
     * "depend during installation-time on"<br>
     * <br>
     */
    public static final URI DEPENDS_INSTALLATION;


    /**
     * http://metaservice.org/ns/metaservice-swdep#abstractImportanceDependencyProperty<br>
     * An abstract property, which is only used to structure the ontology.<br>
     */
    public static final URI ABSTRACT_IMPORTANCE_DEPENDENCY_PROPERTY;


    /**
     * http://metaservice.org/ns/metaservice-swdep#forkOf<br>
     * "fork of"<br>
     * connects Assets with a common development ancestor<br>
     */
    public static final URI FORK_OF;


////////////////////////
// DATA PROPERTIES
////////////////////////


    /**
     * http://metaservice.org/ns/metaservice-swdep#revisionConstraint<br>
     * ""<br>
     * This is used to describe a range of Software, with specific properties.<br>
     */
    public static final URI REVISION_CONSTRAINT;


    static{
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();

        SOFTWARE = valueFactory.createURI(NAMESPACE,"Software");
        HARDWARE = valueFactory.createURI(NAMESPACE,"Hardware");
        ANY_ONE_OF_SOFTWARE = valueFactory.createURI(NAMESPACE,"AnyOneOfSoftware");
        ANY_ONE_OF_HARDWARE = valueFactory.createURI(NAMESPACE,"AnyOneOfHardware");
        ALL_OF_SOFTWARE = valueFactory.createURI(NAMESPACE,"AllOfSoftware");
        SOFTWARE_RANGE = valueFactory.createURI(NAMESPACE,"SoftwareRange");
        ALL_OF_HARDWARE = valueFactory.createURI(NAMESPACE,"AllOfHardware");
        DEPENDS = valueFactory.createURI(NAMESPACE,"depends");
        ANTI_DEPENDS = valueFactory.createURI(NAMESPACE,"antiDepends");
        ABSTRACT_TYPE_DEPENDENCY_PROPERTY = valueFactory.createURI(NAMESPACE,"abstractTypeDependencyProperty");
        DEPENDS_BUILD = valueFactory.createURI(NAMESPACE,"dependsBuild");
        DEPENDS_INTERPRETER = valueFactory.createURI(NAMESPACE,"dependsInterpreter");
        LINKS = valueFactory.createURI(NAMESPACE,"links");
        RELATED_SOFTWARE = valueFactory.createURI(NAMESPACE,"relatedSoftware");
        DEPENDS_STANDALONE = valueFactory.createURI(NAMESPACE,"dependsStandalone");
        DEPENDS_SERVICE = valueFactory.createURI(NAMESPACE,"dependsService");
        DEPENDS_TEST = valueFactory.createURI(NAMESPACE,"dependsTest");
        IMPLEMENTS = valueFactory.createURI(NAMESPACE,"implements");
        DEPENDS_RUNTIME = valueFactory.createURI(NAMESPACE,"dependsRuntime");
        ABSTRACT_STAGE_DEPENDENCY_PROPERTY = valueFactory.createURI(NAMESPACE,"abstractStageDependencyProperty");
        RELEASE_CONSTRAINT = valueFactory.createURI(NAMESPACE,"releaseConstraint");
        RELATED = valueFactory.createURI(NAMESPACE,"related");
        OPTIONAL = valueFactory.createURI(NAMESPACE,"optional");
        REQUIRES = valueFactory.createURI(NAMESPACE,"requires");
        ABSTRACT_USAGE_DEPENDENCY_PROPERTY = valueFactory.createURI(NAMESPACE,"abstractUsageDependencyProperty");
        PROJECT_CONSTRAINT = valueFactory.createURI(NAMESPACE,"projectConstraint");
        PLUGIN_OF = valueFactory.createURI(NAMESPACE,"pluginOf");
        DEPENDS_COMPILER = valueFactory.createURI(NAMESPACE,"dependsCompiler");
        DEPENDS_MIDDLEWARE = valueFactory.createURI(NAMESPACE,"dependsMiddleware");
        DEPENDS_SOFTWARE = valueFactory.createURI(NAMESPACE,"dependsSoftware");
        BINARY = valueFactory.createURI(NAMESPACE,"binary");
        SOURCE = valueFactory.createURI(NAMESPACE,"source");
        DISTRIBUTION_CONSTRAINT = valueFactory.createURI(NAMESPACE,"distributionConstraint");
        DEPENDS_INSTALLATION = valueFactory.createURI(NAMESPACE,"dependsInstallation");
        ABSTRACT_IMPORTANCE_DEPENDENCY_PROPERTY = valueFactory.createURI(NAMESPACE,"abstractImportanceDependencyProperty");
        FORK_OF = valueFactory.createURI(NAMESPACE,"forkOf");
        REVISION_CONSTRAINT = valueFactory.createURI(NAMESPACE,"revisionConstraint");
    }
}
