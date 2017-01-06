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
