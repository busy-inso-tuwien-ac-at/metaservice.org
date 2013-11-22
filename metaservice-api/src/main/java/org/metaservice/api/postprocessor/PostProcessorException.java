package org.metaservice.api.postprocessor;

import org.metaservice.api.MetaserviceException;

public class PostProcessorException extends MetaserviceException {
    public PostProcessorException() {
    }

    public PostProcessorException(String message) {
        super(message);
    }

    public PostProcessorException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostProcessorException(Throwable cause) {
        super(cause);
    }

    public PostProcessorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
