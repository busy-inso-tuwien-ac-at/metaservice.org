package org.metaservice.core;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Iterables;
import org.apache.commons.io.IOUtils;
import org.metaservice.api.rdf.vocabulary.VANN;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
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

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by ilo on 04.07.2014.
 */
public class OntologyToJavaConverter {

    public static void main(String[] args) throws RepositoryException, IOException, RDFParseException, SailException {

        Path root = Paths.get("metaservice-api/src/main/java/org/metaservice/api/rdf/vocabulary");
        if(!root.toFile().isDirectory())
            throw new RuntimeException("Not in correct directory " + Paths.get("").toAbsolutePath().toString());


        //dcterms
        OntologyToJavaConverter ontologyToJavaConverter = new OntologyToJavaConverter(CaseFormat.UPPER_CAMEL);
        ontologyToJavaConverter.load(OntologyToJavaConverter.class.getResourceAsStream("/ontologies/dcterms.rdf"));
        String content = ontologyToJavaConverter.generate("dcterms", "http://purl.org/dc/terms/");
        try(FileWriter writer = new FileWriter(root.resolve("DCTERMS.java").toFile())) {
            IOUtils.write(content, writer);
        }

        //package deb
        ontologyToJavaConverter = new OntologyToJavaConverter(CaseFormat.UPPER_CAMEL);
        ontologyToJavaConverter.load(new FileInputStream(Paths.get("metaservice-core-deb/src/main/resources/ontologies/deb.rdf").toFile()));
        content = ontologyToJavaConverter.generate();
        try(FileWriter writer = new FileWriter(root.resolve("DEB.java").toFile())) {
            IOUtils.write(content, writer);
        }

        //doap
        ontologyToJavaConverter = new OntologyToJavaConverter(CaseFormat.UPPER_CAMEL);
        ontologyToJavaConverter.load(OntologyToJavaConverter.class.getResourceAsStream("/ontologies/doap.rdf"));
        content = ontologyToJavaConverter.generate("doap", null);
        try(FileWriter writer = new FileWriter(root.resolve("DOAP.java").toFile())) {
            IOUtils.write(content, writer);
        }


        //metaservice
        ontologyToJavaConverter = new OntologyToJavaConverter(CaseFormat.UPPER_CAMEL);
        ontologyToJavaConverter.load(OntologyToJavaConverter.class.getResourceAsStream("/ontologies/metaservice.rdf"));
        content = ontologyToJavaConverter.generate();
        try(FileWriter writer = new FileWriter(root.resolve("METASERVICE.java").toFile())) {
            IOUtils.write(content.replaceFirst("class MS","class METASERVICE"), writer);
        }

        //metaservice-swdep
        ontologyToJavaConverter = new OntologyToJavaConverter(CaseFormat.UPPER_CAMEL);
        ontologyToJavaConverter.load(OntologyToJavaConverter.class.getResourceAsStream("/ontologies/metaservice-swdep.rdf"));
        content = ontologyToJavaConverter.generate();
        try(FileWriter writer = new FileWriter(root.resolve("METASERVICE_SWDEP.java").toFile())) {
            IOUtils.write(content.replaceFirst("class SWDEP","class METASERVICE_SWDEP"), writer);
        }


        //propertyreification
        ontologyToJavaConverter = new OntologyToJavaConverter(CaseFormat.UPPER_CAMEL);
        ontologyToJavaConverter.load(OntologyToJavaConverter.class.getResourceAsStream("/ontologies/propertyreification.rdf"));
        content = ontologyToJavaConverter.generate();
        try(FileWriter writer = new FileWriter(root.resolve("PRV.java").toFile())) {
            IOUtils.write(content, writer);
        }

        //schema
        ontologyToJavaConverter = new OntologyToJavaConverter(CaseFormat.UPPER_CAMEL);
        ontologyToJavaConverter.load(OntologyToJavaConverter.class.getResourceAsStream("/ontologies/schema.rdf"));
        content = ontologyToJavaConverter.generate("schema","http://schema.org/");
        try(FileWriter writer = new FileWriter(root.resolve("SCHEMA.java").toFile())) {
            IOUtils.write(content, writer);
        }

        //vann
        ontologyToJavaConverter = new OntologyToJavaConverter(CaseFormat.UPPER_CAMEL);
        ontologyToJavaConverter.load(OntologyToJavaConverter.class.getResourceAsStream("/ontologies/vann.rdf"));
        content = ontologyToJavaConverter.generate("vann","http://purl.org/vocab/vann/");
        try(FileWriter writer = new FileWriter(root.resolve("VANN.java").toFile())) {
            IOUtils.write(content, writer);
        }

        //skos
        ontologyToJavaConverter = new OntologyToJavaConverter(CaseFormat.UPPER_CAMEL);
        ontologyToJavaConverter.load(OntologyToJavaConverter.class.getResourceAsStream("/ontologies/skos.rdf"));
        content = ontologyToJavaConverter.generate("skos",null);
        try(FileWriter writer = new FileWriter(root.resolve("SKOS.java").toFile())) {
            IOUtils.write(content, writer);
        }
        //spdx
        ontologyToJavaConverter = new OntologyToJavaConverter(CaseFormat.UPPER_CAMEL);
        ontologyToJavaConverter.load(OntologyToJavaConverter.class.getResourceAsStream("/ontologies/spdx.rdf"));
        content = ontologyToJavaConverter.generate("spdx",null);
        try(FileWriter writer = new FileWriter(root.resolve("SPDX.java").toFile())) {
            IOUtils.write(content, writer);
        }

        //foaf
        ontologyToJavaConverter = new OntologyToJavaConverter(CaseFormat.UPPER_CAMEL);
        ontologyToJavaConverter.load(OntologyToJavaConverter.class.getResourceAsStream("/ontologies/foaf.rdf"));
        content = ontologyToJavaConverter.generate("foaf",null);
        try(FileWriter writer = new FileWriter(root.resolve("FOAF.java").toFile())) {
            IOUtils.write(content, writer);
        }

        //xhv
        ontologyToJavaConverter = new OntologyToJavaConverter(CaseFormat.UPPER_CAMEL);
        ontologyToJavaConverter.load(OntologyToJavaConverter.class.getResourceAsStream("/ontologies/xhv.rdf"));
        content = ontologyToJavaConverter.generate("xhv","http://www.w3.org/1999/xhtml/vocab#");
        try(FileWriter writer = new FileWriter(root.resolve("XHV.java").toFile())) {
            IOUtils.write(content, writer);
        }

        //admssw
        ontologyToJavaConverter = new OntologyToJavaConverter(CaseFormat.UPPER_CAMEL);
        ontologyToJavaConverter.load(OntologyToJavaConverter.class.getResourceAsStream("/ontologies/adms_sw.rdf"));
        content = ontologyToJavaConverter.generate("admssw","http://purl.org/adms/sw/");
        try(FileWriter writer = new FileWriter(root.resolve("ADMSSW.java").toFile())) {
            IOUtils.write(content, writer);
        }

        //licensing
        ontologyToJavaConverter = new OntologyToJavaConverter(CaseFormat.UPPER_CAMEL);
        ontologyToJavaConverter.load(new FileInputStream(Paths.get("metaservice-demo-license/src/main/resources/ontologies/licensing.rdf").toFile()));
        content = ontologyToJavaConverter.generate();
        try(FileWriter writer = new FileWriter(Paths.get("metaservice-demo-license/src/main/java/org/metaservice/demo/license/LIC.java").toFile())) {
            IOUtils.write(content, writer);
        }
    }

