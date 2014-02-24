package org.metaservice.core.injection;

import com.google.inject.Provider;
import org.apache.commons.io.IOUtils;
import org.metaservice.core.config.ManagerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ilo on 18.02.14.
 */
public class ManagerConfigProvider implements Provider<ManagerConfig> {
    public static final Logger LOGGER = LoggerFactory.getLogger(ManagerConfigProvider.class);
    public final static String MANAGERCONFIG_XML = "/opt/metaservice/managerconfig.xml";

    private ManagerConfig managerConfig = null;

    @Override
    public ManagerConfig get() {
        if(managerConfig == null){
            File file = new File(MANAGERCONFIG_XML);
            if(!file.exists()){
                try {
                    IOUtils.copy(ManagerConfigProvider.class.getResourceAsStream("/metaservice-default-config.xml"), new FileOutputStream(file));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            managerConfig = JAXB.unmarshal(file, ManagerConfig.class);
            LOGGER.info("Loaded Configuration:");
            LOGGER.info(String.valueOf(managerConfig.getConfig()));
        }
        return managerConfig;
    }
}
