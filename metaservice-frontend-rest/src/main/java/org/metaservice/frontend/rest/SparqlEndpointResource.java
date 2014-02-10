package org.metaservice.frontend.rest;

import com.sun.jersey.spi.resource.Singleton;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ilo on 07.02.14.
 */
@Singleton
@Path("/")
public class SparqlEndpointResource {

    private final String namespaces;
    private final String searchQuery;
    private final String resourceQuery;

    public SparqlEndpointResource() throws IOException {
        namespaces = generateNamespaceString();
        searchQuery = loadSparql("sparql/search.sparql");
        resourceQuery = loadSparql("sparql/resource.sparql");
    }

    private String loadSparql(String s) throws IOException {
        StringWriter stringWriter = new StringWriter();
        InputStream inputStream =  ClassLoader.getSystemResourceAsStream(s);
        IOUtils.copy(inputStream,stringWriter);
        return stringWriter.toString();
    }

    private String generateNamespaceString() {
        HashMap<String,String> namespaceMap = new HashMap<>();
        namespaceMap.put("admssw", "http://purl.org/adms/sw/");
        namespaceMap.put("bds","http://www.bigdata.com/rdf/search#");
        namespaceMap.put("cc","http://creativecommons.org/ns#");
        namespaceMap.put("dc","http://purl.org/dc/elements/1.1/");
        namespaceMap.put("dcterms","http://purl.org/dc/terms/");
        namespaceMap.put("doap","http://usefulinc.com/ns/doap#");
        namespaceMap.put("foaf","http://xmlns.com/foaf/0.1/");
        namespaceMap.put("ms","http://metaservice.org/ns/metaservice#");
        namespaceMap.put("ms-deb","http://metaservice.org/ns/metaservice-deb#");
        namespaceMap.put("owl","http://www.w3.org/2002/07/owl#");
        namespaceMap.put("rad","http://www.w3.org/ns/radion#");
        namespaceMap.put("rdf","http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        namespaceMap.put("rdfs","http://www.w3.org/2000/01/rdf-schema#");
        namespaceMap.put("sf","http://sourceforge.net/api/sfelements.rdf#");
        namespaceMap.put("skos","http://www.w3.org/2004/02/skos/core#");
        namespaceMap.put("vcard","http://www.w3.org/2006/vcard/ns#");
        namespaceMap.put("xhv","http://www.w3.org/1999/xhtml/vocab#");
        namespaceMap.put("xsd","http://www.w3.org/2001/XMLSchema#");

        StringBuilder namespaceBuilder = new StringBuilder();
        for(Map.Entry<String,String> entry : namespaceMap.entrySet()){
            namespaceBuilder
                    .append("PREFIX ")
                    .append(entry.getKey())
                    .append(": <")
                    .append(entry.getValue())
                    .append(">\n");
        }
        return namespaceBuilder.toString();
    }

    @GET
    @Path("/search")
    //@Consumes("application/sparql-results+json")
    @Produces("application/sparql-results+json")
    public Response search(
            @QueryParam("q") String q,
            @QueryParam("limit") int limit,
            @QueryParam("offset") int offset){
        try {
            return generateResponseSearch("application/sparql-results+json", q, limit, offset);
        } catch (IOException|URISyntaxException e) {
            return Response.serverError().build();
        }
    }

    public Response generateResponseSearch(
            String mimeType,
            String q,
            int limit,
            int offset
    ) throws IOException, URISyntaxException {
        String query = namespaces + searchQuery;
        query = query.replace("$q",stringToLiteral(q));
        query = query.replace("$limit",Integer.toString(limit));
        query = query.replace("$offset",Integer.toString(offset));
        return Response.ok(querySparql(mimeType,query)).build();

    }
    @GET
    @Path("/resource")
    @Produces("application/ld+json")
    public Response resourceJsonLD(
            @QueryParam("path")String path
    ){
        System.err.println("LD");
        return generateResponseResearch("application/ld+json", path);
    }

    @GET
    @Path("/resource")
    @Produces("application/rdf+xml")
    public Response resourceRdfXml(@QueryParam("path")String path){
        System.err.println("XML");
        return generateResponseResearch("application/rdf+xml", path);
    }


    public Response generateResponseResearch(
            String mimeType,
            String path
    ){
        try{
            String query = namespaces +resourceQuery;
            query = query.replace("$path",stringToIri(path));
            return Response.ok(querySparql(mimeType,query)).build();
        }  catch (URISyntaxException | IOException e) {
            return Response.serverError().build();
        }
    }


    private InputStream querySparql(
            String mimeType,
            String query
    ) throws URISyntaxException, IOException {
        System.err.println(query);
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme("http")
                .setHost("metaservice.org")
                .setPort(8080)
                .setPath("/sparql")
                .setParameter("query", query);

        return Request.Get(uriBuilder.build())
                .connectTimeout(1000)
                .socketTimeout(10000)
                .setHeader("Accept", mimeType)
                .execute()
                .returnContent().asStream();
    }

    public String stringToLiteral(String value){
        return "'"+value.replace("'","\\'")+"'";
    }

    public String stringToIri(String value){
        return "<"+value.replace(">","\\>")+">";
    }
}
