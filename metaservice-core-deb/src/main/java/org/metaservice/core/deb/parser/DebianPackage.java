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

package org.metaservice.core.deb.parser;

import org.jetbrains.annotations.NotNull;
import org.jsoup.helper.StringUtil;
import org.metaservice.core.deb.parser.ast.SuperNode;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ilo
 * Date: 14.10.13
 * Time: 00:40
 * To change this template use File | Settings | File Templates.
 */
public class DebianPackage {
    public DebianPackageIdentifier packageId;
    public DebianPackageIdentifier sourceId;
    public DebianVersion version;
    public DebianDependencyRelation depends;
    public DebianDependencyRelation preDepends;
    public DebianDependencyRelation recommends;
    public DebianDependencyRelation suggests;
    public DebianDependencyRelation breaks;
    public DebianDependencyRelation conflicts;
    public DebianDependencyRelation provides;
    public DebianDependencyRelation replaces;
    public DebianDependencyRelation enhances;
    public DebianDependencyRelation buildDepends;
    public DebianDependencyRelation buildDependsIndep;
    public DebianDependencyRelation buildConflicts;
    public DebianDependencyRelation buildConflictsIndep;





    public String description;

    @NotNull
    @Override
    public String toString() {
        return "DebianPackage{" +
                "packageId=" + packageId +
                ", sourceId=" + sourceId +
                ", version=" + version +
                ", depends=" + depends +
                ", preDepends=" + preDepends +
                ", recommends=" + recommends +
                ", suggests=" + suggests +
                ", breaks=" + breaks +
                ", conflicts=" + conflicts +
                ", provides=" + provides +
                ", replaces=" + replaces +
                ", enhances=" + enhances +
                ", buildDepends=" + buildDepends +
                ", buildDependsIndep=" + buildDependsIndep +
                ", buildConflicts=" + buildConflicts +
                ", buildConflictsIndep=" + buildConflictsIndep +
                ", description='" + description + '\'' +
                '}';
    }

    public static class DebianDependencyRelation{
        @NotNull
        List<List<DebianPackageIdentifier>> list = new LinkedList<>();

        @Override
        public String toString() {
            List<String> disjunctions = new LinkedList<>();
            for(List<DebianPackageIdentifier> disjunction : list ){
                disjunctions.add(StringUtil.join(disjunction," | "));
            }
            return StringUtil.join(disjunctions,", ");
        }
    }

    public static class DebianPackageIdentifier{
        String name;
        ArchitectureConstraint architectureConstraint;
        DebianVersion version;

        @NotNull
        @Override
        public String toString() {
            return  '\'' + name + '\'' +
                    " " + version +
                    " " + architectureConstraint;
        }

    }


    public static class DebianVersion extends SuperNode {
        String epoch;
        String upstreamVersion;
        String debversion;

        @NotNull
        String relation = "=";

        @NotNull
        @Override
        public String toString() {
            if(epoch!=null || upstreamVersion!= null ||debversion!= null)
                return "(" + relation + " " + ((epoch != null)?epoch + ':':"") + upstreamVersion + ((debversion!=null)?'-'+ debversion :"") +')';
            else
                return "";
        }
    }
    public static class ArchitectureConstraint{

    }
}
