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

package org.metaservice.core.maven;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.multibindings.Multibinder;
import org.apache.commons.io.IOUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.resolution.ModelResolver;
import org.apache.maven.repository.internal.MavenAetherModule;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.apache.maven.wagon.Wagon;
import org.apache.maven.wagon.providers.http.HttpWagon;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.RequestTrace;
import org.eclipse.aether.connector.file.FileRepositoryConnectorFactory;
import org.eclipse.aether.connector.wagon.WagonConfigurator;
import org.eclipse.aether.connector.wagon.WagonProvider;
import org.eclipse.aether.connector.wagon.WagonRepositoryConnectorFactory;
import org.eclipse.aether.impl.ArtifactResolver;
import org.eclipse.aether.impl.RemoteRepositoryManager;
import org.eclipse.aether.internal.impl.*;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.parser.Parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

public class MavenPomParser implements Parser<Model> {

    private ModelResolver makeModelResolver() {
        try {

            Injector  injector = Guice.createInjector(new MavenAetherModule(), new AbstractModule() {
                @Override
                protected void configure() {
                    Multibinder<RepositoryConnectorFactory> multibinder
                            = Multibinder.newSetBinder(binder(), RepositoryConnectorFactory.class);
                    multibinder.addBinding().to(FileRepositoryConnectorFactory.class);
                    multibinder.addBinding().to(WagonRepositoryConnectorFactory.class);

                    //fake wagon configurator
                    bind(WagonConfigurator.class).toInstance(new WagonConfigurator() {
                        @Override
                        public void configure(Wagon wagon, Object configuration) throws Exception {
                        }
                    });

                    //fake wagon provider
                    //todo test for reusage?
                    bind(WagonProvider.class).toInstance(new WagonProvider() {
                        @Override
                        public Wagon lookup(String roleHint) throws Exception {
                            return new HttpWagon();
                        }

                        @Override
                        public void release(Wagon wagon) {
                        }
                    });

                }
            });
            Class c = Class.forName("org.apache.maven.repository.internal.DefaultModelResolver");
            Constructor ct = c.getConstructor(new Class[]{RepositorySystemSession.class,
                    RequestTrace.class, String.class,
                    ArtifactResolver.class, RemoteRepositoryManager.class,
                    List.class});
            ct.setAccessible(true);

            //todo handle resoultion repositories
            // maybe using standard configuration files and dynamic reading of repositories in pom?
            RemoteRepository.Builder b = new RemoteRepository.Builder("central","default","http://repo1.maven.org/maven2");

            DefaultRepositorySystemSession systemSession =MavenRepositorySystemUtils.newSession();
            systemSession.setLocalRepositoryManager(new SimpleLocalRepositoryManagerFactory().newInstance(systemSession,new LocalRepository("/foo")));


            return (org.apache.maven.model.resolution.ModelResolver) ct.newInstance(systemSession,
                    null, null, injector.getInstance(ArtifactResolver.class), injector.getInstance(RemoteRepositoryManager.class),
                    Arrays.asList(b.build()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Model> parse(Reader s, ArchiveAddress archiveParameters) {
        File file = null;
        try {
            file = File.createTempFile("temp",".pom");
            FileWriter writer = new FileWriter(file);
            IOUtils.copy(s,writer);
            writer.close();

            ModelBuildingRequest req = new DefaultModelBuildingRequest();
            req.setProcessPlugins( false );
            req.setPomFile(file);
            req.setModelResolver(makeModelResolver());
            req.setValidationLevel(ModelBuildingRequest.VALIDATION_LEVEL_MINIMAL);

            DefaultModelBuilderFactory factory = new DefaultModelBuilderFactory();
            org.apache.maven.model.Model model = factory.newInstance().build(req).getEffectiveModel();
            return Arrays.asList(model);
        } catch (IOException | ModelBuildingException e) {
            throw new RuntimeException(e);
        } finally {
            if(file!=null && file.exists()){
                file.delete();
            }
        }
    }
}
