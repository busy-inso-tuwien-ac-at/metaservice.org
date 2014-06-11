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
