package org.metaservice.core.archive;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaservice.api.archive.ArchiveException;
import org.metaservice.api.archive.ArchiveParameters;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class GitArchiveFileMock extends GitArchive {

    public GitArchiveFileMock(@NotNull ArchiveParameters archiveParameters) throws ArchiveException {
        super(archiveParameters);
    }


    @NotNull
    @Override
    public String getSourceBaseUri() {
        return super.getSourceBaseUri();
    }

    @Nullable
    @Override
    public Contents getContent(@NotNull Date time, @NotNull String path) throws ArchiveException {
        return super.getContent(time, path);
    }

    @Nullable
    @Override
    public String processPath(@NotNull String commit, @NotNull File f) throws ArchiveException {
        return super.processPath(commit, f);
    }

    @NotNull
    @Override
    public List<Date> getTimes() throws ArchiveException {
        return super.getTimes();
    }

    @Override
    public void synchronizeWithCentral() throws ArchiveException {
        super.synchronizeWithCentral();
    }

    @Override
    public void addContent(@NotNull String path, @NotNull InputStream inputStream) throws ArchiveException {
        super.addContent(path, inputStream);
    }

    @Override
    public boolean commitContent() throws ArchiveException {
        return super.commitContent();
    }

    @NotNull
    @Override
    public Date getLastCommitTime() throws ArchiveException {
        return super.getLastCommitTime();
    }

    @NotNull
    @Override
    public String[] getLastChangedPaths() throws ArchiveException {
        return super.getLastChangedPaths();
    }

    @NotNull
    @Override
    public String[] getChangedPaths(@NotNull Date commitTime) throws ArchiveException {
        return super.getChangedPaths(commitTime);
    }
}