package org.metaservice.core.deb;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.ns.ADMSSW;
import org.metaservice.api.ns.PACKAGE_DEB;
import org.metaservice.api.provider.Provider;
import org.metaservice.api.provider.ProviderException;
import org.metaservice.core.deb.parser.ast.*;
import org.metaservice.core.deb.parser.ast.Package;
import org.openrdf.model.*;
import org.openrdf.model.impl.TreeModel;
import org.openrdf.model.vocabulary.DC;
import org.openrdf.model.vocabulary.FOAF;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.BooleanQuery;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
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

public class DebianPackageProvider implements Provider<Package> {
    public static final Logger LOGGER = LoggerFactory.getLogger(DebianPackageProvider.class);

    @NotNull
    public static String rooturl = "http://metaservice.org/d/packages/debian/";

    private final BooleanQuery checkMailExists;
    private final ValueFactory valueFactory;
    private final RepositoryConnection connection;

    @Inject
    public DebianPackageProvider(
            ValueFactory valueFactory,
            RepositoryConnection connection
    ) throws RepositoryException, MalformedQueryException, IOException, InterruptedException {
        this.valueFactory = valueFactory;
        this.connection = connection;
        checkMailExists = this.connection.prepareBooleanQuery(QueryLanguage.SPARQL, "ASK { ?package ?property ?x. ?x <" + FOAF.MBOX + "> ?mboxvalue; <" + FOAF.NAME + "> ?mboxname }");
    }

    enum PackageType{binary,source}


    public static interface SuperNodeQuery{
        @NotNull
        public SuperNodeQuery forEachChild(Class c);
        public void execute(Function f) throws RepositoryException, QueryEvaluationException;
        public static interface Function{
            public void execute(SuperNode n) throws RepositoryException, QueryEvaluationException;
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
        public void execute(@NotNull Function f) throws RepositoryException, QueryEvaluationException {
            for(SuperNode node : nodeList){
                f.execute(node);
            }
        }
    }

    //  public void setContext();

    public Model provideModelFor(@NotNull Package p) throws ProviderException {
        try{
            final Model model = new TreeModel();

            calculateURIs(p);

            BasicSuperNode packageQuery = new BasicSuperNode(p);
            packageQuery
                    .forEachChild(Entries.Package.class)
                    .forEachChild(PackageIdentifier.class)
                    .execute(new SuperNodeQuery.Function() {
                        @Override
                        public void execute(@NotNull SuperNode n) throws RepositoryException {
                            Value titleLiteral = valueFactory.createLiteral(n.toString());
                            model.add(packageURI, DC.TITLE, titleLiteral);
                            model.add(releaseURI, DC.TITLE, titleLiteral);
                            model.add(projectURI, DC.TITLE, titleLiteral);
                        }
                    });


            //project
            model.add(projectURI, RDF.TYPE, ADMSSW.SOFTWARE_PROJECT);
            model.add(projectURI, ADMSSW.RELEASE, releaseURI);

            //release
            model.add(releaseURI, ADMSSW.PACKAGE, packageURI);
            model.add(releaseURI, RDF.TYPE, ADMSSW.SOFTWARE_RELEASE);
            createStringEntry(packageQuery, releaseURI, Entries.Version.class, PACKAGE_DEB.VERSION, model);
            createVersionEntry(packageQuery,releaseURI,PACKAGE_DEB.VERSION,model);

            //package
            model.add(packageURI, RDF.TYPE, PACKAGE_DEB.PACKAGE);
            createStringEntry(packageQuery, packageURI, Entries.MD5sum.class, PACKAGE_DEB.MD5SUM, model);
            createStringEntry(packageQuery, packageURI, Entries.SHA1.class, PACKAGE_DEB.SHA1, model);
            createStringEntry(packageQuery, packageURI, Entries.SHA256.class, PACKAGE_DEB.SHA256, model);
            createStringEntry(packageQuery, packageURI, Entries.Homepage.class, PACKAGE_DEB.HOMEPAGE, model);
            createStringEntry(packageQuery, packageURI, Entries.Architecture.class, PACKAGE_DEB.ARCHITECTURE, model);
            createStringEntry(packageQuery, packageURI, Entries.Description.class, PACKAGE_DEB.DESCRIPTION, model);
            createStringEntry(packageQuery, packageURI, Entries.Filename.class, PACKAGE_DEB.FILENAME, model);
            createEmailEntry(packageQuery, packageURI, Entries.Maintainer.class, PACKAGE_DEB.MAINTAINER_PROPERTY,model);
            createEmailEntry(packageQuery, packageURI, Entries.Uploaders.class, PACKAGE_DEB.UPLOADER,model);

            createDepedencyEntry(packageQuery, packageURI, Entries.Depends.class, PACKAGE_DEB.DEPENDS, model);
            createDepedencyEntry(packageQuery, packageURI, Entries.PreDepends.class, PACKAGE_DEB.PRE_DEPENDS, model);
            createDepedencyEntry(packageQuery, packageURI, Entries.Recommends.class, PACKAGE_DEB.RECOMMENDS, model);
            createDepedencyEntry(packageQuery, packageURI, Entries.Suggests.class, PACKAGE_DEB.SUGGESTS, model);
            createDepedencyEntry(packageQuery,packageURI,Entries.Breaks.class,PACKAGE_DEB.BREAKS, model);
            createDepedencyEntry(packageQuery, packageURI, Entries.Replaces.class, PACKAGE_DEB.REPLACES, model);
            createDepedencyEntry(packageQuery, packageURI, Entries.Provides.class, PACKAGE_DEB.PROVIDES, model);
            createDepedencyEntry(packageQuery,packageURI,Entries.BuiltUsing.class,PACKAGE_DEB.BUILT_USING, model);
            createDepedencyEntry(packageQuery,packageURI,Entries.Conflicts.class,PACKAGE_DEB.CONFLICTS, model);
            createDepedencyEntry(packageQuery,packageURI,Entries.BuildConflicts.class,PACKAGE_DEB.BUILD_CONFLICTS, model);
            createDepedencyEntry(packageQuery,packageURI,Entries.BuildConflictsIndep.class,PACKAGE_DEB.BUILD_CONFLICTS_INDEP, model);
            createDepedencyEntry(packageQuery,packageURI,Entries.BuildDepends.class,PACKAGE_DEB.BUILD_DEPENDS, model);
            createDepedencyEntry(packageQuery,packageURI,Entries.BuildDependsIndep.class,PACKAGE_DEB.BUILD_DEPENDS_INDEP, model);
            createVersionEntry(packageQuery,packageURI,PACKAGE_DEB.VERSION,model);
            return model;

        } catch (RepositoryException | QueryEvaluationException e) {
            throw new ProviderException(e);
        }
    }

