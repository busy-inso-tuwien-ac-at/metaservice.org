package org.metaservice.core.archive;

import org.metaservice.api.archive.ArchiveParameters;

import java.io.File;

/**
 * Created by ilo on 05.01.14.
 */
public class ArchiveParametersImpl implements ArchiveParameters {
    private String uri;
    private File directory;

    public ArchiveParametersImpl(String uri, File directory) {
        this.uri = uri;
        this.directory = directory;
    }

    @Override
    public String getSourceBaseUri() {
        return uri;
    }

    @Override
    public File getDirectory() {
        return directory;
    }
}
