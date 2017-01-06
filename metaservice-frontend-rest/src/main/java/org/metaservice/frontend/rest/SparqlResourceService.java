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

package org.metaservice.frontend.rest;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;
import org.metaservice.frontend.rest.api.ResourceService;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ilo on 02.09.2015.
 */
public class SparqlResourceService implements ResourceService {
    private final URI uri;
    private final String resourceQuery;

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

    private final String namespaces;

    public SparqlResourceService() throws URISyntaxException, IOException {
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme("http")
                .setHost("graph.metaservice.org")
                .setPort(8080)
                .setPath("/bigdata/sparql");
        uri = uriBuilder.build();

        namespaces = generateNamespaceString();
        resourceQuery = loadSparql("/sparql/resourceWithLatest.sparql");
    }

    private String loadSparql(String s) throws IOException {
        return IOUtils.toString(SparqlEndpointResource.class.getResourceAsStream(s));
    }


    @Override
    public InputStream getResource(String resource, Calendar date, String mimetype) {

        String query = namespaces +resourceQuery;
        query = query.replace("$path",stringToIri(resource));
        query = query.replace("$selectedTime",dateToLiteral(date));
        try {
            return querySparql(mimetype,query);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException("Error executing SPARQL query",e);
        }
    }



    private @NotNull
    InputStream querySparql(
            @NotNull String mimeType,
            @NotNull String query
    ) throws URISyntaxException, IOException {
        System.err.println(query);
        return Request.Post(uri)
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
        return "\""+ DatatypeConverter.printDateTime(date)+"\"^^xsd:dateTime";
    }

}
