package org.metaservice.management;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.ns.METASERVICE;
import org.metaservice.core.Config;
import org.metaservice.core.descriptor.MetaserviceDescriptorImpl;
import org.openrdf.model.Literal;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

import java.nio.file.*;

/**
 * Created by ilo on 05.01.14.
 */
public class Manager {
    private static Logger LOGGER = LoggerFactory.getLogger(Manager.class);

    private final Config config;
    private final RepositoryConnection connection;
    private final ValueFactory valueFactory;


    @Inject
    public Manager(Config config, RepositoryConnection connection, ValueFactory valueFactory){
        this.config = config;
        this.connection = connection;
        this.valueFactory = valueFactory;
    }

    public void init(){
        //scan all plugins and install if necessary
    }

    public void install(@NotNull File f){
        LOGGER.info("Starting install of {}",f.getName());
        Path p = Paths.get(f.toURI());
        try (FileSystem zipfs = FileSystems.newFileSystem(p,Thread.currentThread().getContextClassLoader())){
            MetaserviceDescriptor descriptor = new MetaserviceDescriptorImpl(Files.newInputStream(zipfs.getPath("/metaservice.xml")));

            System.err.println(descriptor.getTemplateList());
            //todo namespace the paths of templates
            for(MetaserviceDescriptor.TemplateDescriptor templateDescriptor : descriptor.getTemplateList()){
                Path from = zipfs.getPath("/templates/" + templateDescriptor.getName());
                Path to =  Paths.get(config.getHttpdDataDirectory() + templateDescriptor.getName());
                LOGGER.info("Copying {} to {}",from,to);
                Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);

                URI  subject= valueFactory.createURI(templateDescriptor.getAppliesTo());
                Literal filename = valueFactory.createLiteral(templateDescriptor.getName());

                Statement s = valueFactory.createStatement(subject, METASERVICE.VIEW,filename);
                try{
                    LOGGER.info("Adding Statement {} to the database", s);
                    connection.add(s);
                } catch (RepositoryException e) {
                    LOGGER.error("Could not add the Statement {} to the database",s,e);
                }

            }


        } catch (IOException e) {
            LOGGER.error("Could not install {}", f.getName(),e);
        }


        //schedule crawler
        //install ontologies
        //run and monitor providers and postprocessors
        //if upgrade trigger refresh of outdated
    }

    public void uninstall(File f){
        // undo install
    }
}