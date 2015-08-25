package org.metaservice.core;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Iterables;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.metaservice.api.rdf.vocabulary.DC;
import org.metaservice.api.rdf.vocabulary.VANN;
import org.openrdf.model.*;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;
import org.openrdf.sail.NotifyingSail;
import org.openrdf.sail.SailException;
import org.openrdf.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.openrdf.sail.memory.MemoryStore;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by ilo on 04.07.2014.
 */
public class OntologyToLatexConverter {

    public static void main(String[] args) throws RepositoryException, IOException, RDFParseException, SailException {

        Path root = Paths.get("metaservice-api/src/main/java/org/metaservice/api/rdf/vocabulary");
        if(!root.toFile().isDirectory())
            throw new RuntimeException("Not in correct directory " + Paths.get("").toAbsolutePath().toString());
        OntologyToLatexConverter ontologyToJavaConverter = new OntologyToLatexConverter(CaseFormat.UPPER_CAMEL);
        String content="";
/*
        //dcterms
        ontologyToJavaConverter.load(OntologyToLatexConverter.class.getResourceAsStream("/ontologies/dcterms.rdf"));
        content = ontologyToJavaConverter.generate("dcterms", "http://purl.org/dc/terms/");





        //doap
        ontologyToJavaConverter = new OntologyToLatexConverter(CaseFormat.UPPER_CAMEL);
        ontologyToJavaConverter.load(OntologyToLatexConverter.class.getResourceAsStream("/ontologies/doap.rdf"));
        content = ontologyToJavaConverter.generate("doap", null);
        System.err.println(content);


        //doap
        ontologyToJavaConverter = new OntologyToLatexConverter(CaseFormat.UPPER_CAMEL);
        ontologyToJavaConverter.load(OntologyToLatexConverter.class.getResourceAsStream("/ontologies/adms_sw.rdf"));
        content = ontologyToJavaConverter.generate("admssw", null);
        System.err.println(content);



        //doap
        ontologyToJavaConverter = new OntologyToLatexConverter(CaseFormat.UPPER_CAMEL);
        ontologyToJavaConverter.load(OntologyToLatexConverter.class.getResourceAsStream("/ontologies/spdx.rdf"));
        content = ontologyToJavaConverter.generate("spdx", null);
        System.err.println(content
                .replaceAll("<(code|a|p) xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:dc=\"http://purl.org/dc/terms/\" xmlns:foaf=\"http://xmlns.com/foaf/0.1/\" xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\" xmlns:owl=\"http://www.w3.org/2002/07/owl#\" xmlns:doap=\"http://usefulinc.com/ns/doap\" xmlns:vs=\"http://www.w3.org/2003/06/sw-vocab-status/ns#\"( href=\"[^\"]*\")?>","")
                .replaceAll("</code>","")
                .replaceAll(".texttt..\\\\","~\\\\")
        .replaceAll("</a>",""));



        //doap
        ontologyToJavaConverter = new OntologyToLatexConverter(CaseFormat.UPPER_CAMEL);
        ontologyToJavaConverter.load(OntologyToLatexConverter.class.getResourceAsStream("/ontologies/metaservice-swrel.rdf"));
        content = ontologyToJavaConverter.generate("swrel", null);
        System.err.println(content);
*/


        //package deb
        ontologyToJavaConverter = new OntologyToLatexConverter(CaseFormat.UPPER_CAMEL);
        ontologyToJavaConverter.load(new FileInputStream(Paths.get("metaservice-core-deb/src/main/resources/ontologies/deb.rdf").toFile()));
        content = ontologyToJavaConverter.generate();
        System.err.println(content);

    }

    private String generate() throws RepositoryException, SailException, RDFParseException, IOException {
        return generate(null, null);
    }

    private final CaseFormat caseFormat;
    private final RepositoryConnection connection;
    private final HashMap<URI,String> nameMap = new HashMap<>();

    public OntologyToLatexConverter(CaseFormat caseFormat) throws RepositoryException, SailException {

        NotifyingSail sail = new MemoryStore();
        sail = new ForwardChainingRDFSInferencer(sail);
        sail.initialize();
        Repository repository = new SailRepository(sail);
        connection = repository.getConnection();
        this.caseFormat = caseFormat;
    }

    public void load(InputStream inputStream) throws RepositoryException, RDFParseException, IOException {
        connection.add(inputStream, "", RDFFormat.RDFXML);
    }

