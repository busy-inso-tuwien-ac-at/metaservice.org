package org.metaservice.api.sparql.buildingcontexts;

import org.junit.Test;
import org.metaservice.api.postprocessor.PostProcessorSparqlQuery;
import org.metaservice.api.rdf.vocabulary.ADMSSW;
import org.metaservice.api.rdf.vocabulary.DEB;
import org.metaservice.api.rdf.vocabulary.DOAP;
import org.metaservice.api.rdf.vocabulary.METASERVICE;
import org.metaservice.api.sparql.builders.QueryBuilder;
import org.metaservice.api.sparql.builders.SelectQueryBuilder;
import org.metaservice.api.sparql.impl.SparqlQueryBuilderImpl;
import org.metaservice.api.sparql.nodes.Variable;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;

import java.util.Date;

/**
 * Created by ilo on 05.03.14.
 */
public class DefaultSparqlQueryTest {
    private final Variable resource = new Variable("resource");
    private final Variable version = new Variable("version");
    private final Variable project = new Variable("project");
    private final Variable title = new Variable("title");
    private final Variable release = new Variable("release");
    private final Variable arch = new Variable("arch");
    private final Variable _package = new Variable("package");
    private final Variable _package2 = new Variable("package2");

    ValueFactory valueFactory = ValueFactoryImpl.getInstance();

    @Test
    public void basic(){

        SparqlQuery sparqlQuery = new DefaultSparqlQuery() {
            @Override
            public String build() {
                return
                        select(false,
                                var(project)
                        )
                                .where(
                                        union(
                                                graphPattern(
                                                        triplePattern(project, DOAP.RELEASE, resource)
                                                ),
                                                graphPattern(
                                                        triplePattern(project, DOAP.RELEASE, release),
                                                        triplePattern(release, ADMSSW.PACKAGE, resource)
                                                )
                                        )
                                )
                                .limit(1)
                                .build(true);
            }
        };
        System.err.println(sparqlQuery);
    }

    @Test
    public void basic2(){
        final Variable time = new Variable("time");
        final Variable resource = new Variable("resource");
        SparqlQuery sparqlQuery = new DefaultSparqlQuery() {
            @Override
            public String build() {
                return
                        select(false,
                                var(time)
                        )
                                .where(
                                        triplePattern(resource, METASERVICE.TIME, valueFactory.createLiteral(new Date())),
                                        filter(
                                                and(
                                                        sameTerm(val(time), val(time)),
                                                        or(
                                                                not(sameTerm(val(time), val(time))),
                                                                not(sameTerm(val(time), val(time)))
                                                        )
                                                )
                                        )
                                )
                                .orderByAsc(time)
                                .build(true);
            }
        };
        System.err.println(sparqlQuery);
    }

    @Test
    public void basic3(){
        SparqlQuery sparqlQuery = new DefaultSparqlQuery() {
            @Override
            public String build() {
             return select(false,
                     var(version),
                     var(title),
                     var(arch),
                     var(resource)
             )
                        .where(
                                triplePattern(project, DOAP.RELEASE, release),
                                triplePattern(release, ADMSSW.PACKAGE, resource),
                                triplePattern(resource, RDFS.LABEL, title),
                                triplePattern(resource, DEB.VERSION, version),
                                triplePattern(resource, DEB.ARCHITECTURE, arch)
                        )
                        .build(true);
            }
        };
        System.err.println(sparqlQuery);
    }
    private final Variable packageName = new Variable("packageName");
    private final Variable metaDistribution = new Variable("metaDistribution");
    @Test
    public void debRelease1(){
        final Variable graph = new Variable("g");
        final Variable graph2 = new Variable("g2");

        final URI resource = valueFactory.createURI("http://metaservice.org/d/packages/ubuntu/kubuntu-restricted-extras/57/amd64");
        System.err.println( new DefaultSparqlQuery(){
            @Override
            public String build() {
                return select(
                        true,
                        var(_package),var(version),var(packageName),var(metaDistribution))
                        .where(
                                quadPattern(resource, DEB.VERSION, version, graph2),
                                quadPattern(resource, DEB.PACKAGE_NAME, packageName, graph2),
                                quadPattern(resource, DEB.META_DISTRIBUTION, metaDistribution, graph2),
                                quadPattern(resource, RDF.TYPE, DEB.PACKAGE, graph2),
                                quadPattern(_package, DEB.VERSION, version, graph),
                                quadPattern(_package, DEB.PACKAGE_NAME, packageName, graph),
                                quadPattern(_package, DEB.META_DISTRIBUTION, metaDistribution, graph),
                                quadPattern(_package, RDF.TYPE, DEB.PACKAGE, graph)
                        )
                        .build(true);
            }
        }.toString());

        System.err.println( new PostProcessorSparqlQuery(){
            @Override
            public String build() {
                return select(
                        true,
                        var(_package),var(version),var(packageName),var(metaDistribution))
                        .where(
                                quadPattern(resource, DEB.VERSION, version, graph2),
                                quadPattern(resource, DEB.PACKAGE_NAME, packageName, graph2),
                                quadPattern(resource, DEB.META_DISTRIBUTION, metaDistribution, graph2),
                                quadPattern(resource, RDF.TYPE, DEB.PACKAGE, graph2),
                                quadPattern(_package, DEB.VERSION, version, graph),
                                quadPattern(_package, DEB.PACKAGE_NAME, packageName, graph),
                                quadPattern(_package, DEB.META_DISTRIBUTION, metaDistribution, graph),
                                quadPattern(_package, RDF.TYPE, DEB.PACKAGE, graph)
                        )
                        .build(true);
            }
        }.toString());

    }

