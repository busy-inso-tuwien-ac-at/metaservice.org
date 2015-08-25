package org.metaservice.api.messaging.dispatcher;

/**
 * Created by ilo on 23.07.2014.
 */
public interface Callback {
    void handleOk();
    void handleException(Exception e);
}