    public String generate(String prefix, String namespace) throws RepositoryException, IOException, RDFParseException, SailException {

        RepositoryResult<Statement> result;

        if(namespace == null) {
            result = connection.getStatements(null, VANN.PREFERRED_NAMESPACE_URI, null, true);
            if(result.hasNext()){
                namespace =  result.next().getObject().stringValue();
            }else{
                result = connection.getStatements(null, RDF.TYPE, OWL.ONTOLOGY, true);
                namespace = result.next().getSubject().stringValue();
            }
        }
        if(prefix == null) {
            prefix = connection.getStatements(null, VANN.PREFERRED_NAMESPACE_PREFIX, null, true).next().getObject().stringValue();
        }
        StringBuilder out = new StringBuilder();

        HashSet<URI> classes = new HashSet<>();
        HashSet<URI> properties = new HashSet<>();
        HashSet<URI> objectProperties = new HashSet<>();
        HashSet<URI> dataProperties = new HashSet<>();
        HashSet<URI> annotationProperties = new HashSet<>();
        HashSet<URI> things = new HashSet<>();


        result = connection.getStatements(null, RDF.TYPE, RDFS.CLASS, true);
        result.enableDuplicateFilter();
        while(result.hasNext()) {
            Resource resource = result.next().getSubject();
            if (resource instanceof URI && resource.toString().startsWith(namespace)){
                URI subject = (URI)resource;
                classes.add(subject);
            }
        }


        result = connection.getStatements(null, RDF.TYPE, OWL.OBJECTPROPERTY,true);
        while(result.hasNext()){
            Resource resource = result.next().getSubject();
            if (resource instanceof URI && resource.toString().startsWith(namespace)) {
                URI subject = (URI) resource;
                objectProperties.add(subject);
            }
        }


        result = connection.getStatements(null, RDF.TYPE, OWL.DATATYPEPROPERTY,true);
        while(result.hasNext()){
            Resource resource = result.next().getSubject();
            if (resource instanceof URI && resource.toString().startsWith(namespace)) {
                URI subject = (URI) resource;
                dataProperties.add(subject);
            }
        }


        result = connection.getStatements(null, RDF.TYPE, OWL.ANNOTATIONPROPERTY,true);
        while(result.hasNext()){
            Resource resource = result.next().getSubject();
            if (resource instanceof URI && resource.toString().startsWith(namespace)) {
                URI subject = (URI) resource;
                annotationProperties.add(subject);
            }
        }

        result = connection.getStatements(null, RDF.TYPE, RDF.PROPERTY,true);
        while(result.hasNext()){
            Resource resource = result.next().getSubject();
            if (resource instanceof URI && resource.toString().startsWith(namespace)) {
                URI subject = (URI) resource;
                properties.add(subject);
            }
        }
        result = connection.getStatements(null, RDF.TYPE, OWL.THING,true);
        while(result.hasNext()){
            Resource resource = result.next().getSubject();
            if (resource instanceof URI && resource.toString().startsWith(namespace)) {
                URI subject = (URI) resource;
                things.add(subject);
            }
        }

        out.append("package org.metaservice.api.rdf.vocabulary;\n" +
                "\n" +
                "import org.openrdf.model.*;\n" +
                "import org.openrdf.model.impl.*;\n\n\n\n");
        out.append("/**\n")
                .append(" * This is an automatically generated class\n")
                .append(" * Generator: " + OntologyToLatexConverter.class.getCanonicalName() +"\n")
                .append(" * @see <a href=\""+namespace+"\">"+prefix+"</a>\n" )
                .append(" */\n")
                .append("public class ")
                .append(prefix.toUpperCase())
                .append("{\n\n");

        out.append("    public static final String NAMESPACE = \"")
                .append(namespace)
                .append("\";\n\n");
        out.append("    public static final String PREFIX = \"")
                .append(prefix)
                .append("\";\n\n");

        out.append("    public static final Namespace NS = new NamespaceImpl(PREFIX, NAMESPACE);\n\n");


        properties.removeAll(objectProperties);
        properties.removeAll(dataProperties);
        properties.removeAll(annotationProperties);
        things.removeAll(properties);
        things.removeAll(classes);
        things.removeAll(objectProperties);
        things.removeAll(dataProperties);
        things.removeAll(annotationProperties);


        if(classes.size()> 0) {
            out.append("////////////////////////\n");
            out.append("// CLASSES\n");
            out.append("////////////////////////\n\n\n");
            addPrettyClass(classes, out, connection, "_CLASS");
        }
       if(objectProperties.size() > 0) {
            out.append("////////////////////////\n");
            out.append("// OBJECT PROPERTIES\n");
            out.append("////////////////////////\n\n\n");
            addPretty(objectProperties, out, connection,"_PROPERTY");
        }
        if(dataProperties.size() > 0) {
            out.append("////////////////////////\n");
            out.append("// DATA PROPERTIES\n");
            out.append("////////////////////////\n\n\n");
            addPretty(dataProperties, out, connection,"_PROPERTY");
        }
        if(annotationProperties.size() > 0) {
            out.append("////////////////////////\n");
            out.append("// ANNOTATION PROPERTIES\n");
            out.append("////////////////////////\n\n\n");
            addPretty(annotationProperties, out, connection,"_PROPERTY");
        }
        if(properties.size() > 0) {
            out.append("////////////////////////\n");
            out.append("// PROPERTIES\n");
            out.append("////////////////////////\n\n\n");
            addPretty(properties, out, connection,"_PROPERTY");
        }
        if(things.size() > 0) {
            out.append("////////////////////////\n");
            out.append("// THINGS\n");
            out.append("////////////////////////\n\n\n");
            addPretty(things, out, connection,"_THING");
        }

        return out.toString();
    }