    @Test
    public void complex1(){
        final Variable g1 = new Variable("g1");
        final Variable g2 = new Variable("g2");
        final Variable g3 = new Variable("g3");
        final Variable g4 = new Variable("g4");
        final Variable g5 = new Variable("g5");

        final Variable t1 = new Variable("t1");
        final Variable t2 = new Variable("t2");
        final Variable t3 = new Variable("t3");
        final Variable t4 = new Variable("t4");
        final Variable t5 = new Variable("t5");
        final Variable tselected = new Variable("tselected");

        SparqlQuery sparqlQuery = new BigdataSparqlQuery() {

            @Override
            public String build() {
                QueryBuilder base = SparqlQueryBuilderImpl.getInstance()
                        .select(false,all())
                        .where(
                                quadPattern(project, DOAP.RELEASE, release,g1),
                                quadPattern(release, ADMSSW.PACKAGE, resource,g2),
                                quadPattern(resource, RDFS.LABEL, title,g3),
                                quadPattern(resource, DEB.VERSION, version,g4),
                                quadPattern(resource, DEB.ARCHITECTURE, arch,g5),
                                triplePattern(g1,METASERVICE.TIME,t1),
                                triplePattern(g2,METASERVICE.TIME,t2),
                                triplePattern(g3,METASERVICE.TIME,t3),
                                triplePattern(g4,METASERVICE.TIME,t4),
                                triplePattern(g5,METASERVICE.TIME,t5),
                                filter(lessOrEqual(val(t1),val(tselected))),
                                filter(lessOrEqual(val(t2),val(tselected))),
                                filter(lessOrEqual(val(t3),val(tselected))),
                                filter(lessOrEqual(val(t4),val(tselected))),
                                filter(lessOrEqual(val(t5),val(tselected)))
                        );

                SelectQueryBuilder sub1 = SparqlQueryBuilderImpl.getInstance()
                        .select(false,aggregate("MAX", t1, t1))
                        .where(include("base"));

                SelectQueryBuilder sub2 = SparqlQueryBuilderImpl.getInstance()
                        .select(false,aggregate("MAX", t2, t2))
                        .where(include("base"));

                SelectQueryBuilder sub3 = SparqlQueryBuilderImpl.getInstance()
                        .select(false,aggregate("MAX", t3, t3))
                        .where(include("base"));

                SelectQueryBuilder sub4 = SparqlQueryBuilderImpl.getInstance()
                        .select(false,aggregate("MAX", t4, t4))
                        .where(include("base"));

                SelectQueryBuilder sub5 = select(false,aggregate("MAX", t5, t5))
                        .where(include("base"));

                return  select(false,
                                var(version),
                                var(title),
                                var(arch),
                                var(resource)
                        ).with(
                                namedSubQuery("base",base),
                                namedSubQuery("sub1",sub1),
                                namedSubQuery("sub2",sub2),
                                namedSubQuery("sub3",sub3),
                                namedSubQuery("sub4",sub4),
                                namedSubQuery("sub5",sub5)
                        )
                        .where(
                                include("base"),
                                include("sub1"),
                                include("sub2"),
                                include("sub3"),
                                include("sub4"),
                                include("sub5"),
                                triplePattern(g1, METASERVICE.ACTION, METASERVICE.ACTION_ADD),
                                triplePattern(g2, METASERVICE.ACTION, METASERVICE.ACTION_ADD),
                                triplePattern(g3, METASERVICE.ACTION, METASERVICE.ACTION_ADD),
                                triplePattern(g4,METASERVICE.ACTION, METASERVICE.ACTION_ADD),
                                triplePattern(g5,METASERVICE.ACTION, METASERVICE.ACTION_ADD)
                        )
                        .build(true);
            }
        };
        System.err.println(sparqlQuery);

    }
}
