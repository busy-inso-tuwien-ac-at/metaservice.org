package org.metaservice.api.rdf.vocabulary;

import org.openrdf.model.*;
import org.openrdf.model.impl.*;



/**
 * This is an automatically generated class
 * Generator: org.metaservice.core.OntologyToJavaConverter
 * @see <a href="http://www.w3.org/2004/02/skos/core">skos</a>
 */
public class SKOS{

    public static final String NAMESPACE = "http://www.w3.org/2004/02/skos/core";

    public static final String PREFIX = "skos";

    public static final Namespace NS = new NamespaceImpl(PREFIX, NAMESPACE);

////////////////////////
// CLASSES
////////////////////////


    /**
     * http://www.w3.org/2004/02/skos/core#Collection<br>
     * "Collection"<br>
     */
    public static final URI COLLECTION;


    /**
     * http://www.w3.org/2004/02/skos/core#OrderedCollection<br>
     * "Ordered Collection"<br>
     */
    public static final URI ORDERED_COLLECTION;


    /**
     * http://www.w3.org/2004/02/skos/core#ConceptScheme<br>
     * "Concept Scheme"<br>
     */
    public static final URI CONCEPT_SCHEME;


    /**
     * http://www.w3.org/2004/02/skos/core#Concept<br>
     * "Concept"<br>
     */
    public static final URI CONCEPT;


////////////////////////
// OBJECT PROPERTIES
////////////////////////


    /**
     * http://www.w3.org/2004/02/skos/core#narrowMatch<br>
     * "has narrower match"<br>
     */
    public static final URI NARROW_MATCH;


    /**
     * http://www.w3.org/2004/02/skos/core#semanticRelation<br>
     * "is in semantic relation with"<br>
     */
    public static final URI SEMANTIC_RELATION;


    /**
     * http://www.w3.org/2004/02/skos/core#broaderTransitive<br>
     * "has broader transitive"<br>
     */
    public static final URI BROADER_TRANSITIVE;


    /**
     * http://www.w3.org/2004/02/skos/core#topConceptOf<br>
     * "is top concept in scheme"<br>
     */
    public static final URI TOP_CONCEPT_OF;


    /**
     * http://www.w3.org/2004/02/skos/core#member<br>
     * "has member"<br>
     */
    public static final URI MEMBER;


    /**
     * http://www.w3.org/2004/02/skos/core#mappingRelation<br>
     * "is in mapping relation with"<br>
     * These concept mapping relations mirror semantic relations, and the data model defined below is similar (with the exception of skos:exactMatch) to the data model defined for semantic relations. A distinct vocabulary is provided for concept mapping relations, to provide a convenient way to differentiate links within a concept scheme from links between concept schemes. However, this pattern of usage is not a formal requirement of the SKOS data model, and relies on informal definitions of best practice.<br>
     */
    public static final URI MAPPING_RELATION;


    /**
     * http://www.w3.org/2004/02/skos/core#inScheme<br>
     * "is in scheme"<br>
     */
    public static final URI IN_SCHEME;


    /**
     * http://www.w3.org/2004/02/skos/core#related<br>
     * "has related"<br>
     * skos:related is disjoint with skos:broaderTransitive<br>
     */
    public static final URI RELATED;


    /**
     * http://www.w3.org/2004/02/skos/core#broadMatch<br>
     * "has broader match"<br>
     */
    public static final URI BROAD_MATCH;


    /**
     * http://www.w3.org/2004/02/skos/core#narrowerTransitive<br>
     * "has narrower transitive"<br>
     */
    public static final URI NARROWER_TRANSITIVE;


    /**
     * http://www.w3.org/2004/02/skos/core#hasTopConcept<br>
     * "has top concept"<br>
     */
    public static final URI HAS_TOP_CONCEPT;


    /**
     * http://www.w3.org/2004/02/skos/core#memberList<br>
     * "has member list"<br>
     * For any resource, every item in the list given as the value of the<br>
     *       skos:memberList property is also a value of the skos:member property.<br>
     */
    public static final URI MEMBER_LIST;


    /**
     * http://www.w3.org/2004/02/skos/core#narrower<br>
     * "has narrower"<br>
     * Narrower concepts are typically rendered as children in a concept hierarchy (tree).<br>
     */
    public static final URI NARROWER;


    /**
     * http://www.w3.org/2004/02/skos/core#broader<br>
     * "has broader"<br>
     * Broader concepts are typically rendered as parents in a concept hierarchy (tree).<br>
     */
    public static final URI BROADER;


    /**
     * http://www.w3.org/2004/02/skos/core#closeMatch<br>
     * "has close match"<br>
     */
    public static final URI CLOSE_MATCH;


    /**
     * http://www.w3.org/2004/02/skos/core#relatedMatch<br>
     * "has related match"<br>
     */
    public static final URI RELATED_MATCH;


    /**
     * http://www.w3.org/2004/02/skos/core#exactMatch<br>
     * "has exact match"<br>
     * skos:exactMatch is disjoint with each of the properties skos:broadMatch and skos:relatedMatch.<br>
     */
    public static final URI EXACT_MATCH;


////////////////////////
// DATA PROPERTIES
////////////////////////


