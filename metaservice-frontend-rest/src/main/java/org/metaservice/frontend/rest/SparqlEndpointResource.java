package org.metaservice.frontend.rest;

import org.apache.http.client.fluent.Request;
import sun.net.www.http.HttpClient;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ilo on 07.02.14.
 */
public class SparqlEndpointResource {
    @GET
    @Path("/sparql")
    //@Consumes("application/sparql-results+json")
    @Produces("application/sparql-results+json")
    public Response getSparql(@QueryParam("path") String path){
        try {
            InputStream stream =
                    Request.Get(path)
                   .connectTimeout(1000)
                   .socketTimeout(10000)
                   //.setHeader("User-Agent", USER_AGENT)
                   .setHeader("Accept", "application/sparql-results+json")
                   .execute()
                   .returnContent().asStream();

        } catch (IOException e) {
            return Response.serverError().build();
        }
    }
}
