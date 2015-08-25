package org.metaservice.api.rdf.vocabulary;

import org.openrdf.model.*;
import org.openrdf.model.impl.*;



/**
 * This is an automatically generated class
 * Generator: org.metaservice.core.OntologyToJavaConverter
 * @see <a href="http://metaservice.org/ns/metaservice#">ms</a>
 */
public class METASERVICE{

    public static final String NAMESPACE = "http://metaservice.org/ns/metaservice#";

    public static final String PREFIX = "ms";

    public static final Namespace NS = new NamespaceImpl(PREFIX, NAMESPACE);

////////////////////////
// CLASSES
////////////////////////


    /**
     * http://metaservice.org/ns/metaservice#Source<br>
     */
    public static final URI SOURCE;


    /**
     * http://metaservice.org/ns/metaservice#Generator<br>
     */
    public static final URI GENERATOR;


    /**
     * http://metaservice.org/ns/metaservice#View<br>
     */
    public static final URI VIEW;


    /**
     * http://metaservice.org/ns/metaservice#Provider<br>
     */
    public static final URI PROVIDER;


    /**
     * http://metaservice.org/ns/metaservice#Module<br>
     */
    public static final URI MODULE;


    /**
     * http://metaservice.org/ns/metaservice#AddObservation<br>
     */
    public static final URI ADD_OBSERVATION;


    /**
     * http://metaservice.org/ns/metaservice#Observation<br>
     * "Observation"<br>
     * Observations are named graphs, which contain information produced by a generator.<br>
     */
    public static final URI OBSERVATION;


    /**
     * http://metaservice.org/ns/metaservice#ContinuousObservation<br>
     */
    public static final URI CONTINUOUS_OBSERVATION;


    /**
     * http://metaservice.org/ns/metaservice#SourceRepository<br>
     */
    public static final URI SOURCE_REPOSITORY;


    /**
     * http://metaservice.org/ns/metaservice#RepositoryPath<br>
     */
    public static final URI REPOSITORY_PATH;


    /**
     * http://metaservice.org/ns/metaservice#RemoveObservation<br>
     */
    public static final URI REMOVE_OBSERVATION;


    /**
     * http://metaservice.org/ns/metaservice#Postprocessor<br>
     */
    public static final URI POSTPROCESSOR;


////////////////////////
// OBJECT PROPERTIES
////////////////////////


    /**
     * http://metaservice.org/ns/metaservice#source<br>
     * "retrieved from"<br>
     * Links an Observation to ithe source, where its statements were retrieved from.<br>
     */
    public static final URI SOURCE_PROPERTY;


    /**
     * http://metaservice.org/ns/metaservice#repository<br>
     */
    public static final URI REPOSITORY;


    /**
     * http://metaservice.org/ns/metaservice#generator<br>
     * "generator of data"<br>
     * Identifier of the software which was used to create the statements.<br>
     */
    public static final URI GENERATOR_PROPERTY;


    /**
     * http://metaservice.org/ns/metaservice#module<br>
     */
    public static final URI MODULE_PROPERTY;


    /**
     * http://metaservice.org/ns/metaservice#dummy<br>
     * "Dummy Statement"<br>
     * A dummy statement - used internally to denote empty results<br>
     */
    public static final URI DUMMY;


    /**
     * http://metaservice.org/ns/metaservice#authoritiveSubject<br>
     * "produced from"<br>
     * Resources which are processed together and are important for the time validity scope of the  observation.<br>
     */
    public static final URI AUTHORITIVE_SUBJECT;


////////////////////////
// DATA PROPERTIES
////////////////////////


    /**
     * http://metaservice.org/ns/metaservice#creationTime<br>
     * "created on"<br>
     * The time an Observation was created.<br>
     */
    public static final URI CREATION_TIME;


    /**
     * http://metaservice.org/ns/metaservice#id<br>
     * "id"<br>
     * metaservice id<br>
     */
    public static final URI ID;


    /**
     * http://metaservice.org/ns/metaservice#lastCheckedTime<br>
     * "last checked on"<br>
     * The last time the set of statements where checked for changes and found unchanged.<br>
     */
    public static final URI LAST_CHECKED_TIME;


    /**
     * http://metaservice.org/ns/metaservice#path<br>
     * "originates from Path"<br>
     * <br>
     */
    public static final URI PATH;


    /**
     * http://metaservice.org/ns/metaservice#dataTime<br>
     * "data source time"<br>
     * Time the described data was originally retrieved<br>
     */
    public static final URI DATA_TIME;


    /**
     * http://metaservice.org/ns/metaservice#latest<br>
     * "valid on date"<br>
     * valid for postprocessors. Calculated validity of the statements for the given date.<br>
     */
    public static final URI LATEST;


////////////////////////
// ANNOTATION PROPERTIES
////////////////////////


    /**
     * http://metaservice.org/ns/metaservice#view<br>
     * "displayable by template"<br>
     * Links a class to a metaservice.org template<br>
     */
    public static final URI VIEW_PROPERTY;


////////////////////////
// THINGS
////////////////////////


    /**
     * http://metaservice.org/ns/metaservice#Dummy<br>
     */
    public static final URI DUMMY_THING;


    static{
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();

        SOURCE = valueFactory.createURI(NAMESPACE,"Source");
        GENERATOR = valueFactory.createURI(NAMESPACE,"Generator");
        VIEW = valueFactory.createURI(NAMESPACE,"View");
        PROVIDER = valueFactory.createURI(NAMESPACE,"Provider");
        MODULE = valueFactory.createURI(NAMESPACE,"Module");
        ADD_OBSERVATION = valueFactory.createURI(NAMESPACE,"AddObservation");
        OBSERVATION = valueFactory.createURI(NAMESPACE,"Observation");
        CONTINUOUS_OBSERVATION = valueFactory.createURI(NAMESPACE,"ContinuousObservation");
        SOURCE_REPOSITORY = valueFactory.createURI(NAMESPACE,"SourceRepository");
        REPOSITORY_PATH = valueFactory.createURI(NAMESPACE,"RepositoryPath");
        REMOVE_OBSERVATION = valueFactory.createURI(NAMESPACE,"RemoveObservation");
        POSTPROCESSOR = valueFactory.createURI(NAMESPACE,"Postprocessor");
        SOURCE_PROPERTY = valueFactory.createURI(NAMESPACE,"source");
        REPOSITORY = valueFactory.createURI(NAMESPACE,"repository");
        GENERATOR_PROPERTY = valueFactory.createURI(NAMESPACE,"generator");
        MODULE_PROPERTY = valueFactory.createURI(NAMESPACE,"module");
        DUMMY = valueFactory.createURI(NAMESPACE,"dummy");
        AUTHORITIVE_SUBJECT = valueFactory.createURI(NAMESPACE,"authoritiveSubject");
        CREATION_TIME = valueFactory.createURI(NAMESPACE,"creationTime");
        ID = valueFactory.createURI(NAMESPACE,"id");
        LAST_CHECKED_TIME = valueFactory.createURI(NAMESPACE,"lastCheckedTime");
        PATH = valueFactory.createURI(NAMESPACE,"path");
        DATA_TIME = valueFactory.createURI(NAMESPACE,"dataTime");
        LATEST = valueFactory.createURI(NAMESPACE,"latest");
        VIEW_PROPERTY = valueFactory.createURI(NAMESPACE,"view");
        DUMMY_THING = valueFactory.createURI(NAMESPACE,"Dummy");
    }
}
