package org.metaservice.kryo.beans;

import org.metaservice.api.archive.ArchiveAddress;

/**
 * Created by ilo on 07.06.2014.
 */
public class ProviderCreateMessage extends AbstractMessage{
    private ArchiveAddress archiveAddress;

    public ArchiveAddress getArchiveAddress() {
        return archiveAddress;
    }

    public void setArchiveAddress(ArchiveAddress archiveAddress) {
        this.archiveAddress = archiveAddress;
    }
}
