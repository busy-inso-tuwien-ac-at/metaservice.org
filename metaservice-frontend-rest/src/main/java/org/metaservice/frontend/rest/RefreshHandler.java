package org.metaservice.frontend.rest;

import com.sun.grizzly.http.embed.GrizzlyWebServer;
import com.sun.grizzly.http.servlet.ServletAdapter;
import com.sun.grizzly.tcp.http11.GrizzlyAdapter;
import com.sun.grizzly.tcp.http11.GrizzlyRequest;
import com.sun.grizzly.tcp.http11.GrizzlyResponse;
import com.sun.jersey.spi.container.servlet.ServletContainer;

import java.io.IOException;

public class RefreshHandler {
    public static void main(String[] args) throws IOException {
        GrizzlyWebServer grizzly = new GrizzlyWebServer(8088,"src/main/resources/WEB-INF");

        ServletAdapter jerseyAdapter = new ServletAdapter();
        jerseyAdapter.addInitParameter("com.sun.jersey.config.property.packages",
                "org.metaservice.rest");
        jerseyAdapter.addInitParameter("com.sun.jersey.api.json.POJOMappingFeature","true");
        jerseyAdapter.setContextPath("/rest");
        jerseyAdapter.setServletInstance(new ServletContainer());

        grizzly.addGrizzlyAdapter(jerseyAdapter, new String[]{"/rest"});
        grizzly.addGrizzlyAdapter(new GrizzlyAdapter() {
            public void service(GrizzlyRequest request, GrizzlyResponse response) {
            }
        }, new String[]{"/"});

        grizzly.start();
    }
}
