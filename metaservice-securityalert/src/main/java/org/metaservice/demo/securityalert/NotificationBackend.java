package org.metaservice.demo.securityalert;

import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ilo on 24.06.2014.
 */
public class NotificationBackend {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationBackend.class);

    private final HashMap<String,List<String>> mapping = new HashMap<>();
    private final HashMap<String,List<String>> notified= new HashMap<>();
    @Inject
    public NotificationBackend(){
        ArrayList<String> apiKeys = new ArrayList<>();
        apiKeys.add("3be819abf65d965cc64d87a931d845ca87cd2a3757beb7b9");
        mapping.put("http://metaservice.org/d/projects/wordpress",apiKeys);
    }



    public synchronized void notify(String project, String projectName, String cve){

        List<String> alreadyNotified = notified.get(project);
        if(alreadyNotified != null && alreadyNotified.contains(cve)){
            LOGGER.info("already notified");
            return;
        }
        List<String> keys = mapping.get(project);
        if(keys != null){
            for(String api : keys){
                send(api,"Attention "+ projectName +" affected by a CVE",cve);
            }
        }
        if(alreadyNotified == null)
        {
            alreadyNotified = new ArrayList<>();
            notified.put(project,alreadyNotified);
        }
        alreadyNotified.add(cve);

    }

    public void send(String api, String s, String cve) {
        LOGGER.info("Sending {} {}",s,cve);
        URIBuilder uriBuilder = new URIBuilder();


        uriBuilder.setScheme("http")
                .setHost("www.notifymyandroid.com")
                .setPort(80)
                .setPath("/publicapi/notify");

        try {
            String result =  Request.Post(uriBuilder.build())
                    .bodyForm(Form.form()
                                    .add("apikey", api)
                                    .add("event", s)
                                    .add("application","metaservice.org")
                                    .add("priority", "2")
                                    .add("description", s)
                                    .add("url", cve)
                                    .build()
                    )
                    .connectTimeout(1000)
                    .socketTimeout(30000)
                   // .setHeader("Accept", mimeType)
                    .execute()
                    .returnContent().asString();
            LOGGER.info(result);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
