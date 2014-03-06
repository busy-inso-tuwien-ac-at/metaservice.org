package org.metaservice.api.sparql;
import org.junit.Test;
import org.metaservice.api.rdf.vocabulary.ADMSSW;
import org.metaservice.api.rdf.vocabulary.DOAP;
import org.metaservice.api.rdf.vocabulary.METASERVICE;
import org.metaservice.api.rdf.vocabulary.PACKAGE_DEB;
import org.metaservice.api.sparql.builders.QueryBuilder;
import org.metaservice.api.sparql.impl.SparqlQueryBuilderImpl;
import org.metaservice.api.sparql.nodes.Variable;
import org.openrdf.model.Literal;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 * Created by ilo on 05.03.14.
 */
public class SPARQLQueryBuilderTest {
    private final Variable resource = new Variable("resource");
    private final Variable version = new Variable("version");
    private final Variable project = new Variable("project");
    private final Variable title = new Variable("title");
    private final Variable release = new Variable("release");
    private final Variable arch = new Variable("arch");
    private final Variable _package = new Variable("package");
    private final Variable _package2 = new Variable("package2");
    ValueFactory valueFactory = ValueFactoryImpl.getInstance();

/*
    @Test
    public void asdf(){
        String query = SparqlQueryBuilderImpl.getInstance()
                .select(
                        aggregate("SAMPLE",_package2,_package),
                        var(arch))

                .where(
                        triplePattern(project, DOAP.RELEASE, release),
                        triplePattern(release, ADMSSW.PACKAGE, _package2),
                        triplePattern(_package2, PACKAGE_DEB.ARCHITECTURE, arch)
                )
                .groupBy(arch)
                .build(true);
        System.err.println(query);
    }

    @Test
    public void testUnion(){
        String query = SparqlQueryBuilderImpl.getInstance()
                .select(
                        var(project)
                )
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
                .build(true);
        System.err.println(query);

    }

    @Test
    public void fooAutomated(){

    }
    @Test
    public void foo(){

    }*/
}
