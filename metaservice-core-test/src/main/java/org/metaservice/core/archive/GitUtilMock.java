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

package org.metaservice.core.archive;

import junit.framework.Test;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaservice.api.archive.ArchiveException;
import org.metaservice.core.utils.GitUtil;

import javax.rmi.CORBA.Util;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ilo on 27.05.2014.
 */
public class GitUtilMock extends GitUtil {


    public GitUtilMock(File workdir) throws GitException {
        super(workdir);
    }


    @Override
    public String[] getCommitMessages() {
        return super.getCommitMessages();
    }

    @Override
    public void reInit() throws GitException {
        ArrayList<File> files = new ArrayList<>();
        Collections.addAll(files,workdir.listFiles());
        Collections.sort(files);
        this.messages = new String[files.size()];
        this.hashes = new String[files.size()];
        int i = 0;
        for(File f : files){
            try {
                messages[i] = TestUtils.readFile(f.toString() + "/" + ".message");
                hashes[i] = f.getName();
            } catch (IOException e) {
                throw new GitException(e);
            }
            i++;
        }
    }

    @Override
    public String findFirsRevisionWithMessage(String message) throws GitException {
        return super.findFirsRevisionWithMessage(message);
    }

    @Override
    public void checkOutNext() throws GitException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void checkOutNext(int n) throws GitException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void checkOut(String hash) throws GitException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void checkOutPrev() throws GitException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void checkOutFirst() throws GitException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isLast() throws GitException {
        return true;
    }

    @Override
    public boolean isFirst() throws GitException {
        return false;
    }

    @Override
    public String getCurrentRevision() throws GitException {
        return this.hashes[this.hashes.length-1];
    }

    @Override
    public File[] getChangedFilesInHead() throws GitException {
        return getChangedFiles(this.hashes[this.hashes.length-1]);
    }

    @Override
    public File[] getChangedFiles(String revision) throws GitException {
        ArrayList<File> result = new ArrayList<>();
        String prev = null;
        for(int i = 1; i < hashes.length;i++){
            if(hashes[i].equals(revision)){
                prev = hashes[i-1];
            }
        }
        if(prev == null){
            getChangedFilesRecursive(new File(workdir.toString()+"/"+revision),null,result);
        }else{
            getChangedFilesRecursive(new File(workdir.toString()+"/"+revision),new File(workdir.toString()+"/"+prev),result);
        }
        return result.toArray(new File[result.size()]);
    }

    private void getChangedFilesRecursive(File dirNow,File dirPrev,ArrayList<File> result){
        if(dirNow != null) {
            for (File f : dirNow.listFiles()) {
                if(f.getName().equals(".message"))
                    continue;
                if (f.isDirectory()) {
                    if (dirPrev != null){
                        File fPrev = new File(dirPrev.toString()+"/"+f.getName());
                        if(fPrev.exists() && fPrev.isDirectory()){
                           getChangedFilesRecursive(f,fPrev,result);
                        }else if(fPrev.exists() && fPrev.isFile()){
                           result.add(fPrev);
                        }else{
                            //file missing
                            result.add(f);
                        }
                    }else{
                        getChangedFilesRecursive(f,null,result);
                    }
                }else if(f.isFile()){
                    if (dirPrev != null){
                        File fPrev = new File(dirPrev.toString()+"/"+f.getName());
                        if(fPrev.exists() && fPrev.isDirectory()){
                            //recursive call to follow previously existing files
                            getChangedFilesRecursive(null,fPrev,result);
                        }else if(fPrev.exists() && fPrev.isFile()){
                            try {
                                if(!TestUtils.readFile(f).equals(TestUtils.readFile(fPrev))){
                                    //content changed
                                    result.add(f);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else{
                            //file missing
                            System.err.println("MISSING" + fPrev);
                            result.add(f);
                        }
                    }else{
                        //other path missing completely
                        result.add(f);
                    }
                }
            }
            if (dirPrev != null) {
                for (File fPrev : dirPrev.listFiles()) {
                    File f = new File(dirNow.toString()+"/"+fPrev.getName());
                    if(!f.exists()){
                        result.add(f);
                    }

                }
            }
        }else if(dirPrev !=null){
            getChangedFilesRecursive(dirPrev, null, result);
        }
    }

    @Override
    public void initRepository() throws GitException {
        throw new UnsupportedOperationException();

    }

    @Override
    public boolean hasChangesToCommit() throws GitException {
        return false;
    }

    @Override
    public void commitChanges(String m) throws GitException {
        throw new UnsupportedOperationException();

    }

    @Override
    public void gc() throws GitException {

    }

    @Override
    public String getCurrentMessage() throws GitException {
        return super.getCurrentMessage();
    }

    @Override
    public void fetch() throws GitException {

    }

    @Override
    public void checkOutLast() throws GitException {
        super.checkOutLast();
    }

    @Override
    public boolean isInitialized() throws GitException {
        return true;
    }

    @Nullable
    @Override
    public String getFileContent(@NotNull String revision, @NotNull String path) throws GitException {
        try {
            return TestUtils.readFile(workdir.toString() +"/"+revision+"/"+path);
        } catch (IOException e) {
            throw new GitException(e);
        }
    }

    @NotNull
    @Override
    public Line[] getChangeList(@NotNull String relPath, @NotNull String revision, String revision2) throws GitException {
        return super.getChangeList(relPath, revision, revision2);
    }

    @Override
    public void compressRepository() throws GitException {

    }
}
