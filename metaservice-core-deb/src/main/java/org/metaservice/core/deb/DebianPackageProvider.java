package org.metaservice.core.deb;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.rdf.vocabulary.*;
import org.metaservice.api.provider.Provider;
import org.metaservice.api.provider.ProviderException;
import org.metaservice.core.deb.parser.ast.*;
import org.metaservice.core.deb.parser.ast.Package;
import org.openrdf.model.*;
import org.openrdf.model.vocabulary.RDF;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class DebianPackageProvider implements Provider<Package> {
    public static final Logger LOGGER = LoggerFactory.getLogger(DebianPackageProvider.class);
    public final static String PROPERTY_META_DISTRIBUTION = "meta-distribution";
    public final static String PROPERTY_DISTRIBUTION = "distribution";
    public final static String PROPERTY_DISTRIBUTION_REGEX = "distribution-regex";


    @NotNull
    public static final String rooturl = "http://metaservice.org/d/packages/";

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

    public void provideModelFor(@NotNull Package p, @NotNull final RepositoryConnection resultConnection,@NotNull HashMap<String,String> properties) throws ProviderException {
        try{
            calculateURIs(p,properties);

            BasicSuperNode packageQuery = new BasicSuperNode(p);
            packageQuery
                    .forEachChild(Entries.Package.class)
                    .forEachChild(PackageIdentifier.class)
                    .execute(new SuperNodeQuery.Function() {
                        @Override
                        public void execute(@NotNull SuperNode n) throws RepositoryException {
                            Value titleLiteral = valueFactory.createLiteral(n.toString());
                            resultConnection.add(packageURI, PACKAGE_DEB.PACKAGE_NAME, titleLiteral);
                        }
                    });



            //package
            resultConnection.add(packageURI, RDF.TYPE, PACKAGE_DEB.PACKAGE);

            resultConnection.add(packageURI,PACKAGE_DEB.META_DISTRIBUTION,valueFactory.createLiteral(properties.get(PROPERTY_META_DISTRIBUTION)));
            resultConnection.add(packageURI,PACKAGE_DEB.DISTRIBUTION,valueFactory.createLiteral(properties.get(PROPERTY_DISTRIBUTION)));

            createStringEntry(packageQuery, packageURI, Entries.Source.class, PACKAGE_DEB.SOURCE, resultConnection);
            createStringEntry(packageQuery, packageURI, Entries.MD5sum.class, PACKAGE_DEB.MD5SUM, resultConnection);
            createStringEntry(packageQuery, packageURI, Entries.SHA1.class, PACKAGE_DEB.SHA1, resultConnection);
            createStringEntry(packageQuery, packageURI, Entries.SHA256.class, PACKAGE_DEB.SHA256, resultConnection);
            createStringEntry(packageQuery, packageURI, Entries.Homepage.class, PACKAGE_DEB.HOMEPAGE, resultConnection);
            createStringEntry(packageQuery, packageURI, Entries.Architecture.class, PACKAGE_DEB.ARCHITECTURE, resultConnection);
            createStringEntry(packageQuery, packageURI, Entries.Description.class, PACKAGE_DEB.DESCRIPTION, resultConnection);
            createStringEntry(packageQuery, packageURI, Entries.Filename.class, PACKAGE_DEB.FILENAME, resultConnection);
            createStringEntry(packageQuery, packageURI, Entries.Filename.class, DC.TITLE, resultConnection);
            createEmailEntry(packageQuery, packageURI, Entries.Maintainer.class, PACKAGE_DEB.MAINTAINER_PROPERTY,resultConnection);
            createEmailEntry(packageQuery, packageURI, Entries.Uploaders.class, PACKAGE_DEB.UPLOADER,resultConnection);

            createDependencyEntry(properties,packageQuery, packageURI, Entries.Depends.class, PACKAGE_DEB.DEPENDS, resultConnection);
            createDependencyEntry(properties,packageQuery, packageURI, Entries.PreDepends.class, PACKAGE_DEB.PRE_DEPENDS, resultConnection);
            createDependencyEntry(properties,packageQuery, packageURI, Entries.Recommends.class, PACKAGE_DEB.RECOMMENDS, resultConnection);
            createDependencyEntry(properties,packageQuery, packageURI, Entries.Suggests.class, PACKAGE_DEB.SUGGESTS, resultConnection);
            createDependencyEntry(properties,packageQuery, packageURI, Entries.Breaks.class, PACKAGE_DEB.BREAKS, resultConnection);
            createDependencyEntry(properties,packageQuery, packageURI, Entries.Replaces.class, PACKAGE_DEB.REPLACES, resultConnection);
            createDependencyEntry(properties,packageQuery, packageURI, Entries.Provides.class, PACKAGE_DEB.PROVIDES, resultConnection);
            createDependencyEntry(properties,packageQuery, packageURI, Entries.BuiltUsing.class, PACKAGE_DEB.BUILT_USING, resultConnection);
            createDependencyEntry(properties,packageQuery, packageURI, Entries.Conflicts.class, PACKAGE_DEB.CONFLICTS, resultConnection);
            createDependencyEntry(properties,packageQuery, packageURI, Entries.BuildConflicts.class, PACKAGE_DEB.BUILD_CONFLICTS, resultConnection);
            createDependencyEntry(properties,packageQuery, packageURI, Entries.BuildConflictsIndep.class, PACKAGE_DEB.BUILD_CONFLICTS_INDEP, resultConnection);
            createDependencyEntry(properties,packageQuery, packageURI, Entries.BuildDepends.class, PACKAGE_DEB.BUILD_DEPENDS, resultConnection);
            createDependencyEntry(properties,packageQuery, packageURI, Entries.BuildDependsIndep.class, PACKAGE_DEB.BUILD_DEPENDS_INDEP, resultConnection);
            createVersionEntry(packageQuery,packageURI,PACKAGE_DEB.VERSION,resultConnection);

        } catch (RepositoryException | QueryEvaluationException e) {
            throw new ProviderException(e);
        }
    }

    private void createEmailEntry(final BasicSuperNode packageQuery,final URI packageURI,final Class c,final URI property,final RepositoryConnection resultConnection) throws RepositoryException {
        packageQuery
                .forEachChildStringNode(c)
                .execute(new SuperNodeQuery.Function() {
                    @Override
                    public void execute(@NotNull SuperNode n) throws RepositoryException {
                        try {
                            for(InternetAddress a : InternetAddress.parse(n.toString(),false)){
                                BNode bNode = valueFactory.createBNode();
                                if(a.getAddress() != null && a.getPersonal() != null){
                                    URI mboxVal =    valueFactory.createURI("mailto:" + a.getAddress());
                                    Value nameVal = valueFactory.createLiteral(a.getPersonal());
                                    resultConnection.add(packageURI, property, bNode);
                                    resultConnection.add(bNode, FOAF.MBOX, mboxVal);
                                    resultConnection.add(bNode, FOAF.NAME, nameVal);
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

    private void createDependencyEntry(final HashMap<String,String> properties,final BasicSuperNode packageQuery, final URI packageURI, final Class c, final URI property, final RepositoryConnection resultConnection) throws RepositoryException, QueryEvaluationException {
        packageQuery
                .forEachChild(c)
                .forEachChild(DependencyConjunction.class).execute(new SuperNodeQuery.Function() {
            @Override
            public void execute(SuperNode n) throws RepositoryException {
                BasicSuperNode query = new BasicSuperNode(n);
                query.forEachChild(DependencyDisjunction.class).execute(new SuperNodeQuery.Function() {
                    @Override
                    public void execute(SuperNode n) throws RepositoryException {
                        final BNode container = valueFactory.createBNode();
                        resultConnection.add(container,RDF.TYPE,METASERVICE_SWDEP.ANY_ONE_OF_SW);
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
        resultConnection.add(dependencyURI, DC.DESCRIPTION, valueFactory.createLiteral(packageIdentifier.toString()));
        Version version = packageIdentifier.getVersion();
        if (version != null){
            resultConnection.add(dependencyURI, METASERVICE_SWDEP.VERSION, valueFactory.createLiteral(version.toString()));
            if(version.relation != null) {
                switch (version.relation){
                    case ">>":
                    case ">":
                        resultConnection.add(dependencyURI,RDF.TYPE,METASERVICE_SWDEP.LATER_THAN_VERSION_DEPENDENCY_RELATION);
                        break;
                    case "<<":
                    case "<":
                        resultConnection.add(dependencyURI,RDF.TYPE,METASERVICE_SWDEP.PRIOR_TO_VERSION_DEPENDENCY_RELATION);
                        break;
                    case ">=":
                        resultConnection.add(dependencyURI,RDF.TYPE,METASERVICE_SWDEP.LATER_THAN_OR_EQUAL_VERSION_DEPENDENCY_RELATION);
                        break;
                    case "<=":
                        resultConnection.add(dependencyURI,RDF.TYPE,METASERVICE_SWDEP.PRIOR_TO_OR_EQUAL_VERSION_DEPENDENCY_RELATION);
                        break;
                    case "=":
                    default:
                        resultConnection.add(dependencyURI,RDF.TYPE,METASERVICE_SWDEP.EXACT_VERSION_DEPENDENCY_RELATION);
                        break;
                }
            }else {
                resultConnection.add(dependencyURI,RDF.TYPE,METASERVICE_SWDEP.VERSION_DEPENDENCY_RELATION);
            }
            resultConnection.add(dependencyURI, METASERVICE_SWDEP.DEPEND_ON, createReleaseUri(properties,packageIdentifier.getName(), version.toFileNameString()));

        }else{
            resultConnection.add(dependencyURI, METASERVICE_SWDEP.DEPEND_ON, createProjectUri(properties,packageIdentifier.getName()));
        }
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
        return  valueFactory.createURI(rooturl, properties.get(PROPERTY_META_DISTRIBUTION) + "/" + projectName);
    }
    public URI createPackageUri(HashMap<String,String> properties, String projectName,String version,String arch){
        return  valueFactory.createURI(rooturl, properties.get(PROPERTY_META_DISTRIBUTION) + "/" + projectName +"/" + version + "/" + arch);
    }


    public URI createReleaseUri(HashMap<String,String> properties, String projectName,String version){
        return valueFactory.createURI(rooturl, properties.get(PROPERTY_META_DISTRIBUTION) + "/" + projectName+"/" + version);
    }


}
