package org.metaservice.core.deb;

import org.junit.Test;
import org.metaservice.core.utils.GitUtil;

/**
 * Created with IntelliJ IDEA.
 * User: ilo
 * Date: 22.10.13
 * Time: 00:11
 * To change this template use File | Settings | File Templates.
 */
public class GitCacheReaderTest {
    @Test
    public void   testChangeExpansion(){
          GitUtil.Line[] array = new GitUtil.Line[]{
                  new GitUtil.Line(GitUtil.Line.ChangeType.UNCHANGED,"Package: fooo long"),
                  new GitUtil.Line(GitUtil.Line.ChangeType.UNCHANGED,"lirum apsum"),
                  new GitUtil.Line(GitUtil.Line.ChangeType.NEW,"lirum larum"),
                  new GitUtil.Line(GitUtil.Line.ChangeType.UNCHANGED,"dd"),
                  new GitUtil.Line(GitUtil.Line.ChangeType.UNCHANGED,"asdfasdf"),
                  new GitUtil.Line(GitUtil.Line.ChangeType.UNCHANGED,"adsf"),
                  new GitUtil.Line(GitUtil.Line.ChangeType.OLD,"asdf HIDDNE"),
                  new GitUtil.Line(GitUtil.Line.ChangeType.UNCHANGED,"end"),
                  new GitUtil.Line(GitUtil.Line.ChangeType.UNCHANGED,""),
                  new GitUtil.Line(GitUtil.Line.ChangeType.NEW,"Package: long foo"),
                  new GitUtil.Line(GitUtil.Line.ChangeType.OLD,"kading"),
                  new GitUtil.Line(GitUtil.Line.ChangeType.UNCHANGED,"end2"),
                  new GitUtil.Line(GitUtil.Line.ChangeType.UNCHANGED,"Package: long foo"),
                  new GitUtil.Line(GitUtil.Line.ChangeType.UNCHANGED,"Package: long foo"),
                  new GitUtil.Line(GitUtil.Line.ChangeType.NEW,"Package: END"),

          };
        for (String s : GitCacheReader.extractFullPackages(array)){
            System.err.println(s);
        }


    }
}
