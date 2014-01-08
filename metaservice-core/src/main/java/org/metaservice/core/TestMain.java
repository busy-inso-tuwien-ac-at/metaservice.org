package org.metaservice.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.metaservice.core.injection.MetaserviceModule;
import org.openrdf.model.ValueFactory;

public class TestMain {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new MetaserviceModule());
        injector.getInstance(ValueFactory.class);
    }
}
