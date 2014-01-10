package org.metaservice.management;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.metaservice.core.ProductionConfig;
import org.metaservice.core.injection.MetaserviceModule;

import java.io.File;

/**
 * Created by ilo on 08.01.14.
 */
public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new MetaserviceModule());
        Manager manager = injector.getInstance(Manager.class);
        manager.install(new File(args[0]));
    }
}
