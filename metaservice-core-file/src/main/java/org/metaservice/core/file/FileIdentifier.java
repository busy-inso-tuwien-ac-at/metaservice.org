package org.metaservice.core.file;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 * Created by ilo on 29.05.2014.
 */
public class FileIdentifier {
    private final String sha1sum;
    private final long size;

    public FileIdentifier(String sha1sum, long size) {
        this.sha1sum = sha1sum;
        this.size = size;
    }

    public String getSha1sum() {
        return sha1sum;
    }

    public long getSize() {
        return size;
    }

    public URI getUri(){
        return ValueFactoryImpl.getInstance().createURI("http://metaservice.org/d/files/"+size+"/"+sha1sum);
    }
}
