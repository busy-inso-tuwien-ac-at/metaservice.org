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

package org.metaservice.core.injection;

import com.google.inject.Provider;
import org.apache.commons.io.IOUtils;
import org.metaservice.api.messaging.config.ManagerConfig;
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
                    LOGGER.error("Error loading default ", e);
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
