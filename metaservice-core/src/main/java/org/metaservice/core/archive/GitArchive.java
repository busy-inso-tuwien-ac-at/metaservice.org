package org.metaservice.core.archive;

import org.apache.commons.io.IOUtils;
import org.metaservice.api.archive.Archive;
import org.metaservice.api.archive.ArchiveParameters;
import org.metaservice.api.archive.ArchiveException;
import org.metaservice.core.utils.GitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class GitArchive implements Archive {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitArchive.class);
    protected final GitUtil gitUtil;
    protected final File workdir;
    protected final String sourceBaseUri;


    public GitArchive(ArchiveParameters archiveParameters) throws ArchiveException {
        this.workdir = archiveParameters.getDirectory();
        this.sourceBaseUri = archiveParameters.getSourceBaseUri();
        try {
            gitUtil= new GitUtil(workdir);
            if(!gitUtil.isInitialized())
                gitUtil.initRepository();
        } catch (GitUtil.GitException e) {
            throw new ArchiveException(e);
        }
    }

    @Override
    public String getSourceBaseUri() {

        return sourceBaseUri;
    }

    @Override
    /**
     * Path must be of form /asdf/asd
     */
    public String getContent(String time, String path) throws ArchiveException {
        String revision = null;
        try {
            revision = gitUtil.findFirsRevisionWithMessage(time);
            LOGGER.info("FOUND REVISION: " + revision);
            if(revision!= null){
                return processPath(time,new File( path));
            }
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        } catch (GitUtil.GitException e) {
            throw new ArchiveException(e);
        }
    }

    public String processPath(String time, File f) throws ArchiveException {
        try {
            f = new File(workdir.getAbsolutePath() + "/"+  f.getPath());
        LOGGER.info("Processing {}", f.getAbsolutePath());
            return gitUtil.getFileContent(gitUtil.findFirsRevisionWithMessage(time),f.getPath());
        } catch (GitUtil.GitException e) {
            throw new ArchiveException(e);
        }

    }

                           /*these are not correct anymore as they depend on currently checked out files
    private void parseAllFiles(String currentRevision) throws RepositoryException, IOException, InterruptedException {
        for(File f : FileUtils.listFiles(workdir, null, true)){
            if("Packages".equals(f.getName())){
                LOGGER.info("All: {} ",f) ;
            //    parseFile(currentRevision,f);
            }

        }
    }

    private void parseChangedFiles(String currentRevision) throws GitUtil.GitException {
        for(File f: gitUtil.getChangedFilesInHead()){
            if("Packages".equals(f.getName())){
                LOGGER.info("Changed: {} ",f) ;
              //  parseFile(currentRevision,f);
            }
        }
    }
                             */
    @Override
    public List<String> getTimes() throws ArchiveException{
        return Arrays.asList(gitUtil.getCommitMessages());
    }

    @Override
    public void synchronizeWithCentral() throws ArchiveException {
        try {
            gitUtil.pull();
        } catch (GitUtil.GitException e) {
            throw new ArchiveException();
        }
    }

    @Override
    public void addContent(String path, InputStream inputStream) throws ArchiveException {
        ensureOnHead();

        File outFile = new File(workdir.getAbsolutePath()  +"/"+ path);
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
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
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
    public String getLastCommitTime() throws ArchiveException {
        try {
            return gitUtil.getCurrentMessage();
        } catch (GitUtil.GitException e) {
            throw new ArchiveException(e);
        }
    }

    @Override
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
    public String[] getChangedPaths(String commitTime) throws ArchiveException {
        try {
           String revision = gitUtil.findFirsRevisionWithMessage(commitTime);
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
