package org.metaservice.core.deb;

import org.metaservice.core.ProductionConfig;
import org.metaservice.api.ns.ADMSSW;
import org.metaservice.api.ns.PACKAGE_DEB;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.core.rdf.BufferedSparql;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.query.*;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DebianVersionReasoner implements PostProcessor {

    public static void main(String[] args) throws RepositoryException, MalformedQueryException, QueryEvaluationException, UpdateExecutionException, PostProcessorException {
        BufferedSparql q = new BufferedSparql(new ProductionConfig());
        DebianVersionReasoner reasoner = new DebianVersionReasoner(q);
        reasoner.process(q.createURI("http://metaservice.org/d/packages/debian/libc6"));
        reasoner.close();
        LOGGER.info("DONE");
        System.exit(0);
    }

    public static final Logger LOGGER = LoggerFactory.getLogger(DebianVersionReasoner.class);
    private final static String baseURI = "http://metaservice.org/d/packages/debian/";

    private final TupleQuery selectPackageVersionsOrderQuery;
    private final TupleQuery selectVersionsOrderQuery;
    private final TupleQuery selectPackageQuery;
    private final TupleQuery selectProjectQuery;
    private final Update update;

    private final BufferedSparql bufferedSparql;

    public DebianVersionReasoner(BufferedSparql bufferedSparql ) throws RepositoryException, MalformedQueryException {
        this.bufferedSparql = bufferedSparql;
        String queryString  = "SELECT ?version ?title ?arch ?resource { ?project <" + ADMSSW.RELEASE+ "> ?release. ?release <"+ADMSSW.PACKAGE+"> ?resource. ?resource <"+ PACKAGE_DEB.TITLE +"> ?title; <" + PACKAGE_DEB.VERSION + "> ?version; <"+PACKAGE_DEB.ARCHITECTURE+"> ?arch.}";
        LOGGER.info(queryString);
        selectPackageVersionsOrderQuery = bufferedSparql.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
        queryString  = "SELECT ?version ?title ?resource {?project <" + ADMSSW.RELEASE+ "> ?resource. ?resource <"+ PACKAGE_DEB.TITLE +"> ?title; <" + PACKAGE_DEB.VERSION + "> ?version.}";
        LOGGER.info(queryString);
        selectVersionsOrderQuery = bufferedSparql.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
        queryString  = "SELECT (SAMPLE(?package2) as ?package) ?arch {?project <" + ADMSSW.RELEASE+ "> ?version. ?version <"+ADMSSW.PACKAGE+"> ?package2. ?package2 <"+PACKAGE_DEB.ARCHITECTURE+"> ?arch} group by ?arch";
        LOGGER.info(queryString);
        selectPackageQuery = bufferedSparql.prepareTupleQuery(QueryLanguage.SPARQL,queryString);

        queryString = "SELECT ?project {?project <" + ADMSSW.RELEASE+ "> ?resource.} UNION {?project <"+ ADMSSW.RELEASE + "> ?release. ?release <"+ADMSSW.PACKAGE+"> ?resource }";
        selectProjectQuery = bufferedSparql.prepareTupleQuery(QueryLanguage.SPARQL,queryString);

        queryString = "DELETE {?subject <"+ADMSSW.NEXT+"> ?object.?object <"+ADMSSW.PREV+"> ?subject. } WHERE \n" +
                "{{?resource <"+ADMSSW.RELEASE+"> ?subject. ?subject <"+ADMSSW.NEXT+"> ?object.} UNION {?resource <"+ADMSSW.RELEASE+"> ?version. ?version <"+ADMSSW.PACKAGE+"> ?subject. ?subject <"+ADMSSW.NEXT+"> ?object.}}";
        LOGGER.info(queryString);

        update = bufferedSparql.prepareUpdate(QueryLanguage.SPARQL,queryString);

    }


    public void update(String uri) throws QueryEvaluationException, RepositoryException, MalformedQueryException, UpdateExecutionException {
        update.setBinding("resource",bufferedSparql.createURI(uri));
        update.execute();

        LOGGER.info("Processing versions");
        updateVersion(uri);

        selectPackageQuery.setBinding("project", bufferedSparql.createURI(uri));
        TupleQueryResult result = selectPackageQuery.evaluate();
        while (result.hasNext()){
            BindingSet set = result.next();
            LOGGER.info("Processing {}", set.getValue("arch").stringValue());
            updatePackage(uri, set.getValue("arch").stringValue());
        }


    }

    private void updateVersion(String uri) throws QueryEvaluationException, RepositoryException {
        selectVersionsOrderQuery.setBinding("project", bufferedSparql.createURI(uri));

        TupleQueryResult result = selectVersionsOrderQuery.evaluate();
        ArrayList<String> list = new ArrayList<String>();
        HashMap<String,String> versionUriMap = new HashMap<String, String>();
        while (result.hasNext()){
            BindingSet set = result.next();
            String version = set.getValue("version").stringValue();
            versionUriMap.put(version, set.getValue("resource").stringValue());
            LOGGER.info(version);
            list.add(version);
        }
        Collections.sort(list, DebianVersionComperator.getInstance());
        LOGGER.info("SORTED:");
        for(String s : list){
            LOGGER.info(s);
        }

        for(int i = 0; i < list.size()-1;i++){
            addStatements(versionUriMap.get(list.get(i)),versionUriMap.get(list.get(i+1)));
        }
    }

    public void updatePackage(String project, String arch) throws RepositoryException, MalformedQueryException, QueryEvaluationException {
        selectPackageVersionsOrderQuery.setBinding("arch", bufferedSparql.createLiteral(arch));
        selectPackageVersionsOrderQuery.setBinding("project", bufferedSparql.createURI(project));
        TupleQueryResult result = selectPackageVersionsOrderQuery.evaluate();
        ArrayList<String> list = new ArrayList<String>();


        HashMap<String,String> versionUriMap = new HashMap<String, String>();
        while (result.hasNext()){
            BindingSet set = result.next();
            String version = set.getValue("version").stringValue();
            versionUriMap.put(version, set.getValue("resource").stringValue());
            LOGGER.info(version);
            list.add(version);
        }
        Collections.sort(list,DebianVersionComperator.getInstance());
        LOGGER.info("SORTED:");
        for(String s : list){
            LOGGER.info(s);
        }

        for(int i = 0; i < list.size()-1;i++){
            addStatements(versionUriMap.get(list.get(i)),versionUriMap.get(list.get(i+1)));
        }
    }

    private void addStatements(String s1,String s2) throws RepositoryException {
        Resource uri1 = bufferedSparql.createURI(s1);
        Resource uri2 = bufferedSparql.createURI(s2);
        bufferedSparql.addStatement(uri1, ADMSSW.NEXT, uri2);
        // not needed because of inference?
        //  bufferedSparql.addStatement(uri2, ADMSSW.PREV,uri1);
        LOGGER.info(uri1 + " next " + uri2);
    }

    private void close() throws RepositoryException {
        bufferedSparql.close();
    }

    @Override
    public void process(URI uri) throws PostProcessorException{
        try{
            selectProjectQuery.setBinding("resource",uri);
            TupleQueryResult result = selectProjectQuery.evaluate();
            try{
                if(result.hasNext()){
                    update(result.next().getBinding("resource").getValue().stringValue());
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
    public boolean abortEarly(URI uri) {
        return uri.stringValue().startsWith(baseURI);
    }
}
