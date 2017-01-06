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

package org.metaservice.core.deb;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.rdf.vocabulary.*;
import org.metaservice.api.provider.Provider;
import org.metaservice.api.provider.ProviderException;
import org.metaservice.core.deb.parser.ast.*;
import org.metaservice.core.deb.parser.ast.Package;
import org.openrdf.model.*;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DebianPackageProvider implements Provider<Package> {
    public static final Logger LOGGER = LoggerFactory.getLogger(DebianPackageProvider.class);
    public final static String PROPERTY_META_DISTRIBUTION = "meta-distribution";
    public final static String PROPERTY_DISTRIBUTION = "distribution";
    public final static String PROPERTY_DISTRIBUTION_REGEX = "distribution-regex";


    @NotNull
    public static final String rooturl = "http://metaservice.org/d/";

    private final ValueFactory valueFactory;

    @Inject
    public DebianPackageProvider(
            ValueFactory valueFactory
    ) throws RepositoryException, MalformedQueryException, IOException, InterruptedException {
        this.valueFactory = valueFactory;
    }

    enum PackageType{binary,source}


    public static interface SuperNodeQuery{
        @NotNull
        public SuperNodeQuery forEachChild(Class c);
        public void execute(Function f) throws RepositoryException;
        public static interface Function{
            public void execute(SuperNode n) throws RepositoryException;
        }
    }

    public static class BasicSuperNode implements SuperNodeQuery{

        final ArrayList<SuperNode>nodeList = new ArrayList<>();
        public BasicSuperNode(SuperNode d){
            nodeList.add(d);
        }

        public BasicSuperNode(Collection<SuperNode> d){
            nodeList.addAll(d);
        }

        @NotNull
        @Override
        public SuperNodeQuery forEachChild(@NotNull Class c) {
            ArrayList<SuperNode>resultList = new ArrayList<>();
            for(SuperNode node : nodeList){
                for(SuperNode child : node.getChildren()){
                    if(c.isInstance(child)){
                        resultList.add(child);
                    }
                }
            }
            return new BasicSuperNode(resultList);
        }

        public SuperNodeQuery forEachChildStringNode(@NotNull Class c){
            return  forEachChild(c).forEachChild(StringNode.class);
        }

        @Override
        public void execute(@NotNull Function f) throws RepositoryException {
            for(SuperNode node : nodeList){
                f.execute(node);
            }
        }
    }

    //  public void setContext();

    public void provideModelFor(@NotNull Package p, @NotNull final RepositoryConnection resultConnection,@NotNull final HashMap<String,String> properties) throws ProviderException {
        try{
            calculateURIs(p,properties);

            final BasicSuperNode packageQuery = new BasicSuperNode(p);
            packageQuery
                    .forEachChild(Entries.Package.class)
                    .forEachChild(PackageIdentifier.class)
                    .execute(new SuperNodeQuery.Function() {
                        @Override
                        public void execute(@NotNull SuperNode n) throws RepositoryException {
                            Value titleLiteral = valueFactory.createLiteral(n.toString());
                            resultConnection.add(packageURI, DEB.PACKAGE_NAME, titleLiteral);
                        }
                    });



            //package
            resultConnection.add(packageURI, RDF.TYPE, DEB.PACKAGE);

            resultConnection.add(packageURI, DEB.META_DISTRIBUTION,valueFactory.createLiteral(properties.get(PROPERTY_META_DISTRIBUTION)));
            resultConnection.add(packageURI, DEB.DISTRIBUTION,valueFactory.createLiteral(properties.get(PROPERTY_DISTRIBUTION)));
            resultConnection.add(packageURI,DCTERMS.FILE_FORMAT,valueFactory.createURI("http://mediatypes.appspot.com/application/vnd.debian.binary-package"));
            createStringEntry(packageQuery, packageURI, Entries.Source.class, DEB.SOURCE, resultConnection);
            createStringEntry(packageQuery, packageURI, Entries.MD5sum.class, DEB.MD5SUM, resultConnection);
            createStringEntry(packageQuery, packageURI, Entries.SHA1.class, DEB.SHA1SUM, resultConnection);
            createStringEntry(packageQuery, packageURI, Entries.SHA256.class, DEB.SHA256SUM, resultConnection);
            createStringEntry(packageQuery, packageURI, Entries.Homepage.class, DEB.HOMEPAGE, resultConnection);
            createStringEntry(packageQuery, packageURI, Entries.Architecture.class, DEB.ARCHITECTURE, resultConnection);
            createStringEntry(packageQuery, packageURI, Entries.Description.class, DEB.DESCRIPTION, resultConnection);
            packageQuery
                    .forEachChildStringNode( Entries.Filename.class)
                    .execute(new SuperNodeQuery.Function() {
                        @Override
                        public void execute(@NotNull SuperNode n) throws RepositoryException {
                            String filename = Paths.get(n.toString()).getFileName().toString();
                            Literal fileNameLiteral = valueFactory.createLiteral(filename);
                            resultConnection.add(packageURI, DEB.FILENAME,fileNameLiteral );
                            resultConnection.add(packageURI, RDFS.LABEL, fileNameLiteral);
                            resultConnection.add(packageURI, DC.SUBJECT,valueFactory.createURI(properties.get("metadata_source")+"../" +n.toString()));
                        }
                    });

            createEmailEntry(packageQuery, packageURI, Entries.Maintainer.class, DEB.MAINTAINER_PROPERTY, DEB.MAINTAINER,resultConnection);
            createEmailEntry(packageQuery, packageURI, Entries.Uploaders.class, DEB.UPLOADER_PROPERTY, DEB.UPLOADER,resultConnection);

            createDependencyEntry(properties,packageQuery, packageURI, Entries.Depends.class, DEB.DEPENDS, resultConnection);
            createDependencyEntry(properties,packageQuery, packageURI, Entries.PreDepends.class, DEB.PRE_DEPENDS, resultConnection);
            createDependencyEntry(properties,packageQuery, packageURI, Entries.Recommends.class, DEB.RECOMMENDS, resultConnection);
            createDependencyEntry(properties,packageQuery, packageURI, Entries.Suggests.class, DEB.SUGGESTS, resultConnection);
            createDependencyEntry(properties,packageQuery, packageURI, Entries.Breaks.class, DEB.BREAKS, resultConnection);
            createDependencyEntry(properties,packageQuery, packageURI, Entries.Replaces.class, DEB.REPLACES, resultConnection);
            createDependencyEntry(properties,packageQuery, packageURI, Entries.Provides.class, DEB.PROVIDES, resultConnection);
            createDependencyEntry(properties,packageQuery, packageURI, Entries.BuiltUsing.class, DEB.BUILT_USING, resultConnection);
            createDependencyEntry(properties,packageQuery, packageURI, Entries.Conflicts.class, DEB.CONFLICTS, resultConnection);
            createDependencyEntry(properties,packageQuery, packageURI, Entries.BuildConflicts.class, DEB.BUILD_CONFLICTS, resultConnection);
            createDependencyEntry(properties,packageQuery, packageURI, Entries.BuildConflictsIndep.class, DEB.BUILD_CONFLICTS_INDEP, resultConnection);
            createDependencyEntry(properties,packageQuery, packageURI, Entries.BuildDepends.class, DEB.BUILD_DEPENDS, resultConnection);
            createDependencyEntry(properties,packageQuery, packageURI, Entries.BuildDependsIndep.class, DEB.BUILD_DEPENDS_INDEP, resultConnection);
            createVersionEntry(packageQuery,packageURI, DEB.VERSION,resultConnection);

        } catch (RepositoryException | QueryEvaluationException e) {
            throw new ProviderException(e);
        }
    }

    private void createEmailEntry(final BasicSuperNode packageQuery,final URI packageURI,final Class c,final URI property,final URI type,final RepositoryConnection resultConnection) throws RepositoryException {
        packageQuery
                .forEachChildStringNode(c)
                .execute(new SuperNodeQuery.Function() {
                    @Override
                    public void execute(@NotNull SuperNode n) throws RepositoryException {
                        try {
                            for(InternetAddress a : InternetAddress.parse(n.toString(),false)){
                                if(a.getAddress() != null && a.getPersonal() != null){
                                    URI mboxVal =    valueFactory.createURI("mailto:" + a.getAddress());
                                    Value nameVal = valueFactory.createLiteral(a.getPersonal());
                                    URI addressURI = valueFactory.createURI(packageURI.toString()+"#"+a.getAddress());
                                    resultConnection.add(packageURI, property, addressURI);
                                    resultConnection.add(addressURI, FOAF.MBOX, mboxVal);
                                    resultConnection.add(addressURI, FOAF.NAME, nameVal);
                                }
                            }
                        } catch (AddressException e) {
                            LOGGER.error("ERROR parsing EMAIL {}", n.toString(), e);
                        }
                    }
                });
    }

    private void createVersionEntry(final BasicSuperNode packageQuery,final URI packageURI,final URI property,final  RepositoryConnection resultConnection) throws RepositoryException, QueryEvaluationException {
        packageQuery
                .forEachChild(Entries.Version.class)
                .forEachChild(Version.class)
                .execute(new SuperNodeQuery.Function() {
                    @Override
                    public void execute(@NotNull SuperNode n) throws RepositoryException {
                        resultConnection.add(packageURI, property, valueFactory.createLiteral(n.toString()));
                    }
                });
    }

    //may only be called once per property, because uri generation depends on it
    private void createDependencyEntry(final HashMap<String,String> properties,final BasicSuperNode packageQuery, final URI packageURI, final Class c, final URI property, final RepositoryConnection resultConnection) throws RepositoryException, QueryEvaluationException {
        final AtomicInteger i = new AtomicInteger(0);
        packageQuery
                .forEachChild(c)
                .forEachChild(DependencyConjunction.class).execute(new SuperNodeQuery.Function() {
            @Override
            public void execute(SuperNode n) throws RepositoryException {
                BasicSuperNode query = new BasicSuperNode(n);
                query.forEachChild(DependencyDisjunction.class).execute(new SuperNodeQuery.Function() {
                    @Override
                    public void execute(SuperNode n) throws RepositoryException {
                        final URI container = valueFactory.createURI(packageURI+"#"+property.getLocalName()+i.incrementAndGet());
                        resultConnection.add(container,RDF.TYPE,METASERVICE_SWDEP.ANY_ONE_OF_SOFTWARE);
                        resultConnection.add(packageURI, property, container);
                        new BasicSuperNode(n)
                                .forEachChild(PackageIdentifier.class).execute(new SuperNodeQuery.Function() {
                            @Override
                            public void execute(@NotNull SuperNode n) throws RepositoryException {
                                URI dependencyURI =  createDependencyPackageIdentifier(properties,(PackageIdentifier) n,property,resultConnection);
                                resultConnection.add(container,RDF.LI,dependencyURI);
                            }
                        });
                    }
                });
                query.forEachChild(PackageIdentifier.class).execute(new SuperNodeQuery.Function() {
                    @Override
                    public void execute(SuperNode n) throws RepositoryException {
                        URI dependencyURI = createDependencyPackageIdentifier(properties,(PackageIdentifier) n,property,resultConnection);
                        resultConnection.add(packageURI, property, dependencyURI);
                    }
                });
            }
        });
    }

    public URI createDependencyPackageIdentifier(HashMap<String,String> properties,PackageIdentifier packageIdentifier, final URI property, RepositoryConnection resultConnection) throws RepositoryException {
        URI dependencyURI = valueFactory.createURI(packageURI.toString() + "#", property.getLocalName() + "_" + packageIdentifier.getName());
        Version version = packageIdentifier.getVersion();
        if (version != null){
            if(version.relation != null) {
                resultConnection.add(dependencyURI, METASERVICE_SWDEP.REVISION_CONSTRAINT,valueFactory.createLiteral(version.toString()));
            }else {
                //todo check architecture
                return createReleaseUri(properties, packageIdentifier.getName(), version.toFileNameString());
            }
        }
        resultConnection.add(dependencyURI, RDF.TYPE, METASERVICE_SWDEP.SOFTWARE_RANGE);
        resultConnection.add(dependencyURI, DC.DESCRIPTION, valueFactory.createLiteral(packageIdentifier.toString()));
        resultConnection.add(dependencyURI, METASERVICE_SWDEP.PROJECT_CONSTRAINT, createProjectUri(properties, packageIdentifier.getName()));
        return dependencyURI;
    }

    private void createStringEntry(@NotNull final BasicSuperNode packageQuery, @NotNull final URI packageURI, @NotNull final Class c, @NotNull final URI property,@NotNull final RepositoryConnection resultConnection) throws RepositoryException, QueryEvaluationException {
        packageQuery
                .forEachChildStringNode(c)
                .execute(new SuperNodeQuery.Function() {
                    @Override
                    public void execute(@NotNull SuperNode n) throws RepositoryException {
                        resultConnection.add(packageURI, property, valueFactory.createLiteral(n.toString()));
                    }
                });

    }

    private URI packageURI;

    private void calculateURIs(@NotNull Package p,HashMap<String,String> properties) {
        PackageType type = null;
        String name = null;
        String version = null;
        String arch = null;
        for(SuperNode node : p.getChildren()){
            if(node instanceof Entries.Package){
                type= PackageType.binary;
                for(SuperNode x : node.getChildren()){
                    if(x instanceof PackageIdentifier){
                        name = ((PackageIdentifier) x).getName();
                        break;
                    }
                }
            } else if(node instanceof Entries.Architecture){
                for(SuperNode x : node.getChildren()){
                    if(x instanceof StringNode){
                        arch = x.toString();
                        break;
                    }
                }
            } else if(node instanceof Entries.Version){
                for(SuperNode x : node.getChildren()){
                    if(x instanceof Version){
                        version = ((Version)x).toFileNameString();
                        break;
                    }
                }
            }
        }
        packageURI = createPackageUri(properties,name,version,arch);
    }


    public URI createProjectUri(HashMap<String,String> properties, String projectName){
        return  valueFactory.createURI(rooturl, "projects/" + properties.get(PROPERTY_META_DISTRIBUTION) + "/" + projectName);
    }
    public URI createPackageUri(HashMap<String,String> properties, String projectName,String version,String arch){
        return  valueFactory.createURI(rooturl, "packages/" +properties.get(PROPERTY_META_DISTRIBUTION) + "/" + projectName +"/" + version + "/" + arch);
    }


    public URI createReleaseUri(HashMap<String,String> properties, String projectName,String version){
        return valueFactory.createURI(rooturl, "releases/" + properties.get(PROPERTY_META_DISTRIBUTION) + "/" + projectName+"/" + version);
    }


}
