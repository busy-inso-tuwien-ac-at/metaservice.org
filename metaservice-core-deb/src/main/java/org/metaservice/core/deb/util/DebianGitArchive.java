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
                GitUtil.Line[] changes;
                //String relpath =  f.getAbsolutePath().replace(workdir.getAbsolutePath()+"/","");
                changes = gitUtil.getChangeList(f.getPath(),revision,revision+"^");
                String[] packageAreas = extractFullPackages(changes);
                changes= null;
                contents.now  = StringUtils.join(packageAreas, "\n");
                changes = gitUtil.getChangeList(f.getPath(),revision+"^",revision);
                packageAreas = extractFullPackages(changes);
                changes= null;
                contents.prev  = StringUtils.join(packageAreas, "\n");
                return contents;
            }
            return null;//todo NOTNULL?
        } catch (GitUtil.GitException e) {
            throw new ArchiveException(e);
        }
    }



    public static String[] extractFullPackages(@NotNull GitUtil.Line[] changes) {
        ArrayList<String> result = new ArrayList<>();
        ArrayList<GitUtil.Line> cleaned = new ArrayList<>();
        for (GitUtil.Line change : changes) {
            if (change.changeType != GitUtil.Line.ChangeType.OLD) {
                cleaned.add(change);
            }
        }
        changes = cleaned.toArray(new GitUtil.Line[cleaned.size()]);


        int packageStart = 0;
        for(int i = 0; i < changes.length;i++){
            if(changes[i].line.startsWith("Package: ") &&
                    (changes[i].changeType == GitUtil.Line.ChangeType.UNCHANGED ||changes[i].changeType == GitUtil.Line.ChangeType.NEW))
                packageStart = i;
            switch (changes[i].changeType){
                case UNCHANGED:
                    break;
                case NEW:
                    //from Packagestart to here set unchange to new
                    for(int j = packageStart; j < i;j++){
                        if(changes[j].changeType == GitUtil.Line.ChangeType.UNCHANGED)
                            changes[j].changeType = GitUtil.Line.ChangeType.NEW;
                    }

                    while (i < changes.length){
                        if(changes[i].changeType == GitUtil.Line.ChangeType.UNCHANGED)
                            changes[i].changeType = GitUtil.Line.ChangeType.NEW;
                        if( i+ 1 == changes.length || changes[i+1].line.startsWith("Package: ")){
                            break;
                        }
                        i++;
                    }
            }
        }
        StringBuilder b = new StringBuilder();
        for(int i = 0; i < changes.length;i++){
            if(changes[i].changeType == GitUtil.Line.ChangeType.NEW){
                b.append(changes[i].line);
                b.append('\n');
                if(i+1 >= changes.length || changes[i+1].changeType != GitUtil.Line.ChangeType.NEW){
                    result.add(b.toString());
                    b = new StringBuilder();
                }
            }
        }
        return result.toArray(new String[result.size()]);
    }
}
