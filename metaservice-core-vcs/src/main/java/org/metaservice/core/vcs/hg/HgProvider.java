package org.metaservice.core.vcs.hg;

import org.metaservice.core.vcs.VcsProvider;
import org.openrdf.model.URI;
import org.openrdf.repository.RepositoryConnection;

/**
 * Created by ilo on 01.06.2014.
 */
public class HgProvider implements VcsProvider {
    @Override
    public void process(URI uri, RepositoryConnection repositoryConnection) {
        throw new UnsupportedOperationException();
    }
}