    /**
     * http://www.w3.org/2004/02/skos/core#notation<br>
     * "notation"<br>
     */
    public static final URI NOTATION;


////////////////////////
// ANNOTATION PROPERTIES
////////////////////////


    /**
     * http://www.w3.org/2004/02/skos/core#scopeNote<br>
     * "scope note"<br>
     */
    public static final URI SCOPE_NOTE;


    /**
     * http://www.w3.org/2004/02/skos/core#note<br>
     * "note"<br>
     */
    public static final URI NOTE;


    /**
     * http://www.w3.org/2004/02/skos/core#hiddenLabel<br>
     * "hidden label"<br>
     * The range of skos:hiddenLabel is the class of RDF plain literals.<br>
     */
    public static final URI HIDDEN_LABEL;


    /**
     * http://www.w3.org/2004/02/skos/core#editorialNote<br>
     * "editorial note"<br>
     */
    public static final URI EDITORIAL_NOTE;


    /**
     * http://www.w3.org/2004/02/skos/core#altLabel<br>
     * "alternative label"<br>
     * The range of skos:altLabel is the class of RDF plain literals.<br>
     */
    public static final URI ALT_LABEL;


    /**
     * http://www.w3.org/2004/02/skos/core#historyNote<br>
     * "history note"<br>
     */
    public static final URI HISTORY_NOTE;


    /**
     * http://www.w3.org/2004/02/skos/core#example<br>
     * "example"<br>
     */
    public static final URI EXAMPLE;


    /**
     * http://www.w3.org/2004/02/skos/core#changeNote<br>
     * "change note"<br>
     */
    public static final URI CHANGE_NOTE;


    /**
     * http://www.w3.org/2004/02/skos/core#prefLabel<br>
     * "preferred label"<br>
     * A resource has no more than one value of skos:prefLabel per language tag, and no more than one value of skos:prefLabel without language tag.<br>
     */
    public static final URI PREF_LABEL;


    /**
     * http://www.w3.org/2004/02/skos/core#definition<br>
     * "definition"<br>
     */
    public static final URI DEFINITION;


    static{
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();

        COLLECTION = valueFactory.createURI(NAMESPACE,"Collection");
        ORDERED_COLLECTION = valueFactory.createURI(NAMESPACE,"OrderedCollection");
        CONCEPT_SCHEME = valueFactory.createURI(NAMESPACE,"ConceptScheme");
        CONCEPT = valueFactory.createURI(NAMESPACE,"Concept");
        NARROW_MATCH = valueFactory.createURI(NAMESPACE,"narrowMatch");
        SEMANTIC_RELATION = valueFactory.createURI(NAMESPACE,"semanticRelation");
        BROADER_TRANSITIVE = valueFactory.createURI(NAMESPACE,"broaderTransitive");
        TOP_CONCEPT_OF = valueFactory.createURI(NAMESPACE,"topConceptOf");
        MEMBER = valueFactory.createURI(NAMESPACE,"member");
        MAPPING_RELATION = valueFactory.createURI(NAMESPACE,"mappingRelation");
        IN_SCHEME = valueFactory.createURI(NAMESPACE,"inScheme");
        RELATED = valueFactory.createURI(NAMESPACE,"related");
        BROAD_MATCH = valueFactory.createURI(NAMESPACE,"broadMatch");
        NARROWER_TRANSITIVE = valueFactory.createURI(NAMESPACE,"narrowerTransitive");
        HAS_TOP_CONCEPT = valueFactory.createURI(NAMESPACE,"hasTopConcept");
        MEMBER_LIST = valueFactory.createURI(NAMESPACE,"memberList");
        NARROWER = valueFactory.createURI(NAMESPACE,"narrower");
        BROADER = valueFactory.createURI(NAMESPACE,"broader");
        CLOSE_MATCH = valueFactory.createURI(NAMESPACE,"closeMatch");
        RELATED_MATCH = valueFactory.createURI(NAMESPACE,"relatedMatch");
        EXACT_MATCH = valueFactory.createURI(NAMESPACE,"exactMatch");
        NOTATION = valueFactory.createURI(NAMESPACE,"notation");
        SCOPE_NOTE = valueFactory.createURI(NAMESPACE,"scopeNote");
        NOTE = valueFactory.createURI(NAMESPACE,"note");
        HIDDEN_LABEL = valueFactory.createURI(NAMESPACE,"hiddenLabel");
        EDITORIAL_NOTE = valueFactory.createURI(NAMESPACE,"editorialNote");
        ALT_LABEL = valueFactory.createURI(NAMESPACE,"altLabel");
        HISTORY_NOTE = valueFactory.createURI(NAMESPACE,"historyNote");
        EXAMPLE = valueFactory.createURI(NAMESPACE,"example");
        CHANGE_NOTE = valueFactory.createURI(NAMESPACE,"changeNote");
        PREF_LABEL = valueFactory.createURI(NAMESPACE,"prefLabel");
        DEFINITION = valueFactory.createURI(NAMESPACE,"definition");
    }
}
