package org.metaservice.core.vcs;

import org.metaservice.api.MetaserviceException;

/**
 * Created by ilo on 01.06.2014.
 */
public class VcsException extends MetaserviceException {
    public VcsException(Exception e) {
        super(e);
    }
}
