/*
 * Copyright 2015 Nikola Ilo
 * Research Group for Industrial Software, Vienna University of Technology, Austria
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

package org.metaservice.api.archive;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.Reader;
import java.util.Date;
import java.util.List;

public interface Archive {

    @NotNull
    String getSourceBaseUri();

    public static class Contents{
        public Reader now;
        public Reader prev;
    }

    @Nullable
    Contents getContent(@NotNull Date time,@NotNull String path) throws ArchiveException;
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
