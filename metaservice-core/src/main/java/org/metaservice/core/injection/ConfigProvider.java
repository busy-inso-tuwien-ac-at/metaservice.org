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

package org.metaservice.core.injection;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.metaservice.api.messaging.Config;
import org.metaservice.api.messaging.config.ManagerConfig;

/**
 * Created by ilo on 18.02.14.
 */
public class ConfigProvider  implements Provider<Config>{

    private final ManagerConfig managerConfig;

    @Inject
    public ConfigProvider(ManagerConfig managerConfig) {
        this.managerConfig = managerConfig;
    }

    @Override
    public Config get() {
        return managerConfig.getConfig();
    }
}
