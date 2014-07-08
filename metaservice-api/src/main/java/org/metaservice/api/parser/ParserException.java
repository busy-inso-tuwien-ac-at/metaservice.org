package org.metaservice.api.parser;

import org.metaservice.api.MetaserviceException;

/**
 * Created by ilo on 03.07.2014.
 */
public class ParserException extends MetaserviceException {
    public ParserException() {
    }

    public ParserException(String message) {
        super(message);
    }

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParserException(Throwable cause) {
        super(cause);
    }

    public ParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
