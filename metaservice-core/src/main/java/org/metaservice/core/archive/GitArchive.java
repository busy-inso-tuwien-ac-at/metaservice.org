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

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaservice.api.archive.Archive;
import org.metaservice.api.archive.ArchiveParameters;
import org.metaservice.api.archive.ArchiveException;
import org.metaservice.core.utils.GitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GitArchive implements Archive {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitArchive.class);
    protected final GitUtil gitUtil;
    protected final File workdir;
    protected final String sourceBaseUri;
    protected final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");



    public GitArchive(@NotNull ArchiveParameters archiveParameters) throws ArchiveException {
        this.workdir = archiveParameters.getDirectory();
        try {
        if(!this.workdir.exists()) {
            LOGGER.info("directory {} doesn't exist -> creating it",this.workdir.toString());
            Files.createDirectories(this.workdir.toPath());
        }
        this.sourceBaseUri = archiveParameters.getSourceBaseUri();

            gitUtil= new GitUtil(workdir);
            if(!gitUtil.isInitialized())
                gitUtil.initRepository();
        } catch (GitUtil.GitException | IOException e) {
            throw new ArchiveException(e);
        }
    }

    @NotNull
    @Override
    public String getSourceBaseUri() {

        return sourceBaseUri;
    }



    @Nullable
    @Override
    /**
     * Path must be of form /asdf/asd
     */
    public Contents getContent(@NotNull Date time,@NotNull String path) throws ArchiveException {
        try {
            String revision = gitUtil.findFirsRevisionWithMessage(dateFormat.format(time));
            LOGGER.info("FOUND REVISION: " + revision);
            if(revision!= null){
                Contents contents = new Contents();
                String s = processPath(revision,new File( path));
                String s2 = processPath(revision+"^",new File(path));
                if(s != null && s.length()> 20) {
                    contents.now = new StringReader(s);
                }
                if(s2 != null && s2.length()> 20) {
                    contents.prev = new StringReader(s2);
                }
                return contents;
            }
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        } catch (GitUtil.GitException e) {
            throw new ArchiveException(e);
        }
    }

    @Nullable
    public String processPath(@NotNull String commit,@NotNull File f) throws ArchiveException {
        try {
            f = new File(workdir.getAbsolutePath() + "/" + f.getPath());
            LOGGER.info("Processing {}", f.getAbsolutePath());
            return gitUtil.getFileContent(commit, f.getPath());
        }catch (GitUtil.GitException e){
            throw new ArchiveException(e);
        }
    }

    @Override
    @NotNull
    public List<Date> getTimes() throws ArchiveException{
        ArrayList<Date> result = new ArrayList<>();
        for(String s: gitUtil.getCommitMessages()){
            try {
                if(!"empty".equals(s))
                    result.add(dateFormat.parse(s));
            } catch (ParseException e) {
                LOGGER.error("Could not parse message '{}'",s,e);
            }
        }
        return result;
    }

    @Override
    public void synchronizeWithCentral() throws ArchiveException {
        try {
            gitUtil.fetch();
        } catch (GitUtil.GitException e) {
            throw new ArchiveException();
        }
    }

    @Override
    public void addContent(@NotNull String path,@NotNull InputStream inputStream) throws ArchiveException {
        ensureOnHead();

        File outFile = new File(workdir.getAbsolutePath()  +"/"+ path);
        LOGGER.info("writing " + outFile);
        if(!outFile.getParentFile().exists())
            outFile.getParentFile().mkdirs();
        try (
                FileOutputStream outputStream = new FileOutputStream(outFile)
        ){
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ensureOnHead() throws ArchiveException {
        try {
            if(!gitUtil.isLast())
                gitUtil.checkOutLast();
        } catch (GitUtil.GitException e) {
            throw new ArchiveException(e);
        }
    }

    @Override
    public boolean commitContent() throws ArchiveException{
        try {
            //todo maybe use a specified date?
            String date = dateFormat.format(new Date());
            if(gitUtil.hasChangesToCommit()){
                gitUtil.commitChanges(date);
                return true;
            }
            return false;
        } catch (GitUtil.GitException e) {
            throw new ArchiveException(e);
        }
    }

    @Override
    @NotNull
    public Date getLastCommitTime() throws ArchiveException {
        try {
            return dateFormat.parse(gitUtil.getCurrentMessage());
        } catch (GitUtil.GitException | ParseException e) {
            throw new ArchiveException(e);
        }
    }

    @Override
    @NotNull
    public String[] getLastChangedPaths() throws ArchiveException{
        try {
            ArrayList<String> res = new ArrayList<>();
            for(File f: gitUtil.getChangedFilesInHead()){
                res.add(f.getPath());
            }
            return res.toArray(new String[res.size()]);
        } catch (GitUtil.GitException e) {
            throw new ArchiveException(e);
        }
    }

    @Override
    @NotNull
    public String[] getChangedPaths(@NotNull Date commitTime) throws ArchiveException {
        try {
            String revision = gitUtil.findFirsRevisionWithMessage(dateFormat.format(commitTime));
            ArrayList<String> res = new ArrayList<>();
            for(File f: gitUtil.getChangedFiles(revision)){
                res.add(f.getPath());
            }
            return res.toArray(new String[res.size()]);
        } catch (GitUtil.GitException e) {
            throw new ArchiveException(e);
        }
    }
}
