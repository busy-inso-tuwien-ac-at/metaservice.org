package org.metaservice.core.deb.postprocessor;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.postprocessor.PostProcessorSparqlBuilder;
import org.metaservice.api.postprocessor.PostProcessorSparqlQuery;
import org.metaservice.api.rdf.vocabulary.ADMSSW;
import org.metaservice.api.rdf.vocabulary.DOAP;
import org.metaservice.api.rdf.vocabulary.PACKAGE_DEB;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.api.sparql.buildingcontexts.SparqlQuery;
import org.metaservice.api.sparql.nodes.Variable;
import org.metaservice.core.deb.util.DebianVersionComparator;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.*;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;

public class DebianVersionReasoner implements PostProcessor {
    public static final Logger LOGGER = LoggerFactory.getLogger(DebianVersionReasoner.class);
    public final static String URI_REGEX = "^http://metaservice.org/d/(packages|releases|projects)/[^/#]+/[^/#]+/[^/#]+(/[^/#]+)?$";

    private final TupleQuery selectPackageVersionsOrderQuery;
    private final TupleQuery selectVersionsOrderQuery;
    private final TupleQuery selectPackageQuery;
    private final TupleQuery selectProjectQuery;

    private final ValueFactory valueFactory;

    private final Variable resource = new Variable("resource");
    private final Variable version = new Variable("version");
    private final Variable project = new Variable("project");
    private final Variable title = new Variable("title");
    private final Variable release = new Variable("release");
    private final Variable arch = new Variable("arch");
    private final Variable _package = new Variable("package");
    private final Variable _package2 = new Variable("package2");

    @Inject
    public DebianVersionReasoner(RepositoryConnection repositoryConnection, ValueFactory valueFactory) throws RepositoryException, MalformedQueryException {
        this.valueFactory = valueFactory;


        String queryString;
        queryString = new PostProcessorSparqlQuery(){
            @Override
            public String build() {
                return select(true,
                        var(version),
                        var(title),
                        var(arch),
                        var(resource)
                )
                        .where(triplePattern(project,DOAP.RELEASE,release),
                                triplePattern(release,ADMSSW.PACKAGE,resource),
                                triplePattern(resource,PACKAGE_DEB.TITLE,title),
                                triplePattern(resource,PACKAGE_DEB.VERSION,version),
                                triplePattern(resource,PACKAGE_DEB.ARCHITECTURE,arch)
                        )
                        .build();
            }
        }.toString();
        LOGGER.info(queryString);
        selectPackageVersionsOrderQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);