    private String generate() throws RepositoryException, SailException, RDFParseException, IOException {
        return generate(null, null);
    }

    private final CaseFormat caseFormat;
    private final RepositoryConnection connection;
    private final HashMap<URI,String> nameMap = new HashMap<>();

    public OntologyToJavaConverter(CaseFormat caseFormat) throws RepositoryException, SailException {

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
            if (resource instanceof org.openrdf.model.URI && resource.toString().startsWith(namespace)){
                URI subject = (URI)resource;
                classes.add(subject);
            }
        }


        result = connection.getStatements(null, RDF.TYPE, OWL.OBJECTPROPERTY,true);
        while(result.hasNext()){
            Resource resource = result.next().getSubject();
            if (resource instanceof org.openrdf.model.URI && resource.toString().startsWith(namespace)) {
                URI subject = (URI) resource;
                objectProperties.add(subject);
            }
        }


        result = connection.getStatements(null, RDF.TYPE, OWL.DATATYPEPROPERTY,true);
        while(result.hasNext()){
            Resource resource = result.next().getSubject();
            if (resource instanceof org.openrdf.model.URI && resource.toString().startsWith(namespace)) {
                URI subject = (URI) resource;
                dataProperties.add(subject);
            }
        }


        result = connection.getStatements(null, RDF.TYPE, OWL.ANNOTATIONPROPERTY,true);
        while(result.hasNext()){
            Resource resource = result.next().getSubject();
            if (resource instanceof org.openrdf.model.URI && resource.toString().startsWith(namespace)) {
                URI subject = (URI) resource;
                annotationProperties.add(subject);
            }
        }

