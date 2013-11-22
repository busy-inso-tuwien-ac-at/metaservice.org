package org.metaservice.core;

import com.google.inject.Binder;
import com.google.inject.Module;

public class MetaserviceModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(Config.class).to(ProductionConfig.class);
    }
}
