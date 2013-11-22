package org.metaservice.core.deb;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.metaservice.core.deb.utils.GitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DebianGitCacheArchive extends GitCacheArchive {
    private final static Logger LOGGER = LoggerFactory.getLogger(DebianGitCacheArchive.class);

    @Inject
    public DebianGitCacheArchive() throws IOException, InterruptedException {
        super(new File("/opt/crawlertest/data_all_i386_amd64/"));
    }

    @Override
    public String getContent(String time, String path) {
        return processPath(time,new File(path));
    }

    @Override
    public String processPath(String time,File f) {
        LOGGER.info("Processing {}", f.getPath());
        try {
            if("Packages".equals(f.getName())){
                GitUtil.Line[] changes;
                //String relpath =  f.getAbsolutePath().replace(workdir.getAbsolutePath()+"/","");
                changes = gitUtil.getChangeList(f.getPath(),gitUtil.findFirsRevisionWithMessage(time));
                String[] packageAreas = extractFullPackages(changes);
                String s =  StringUtils.join(packageAreas, "\n");
                return s;
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }


    public static String[] extractFullPackages(@NotNull GitUtil.Line[] changes) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<GitUtil.Line> cleaned = new ArrayList<GitUtil.Line>();
        for(int i = 0; i < changes.length;i++){
            if(changes[i].changeType != GitUtil.Line.ChangeType.OLD){
                cleaned.add(changes[i]);
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
                    //from Packagestart to here set unchagne to new
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