        result = connection.getStatements(null, RDF.TYPE, RDF.PROPERTY,true);
        while(result.hasNext()){
            Resource resource = result.next().getSubject();
            if (resource instanceof org.openrdf.model.URI && resource.toString().startsWith(namespace)) {
                URI subject = (URI) resource;
                properties.add(subject);
            }
        }
        result = connection.getStatements(null, RDF.TYPE, OWL.THING,true);
        while(result.hasNext()){
            Resource resource = result.next().getSubject();
            if (resource instanceof org.openrdf.model.URI && resource.toString().startsWith(namespace)) {
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
                .append(" * Generator: " + OntologyToJavaConverter.class.getCanonicalName() +"\n")
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
            addPretty(classes, out, connection,"_CLASS");
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

        out.append("    static{\n");
        out.append("        ValueFactory valueFactory = ValueFactoryImpl.getInstance();\n\n");
        for(URI c : Iterables.concat(classes,objectProperties,dataProperties,annotationProperties,properties,things)){
            out.append("        ")
                    .append(nameMap.get(c))
                    .append(" = valueFactory.createURI(NAMESPACE,\"")
                    .append(c.getLocalName())
                    .append("\");\n");
        }
        out.append("    }\n");//static
        out.append("}\n");//class

        return out.toString();
    }

    private void addPretty(HashSet<URI> classes, StringBuilder out, RepositoryConnection connection,String conflictString) throws RepositoryException {
        RepositoryResult<Statement> result;
        for(URI c : classes){

            out
                    .append("    /**\n");
            out.append("     * ")
                    .append(c.toString())
                    .append("<br>\n");
            result =connection.getStatements(c,RDFS.LABEL,null,true);
            if(result.hasNext()){
                out.append("     * \"")
                        .append(result.next().getObject().stringValue())
                        .append("\"<br>\n");
            }
            result = connection.getStatements(c,RDFS.COMMENT,null,true);
            if(result.hasNext()){
                for(String s : result.next().getObject().stringValue().split("\n")) {
                    out.append("     * ")
                            .append(s)
                            .append("<br>\n");
                }
            }
            result = connection.getStatements(c,VANN.USAGE_NOTE,null,true);
            if(result.hasNext()){
                out.append("     * ")
                        .append("Usage Note <br>\n");
                for(String s : result.next().getObject().stringValue().split("\n")) {
                    out.append("     * ")
                            .append(s)
                            .append("<br>\n");
                }
            }
            out
                    .append("     */\n")
                    .append("    public static final URI ")
                    .append(toJavaName( c, conflictString))
                    .append(";\n\n" +
                            "\n");
        }
    }

    /**
     * for conflict resolution it must be called from generate
     * @param uri uri to encode
     * @param conflictString String to append when there is a nameconflict
     * @return Upper case, underscore separated java identifier of localname of uri
     */
    String toJavaName(URI uri, String conflictString){
        String s = uri.getLocalName();
        boolean upper = false;
        StringBuilder builder = new StringBuilder();
        for(char c : s.toCharArray()){
            if(Character.isUpperCase(c)){
                if(upper) {
                    c = Character.toLowerCase(c);
                }
                upper = true;
            }else{
                upper = false;
            }
            builder.append(c);
        }
        s = caseFormat.to(CaseFormat.UPPER_UNDERSCORE,builder.toString());
        s  = s.replaceAll("[^A-Z0-9]","_");
        while(nameMap.values().contains(s)){
            s = s + conflictString;
        }
        nameMap.put(uri,s);
        return s;
    }
}
