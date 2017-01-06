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

package org.metaservice.core.deb.util;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaservice.api.archive.ArchiveParameters;
import org.metaservice.api.archive.ArchiveException;
import org.metaservice.core.archive.GitArchive;
import org.metaservice.core.utils.GitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;

public class DebianGitArchive extends GitArchive {
    private final static Logger LOGGER = LoggerFactory.getLogger(DebianGitArchive.class);

    public DebianGitArchive(ArchiveParameters configuration) throws ArchiveException {
        super(configuration);
    }

    @NotNull
    @Override
    public Contents getContent(@NotNull Date time, @NotNull String path) throws ArchiveException {
        String revision = null;
        try {
            revision = gitUtil.findFirsRevisionWithMessage(dateFormat.format(time));
            LOGGER.info("FOUND REVISION: " + revision);
            if(revision!= null){
                return getContents(revision,new File( path));
            }
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        } catch (GitUtil.GitException e) {
            throw new ArchiveException(e);
        }
    }

    private  Contents getContents(String revision, File f) throws ArchiveException {
        LOGGER.info("Processing {}", f.getPath());
        try {
            if("Packages".equals(f.getName())){
                Contents contents =new Contents();
                GitUtil.Line[] changes = gitUtil.getChangeList(f.getPath(),revision,revision+"^");
                String packageAreas = extractFullPackages(changes, GitUtil.Line.ChangeType.NEW, GitUtil.Line.ChangeType.OLD, GitUtil.Line.ChangeType.UNCHANGED);
                LOGGER.info("now: " + packageAreas.length());
                if(packageAreas.length() > 10) {
                    contents.now = new StringReader(packageAreas);
                }else{
                    LOGGER.info("setting null because size to small");
                }
                packageAreas = extractFullPackages(changes, GitUtil.Line.ChangeType.OLD, GitUtil.Line.ChangeType.NEW, GitUtil.Line.ChangeType.UNCHANGED);
                if(packageAreas.length() > 10) {
                    contents.prev = new StringReader(packageAreas);
                }else{
                    LOGGER.info("setting null because size to small");
                }
                LOGGER.info("prev: " + packageAreas.length());
                return contents;
            }
            return null;//todo NOTNULL?
        } catch (GitUtil.GitException e) {
            throw new ArchiveException(e);
        }
    }



    public static String extractFullPackages(@NotNull GitUtil.Line[] changes,GitUtil.Line.ChangeType wanted, GitUtil.Line.ChangeType notWanted, GitUtil.Line.ChangeType neutral) {
        ArrayList<String> result = new ArrayList<>();
        ArrayList<GitUtil.Line> cleaned = new ArrayList<>();
        for (GitUtil.Line change : changes) {
            if (change.changeType != GitUtil.Line.ChangeType.OLD) {
                cleaned.add(change);
            }
        }
        changes = cleaned.toArray(new GitUtil.Line[cleaned.size()]);
        ArrayList<GitUtil.Line> resultList = new ArrayList<>();
        int packageStart = 0;
        for(int i = 0; i < changes.length;i++){
            if(changes[i].line.startsWith("Package: ") &&
                    (changes[i].changeType == neutral ||changes[i].changeType == wanted))
                packageStart = i;
            if(changes[i].changeType == neutral){
                break;
            }else if (changes[i].changeType == wanted){
                //from Packagestart to here set unchange to wanted
                for(int j = packageStart; j < i;j++){
                    resultList.add(changes[j]);
                }

                while (i < changes.length){
                    resultList.add(changes[i]);
                    if( i+ 1 == changes.length || changes[i+1].line.startsWith("Package: ")){
                        break;
                    }
                    i++;
                }
            }
        }
        StringBuilder b = new StringBuilder();
        for(GitUtil.Line line : resultList){
            b.append(line.line);
            b.append('\n');
        }
        return b.toString();
    }
}