    private void addPrettyClass(HashSet<URI> classes, StringBuilder out, RepositoryConnection connection,String conflictString) throws RepositoryException {
        RepositoryResult<Statement> result;
        for(URI c : classes){
            out.append("\\textbf{URI}&\\texttt{" +shortened(c)+"}");
            out.append("\\\\\n");
            ArrayList<String> list = new ArrayList<>();
            list.clear();
            result =connection.getStatements(c,RDFS.SUBCLASSOF ,null,false);
            while(result.hasNext()){
                Statement value = result.next();

                if(value.getObject() instanceof  URI)
                    list.add( shortened((URI) value.getObject()));
            }
            out.append("\\textbf{Subclass of}&\\texttt{"+StringUtils.join(list,", ")+"}");
            out.append("\\\\\n");
            list.clear();
            result =connection.getStatements(c,RDFS.COMMENT ,null,false);
            while(result.hasNext()){
                list.add(literal((Literal)result.next().getObject()));
            }
            //  out.append("\\\\\n");
         //   out.append("&");
            //out.append("\\multicolumn{4}{l}{");

            out.append("\\textbf{Description}&"+StringUtils.join(list,","));
            out.append("\\\\\\midrule\n");

            // out.append("}");
        }
    }

    private void addPretty(HashSet<URI> classes, StringBuilder out, RepositoryConnection connection,String conflictString) throws RepositoryException {
        RepositoryResult<Statement> result;
        for(URI c : classes){
            out.append("\\texttt{" + c.getLocalName()+"} & \\texttt{" +shortened(c)+"} & ");
            ArrayList<String> list = new ArrayList<>();

            result =connection.getStatements(c,RDFS.SUBPROPERTYOF,null,false);
            while(result.hasNext()){
                list.add("\\texttt{" +shortened((URI)result.next().getObject())+"}");
            }
            out.append(StringUtils.join(list,", "));
   /*         out.append(" & ");
             list.clear();
           result =connection.getStatements(c,RDFS.DOMAIN ,null,false);
            if(result.hasNext()){
                Value value = result.next().getObject();
                if(value instanceof URI) {
                    list.add( shortened((URI)value));
                }else{
                    list.addAll(collectionHandling(value,connection));
                }
            }
            out.append(StringUtils.join(list,","));
            out.append(" & ");
            list.clear();
            result =connection.getStatements(c,RDFS.RANGE ,null,false);
            if(result.hasNext()){
//                list.add( shortened((URI)result.next().getObject()));
            }

            list.clear();
            result =connection.getStatements(c,RDFS.COMMENT ,null,false);
            if(result.hasNext()){
                list.add(literal((Literal)result.next().getObject()));
            }
          //  out.append("\\\\\n");
            out.append("&");
                        out.append(StringUtils.join(list,","));

            //out.append("\\multicolumn{4}{l}{");
            out.append(StringUtils.join(list,","));
           // out.append("}");
*/
            out.append("\\\\\n");
        }
    }

    private Collection<? extends String> collectionHandling(Value value, RepositoryConnection connection) throws RepositoryException {
        ArrayList<String> result= new ArrayList<>();
        RepositoryResult<Statement> x = connection.getStatements((Resource)value,OWL.UNIONOF,null,false);
        while(x.hasNext()){
            RepositoryResult<Statement> y = connection.getStatements((Resource)x.next().getObject(),RDFS.MEMBER,null,false);
            while(y.hasNext()){
                result.add(y.next().getObject().toString());
            }
        }
        return result;
    }

    private String literal(Literal object) {
        if( object.getLanguage() == null || object.getLanguage().equals("en")){
            return object.stringValue();
        }
        return "";
    }

    public String shortened(URI uri){
        switch (uri.getNamespace()){
            case "http://usefulinc.com/ns/doap#":
                return "doap:" + uri.getLocalName();
            case "http://www.w3.org/2000/01/rdf-schema#":
                return "rdfs:" + uri.getLocalName();
            case "http://xmlns.com/foaf/0.1/":
                return "foaf:" + uri.getLocalName();
            case "http://www.w3.org/ns/radion#":
                return "rad:" + uri.getLocalName();
            case "http://schema.org/":
                return "schema:"+ uri.getLocalName();
            case "http://purl.org/adms/sw/":
                return "admssw:" + uri.getLocalName();
            case "http://spdx.org/rdf/terms#":
                return "spdx:"+uri.getLocalName();
            case "http://metaservice.org/ns/metaservice-swrel#":
                return "swrel:"+uri.getLocalName();
            case "http://metaservice.org/ns/deb#":
                return "deb:" + uri.getLocalName();
            default:
                return uri.toString();
        }

    }
}
