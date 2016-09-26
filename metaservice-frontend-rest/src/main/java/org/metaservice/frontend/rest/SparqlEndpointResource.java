/*
 * Copyright 2015 Nikola Ilo
 * Research Group for Industrial Software, Vienna University of Technology, Austria
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

package org.metaservice.frontend.rest;

import com.sun.jersey.spi.resource.Singleton;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaservice.frontend.rest.api.ResourceService;
import org.metaservice.frontend.rest.cache.FileSystemCacheResourceService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ilo on 07.02.14.
 */
@Singleton
@Path("/")
public class SparqlEndpointResource {

    private final ResourceService resourceService;
    private final String namespaces;
    private final String searchQuery;
    private final String timeQuery;

    public SparqlEndpointResource() throws IOException, URISyntaxException {
        resourceService = new FileSystemCacheResourceService(new SparqlResourceService());
        namespaces = generateNamespaceString();
        searchQuery = loadSparql("/sparql/search.sparql");
        timeQuery = loadSparql("/sparql/resourceTimes.sparql");
    }

    private String loadSparql(String s) throws IOException {
        return IOUtils.toString(SparqlEndpointResource.class.getResourceAsStream(s));
    }

    private String generateNamespaceString() {
        HashMap<String,String> namespaceMap = new HashMap<>();
        namespaceMap.put("admssw", "http://purl.org/adms/sw/");
        namespaceMap.put("bds","http://www.blazegraph.com/rdf/search#");
        namespaceMap.put("cc","http://creativecommons.org/ns#");
        namespaceMap.put("dc","http://purl.org/dc/elements/1.1/");
        namespaceMap.put("dcterms","http://purl.org/dc/terms/");
        namespaceMap.put("doap","http://usefulinc.com/ns/doap#");
        namespaceMap.put("foaf","http://xmlns.com/foaf/0.1/");
        namespaceMap.put("ms","http://metaservice.org/ns/metaservice#");
        namespaceMap.put("deb","http://metaservice.org/ns/metaservice-deb#");
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
    @Produces("application/sparql-results+json")
    public Response search(
            @Nullable @QueryParam("q") String q,
            @Nullable @QueryParam("limit") Integer limit,
            @Nullable @QueryParam("offset") Integer offset){
        try {
            return generateResponseSearch("application/sparql-results+json", q, limit, offset);
        } catch (IOException|URISyntaxException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    public @NotNull Response generateResponseSearch(
           @NotNull String mimeType,
           @Nullable String q,
           @Nullable Integer limit,
           @Nullable Integer offset
    ) throws IOException, URISyntaxException {
        if(q == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        //defaults
        if(limit == null){
            limit = 10;
        }
        if(offset == null){
            offset = 0;
        }
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
            @Nullable @QueryParam("path")String path,
            @Nullable @QueryParam("datetime") String date
    ){
        return generateResponseResource("application/ld+json", path, date, false);
    }

    @GET
    @Path("/resource")
    @Produces("application/rdf+xml")
    public Response resourceRdfXml(
            @Nullable @QueryParam("path")String path,
            @Nullable @QueryParam("datetime") String date
    ){
        return generateResponseExport("application/rdf+xml", path, date, false);
    }

    @GET
    @Path("/resource/jsonld")
    @Produces("application/ld+json")
    public Response exportJsonLDDownload(
            @Nullable @QueryParam("path") String path,
            @Nullable @QueryParam("datetime") String date
    ){
        return generateResponseExport("application/ld+json", path, date, true);
    }

    @GET
    @Path("/resource/jsonldquad")
    @Produces("application/ld+json")
    public Response exportJsonLDDownloadQuad(
            @Nullable @QueryParam("path") String path,
            @Nullable @QueryParam("datetime") String date
    ){
        return generateResponseResource("application/ld+json", path, date, true);
    }

    @GET
    @Path("/resource/rdf")
    @Produces("application/rdf+xml")
    public Response exportRdfXmlDownload(
            @Nullable @QueryParam("path") String path,
            @Nullable @QueryParam("datetime") String date
    ){
        return generateResponseExport("application/rdf+xml", path, date, true);
    }


    @GET
    @Path("/resource/ttl")
    @Produces("application/x-turtle")
    public Response exportTurtleDownload(
            @Nullable @QueryParam("path") String path,
            @Nullable @QueryParam("datetime") String date
    ){
        return generateResponseExport("application/x-turtle", path, date, true);
    }

    @GET
    @Path("/resource/times")
    @Produces("application/sparql-results+json")
    public Response resourceTimes(
            @Nullable @QueryParam("path") String path
    ) throws IOException, URISyntaxException {
        if(path ==null)
            return Response.serverError().build();
        path = path.replaceFirst("\\.times","");
        if(path.contains("http://www.metaservice.org")){
            path =  path.replaceAll("http://www.metaservice.org", "http://metaservice.org");
        }
        System.err.println(path);
        String query = namespaces +timeQuery;
        query = query.replace("$path",stringToIri(path));
        return Response.ok(querySparql("application/sparql-results+json",query)).build();
    }

    public @NotNull Response generateResponseExport(
            @NotNull String mimeType,
            @Nullable String path,
            @Nullable String date,
            boolean download
    ) {
        return generateResponseResourceOrExport( mimeType, path, date, download);
    }
    public @NotNull Response generateResponseResource(
            @NotNull String mimeType,
            @Nullable String path,
            @Nullable String date,
            boolean download
    ){
       return generateResponseResourceOrExport(mimeType,path,date,download);
    }

    public @NotNull Response generateResponseResourceOrExport(
            @NotNull String mimeType,
            @Nullable String path,
            @Nullable String date,
            boolean download
    ){
        if(path == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Calendar calendar = null;
        try{
            if(date!=null) {
                calendar = DatatypeConverter.parseDateTime(date);
            }
        }catch (IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if(download){
            path = path.replaceFirst("(\\.quad)?\\.(jsonld|rdf|ttl)$","");
        }
        if(path.contains("http://www.metaservice.org")){
            path =  path.replaceAll("http://www.metaservice.org", "http://metaservice.org");
        }
        Response.ResponseBuilder builder = Response
                .ok(resourceService.getResource(path, calendar, mimeType));
        if(download){
            builder.header("Content-Disposition", "attachment");
        }
        return builder.build();
    }

    @Deprecated
    private @NotNull
    InputStream querySparql(
            @NotNull String mimeType,
            @NotNull String query
    ) throws URISyntaxException, IOException {
        System.err.println(query);
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme("http")
                .setHost("graph.metaservice.org")
                .setPort(8080)
                .setPath("/bigdata/sparql");

        return Request.Post(uriBuilder.build())
                .bodyForm(Form.form().add("query", query).build())
                .connectTimeout(1000)
                .socketTimeout(60000)
                .setHeader("Accept", mimeType)
                .execute()
                .returnContent().asStream();
    }

    public @NotNull String stringToLiteral(@NotNull String value){
        return "'"+value.replace("'","\\'")+"'";
    }

    public @NotNull String stringToIri(@NotNull String value){
        return "<"+value.replace(">","\\>")+">";
    }

    public @NotNull String dateToLiteral(@NotNull Calendar date) {
        return "\""+DatatypeConverter.printDateTime(date)+"\"^^xsd:dateTime";
    }

}
