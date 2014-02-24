package org.metaservice.manager.bigdata;

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
                uriBuilder.setParameter("s","<"+subject.stringValue()+">");
            }
            if(object != null){
                uriBuilder.setParameter("o","<"+object.stringValue()+">");
            }
            if(predicate != null){
                uriBuilder.setParameter("p","<"+predicate.stringValue() +">");
            }
            if(context != null){
                uriBuilder.setParameter("c","<" +context.stringValue()+">");
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

}
