package org.metaservice.kryo.beans;

/**
 * Created by ilo on 07.06.2014.
 */
public class ProviderRefreshMessage extends AbstractMessage {

    private String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
