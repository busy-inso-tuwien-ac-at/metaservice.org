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

package org.metaservice.core.deb.parser.ast;

import org.jetbrains.annotations.NotNull;
import org.jsoup.helper.StringUtil;

public class Entries {

    private static class Entry extends SuperNode{
        private final String name;

        public Entry(String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String toString() {
            return name +": " + StringUtil.join(getChildren(), " ERROR ");
        }
    }

    public static class Package extends Entry{
        public Package() {
            super("Package");
        }
    }

    public static class Source extends Entry{
        public Source() {
            super("Source");
        }
    }

    public static class Version extends Entry{
        public Version() {
            super("Version");
        }
    }

    public static class Section extends Entry{
        public Section() {
            super("Section");
        }
    }


    public static class MD5sum extends Entry{
        public MD5sum() {
            super("MD5sum");
        }
    }

    public static class Closes extends Entry{
        public Closes() {
            super("Closes");
        }
    }

    public static class Filename extends Entry{
        public Filename() {
            super("Filename");
        }
    }


    public static class Priority extends Entry{
        public Priority() {
            super("Priority");
        }
    }


    public static class Architecture extends Entry{
        public Architecture() {
            super("Architecture");
        }
    }

    public static class Maintainer extends Entry{
        public Maintainer() {
            super("Maintainer");
        }
    }

    public static class Essential extends Entry{
        public Essential() {
            super("Essential");
        }
    }

    public static class InstalledSize extends Entry{
        public InstalledSize() {
            super("Installed-Size");
        }
    }

    public static class Description extends Entry{
        public Description() {
            super("Description");
        }
    }

    public static class Homepage extends Entry{
        public Homepage() {
            super("Homepage");
        }
    }


    public static class BuiltUsing extends Entry{
        public BuiltUsing() {
            super("Built-Using");
        }
    }

    public static class Format extends Entry{
        public Format() {
            super("Format");
        }
    }
    public static class Binary extends Entry{
        public Binary() {
            super("Binary");
        }
    }
    public static class Uploaders extends Entry{
        public Uploaders() {
            super("Uploaders");
        }
    }
    public static class VcsBrowser extends Entry{
        public VcsBrowser() {
            super("Vcs-Browser");
        }
    }
    public static class VcsArch extends Entry{
        public VcsArch() {
            super("Vcs-Arch");
        }
    }
    public static class VcsBzr extends Entry{
        public VcsBzr() {
            super("Vcs-Bzr");
        }
    }

    public static class VcsCvs extends Entry{
        public VcsCvs() {
            super("Vcs-Cvs");
        }
    }

    public static class VcsDarcs extends Entry{
        public VcsDarcs() {
            super("Vcs-Darcs");
        }
    }

    public static class VcsGit extends Entry{
        public VcsGit() {
            super("Vcs-Git");
        }
    }

    public static class VcsHg extends Entry{
        public VcsHg() {
            super("Vcs-Hg");
        }
    }
    public static class VcsMtn extends Entry{
        public VcsMtn() {
            super("Vcs-Mtn");
        }
    }

    public static class VcsSvn extends Entry{
        public VcsSvn() {
            super("Vcs-Svn");
        }
    }


    public static class DMUploadAllowed extends Entry{
        public DMUploadAllowed() {
            super("DM-Upload-Allowed");
        }
    }

    public static class StandardsVersion extends Entry{
        public StandardsVersion() {
            super("Standards-Version");
        }
    }

    public static class ChecksumsSha1 extends Entry{
        public ChecksumsSha1() {
            super("Checksums-Sha1");
        }
    }

    public static class ChecksumsSha256 extends Entry{
        public ChecksumsSha256() {
            super("Checksums-Sha256");
        }
    }
    public static class Files extends Entry{
        public Files() {
            super("Files");
        }
    }

    public static class Depends extends Entry{
        public Depends() {
            super("Depends");
        }
    }
    public static class PreDepends extends Entry{
        public PreDepends() {
            super("Pre-Depends");
        }
    }
    public static class Recommends extends Entry{
        public Recommends() {
            super("Recommends");
        }
    }
    public static class Suggests extends Entry{
        public Suggests() {
            super("Suggests");
        }
    }
    public static class Breaks extends Entry{
        public Breaks() {
            super("Breaks");
        }
    }
    public static class Conflicts extends Entry{

        public Conflicts() {
            super("Conflicts");
        }
    }


    public static class Provides extends Entry{

        public Provides() {
            super("Provides");
        }
    }
    public static class Replaces extends Entry{

        public Replaces() {
            super("Replaces");
        }
    }

    public static class Enhances extends Entry{

        public Enhances() {
            super("Enhances");
        }
    }



    public static class BuildDepends extends Entry{

        public BuildDepends() {
            super("Build-Depends");
        }
    }


    public static class BuildDependsIndep extends Entry{

        public BuildDependsIndep() {
            super("Build-Depends-Indep");
        }
    }


    public static class BuildConflicts extends Entry{

        public BuildConflicts() {
            super("Build-Conflicts");
        }
    }

    public static class BuildConflictsIndep extends Entry{

        public BuildConflictsIndep() {
            super("Build-Conflicts-Indep");
        }
    }

    public  static class ChangedBy extends Entry{

        public ChangedBy() {
            super("Changed-By");
        }
    }

    public static class Distribution extends Entry {
        public Distribution() {
            super("Distribution");
        }
    }

    public static class Date extends Entry {
        public Date() {
            super("Date");
        }
    }

    public static class Urgency  extends  Entry{
        public Urgency(){
            super("Urgency");
        }
    }

    public static class Changes extends Entry {
        public Changes() {
            super("Changes");
        }
    }

    public static class Size extends Entry{
        public Size(){
            super("Size");
        }
    }

    public static class Task extends Entry {
        public Task(){
            super("Task");
        }
    }

    public static class SHA1 extends Entry {
        public SHA1() {
            super("SHA1");
        }
    }


    public static class SHA256 extends Entry {
        public SHA256() {
            super("SHA256");
        }
    }

    public static class Tag extends Entry {
        public Tag() {
            super("Tag");
        }
    }

    public static class MetaPackage extends Entry{
        public MetaPackage() {
            super("meta-package");
        }
    }

    public static class PythonVersion extends Entry{
        public PythonVersion(){
            super("Python-Version");
        }
    }

    public static class Bugs extends Entry{
        public Bugs(){
            super("Bugs");
        }
    }


    public static class Origin extends Entry{
        public Origin(){
            super("Origin");
        }
    }



    public static class Url extends Entry{
        public Url(){
            super("Url");
        }
    }

    public static class PythonRuntime extends Entry {
        public PythonRuntime() {
            super("Python-Runtime");
        }
    }


    public static class NppName extends Entry {
        public NppName() {
            super("Npp-Name");
        }
    }
    public static class NppMimetype extends Entry {
        public NppMimetype() {
            super("Npp-Mimetype");
        }
    }
    public static class NppFile extends Entry {
        public NppFile() {
            super("Npp-File");
        }
    }
    public static class NppDescription extends Entry {
        public NppDescription() {
            super("Npp-Description");
        }
    }
    public static class NppApplications extends Entry {
        public NppApplications() {
            super("Npp-Applications");
        }
    }









    public static class Dummy extends SuperNode {
    }
}