        queryString = new PostProcessorSparqlQuery(){
            @Override
            public String build() {
                return select(true,
                        var(version),
                        var(title),
                        var(resource)
                )
                        .where(
                                triplePattern(project, DOAP.RELEASE, resource),
                                triplePattern(resource, PACKAGE_DEB.TITLE, title),
                                triplePattern(resource, PACKAGE_DEB.VERSION, version)
                        )
                        .build();
            }
        }.toString();
        LOGGER.info(queryString);
        selectVersionsOrderQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);


        queryString =new PostProcessorSparqlQuery(){
            @Override
            public String build() {
                return select(true,
                        aggregate("SAMPLE", _package2, _package),
                        var(arch)
                )
                        .where(
                                triplePattern(project, DOAP.RELEASE, release),
                                triplePattern(release, ADMSSW.PACKAGE, _package2),
                                triplePattern(_package2, PACKAGE_DEB.ARCHITECTURE, arch)
                        )
                        .groupBy(arch)
                        .build();
            }
        }.toString();
        LOGGER.info(queryString);
        selectPackageQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,queryString);


        queryString = new PostProcessorSparqlQuery(){
            @Override
            public String build() {
                return select(true,var(project))
                        .where(
                                union(
                                        graphPattern(
                                                triplePattern(project,DOAP.RELEASE,resource)
                                        ),
                                        graphPattern(
                                                triplePattern(project,DOAP.RELEASE,release),
                                                triplePattern(release,ADMSSW.PACKAGE,resource)
                                        )
                                )
                        )
                        .limit(1)
                        .build();
            }
        }.toString();
        LOGGER.info(queryString);
        selectProjectQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,queryString);
    }


    public void update(@NotNull final String uri, RepositoryConnection resultConnection) throws QueryEvaluationException, RepositoryException, MalformedQueryException, UpdateExecutionException {
        LOGGER.info("Processing versions");
        updateVersion(uri,resultConnection);

        selectPackageQuery.setBinding(project.toString(), valueFactory.createURI(uri));
        TupleQueryResult result = selectPackageQuery.evaluate();
        while (result.hasNext()){
            BindingSet set = result.next();
            LOGGER.info("Processing {}", set.getValue(arch.toString()).stringValue());
            updatePackage(uri, set.getValue(arch.toString()).stringValue(), resultConnection);
        }


    }

    private void updateVersion(String uri,RepositoryConnection resultConnection) throws QueryEvaluationException, RepositoryException {
        selectVersionsOrderQuery.setBinding(this.project.toString(), valueFactory.createURI(uri));

        TupleQueryResult result = selectVersionsOrderQuery.evaluate();
        ArrayList<String> list = new ArrayList<>();
        HashMap<String,String> versionUriMap = new HashMap<>();
        while (result.hasNext()){
            BindingSet set = result.next();
            String version = set.getValue(this.version.toString()).stringValue();
            versionUriMap.put(version, set.getValue(this.resource.toString()).stringValue());
            LOGGER.info(version);
            list.add(version);
        }
        Collections.sort(list, DebianVersionComparator.getInstance());
        LOGGER.info("SORTED:");
        for(String s : list){
            LOGGER.info(s);
        }

        for(int i = 0; i < list.size()-1;i++){
            addStatements(versionUriMap.get(list.get(i)),versionUriMap.get(list.get(i+1)),resultConnection);
        }
    }

    public void updatePackage(String project, String arch,RepositoryConnection resultConnection) throws RepositoryException, MalformedQueryException, QueryEvaluationException {
        selectPackageVersionsOrderQuery.setBinding(this.arch.toString(), valueFactory.createLiteral(arch));
        selectPackageVersionsOrderQuery.setBinding(this.project.toString(), valueFactory.createURI(project));
        TupleQueryResult result = selectPackageVersionsOrderQuery.evaluate();
        ArrayList<String> list = new ArrayList<>();


        HashMap<String,String> releaseUriMap = new HashMap<>();
        while (result.hasNext()){
            BindingSet set = result.next();
            String release = set.getValue(this.release.toString()).stringValue();
            releaseUriMap.put(release, set.getValue(this.resource.toString()).stringValue());
            LOGGER.info(release);
            list.add(release);
        }
        Collections.sort(list, DebianVersionComparator.getInstance());
        LOGGER.info("SORTED:");
        for(String s : list){
            LOGGER.info(s);
        }

        for(int i = 0; i < list.size()-1;i++){
            addStatements(releaseUriMap.get(list.get(i)), releaseUriMap.get(list.get(i + 1)), resultConnection);
        }
    }

    private void addStatements(String s1,String s2,RepositoryConnection resultConnection) throws RepositoryException {
        Resource uri1 = valueFactory.createURI(s1);
        Resource uri2 = valueFactory.createURI(s2);
        resultConnection.add(uri1, ADMSSW.NEXT, uri2);
        resultConnection.add(uri2, ADMSSW.PREV,uri1);
        LOGGER.info(uri1 + " next " + uri2);
    }

    @Override
    public void process(@NotNull final URI uri, @NotNull final RepositoryConnection resultConnection, Date time) throws PostProcessorException{
        try{
            selectProjectQuery.setBinding(PostProcessorSparqlBuilder.getDateVariable().toString(),valueFactory.createLiteral(time));
            selectPackageQuery.setBinding(PostProcessorSparqlBuilder.getDateVariable().toString(),valueFactory.createLiteral(time));
            selectPackageVersionsOrderQuery.setBinding(PostProcessorSparqlBuilder.getDateVariable().toString(),valueFactory.createLiteral(time));
            selectVersionsOrderQuery.setBinding(PostProcessorSparqlBuilder.getDateVariable().toString(),valueFactory.createLiteral(time));
            selectProjectQuery.setBinding(resource.toString(),uri);
            TupleQueryResult result = selectProjectQuery.evaluate();
            if(result == null)
            {
                LOGGER.error("result is somehow null - why can this happen? {}" ,uri.toString());
                return;
            }
            try{
                if(result.hasNext()){
                    BindingSet bindings = result.next();
                    Binding binding = bindings.getBinding(project.toString());
                    Value value = binding.getValue();
                    update(value.stringValue(),resultConnection);
                }else {
                    LOGGER.info("could not find project for " + uri.stringValue());
                }
            }catch ( QueryEvaluationException| RepositoryException| UpdateExecutionException| MalformedQueryException e){
                throw new PostProcessorException("unable to process " + uri.stringValue(),e);
            }finally{
                result.close();
            }
        } catch (QueryEvaluationException e) {
            throw new PostProcessorException(e);
        }
    }

    @Override
    public boolean abortEarly(@NotNull final URI uri) throws PostProcessorException{
        return !uri.stringValue().matches(URI_REGEX);
    }

    @Override
    public List<SparqlQuery> getQueries() {
        return null;
    }
}
