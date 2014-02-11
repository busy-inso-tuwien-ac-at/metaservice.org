package org.metaservice.core.management;

import org.metaservice.api.MetaserviceException;

/**
 * Created by ilo on 11.02.14.
 */
public class ManagerException extends MetaserviceException {
    public ManagerException() {
    }

    public ManagerException(String message) {
        super(message);
    }

    public ManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ManagerException(Throwable cause) {
        super(cause);
    }

    public ManagerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
