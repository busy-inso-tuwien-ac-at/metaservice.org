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

package org.metaservice.core.bower;

import java.util.List;

/**
 * Created by ilo on 01.06.2014.
 */
public class BowerPackage {
    private String name;
    private String version;
    private List<String> main;
    private List<BowerPackageReference> dependencies;
    private List<BowerPackageReference> devDependencies;
    private Boolean privateFlag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<String> getMain() {
        return main;
    }

    public void setMain(List<String> main) {
        this.main = main;
    }

    public List<BowerPackageReference> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<BowerPackageReference> dependencies) {
        this.dependencies = dependencies;
    }

    public List<BowerPackageReference> getDevDependencies() {
        return devDependencies;
    }

    public void setDevDependencies(List<BowerPackageReference> devDependencies) {
        this.devDependencies = devDependencies;
    }

    public Boolean getPrivateFlag() {
        return privateFlag;
    }

    public void setPrivateFlag(Boolean privateFlag) {
        this.privateFlag = privateFlag;
    }

    @Override
    public String toString() {
        return "BowerPackage{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", main=" + main +
                ", dependencies=" + dependencies +
                ", devDependencies=" + devDependencies +
                ", privateFlag=" + privateFlag +
                '}';
    }
}
