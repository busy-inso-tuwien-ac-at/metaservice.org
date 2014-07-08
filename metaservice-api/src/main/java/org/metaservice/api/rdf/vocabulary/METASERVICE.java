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
     * http://metaservice.org/ns/metaservice#Metadata<br>
     * "Metadata"<br>
     * Named graph in the metaservice.org database.<br>
     */
    public static final URI METADATA;


////////////////////////
// OBJECT PROPERTIES
////////////////////////


    /**
     * http://metaservice.org/ns/metaservice#sourceSubject<br>
     * "produced from"<br>
     * Resources which are processed together and are important for the time validity scope of the<br>
     *             named graph<br>
     */
    public static final URI SOURCE_SUBJECT;


    /**
     * http://metaservice.org/ns/metaservice#dummy<br>
     * "Dummy Statement"<br>
     * A dummy statement - used internally to denote empty results<br>
     */
    public static final URI DUMMY;


////////////////////////
// DATA PROPERTIES
////////////////////////


    /**
     * http://metaservice.org/ns/metaservice#source<br>
     * "retrieved from"<br>
     * uri of the archive, where the statements were retrieved from<br>
     */
    public static final URI SOURCE;


    /**
     * http://metaservice.org/ns/metaservice#creationTime<br>
     * "created on"<br>
     * The time this set of changes where created.<br>
     */
    public static final URI CREATION_TIME;


    /**
     * http://metaservice.org/ns/metaservice#generator<br>
     * "generator of data"<br>
     * Identifier of the software which was used to create the statements.<br>
     */
    public static final URI GENERATOR;


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
     * http://metaservice.org/ns/metaservice#repositoryId<br>
     * "repository"<br>
     * id of the repository this was retrieved from<br>
     */
    public static final URI REPOSITORY_ID;


    /**
     * http://metaservice.org/ns/metaservice#action<br>
     * "action is"<br>
     * The Action denotes the operation.<br>
     *             These operations determine the time scope on which the statements in the named graph are valid.<br>
     *             Valid operations are "add", "remove" and continuous.<br>
     *             A statement which was introduced in a graph which action was "add" is valid until there it occurs in a<br>
     *             named graph with the action "remove".<br>
     *             The semantics of "continuous" named graphs is as follows: All statements are valid as long as there is no<br>
     *             named graph with a newer time and a subset or all same subject properties.<br>
     */
    public static final URI ACTION;


    /**
     * http://metaservice.org/ns/metaservice#view<br>
     * "displayable by template"<br>
     * Links a class to a metaservice.org template<br>
     */
    public static final URI VIEW;


    /**
     * http://metaservice.org/ns/metaservice#latest<br>
     * "valid on date"<br>
     * valid for postprocessors. Calculated validity of the statements for the given date.<br>
     */
    public static final URI LATEST;


    /**
     * http://metaservice.org/ns/metaservice#time<br>
     * "data source time"<br>
     * Time the described data was originally retrieved<br>
     */
    public static final URI TIME;


////////////////////////
// THINGS
////////////////////////


    /**
     * http://metaservice.org/ns/metaservice#ActionRemove<br>
     * "Add"<br>
     * Remove Statements<br>
     */
    public static final URI ACTION_REMOVE;


    /**
     * http://metaservice.org/ns/metaservice#ActionAdd<br>
     * "Add"<br>
     * Introduce Statements<br>
     */
    public static final URI ACTION_ADD;


    /**
     * http://metaservice.org/ns/metaservice#ActionContinuous<br>
     * "Continuous"<br>
     * Introduce continuous Statements, which are valid until it is stated otherwise.<br>
     */
    public static final URI ACTION_CONTINUOUS;


    static{
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();

        METADATA = valueFactory.createURI(NAMESPACE,"Metadata");
        SOURCE_SUBJECT = valueFactory.createURI(NAMESPACE,"sourceSubject");
        DUMMY = valueFactory.createURI(NAMESPACE,"dummy");
        SOURCE = valueFactory.createURI(NAMESPACE,"source");
        CREATION_TIME = valueFactory.createURI(NAMESPACE,"creationTime");
        GENERATOR = valueFactory.createURI(NAMESPACE,"generator");
        LAST_CHECKED_TIME = valueFactory.createURI(NAMESPACE,"lastCheckedTime");
        PATH = valueFactory.createURI(NAMESPACE,"path");
        REPOSITORY_ID = valueFactory.createURI(NAMESPACE,"repositoryId");
        ACTION = valueFactory.createURI(NAMESPACE,"action");
        VIEW = valueFactory.createURI(NAMESPACE,"view");
        LATEST = valueFactory.createURI(NAMESPACE,"latest");
        TIME = valueFactory.createURI(NAMESPACE,"time");
        ACTION_REMOVE = valueFactory.createURI(NAMESPACE,"ActionRemove");
        ACTION_ADD = valueFactory.createURI(NAMESPACE,"ActionAdd");
        ACTION_CONTINUOUS = valueFactory.createURI(NAMESPACE,"ActionContinuous");
    }
}
