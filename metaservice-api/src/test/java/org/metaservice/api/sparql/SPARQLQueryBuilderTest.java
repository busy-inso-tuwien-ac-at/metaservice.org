package org.metaservice.api.sparql;
import org.junit.Test;
import org.metaservice.api.rdf.vocabulary.ADMSSW;
import org.metaservice.api.rdf.vocabulary.DOAP;
import org.metaservice.api.rdf.vocabulary.METASERVICE;
import org.metaservice.api.rdf.vocabulary.PACKAGE_DEB;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

import java.util.Date;

import static org.metaservice.api.sparql.SparqlQueryBuilderImpl.*;

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
    @Test
    public void genericTest(){
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();
        Variable time = new Variable("time");
        Variable resource = new Variable("resource");
        String query =
                SparqlQueryBuilderImpl.getInstance()
                .select(
                        var(time)
                )
                .where(
                        triplePattern(resource, METASERVICE.TIME, valueFactory.createLiteral(new Date())),
                        filter(
                                and(
                                        sameTerm(val(time),val(time)),
                                        or(
                                                not(sameTerm(val(time),val(time))),
                                                not(sameTerm(val(time),val(time)))
                                        )
                                )
                        )
                )
                .orderByAsc(time)
                .build();

        System.err.println(query);
    }

    @Test
    public  void pTesT(){
        String query = SparqlQueryBuilderImpl.getPrettyInstance()
                .select(
                        var(version),
                        var(title),
                        var(arch),
                        var(resource)
                )
                .where(
                        triplePattern(project, DOAP.RELEASE, release),
                        triplePattern(release, ADMSSW.PACKAGE, resource),
                        triplePattern(resource, PACKAGE_DEB.TITLE, title),
                        triplePattern(resource, PACKAGE_DEB.VERSION, version),
                        triplePattern(resource, PACKAGE_DEB.ARCHITECTURE, arch)
                )
                .build();
        System.err.println(query);
    }

    @Test
    public void asdf(){
        String query = SparqlQueryBuilderImpl.getPrettyInstance()
                .select(
                        aggregate("SAMPLE",_package2,_package),
                        var(arch))

                .where(
                        triplePattern(project, DOAP.RELEASE, release),
                        triplePattern(release, ADMSSW.PACKAGE, _package2),
                        triplePattern(_package2, PACKAGE_DEB.ARCHITECTURE, arch)
                )
                .groupBy(arch)
                .build();
        System.err.println(query);
    }
      
    @Test
    public void testUnion(){
        String query = SparqlQueryBuilderImpl.getPrettyInstance()
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
                .build();
        System.err.println(query);

    }
}
