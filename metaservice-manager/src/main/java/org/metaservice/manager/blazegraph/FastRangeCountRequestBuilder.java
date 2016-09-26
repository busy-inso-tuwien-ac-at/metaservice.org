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

package org.metaservice.manager.blazegraph;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;

import org.metaservice.manager.ManagerException;
import org.openrdf.model.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by ilo on 21.02.14.
 */
public class FastRangeCountRequestBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(FastRangeCountRequestBuilder.class);

    private Value subject;
    private Value object;
    private Value predicate;
    private Value context;

    private String path;

    public FastRangeCountRequestBuilder predicate(Value predicate){
        this.predicate = predicate;
        return this;
    }


    public FastRangeCountRequestBuilder object(Value object){
        this.object = object;
        return this;
    }


    public FastRangeCountRequestBuilder subject(Value subject){
        this.subject = subject;
        return this;
    }

    public FastRangeCountRequestBuilder context(Value context){
        this.context = context;
        return this;
    }

    public FastRangeCountRequestBuilder path(String path){
        this.path = path;
        return this;
    }

    public MutationResult execute() throws ManagerException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(MutationResult.class);

            URIBuilder uriBuilder = new URIBuilder(path);
            if(subject != null){
                uriBuilder.setParameter("s",format(subject));
            }
            if(object != null){
                uriBuilder.setParameter("o",format(object));
            }
            if(predicate != null){
                uriBuilder.setParameter("p",format(predicate));
            }
            if(context != null){
                uriBuilder.setParameter("c",format(context));
            }
            uriBuilder.addParameter("ESTCARD",null);
            URI uri = uriBuilder.build();
            LOGGER.debug("QUERY = " + uri.toString());
            String s = Request
                    .Get(uri)
                    .connectTimeout(1000)
                    .socketTimeout(10000)
                    .setHeader("Accept", "application/xml")
                    .execute()
                    .returnContent().asString();
            LOGGER.debug("RESULT = " + s);
            return (MutationResult) jaxbContext.createUnmarshaller().unmarshal(new StringReader(s));
        } catch (JAXBException | URISyntaxException | IOException e) {
            throw new ManagerException(e);
        }

    }

    private String format(Value value) {
        if(value instanceof  org.openrdf.model.URI){
            return "<"+value.stringValue()+">";
        }else{
            return  "\"" +value.stringValue()+"\"";
        }
    }

}
