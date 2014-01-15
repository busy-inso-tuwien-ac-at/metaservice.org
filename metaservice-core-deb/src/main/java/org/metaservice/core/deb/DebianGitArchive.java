package org.metaservice.core.deb;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.metaservice.api.archive.ArchiveParameters;
import org.metaservice.api.archive.ArchiveException;
import org.metaservice.core.archive.GitArchive;
import org.metaservice.core.utils.GitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;

public class DebianGitArchive extends GitArchive {
    private final static Logger LOGGER = LoggerFactory.getLogger(DebianGitArchive.class);

    public DebianGitArchive(ArchiveParameters configuration) throws ArchiveException {
        super(configuration);
    }

    @Override
    public String getContent(String time, String path) throws ArchiveException {
        return processPath(time,new File(path));
    }

    @Override
    public String processPath(String time,File f) throws ArchiveException {
        LOGGER.info("Processing {}", f.getPath());
        try {
            if("Packages".equals(f.getName())){
                GitUtil.Line[] changes;
                //String relpath =  f.getAbsolutePath().replace(workdir.getAbsolutePath()+"/","");
                String commit = gitUtil.findFirsRevisionWithMessage(time);
                changes = gitUtil.getChangeList(f.getPath(),commit);
                String[] packageAreas = extractFullPackages(changes);
                String s =  StringUtils.join(packageAreas, "\n");
                return s;
            }
            return null;
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
