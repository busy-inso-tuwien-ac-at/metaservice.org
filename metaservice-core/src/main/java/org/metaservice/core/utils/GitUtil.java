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

package org.metaservice.core.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaservice.api.MetaserviceException;
import org.metaservice.api.archive.ArchiveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

public class GitUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(GitUtil.class);

    protected String hashes[];
    protected String messages[];
    protected final File workdir;

    public GitUtil(File workdir) throws GitException {
        this.workdir = workdir;
        reInit();
    }

    public String[] getCommitMessages(){
        return messages;
    }

    public void reInit() throws GitException {
        Process p = execInWorkdir("git log --reverse --all --format=%H___S___%s");

        ArrayList<String> hashList = new ArrayList<>();
        ArrayList<String> messageList = new ArrayList<>();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(p.getInputStream()) );
        String line;
        try {
            while ((line = in.readLine()) != null) {
                String[] lineSplit = line.split("___S___");
                hashList.add(lineSplit[0]);
                messageList.add(lineSplit[1].replaceAll("\"",""));
            }
            p.waitFor();
        }  catch (IOException|InterruptedException e) {
            throw new GitException(e);
        }
        hashes = hashList.toArray(new String[hashList.size()]);
        messages = messageList.toArray(new String[messageList.size()]);
        LOGGER.info("READ " + hashes.length + " revisions");
    }

    public String findFirsRevisionWithMessage(String message) throws GitException {
        for(int i =0 ; i < messages.length;i++){
            if(messages[i].equals(message))
                return hashes[i];
        }
        //try again after re-init if this failed
        reInit();
        for(int i =0 ; i < messages.length;i++){
            if(messages[i].equals(message))
                return hashes[i];
        }
        throw new GitException("Couldn't find revission with message " + message);
    }

    public void checkOutNext() throws GitException {
        checkOutNext(1);
    }

    public void checkOutNext(int n) throws GitException {
        int i = getRevisionIndex(getCurrentRevision());
        if(i+n < hashes.length)
            checkOut(hashes[i+n]);
        else if(!isLast())
            checkOut(hashes[hashes.length-1]);
    }

    public void checkOut(String hash) throws GitException {
        try {
            execInWorkdir("git reset --hard").waitFor();
            execInWorkdir("git checkout " + hash).waitFor();
        } catch (InterruptedException e) {
            throw new GitException(e);
        }
    }

    private Process execInWorkdir(String command) throws GitException {
        LOGGER.info("Executing: " +command);
        try {
            return Runtime.getRuntime().exec(command, null, workdir);
        } catch (IOException e) {
            throw new GitException(e);
        }
    }

    public void checkOutPrev() throws GitException {
        int i = getRevisionIndex(getCurrentRevision());
        if(i -1 > 0)
            checkOut(hashes[i-1]);
    }

    private int getRevisionIndex(String currentRevision) {
        for(int i =0; i < hashes.length;i++)
            if(hashes[i].equals(currentRevision))
                return i;
        return -1;
    }


    public void checkOutFirst() throws GitException{
        checkOut(hashes[0]);
    }

    public boolean isLast() throws GitException{
        return (getRevisionIndex(getCurrentRevision()) == hashes.length-1);
    }

    public boolean isFirst() throws GitException{
        return (getRevisionIndex(getCurrentRevision()) == 0);
    }

    public String getCurrentRevision() throws GitException {
        Process p = execInWorkdir("git log -1 --format=%H");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(p.getInputStream()) );
        String line  = null;
        try {
            line = in.readLine().trim();
            p.waitFor();

        }  catch (IOException|InterruptedException e) {
            throw new GitException(e);
        }
        return line;
    }



    public File[] getChangedFilesInHead() throws GitException {
        return getChangedFiles("HEAD");
    }
    public File[] getChangedFiles(String revisison) throws GitException {
        Process p = execInWorkdir("git diff --name-only "+revisison+" "+revisison+"^");
        File[] changedFiles;

        ArrayList<File> list = new ArrayList<>();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(p.getInputStream()) );
        String line;
        try {
            while ((line = in.readLine()) != null) {
                list.add(new File(line));
            }
            p.waitFor();

        } catch (IOException | InterruptedException e) {
            throw  new GitException(e);
        }
        changedFiles = list.toArray(new File[list.size()]);
        return changedFiles;
    }

    public void initRepository() throws GitException {
        debug(execInWorkdir("git init ."));
        debug(execInWorkdir("git commit --allow-empty -m empty"));
        reInit();
    }

    public boolean hasChangesToCommit() throws GitException {
        Process p = execInWorkdir("git status --porcelain");
        InputStream inputStream = p.getInputStream();
        try{
            boolean dirty = (-1 != inputStream.read());
            p.waitFor();
            return dirty;
        }catch(IOException|InterruptedException e){
            throw new GitException(e);
        }
    }

    public void commitChanges(String m) throws GitException {
        debug(execInWorkdir("git add -A . "));
        debug(execInWorkdir("git commit -m \"" + m + "\""));
        reInit();
    }

    public void gc() throws GitException {
        debug(execInWorkdir("git gc"));
    }

    private void debug(@NotNull Process exec) throws GitException {
        try{
            ProcessUtil.debug(exec);
        }catch (InterruptedException | IOException | MetaserviceException e){
            throw new GitException(e);
        }
    }

    public String getCurrentMessage() throws GitException {
        return messages[getRevisionIndex(getCurrentRevision())];
    }

    public void fetch() throws GitException {
        debug(execInWorkdir("git fetch --all"));
        reInit();
    }

    public void checkOutLast() throws GitException {
        checkOut(hashes[hashes.length - 1]);
    }

    public boolean isInitialized() throws GitException {
        reInit();
        return (hashes.length >0);
    }

    public String[] getParentHashes(@NotNull String revision) throws GitException {
        Process process = execInWorkdir("git rev-list --parents -n 1 " + revision );
        try {
            String s = IOUtils.toString(process.getInputStream());
            debug(process);
            //ignore current
            LOGGER.info(s);
            s = s.replaceAll("^" +revision+"[^\\s]*","").trim();
            HashSet<String> parents = new HashSet<>();
            int i = 0;
            LOGGER.info(s);
            while(!s.isEmpty() && i++ < 10){
                String x = s.replaceAll("^([^\\s])+", "$0");
                 LOGGER.info("X " +x);
                if(x.length() > 1)
                    parents.add(x);
                s = s.replaceAll("^([^\\s])+","").trim();
                LOGGER.info(s);
            }
            LOGGER.info("parents: " + revision +" -> " + parents );
            return parents.toArray(new String[parents.size()]);
        } catch (IOException e) {
            throw new GitException(e);
        }
    }

    @Nullable
    public String getFileContent( @NotNull String revision,@NotNull String path) throws GitException {
        try {
            if(path.contains(workdir.getPath())){
                path = path.replaceFirst(workdir.getPath(),"");
            }
            if(path.startsWith("/")){
                path = path.replaceFirst("/","");
            }
            //todo fix it in the first place when sending...
            if(!getPathsOfRevision(revision).contains(path)){
                LOGGER.debug("doesn't exist in revision");
                return null;
            }
            Process process = execInWorkdir("git show "+revision+":"+path);
            LOGGER.info("git show "+revision+":"+path);
            String result = IOUtils.toString(process.getInputStream());
            debug(process);
            return result;
        } catch (GitException e) {
           LOGGER.debug("Process did not terminate correctly",e);
           return null;
        } catch (IOException e) {
            throw new GitException(e);
        }
    }

    private ArrayList<String> getPathsOfRevision(@NotNull String revision) throws GitException
    {
        Process process = execInWorkdir("git ls-tree -r "+revision+" --name-only");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        ArrayList<String> result =new ArrayList<>();
        String line;
        try {
            while (((line =reader.readLine()))!= null ) {
                result.add(line);
            }
            debug(process);
        } catch (IOException e) {
            throw new GitException(e);
        }
        return result;
    }


    public void clone(String s) throws GitException {
        Process process = execInWorkdir("git clone " + s +" .");
        debug(process);
        reInit();
    }

    public String[] getHashes() {
        return hashes;
    }

    public static class Line{
        public enum ChangeType {
            NEW, OLD,UNCHANGED}

        public ChangeType changeType;
        public String line;
        public Line(){
        }

        public Line(ChangeType changeType, String line) {
            this.changeType = changeType;
            this.line = line;
        }

        @NotNull
        @Override
        public String toString() {
            return (changeType == ChangeType.UNCHANGED)?" ":(changeType == ChangeType.NEW)?"+":"-";
        }
    }

    @NotNull
    public Line[] getChangeList(@NotNull String relPath,@NotNull String revision,String revision2) throws GitException {
        try{
            if(relPath.startsWith("/"))
                relPath = relPath.replaceFirst("/","");

            ProcessBuilder builder =   new ProcessBuilder("git","difftool","--extcmd=diff -d --old-line-format=-%L --new-line-format=+%L --unchanged-line-format=_%L","-y",revision2,revision,relPath).directory(workdir);
            LOGGER.info(StringUtils.join(builder.command(), " "));
            Process p  = builder.start();


            Line[] lines;

            ArrayList<Line> list = new ArrayList<>();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));


            int c;
            while ((c = in.read()) != -1) {
                Line l = new Line();
                switch (c){
                    case '+':
                        l.changeType = Line.ChangeType.NEW;
                        break;
                    case '-':
                        l.changeType = Line.ChangeType.OLD;
                        break;
                    case '_':
                        l.changeType = Line.ChangeType.UNCHANGED;
                        break;
                    default:
                        throw new RuntimeException("INVALID OUTPUT FORMAT '" +(char) c +"'" + in.readLine());
                }
                //readline alternative which does only halt on \n not \r
                //it also discards \r
                StringBuilder b = new StringBuilder(300);
                while((c = in.read()) != '\n'){
                    if(c == -1)
                        break;
                    if(c == '\r')
                        continue;
                    b.append((char)c);
                }
                l.line = b.toString();
                list.add(l);
            }
            debug(p);
            lines = list.toArray(new Line[list.size()]);
            LOGGER.debug("READ {} lines",lines.length);
            return lines;
        }catch (IOException e){
            throw new GitException(e);
        }
    }


    public void compressRepository() throws GitException {
        execInWorkdir("git repack  -af --window 250 --depth 250 --window-memory=5g");
        gc();
    }


    public static class GitException extends Exception{
        public GitException() {
        }

        public GitException(String message) {
            super(message);
        }

        public GitException(String message, Throwable cause) {
            super(message, cause);
        }

        public GitException(Throwable cause) {
            super(cause);
        }

        public GitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
