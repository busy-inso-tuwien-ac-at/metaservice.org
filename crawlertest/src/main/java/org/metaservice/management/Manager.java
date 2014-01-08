package org.metaservice.management;

import java.io.File;

/**
 * Created by ilo on 05.01.14.
 */
public class Manager {
    public void init(){
        //scan all plugins and install if necessary
    }

    public void install(File f){
        //schedule crawler
        //copy templates
        //install ontologies
        //run and monitor providers and postprocessors
    }

    public void uninstall(File f){
        // undo install
    }
}