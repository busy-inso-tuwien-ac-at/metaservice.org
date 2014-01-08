package org.metaservice.api.archive;

import java.io.InputStream;
import java.util.List;

public interface Archive {

    String getSourceBaseUri();

    String getContent(String time, String path) throws ArchiveException;
    List<String> getTimes() throws ArchiveException;
    void synchronizeWithCentral() throws ArchiveException;

    void addContent(String path, InputStream inputStream) throws ArchiveException;
    void commitContent() throws ArchiveException;

    String getLastCommitTime() throws ArchiveException;

    String[] getLastChangedPaths() throws ArchiveException;
}
