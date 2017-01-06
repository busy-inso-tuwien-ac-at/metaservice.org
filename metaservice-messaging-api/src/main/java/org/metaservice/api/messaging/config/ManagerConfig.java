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

package org.metaservice.api.messaging.config;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.Config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilo on 10.02.14.
 */
@XmlRootElement
public class ManagerConfig {

    private Config config;

    private List<Module> installedModules;
    private List<Module> availableModules;

    @XmlElement(type = JaxbConfig.class)
    public Config getConfig() {
        if(config == null){
           config = new JaxbConfig();
        }
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    @XmlElementWrapper
    @XmlElement(name = "module")
    @NotNull
    public List<Module> getInstalledModules() {
        if(installedModules == null){
            installedModules = new ArrayList<>();
        }
        return installedModules;
    }

    public void setInstalledModules(List<Module> installedModules) {
        this.installedModules = installedModules;
    }

    @XmlElementWrapper
    @XmlElement(name = "module")
    @NotNull
    public List<Module> getAvailableModules() {
        if(availableModules == null){
            availableModules = new ArrayList<>();
        }
        return availableModules;
    }

    public void setAvailableModules(List<Module> availableModules) {
        this.availableModules = availableModules;
    }

    public static class Module{
        File location;
        MetaserviceDescriptor metaserviceDescriptor;

        public File getLocation() {
            return location;
        }

        public void setLocation(File location) {
            this.location = location;
        }

        @XmlElement(name = "descriptor",type = JAXBMetaserviceDescriptorImpl.class)
        @NotNull
        public MetaserviceDescriptor getMetaserviceDescriptor() {
            return metaserviceDescriptor;
        }

        public void setMetaserviceDescriptor(MetaserviceDescriptor metaserviceDescriptor) {
            this.metaserviceDescriptor = metaserviceDescriptor;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (obj == this)
                return true;
            if (!(obj instanceof Module))
                return false;

            Module rhs = (Module) obj;
            return new EqualsBuilder()
                    .append(metaserviceDescriptor,rhs.metaserviceDescriptor)
                    .build();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(metaserviceDescriptor).build();
        }
    }
}
