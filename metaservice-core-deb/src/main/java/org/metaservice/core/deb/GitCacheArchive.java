package org.metaservice.core.deb;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.metaservice.api.archive.Archive;
import org.metaservice.core.deb.utils.GitUtil;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class GitCacheArchive  implements Archive {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitCacheArchive.class);
    protected final GitUtil gitUtil;
    protected final File workdir;


    public GitCacheArchive(File workdir) throws IOException, InterruptedException {
        this.workdir = workdir;
        gitUtil= new GitUtil(workdir);
    }

    @Override
    /**
     * Path must be of form /asdf/asd
     */
    public String getContent(String time, String path) {
        try {
            String revision =gitUtil.findFirsRevisionWithMessage(time);
            LOGGER.info("FOUND REVISION: " + revision);
            if(revision!= null){
                gitUtil.checkOut(revision); //todo check null
                return processPath(time,new File( path));
            }
        } catch (IOException | InterruptedException e) {
            LOGGER.error("Error during getContent({},{})",time,path,e);
        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String processPath(String time, File f){
        f = new File(workdir.getAbsolutePath() +  f.getPath());
        LOGGER.info("Processing {}", f.getAbsolutePath());
        try (  FileReader reader = new FileReader(f)) {
            return IOUtils.toString(reader);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }
    }


    private void parseAllFiles(String currentRevision) throws RepositoryException, IOException, InterruptedException {
        for(File f : FileUtils.listFiles(workdir, null, true)){
            if("Packages".equals(f.getName())){
                LOGGER.info("All: {} ",f) ;
            //    parseFile(currentRevision,f);
            }

        }
    }

    private void parseChangedFiles(String currentRevision) throws RepositoryException, IOException, InterruptedException {
        for(File f: gitUtil.getChangedFiles()){
            if("Packages".equals(f.getName())){
                LOGGER.info("Changed: {} ",f) ;
              //  parseFile(currentRevision,f);
            }
        }
    }

    @Override
    public List<String> getTimes() {
        return null;
    }
}
