package org.metaservice.core;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.metaservice.core.rdf.BufferedSparql;

/**
 * Created with IntelliJ IDEA.
 * User: ilo
 * Date: 22.11.13
 * Time: 01:43
 * To change this template use File | Settings | File Templates.
 */
public class TestMain {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new MetaserviceModule());
        injector.getInstance(BufferedSparql.class);
    }
}
