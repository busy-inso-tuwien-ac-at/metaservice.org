package org.metaservice.api.rdf.vocabulary;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 * Created by ilo on 16.02.14.
 */
public class METASERVICE_SWDEP {
    public static final String NAMESPACE = "http://metaservice.org/ns/metaservice-swdep#";

    public static final URI DEPENDENCY_RELATION;
    public static final URI TYPE_DEPENDENCY_RELATION;
    public static final URI EXPLICIT_DEPENDENCY_RELATION;
    public static final URI INCLUDED_DEPENDENCY_RELATION;
    public static final URI ABSTRACT_DEPENDENCY_RELATION;
    public static final URI IMPORTANCE_DEPENDENCY_RELATION;
    public static final URI REQUIRED_DEPENDENCY_RELATION;
    public static final URI OPTIONAL_DEPENDENCY_RELATION;
    public static final URI STAGE_DEPENDENCY_RELATION;
    public static final URI BUILD_DEPENDENCY_RELATION;
    public static final URI RUNTIME_DEPENDENCY_RELATION;
    public static final URI TESTING_DEPENDENCY_RELATION;
    public static final URI USAGE_DEPENDENCY_RELATION;
    public static final URI STANDALONE_DEPENDENCY_RELATION;
    public static final URI MIDDLEWAREBASED_DEPENDENCY_RELATION;
    public static final URI PLUGIN_DEPENDENCY_RELATION;
    public static final URI LINKABLELIBRARY_DEPENDENCY_RELATION;
    public static final URI HW;
    public static final URI HARDWARE_DEPENDENCY_RELATION;
    public static final URI INCOMPATIBLE_WITH_DEPENDENCY_RELATION;
    public static final URI ARCHITECTURE_DEPENDENCY_RELATION;
    public static final URI COMPILER_DEPENDENCY_RELATION;
    public static final URI INTERPRETER_DEPENDENCY_RELATION;
    public static final URI SW;
    public static final URI ANY_ONE_OF_SW;
    public static final URI ANY_ONE_OF_HW;
    public static final URI VERSION_DEPENDENCY_RELATION;
    public static final URI EXACT_VERSION_DEPENDENCY_RELATION;
    public static final URI LATER_THAN_VERSION_DEPENDENCY_RELATION;
    public static final URI PRIOR_TO_VERSION_DEPENDENCY_RELATION;
    public static final URI LATER_THAN_OR_EQUAL_VERSION_DEPENDENCY_RELATION;
    public static final URI PRIOR_TO_OR_EQUAL_VERSION_DEPENDENCY_RELATION;


    public static final URI DEPEND_ON;
    public static final URI DEPENDENCY_OF;
    public static final URI VERSION;


    static {
        ValueFactory factory = ValueFactoryImpl.getInstance();
        //CLASSES
        DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"Dependency_Relation");
        TYPE_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"TypeDependency_Relation");
        EXPLICIT_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"Explicit_Dependency_Relation");
        INCLUDED_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"IncludedDependency_Relation");
        ABSTRACT_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"Abstract_Dependency_Relation");
        IMPORTANCE_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"Importance_Dependency_Relation");
        REQUIRED_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"Required_Dependency_Relation");
        OPTIONAL_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"Optional_Dependency_Relation");
        STAGE_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"Stage_Dependency_Relation");
        BUILD_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"Build_Dependency_Relation");
        RUNTIME_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"Run-Time_Dependency_Relation");
        TESTING_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"Testing_Dependency_Relation");
        USAGE_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"Usage_Dependency_Relation");
        STANDALONE_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"Stand-alone_Dependency_Relation");
        MIDDLEWAREBASED_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"Middleware-based_Dependency_Relation");
        PLUGIN_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"Plug-in_Dependency_Relation");
        LINKABLELIBRARY_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"LinkableLibrary_Dependency_Relation");
        HW = factory.createURI(NAMESPACE,"HW");
        HARDWARE_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"HardwareDependency_Relation");
        INCOMPATIBLE_WITH_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"IncompatibleWith_Relation");
        ARCHITECTURE_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"ArchitectureDependency_Relation");
        COMPILER_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"CompilerDependency_Relation");
        INTERPRETER_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"InterpreterDependency_Relation");
        SW = factory.createURI(NAMESPACE,"SW");
        ANY_ONE_OF_SW = factory.createURI(NAMESPACE,"AnyOneOfSW");
        ANY_ONE_OF_HW = factory.createURI(NAMESPACE,"AnyOneOfHW");
        VERSION_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"VersionDependency_Relation");
        EXACT_VERSION_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"ExactVersionDependency_Relation");
        LATER_THAN_VERSION_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"LaterThanVersionDependency_Relation");
        PRIOR_TO_VERSION_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"PriorToVersionDependency_Relation");
        LATER_THAN_OR_EQUAL_VERSION_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"LaterThanOrEqualVersionDependency_Relation");
        PRIOR_TO_OR_EQUAL_VERSION_DEPENDENCY_RELATION = factory.createURI(NAMESPACE,"PriorToOrEqualVersionDependency_Relation");

        VERSION = factory.createURI(NAMESPACE,"version");
        DEPEND_ON = factory.createURI(NAMESPACE,"dependOn");
        DEPENDENCY_OF = factory.createURI(NAMESPACE,"dependencyOf");
    }
}
