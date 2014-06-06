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
