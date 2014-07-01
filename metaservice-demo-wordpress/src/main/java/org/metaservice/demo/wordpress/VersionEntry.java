package org.metaservice.demo.wordpress;

/**
 * Created by ilo on 21.06.2014.
 */
public class VersionEntry {
    private String zip;
    private String tar;
    private String name;
    private String iis;

    public String getIis() {
        return iis;
    }

    public void setIis(String iis) {

        this.iis = iis;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getTar() {
        return tar;
    }

    public void setTar(String tar) {
        this.tar = tar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "VersionEntry{" +
                "zip='" + zip + '\'' +
                ", tar='" + tar + '\'' +
                ", name='" + name + '\'' +
                ", iis='" + iis + '\'' +
                '}';
    }
}
