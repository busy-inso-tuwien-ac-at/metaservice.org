package org.metaservice.core.management;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.core.Config;
import org.metaservice.core.JaxbConfig;
import org.metaservice.core.descriptor.JAXBMetaserviceDescriptorImpl;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
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
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    @XmlElementWrapper
    @XmlElement(name = "module")
    public List<Module> getInstalledModules() {
        return installedModules;
    }

    public void setInstalledModules(List<Module> installedModules) {
        this.installedModules = installedModules;
    }

    @XmlElementWrapper
    @XmlElement(name = "module")
    public List<Module> getAvailableModules() {
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
