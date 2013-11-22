package org.metaservice.api;

/**
 * Created with IntelliJ IDEA.
 * User: ilo
 * Date: 21.11.13
 * Time: 21:12
 * To change this template use File | Settings | File Templates.
 */
public class MetaserviceException extends Exception {
    public MetaserviceException() {
    }

    public MetaserviceException(String message) {
        super(message);
    }

    public MetaserviceException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetaserviceException(Throwable cause) {
        super(cause);
    }

    public MetaserviceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
