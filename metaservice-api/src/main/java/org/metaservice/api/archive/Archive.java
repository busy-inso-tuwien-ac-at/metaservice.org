package org.metaservice.api.archive;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

public interface Archive {

    @NotNull
    String getSourceBaseUri();

    @Nullable
    String getContent(@NotNull Date time,@NotNull String path) throws ArchiveException;
    @NotNull
    List<Date> getTimes() throws ArchiveException;
    void synchronizeWithCentral() throws ArchiveException;

    void addContent(@NotNull String path,@NotNull InputStream inputStream) throws ArchiveException;
    boolean commitContent() throws ArchiveException;

    @NotNull
    Date getLastCommitTime() throws ArchiveException;

    @NotNull
    String[] getLastChangedPaths() throws ArchiveException;

    @NotNull
    String[] getChangedPaths(@NotNull Date time) throws ArchiveException;
}
