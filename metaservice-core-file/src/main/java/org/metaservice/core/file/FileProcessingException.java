package org.metaservice.core.file;

import java.io.IOException;

/**
 * Created by ilo on 29.05.2014.
 */
public class FileProcessingException extends Exception {
    public FileProcessingException(IOException e) {
        super(e);
    }
}
