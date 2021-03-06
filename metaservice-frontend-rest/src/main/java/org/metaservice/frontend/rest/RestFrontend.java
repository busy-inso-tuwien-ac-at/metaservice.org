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

import com.sun.grizzly.http.embed.GrizzlyWebServer;
import com.sun.grizzly.http.servlet.ServletAdapter;
import com.sun.grizzly.tcp.http11.GrizzlyAdapter;
import com.sun.grizzly.tcp.http11.GrizzlyRequest;
import com.sun.grizzly.tcp.http11.GrizzlyResponse;
import com.sun.jersey.spi.container.servlet.ServletContainer;

import java.io.IOException;

public class RestFrontend {
    public static void main(String[] args) throws IOException {
        GrizzlyWebServer grizzly = new GrizzlyWebServer(8088,"src/main/resources/WEB-INF");

        ServletAdapter jerseyAdapter = new ServletAdapter();
        jerseyAdapter.addInitParameter("com.sun.jersey.config.property.packages",
                "org.metaservice.frontend.rest");
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
