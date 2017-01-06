/*
 * Copyright 2015 Nikola Ilo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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