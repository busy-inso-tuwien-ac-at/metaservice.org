package org.metaservice.core.deb.postprocessor;

import com.google.common.collect.ArrayListMultimap;
import org.jetbrains.annotations.NotNull;
import org.metaservice.api.postprocessor.PostProcessor;
import org.metaservice.api.postprocessor.PostProcessorException;
import org.metaservice.api.postprocessor.PostProcessorSparqlBuilder;
import org.metaservice.api.postprocessor.PostProcessorSparqlQuery;
import org.metaservice.api.rdf.vocabulary.*;
import org.metaservice.api.sparql.nodes.BoundVariable;
import org.metaservice.api.sparql.nodes.Variable;
import org.metaservice.core.deb.util.DebianVersionComparator;
import org.openrdf.model.URI;
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
    public final static String URI_REGEX = "^http://metaservice.org/d/(packages|releases|projects)/(debian|ubuntu)/[^/#]+/[^/#]+(/[^/#]+)?$";

    private final TupleQuery query;

    private final ValueFactory valueFactory;

    private final BoundVariable resource = new BoundVariable("resource");
    private final Variable revision = new Variable("revision");
    private final Variable release = new Variable("release");
    private final Variable arch = new Variable("arch");
    private final Variable _package = new Variable("package");


    @Inject
    public DebianVersionReasoner(RepositoryConnection repositoryConnection, ValueFactory valueFactory) throws RepositoryException, MalformedQueryException {
        this.valueFactory = valueFactory;
        final Variable release_temp = new Variable("release_temp");
        final Variable project = new Variable("project");


        String queryString;
        queryString = new PostProcessorSparqlQuery(){
            @Override
            public String build() {
                return select(true,var(release),var(_package),var(revision),var(arch))
                        .where(
                                triplePattern(BIGDATA.SUB_QUERY,BIGDATA.OPTIMIZE,BIGDATA.NONE),
                                union(
                                        graphPattern(
                                                triplePattern(project, DOAP.RELEASE, resource)
                                        ),
                                        graphPattern(
                                                triplePattern(release_temp, ADMSSW.PACKAGE, resource),
                                                triplePattern(project, DOAP.RELEASE, release_temp)
                                        )
                                ),
                                triplePattern(project, DOAP.RELEASE, release),
                                triplePattern(release, DEB.VERSION, revision),
                                triplePattern(release, ADMSSW.PACKAGE, _package),
                                triplePattern(_package, DEB.ARCHITECTURE, arch)
                        )
                        .build();
            }
        }.toString();
        LOGGER.info(queryString);
        query = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
    }

    @Override
    public void process(@NotNull final URI uri, @NotNull final RepositoryConnection resultConnection, Date time) throws PostProcessorException{
        try{
            query.setBinding(PostProcessorSparqlBuilder.getDateVariable().toString(),valueFactory.createLiteral(time));
            query.setBinding(resource.toString(),uri);
            ArrayListMultimap<String, URI> archMap = ArrayListMultimap.create();
            HashMap<URI,String> revisionMap = new HashMap<>();
            TupleQueryResult result = query.evaluate();
            if(result == null)
            {
                LOGGER.error("result is somehow null - why can this happen? {}" ,uri.toString());
                return;
            }
            try{
                if(!result.hasNext()){
                        LOGGER.info("could not find anything for " + uri.stringValue());
                }
                while (result.hasNext()){
                    BindingSet bindings = result.next();
                    URI releaseURI = (URI) bindings.getValue(release.toString());
                    URI packageURI = (URI) bindings.getValue(_package.toString());
                    String revisionString = bindings.getValue(revision.toString()).stringValue();
                    String archString = bindings.getValue(arch.toString()).stringValue();
                    archMap.put("RELEASE",releaseURI);
                    archMap.put(archString,packageURI);
                    revisionMap.put(releaseURI,revisionString);
                    revisionMap.put(packageURI,revisionString);
                }

                for(Map.Entry<String,Collection<URI>> entry : archMap.asMap().entrySet()) {
                    LOGGER.info("processing arch {}",entry.getKey());
                    HashSet<String> set = new HashSet<>();
                    HashMap<String, URI> versionUriMap = new HashMap<>();
                    for(URI x : entry.getValue()){
                        String revision = revisionMap.get(x);
                        versionUriMap.put(revision, x );
                        set.add(revision);
                        LOGGER.debug(revision);
                    }
                    ArrayList<String> list = new ArrayList<>();
                    list.addAll(set);
                    Collections.sort(list, DebianVersionComparator.getInstance());
                    LOGGER.info("SORTED:");
                    for (String s : list) {
                        LOGGER.info(s);
                    }

                    for (int i = 0; i < list.size() - 1; i++) {
                        URI uri1 =versionUriMap.get(list.get(i));
                        URI uri2 = versionUriMap.get(list.get(i + 1));
                        resultConnection.add(uri1, XHV.NEXT, uri2);
                        resultConnection.add(uri2, XHV.PREV,uri1);
                        LOGGER.info(uri1 + " next " + uri2);
                    }
                }

            }catch ( QueryEvaluationException| RepositoryException e){
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
}
