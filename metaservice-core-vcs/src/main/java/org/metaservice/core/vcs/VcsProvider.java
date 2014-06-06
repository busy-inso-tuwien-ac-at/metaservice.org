package org.metaservice.core.vcs;

import org.openrdf.model.URI;
import org.openrdf.repository.RepositoryConnection;

/**
 * Created by ilo on 01.06.2014.
 */
public interface VcsProvider {
    void process(URI uri, RepositoryConnection repositoryConnection) throws VcsException;
}