    private void createEmailEntry(final BasicSuperNode packageQuery,final URI packageURI,final Class c,final URI property,final Model model) throws RepositoryException, QueryEvaluationException {
        packageQuery
                .forEachChildStringNode(c)
                .execute(new SuperNodeQuery.Function() {
                    @Override
                    public void execute(@NotNull SuperNode n) throws RepositoryException, QueryEvaluationException {
                        try {
                            checkMailExists.setBinding("package",packageURI);
                            checkMailExists.setBinding("property",property);
                            for(InternetAddress a : InternetAddress.parse(n.toString(),false)){
                                BNode bNode = valueFactory.createBNode();
                                if(a.getAddress() != null && a.getPersonal() != null){
                                    URI mboxVal =    valueFactory.createURI("mailto:" + a.getAddress());
                                    Value nameVal = valueFactory.createLiteral(a.getPersonal());
                                    checkMailExists.setBinding("mboxval",mboxVal);
                                    checkMailExists.setBinding("nameval",nameVal);
                                    if(!checkMailExists.evaluate()){
                                        model.add(packageURI, property, bNode);
                                        model.add(bNode, FOAF.MBOX, mboxVal);
                                        model.add(bNode, FOAF.NAME, nameVal);
                                    }
                                }
                            }
                        } catch (AddressException e) {
                            LOGGER.error("ERROR parsing EMAIL {}", n.toString(), e);
                        }
                    }
                });
    }

    private void createVersionEntry(final BasicSuperNode packageQuery,final URI packageURI,final URI property,final Model model) throws RepositoryException, QueryEvaluationException {
        packageQuery
                .forEachChild(Entries.Version.class)
                .forEachChild(Version.class)
                .execute(new SuperNodeQuery.Function() {
                    @Override
                    public void execute(@NotNull SuperNode n) throws RepositoryException {
                        model.add(packageURI, property, valueFactory.createLiteral(n.toString()));
                    }
                });
    }

    private void createDepedencyEntry(final BasicSuperNode packageQuery, final URI packageURI, final Class c, final URI property, final Model model) throws RepositoryException, QueryEvaluationException {
        packageQuery
                .forEachChild(c)
                .forEachChild(DependencyConjunction.class)
                .forEachChild(DependencyDisjunction.class)
                .forEachChild(PackageIdentifier.class).execute(new SuperNodeQuery.Function() {
            @Override
            public void execute(@NotNull SuperNode n) throws RepositoryException {
                PackageIdentifier id = (PackageIdentifier) n;
                URI dependencyURI =  valueFactory.createURI(packageURI.toString()+"#",property.getLocalName()+id.getName());
                model.add(packageURI, property, dependencyURI);
                model.add(dependencyURI, DC.DESCRIPTION, valueFactory.createLiteral(id.toString()));
                Version version = id.getVersion();
                if(version != null)
                    model.add(dependencyURI, PACKAGE_DEB.VERSION, valueFactory.createLiteral(version.toString()));
                model.add(dependencyURI, property, createProjectUri(id.getName()));
            }
        });
    }

    private void createStringEntry(@NotNull final BasicSuperNode packageQuery, @NotNull final URI packageURI, @NotNull final Class c, @NotNull final URI property,@NotNull final Model model) throws RepositoryException, QueryEvaluationException {
        packageQuery
                .forEachChildStringNode(c)
                .execute(new SuperNodeQuery.Function() {
                    @Override
                    public void execute(@NotNull SuperNode n) throws RepositoryException {
                        model.add(packageURI, property, valueFactory.createLiteral(n.toString()));
                    }
                });

    }

    private URI packageURI;
    private URI releaseURI;
    private URI projectURI;

    private void calculateURIs(@NotNull Package p) {
        PackageType type;
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
        packageURI = createPackageUri(name,version,arch);
        releaseURI = createReleaseUri(name,version);
        projectURI = createProjectUri(name);
    }


    public URI createProjectUri(String projectName){
        return  valueFactory.createURI(rooturl, projectName);
    }
    public URI createPackageUri(String projectName,String version,String arch){
        return  valueFactory.createURI(rooturl, projectName +"/" + version + "/" + arch);
    }


    public URI createReleaseUri(String projectName,String version){
        return valueFactory.createURI(rooturl, projectName+"/" + version);
    }

}
