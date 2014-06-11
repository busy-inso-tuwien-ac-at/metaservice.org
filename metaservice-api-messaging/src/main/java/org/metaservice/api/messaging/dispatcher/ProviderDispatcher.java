package org.metaservice.api.messaging.dispatcher;

import org.metaservice.api.archive.ArchiveAddress;
import org.openrdf.model.URI;

/**
 * Created by ilo on 09.06.2014.
 */
public interface ProviderDispatcher {
    void refresh(URI uri);

    void create(ArchiveAddress archiveAddress);
}
