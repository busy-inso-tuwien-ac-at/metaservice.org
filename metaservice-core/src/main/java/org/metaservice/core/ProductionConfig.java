package org.metaservice.core;

import org.apache.activemq.ActiveMQConnection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductionConfig implements Config{

    @Override
    public String getSparqlEndpoint() {
        return "http://metaservice.org/sparql";
    }

    @Override
    public String getJmsBroker() {
        return ActiveMQConnection.DEFAULT_BROKER_URL;
    }

    @Override
    public List<String> getArchivesForProvider(String provider) {
        switch (provider){
            case "org.metaservice.core.deb.DebianPackageProvider":
                return Arrays.asList(
                        "http://ftp.debian.org/debian/",
                        "http://security.debian.org/"
                        );
        }
        return new ArrayList<>();
    }

    @Override
    public String getArchiveBasePath() {
        return "/opt/metaservice_data/";
    }

    @Override
    public String getHttpdDataDirectory() {
        return "/var/www/";
    }
}
