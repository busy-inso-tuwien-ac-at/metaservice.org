package org.metaservice.core.deb;

import org.metaservice.core.deb.parser.ast.*;
import org.metaservice.core.deb.parser.ast.Package;
import org.jetbrains.annotations.NotNull;
import org.metaservice.api.archive.Archive;
import org.metaservice.core.deb.parser.PackagesParser;
import org.metaservice.api.ns.ADMSSW;
import org.metaservice.api.ns.METASERVICE;
import org.metaservice.api.ns.PACKAGE_DEB;
import org.metaservice.api.provider.Provider;
import org.metaservice.api.provider.ProviderException;
import org.metaservice.core.rdf.BufferedSparql;
import org.openrdf.model.*;
import org.openrdf.model.vocabulary.DC;
import org.openrdf.model.vocabulary.FOAF;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.query.*;
import org.openrdf.repository.RepositoryException;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.BasicParseRunner;
import org.parboiled.support.ParsingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DebianPackageProvider implements Provider<Package> {
    public static final Logger LOGGER = LoggerFactory.getLogger(DebianPackageProvider.class);

    @NotNull
    public static String rooturl = "http://metaservice.org/d/packages/debian/";

    private final BufferedSparql bufferedSparql;
    private final Archive archive;
    private final TupleQuery repoSelect;
    private final BooleanQuery checkMailExists;
    private final Map<String,String> repoMap;

    public DebianPackageProvider(BufferedSparql bufferedSparql,DebianGitCacheArchive archive ) throws RepositoryException, MalformedQueryException, IOException, InterruptedException {
        this.bufferedSparql = bufferedSparql;
        checkMailExists = bufferedSparql.prepareBooleanQuery(QueryLanguage.SPARQL,"ASK { ?package ?property ?x. ?x <" + FOAF.MBOX +"> ?mboxvalue; <" + FOAF.NAME + "> ?mboxname }");
        repoSelect = bufferedSparql.prepareTupleQuery(QueryLanguage.SPARQL,"SELECT ?repo ?time ?path { ?resource <"+METASERVICE.METADATA+ "> ?m. ?m <" + METASERVICE.SOURCE +"> ?repo; <"+METASERVICE.TIME+"> ?time; <"+METASERVICE.PATH+ "> ?path.}");
        this.archive = archive;


        repoMap = new HashMap<>();
        repoMap.put("http://ftp.debian.org/debian/","/opt/crawlertest/data/");
        repoMap.put("http://security.debian.org/", "/opt/crawlertest/data_security/");
    }


    public void flushModel() {
        bufferedSparql.flushModel();
    }

    @Override
    public void refresh(URI uri) {


        repoSelect.setBinding("resource",uri);
        try {
            //TODO get correct archive
            TupleQueryResult result =repoSelect.evaluate();
            if(!result.hasNext())
                throw new RuntimeException("CANNOT REFRESH - no metainformation available");
            BindingSet set = result.next();

            String time = set.getBinding("time").getValue().stringValue();
            String repo = set.getBinding("repo").getValue().stringValue();
            String path = set.getBinding("path").getValue().stringValue();
            LOGGER.info("READING time {}",time);
            LOGGER.info("READING repo {}",repo);
            LOGGER.info("READING path {}",path);
            String archivesrc = repoMap.get(repo);

            String toParse = archive.getContent(time,path);

            PackagesParser parser = Parboiled.createParser(PackagesParser.class);
            BasicParseRunner runner = new BasicParseRunner(parser.List());
            try{
                // retrieve version from archive
                ParsingResult<?> parsingResult = runner.run(toParse);
                for (SuperNode node : ((SuperNode) parsingResult.valueStack.pop()).getChildren()) {
                    putPackage((Package) node,repo,time,path);
                }
            }catch (Exception e){
                try {
                    String errorFilename = "errorRefresh";
                    FileWriter fileWriter = new FileWriter(errorFilename);
                    fileWriter.write(archivesrc +"\n");
                    fileWriter.write(e.toString() + "\n");
                    fileWriter.write(toParse);
                    fileWriter.close();
                    LOGGER.error("Parsing failed, dumping to file " + errorFilename,e);
                } catch (IOException e1) {
                    LOGGER.error("Error dumping",e1);
                }
            }
            LOGGER.info("done processing {} {}", time, path);
            // delete existing?
            // put again
        } catch (QueryEvaluationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public void create(Package o) throws ProviderException {
        try {
            putPackage(o,null,null,null);//TODO conceptionally not ok...
        } catch (RepositoryException | QueryEvaluationException e) {
            throw new ProviderException(e);
        }
    }

    enum PackageType{binary,source};


    public static interface SuperNodeQuery{
        @NotNull
        public SuperNodeQuery forEachChild(Class c);
        public void execute(Function f) throws RepositoryException, QueryEvaluationException;
        public static interface Function{
            public void execute(SuperNode n) throws RepositoryException, QueryEvaluationException;
        }
    }

    public static class BasicSuperNode implements SuperNodeQuery{

        final ArrayList<SuperNode>nodeList = new ArrayList<SuperNode>();
        public BasicSuperNode(SuperNode d){
            nodeList.add(d);
        }

        public BasicSuperNode(Collection<SuperNode> d){
            nodeList.addAll(d);
        }

        @NotNull
        @Override
        public SuperNodeQuery forEachChild(@NotNull Class c) {
            ArrayList<SuperNode>resultList = new ArrayList<SuperNode>();
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

    public void putPackage(@NotNull Package p, final String repo, final String time, final String path) throws RepositoryException, QueryEvaluationException {
        calculateURIs(p);
        final BNode metadata = bufferedSparql.createBNode();
        Value pathLiteral = bufferedSparql.createLiteral(path);
        Value timeLiteral = bufferedSparql.createLiteral(time);
        Value repoLiteral = bufferedSparql.createLiteral(repo);
        bufferedSparql.addStatement(metadata, METASERVICE.PATH,pathLiteral);
        bufferedSparql.addStatement(metadata, METASERVICE.TIME,timeLiteral);
        bufferedSparql.addStatement(metadata, METASERVICE.SOURCE,repoLiteral);
        bufferedSparql.addStatement(metadata, DC.DATE,bufferedSparql.createLiteral(new Date()));
        bufferedSparql.addStatement(metadata, DC.CREATOR, bufferedSparql.createLiteral(DebianPackageProvider.class.getCanonicalName()));


        BasicSuperNode packageQuery = new BasicSuperNode(p);
        packageQuery
                .forEachChild(Entries.Package.class)
                .forEachChild(PackageIdentifier.class)
                .execute(new SuperNodeQuery.Function() {
                    @Override
                    public void execute(@NotNull SuperNode n) throws RepositoryException {
                        Value titleLiteral = bufferedSparql.createLiteral(n.toString());
                        bufferedSparql.addStatement(packageURI, DC.TITLE, titleLiteral,metadata);
                        bufferedSparql.addStatement(releaseURI, DC.TITLE, titleLiteral,metadata);
                        bufferedSparql.addStatement(projectURI, DC.TITLE, titleLiteral,metadata);
                    }
                });


        //project
        bufferedSparql.addStatement(projectURI, OWL.CLASS, ADMSSW.SOFTWARE_PROJECT);
        bufferedSparql.addStatement(projectURI, ADMSSW.RELEASE, releaseURI);

        //release
        bufferedSparql.addStatement(releaseURI, ADMSSW.PACKAGE, packageURI);
        bufferedSparql.addStatement(releaseURI, OWL.CLASS, ADMSSW.SOFTWARE_RELEASE);
        createStringEntry(packageQuery, releaseURI, Entries.Version.class, PACKAGE_DEB.VERSION);
        createVersionEntry(packageQuery,releaseURI,PACKAGE_DEB.VERSION);

        //package
        bufferedSparql.addStatement(packageURI, OWL.CLASS, PACKAGE_DEB.PACKAGE);
        createStringEntry(packageQuery, packageURI, Entries.MD5sum.class, PACKAGE_DEB.MD5SUM);
        createStringEntry(packageQuery, packageURI, Entries.SHA1.class, PACKAGE_DEB.SHA1);
        createStringEntry(packageQuery, packageURI, Entries.SHA256.class, PACKAGE_DEB.SHA256);
        createStringEntry(packageQuery, packageURI, Entries.Homepage.class, PACKAGE_DEB.HOMEPAGE);
        createStringEntry(packageQuery, packageURI, Entries.Architecture.class, PACKAGE_DEB.ARCHITECTURE);
        createStringEntry(packageQuery, packageURI, Entries.Description.class, PACKAGE_DEB.DESCRIPTION);
        createStringEntry(packageQuery, packageURI, Entries.Filename.class, PACKAGE_DEB.FILENAME);
        createEmailEntry(packageQuery, packageURI, Entries.Maintainer.class, PACKAGE_DEB.MAINTAINER_PROPERTY);
        createEmailEntry(packageQuery, packageURI, Entries.Uploaders.class, PACKAGE_DEB.UPLOADER);

        createDepedencyEntry(packageQuery, packageURI, Entries.Depends.class, PACKAGE_DEB.DEPENDS);
        createDepedencyEntry(packageQuery, packageURI, Entries.PreDepends.class, PACKAGE_DEB.PRE_DEPENDS);
        createDepedencyEntry(packageQuery, packageURI, Entries.Recommends.class, PACKAGE_DEB.RECOMMENDS);
        createDepedencyEntry(packageQuery, packageURI, Entries.Suggests.class, PACKAGE_DEB.SUGGESTS);
        createDepedencyEntry(packageQuery,packageURI,Entries.Breaks.class,PACKAGE_DEB.BREAKS);
        createDepedencyEntry(packageQuery, packageURI, Entries.Replaces.class, PACKAGE_DEB.REPLACES);
        createDepedencyEntry(packageQuery, packageURI, Entries.Provides.class, PACKAGE_DEB.PROVIDES);
        createDepedencyEntry(packageQuery,packageURI,Entries.BuiltUsing.class,PACKAGE_DEB.BUILT_USING);
        createDepedencyEntry(packageQuery,packageURI,Entries.Conflicts.class,PACKAGE_DEB.CONFLICTS);
        createDepedencyEntry(packageQuery,packageURI,Entries.BuildConflicts.class,PACKAGE_DEB.BUILD_CONFLICTS);
        createDepedencyEntry(packageQuery,packageURI,Entries.BuildConflictsIndep.class,PACKAGE_DEB.BUILD_CONFLICTS_INDEP);
        createDepedencyEntry(packageQuery,packageURI,Entries.BuildDepends.class,PACKAGE_DEB.BUILD_DEPENDS);
        createDepedencyEntry(packageQuery,packageURI,Entries.BuildDependsIndep.class,PACKAGE_DEB.BUILD_DEPENDS_INDEP);
        createVersionEntry(packageQuery,packageURI,PACKAGE_DEB.VERSION);
    }

    private void createEmailEntry(final BasicSuperNode packageQuery,final URI packageURI,final Class c,final URI property) throws RepositoryException, QueryEvaluationException {
        packageQuery
                .forEachChildStringNode(c)
                .execute(new SuperNodeQuery.Function() {
                    @Override
                    public void execute(@NotNull SuperNode n) throws RepositoryException, QueryEvaluationException {
                        try {
                            checkMailExists.setBinding("package",packageURI);
                            checkMailExists.setBinding("property",property);
                            for(InternetAddress a : InternetAddress.parse(n.toString(),false)){
                                BNode bNode = bufferedSparql.createBNode();
                                if(a.getAddress() != null && a.getPersonal() != null){
                                    URI mboxVal =    bufferedSparql.createURI("mailto:" + a.getAddress());
                                    Value nameVal = bufferedSparql.createLiteral(a.getPersonal());
                                    checkMailExists.setBinding("mboxval",mboxVal);
                                    checkMailExists.setBinding("nameval",nameVal);
                                    if(!checkMailExists.evaluate()){
                                        bufferedSparql.addStatement(packageURI, property, bNode);
                                        bufferedSparql.addStatement(bNode, FOAF.MBOX,mboxVal);
                                        bufferedSparql.addStatement(bNode, FOAF.NAME,nameVal);
                                    }
                                }
                            }
                        } catch (AddressException e) {
                            LOGGER.error("ERROR parsing EMAIL {}", n.toString(), e);
                        }
                    }
                });
    }

    private void createVersionEntry(final BasicSuperNode packageQuery,final URI packageURI,final URI property) throws RepositoryException, QueryEvaluationException {
        packageQuery
                .forEachChild(Entries.Version.class)
                .forEachChild(Version.class)
                .execute(new SuperNodeQuery.Function() {
                    @Override
                    public void execute(@NotNull SuperNode n) throws RepositoryException {
                        bufferedSparql.addStatement(packageURI, property, bufferedSparql.createLiteral(n.toString()));
                    }
                });
    }

    private void createDepedencyEntry(final BasicSuperNode packageQuery, final URI packageURI, final Class c,final URI property) throws RepositoryException, QueryEvaluationException {
        packageQuery
                .forEachChild(c)
                .forEachChild(DependencyConjunction.class)
                .forEachChild(DependencyDisjunction.class)
                .forEachChild(PackageIdentifier.class).execute(new SuperNodeQuery.Function() {
            @Override
            public void execute(@NotNull SuperNode n) throws RepositoryException {
                PackageIdentifier id = (PackageIdentifier) n;
                URI dependencyURI =  bufferedSparql.createURI(packageURI.toString()+"#",property.getLocalName()+id.getName());
                bufferedSparql.addStatement(packageURI, property, dependencyURI);
                bufferedSparql.addStatement(dependencyURI,DC.DESCRIPTION,bufferedSparql.createLiteral(id.toString()));
                Version version = id.getVersion();
                if(version != null)
                    bufferedSparql.addStatement(dependencyURI,PACKAGE_DEB.VERSION,bufferedSparql.createLiteral(version.toString()));
                bufferedSparql.addStatement(dependencyURI,property,createProjectUri(id.getName()));
            }
        });
    }

    private void createStringEntry(@NotNull final BasicSuperNode packageQuery, @NotNull final URI packageURI, @NotNull final Class c, @NotNull final URI property) throws RepositoryException, QueryEvaluationException {
        packageQuery
                .forEachChildStringNode(c)
                .execute(new SuperNodeQuery.Function() {
                    @Override
                    public void execute(@NotNull SuperNode n) throws RepositoryException {
                        bufferedSparql.addStatement(packageURI, property, bufferedSparql.createLiteral(n.toString()));
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
                for(SuperNode x : ((Entries.Package) node).getChildren()){
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
                for(SuperNode x : ((Entries.Version) node).getChildren()){
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
        return  bufferedSparql.createURI(rooturl, projectName);
    }
    public URI createPackageUri(String projectName,String version,String arch){
        return  bufferedSparql.createURI(rooturl, projectName +"/" + version + "/" + arch);
    }


    public URI createReleaseUri(String projectName,String version){
        return bufferedSparql.createURI(rooturl, projectName+"/" + version);
    }

}
