package org.metaservice.frontend.rest.api;

import java.io.InputStream;
import java.util.Calendar;

/**
 * Created by ilo on 02.09.2015.
 */
public interface ResourceService {
    InputStream getResource(String resource,Calendar date,String mimetype);
}
