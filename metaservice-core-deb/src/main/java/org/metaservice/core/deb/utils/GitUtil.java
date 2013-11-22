package org.metaservice.core.deb.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;

public class GitUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(GitUtil.class);

    private String hashes[];
    private String messages[];
    private final File workdir;

    public GitUtil(File workdir) throws IOException, InterruptedException {
        this.workdir = workdir;
        reInit();
    }

    public void reInit() throws InterruptedException, IOException {
        Process p = execInWorkdir("git log --reverse --all --format=%H___S___%s");

        ArrayList<String> hashList = new ArrayList<String>();
        ArrayList<String> messageList = new ArrayList<String>();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(p.getInputStream()) );
        String line;
        while ((line = in.readLine()) != null) {
            String[] lineSplit = line.split("___S___");
            hashList.add(lineSplit[0]);
            messageList.add(lineSplit[1].replaceAll("\"",""));
        }
        p.waitFor();
        hashes = hashList.toArray(new String[hashList.size()]);
        messages = messageList.toArray(new String[messageList.size()]);
        LOGGER.info("READ " + hashes.length + " revisions");
    }

    public String findFirsRevisionWithMessage(String message){
        for(int i =0 ; i < messages.length;i++){
            if(messages[i].equals(message))
                return hashes[i];
        }
        return null;
    }

    public void checkOutNext() throws IOException, InterruptedException {
        checkOutNext(1);
    }

    public void checkOutNext(int n) throws IOException, InterruptedException {
        int i = getRevisionIndex(getCurrentRevision());
        if(i+n < hashes.length)
            checkOut(hashes[i+n]);
        else if(!isLast())
            checkOut(hashes[hashes.length-1]);
    }

    public void checkOut(String hash) throws IOException, InterruptedException {
        execInWorkdir("git reset --hard").waitFor();
        execInWorkdir("git checkout " + hash).waitFor();
    }

    private Process execInWorkdir(String command) throws IOException {
        LOGGER.info("Executing: " +command);
        return Runtime.getRuntime().exec(command, null, workdir);
    }

    public void checkOutPrev() throws IOException, InterruptedException {
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


    public void checkOutFirst() throws IOException, InterruptedException {
        checkOut(hashes[0]);
    }

    public boolean isLast() throws IOException,InterruptedException{
        return (getRevisionIndex(getCurrentRevision()) == hashes.length-1);
    }

    public boolean isFirst() throws IOException,InterruptedException{
        return (getRevisionIndex(getCurrentRevision()) == 0);
    }

    public String getCurrentRevision() throws IOException, InterruptedException {
        Process p = execInWorkdir("git log -1 --format=%H");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(p.getInputStream()) );
        String line  = in.readLine().trim();
        p.waitFor();
        return line;
    }

    public File[] getChangedFiles() throws IOException, InterruptedException {
        Process p = execInWorkdir("git diff --name-only HEAD HEAD~1");
        File[] changedFiles;

        ArrayList<File> list = new ArrayList<File>();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(p.getInputStream()) );
        String line;
        while ((line = in.readLine()) != null) {
          //  list.add(new File(workdir + "/" + line));
            list.add(new File(line));
        }
        p.waitFor();
        changedFiles = list.toArray(new File[list.size()]);
        return changedFiles;
    }

    public void initRepository() throws IOException, InterruptedException {
        debug(execInWorkdir("git init ."));
        debug(execInWorkdir("git commit --allow-empty -m empty"));
    }

    public boolean hasChangesToCommit() throws IOException, InterruptedException {
        Process p = execInWorkdir("git status --porcelain");
        InputStream inputStream = p.getInputStream();
        boolean dirty = (-1 != inputStream.read());
        p.waitFor();
        return dirty;
    }

    public void commitChanges(String m) throws IOException, InterruptedException {
        debug(execInWorkdir("git add -A . "));
        debug(execInWorkdir("git commit -m \"" + m + "\""));
    }

    public void gc() throws IOException, InterruptedException {
        debug(execInWorkdir("git gc"));
    }

    private void debug(@NotNull Process exec) throws IOException, InterruptedException {
        StringWriter writer = new StringWriter();
        IOUtils.copy(exec.getInputStream(), writer, "utf-8");
        String input = writer.getBuffer().toString();
        writer = new StringWriter();
        IOUtils.copy(exec.getErrorStream(), writer, "utf-8");
        String error = writer.getBuffer().toString();
        exec.getErrorStream().close();
        exec.getInputStream().close();
        int ret = exec.waitFor();
        if(ret != 0){
            LOGGER.error(input);
            LOGGER.error(error);
            throw new RuntimeException("FOOO");
        }
    }

    public String getCurrentMessage() throws IOException, InterruptedException {
        return messages[getRevisionIndex(getCurrentRevision())];
    }

    public static class Line{
        public enum ChangeType {
            NEW, OLD,UNCHANGED};
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

    public Line[] getChangeList(@NotNull String relPath) throws IOException, InterruptedException {
        return getChangeList(relPath,"HEAD");
    }

        public Line[] getChangeList(@NotNull String relPath,String revision) throws IOException, InterruptedException {

            if(relPath.startsWith("/"))
                relPath = relPath.replaceFirst("/","");

        ProcessBuilder builder =   new ProcessBuilder("git","difftool","--extcmd=diff -d --old-line-format=-%L --new-line-format=+%L --unchanged-line-format=_%L","-y",revision +"^",revision,relPath).directory(workdir);
        LOGGER.info(StringUtils.join(builder.command(), " "));
        Process p  = builder.start();


        Line[] lines;

        ArrayList<Line> list = new ArrayList<Line>();
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
    }


    public void compressRepository() throws IOException, InterruptedException {
        execInWorkdir("git repack  -af --window 250 --depth 250 --window-memory=5g");
        gc();
    }
}
