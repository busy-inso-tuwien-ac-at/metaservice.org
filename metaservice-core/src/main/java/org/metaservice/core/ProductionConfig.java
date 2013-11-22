package org.metaservice.core;

import org.apache.activemq.ActiveMQConnection;

public class ProductionConfig implements Config{

    @Override
    public String getSparqlEndpoint() {
        return "http://metaservice.org/sparql";
    }

    @Override
    public String getJmsBroker() {
        return ActiveMQConnection.DEFAULT_BROKER_URL;
    }
}
