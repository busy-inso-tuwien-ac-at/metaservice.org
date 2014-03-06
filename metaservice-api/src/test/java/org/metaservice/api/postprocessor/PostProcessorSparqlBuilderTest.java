package org.metaservice.api.postprocessor;

import org.junit.Test;
import org.metaservice.api.rdf.vocabulary.ADMSSW;
import org.metaservice.api.rdf.vocabulary.DOAP;
import org.metaservice.api.rdf.vocabulary.PACKAGE_DEB;
import org.metaservice.api.sparql.nodes.Variable;

import java.util.Date;

/**
 * Created by ilo on 06.03.14.
 */
public class PostProcessorSparqlBuilderTest {
    private final Variable resource = new Variable("resource");
    private final Variable version = new Variable("version");
    private final Variable project = new Variable("project");
    private final Variable title = new Variable("title");
    private final Variable release = new Variable("release");
    private final Variable arch = new Variable("arch");
    private final Variable _package = new Variable("package");
    private final Variable _package2 = new Variable("package2");
    @Test
    public void debTest(){
        System.err.println(new PostProcessorSparqlQuery(){

            @Override
            public String build() {
                return select(false,
                        var(version),
                        var(title),
                        var(arch),
                        var(resource)
                )
                        .where(triplePattern(project, DOAP.RELEASE,release),
                                triplePattern(release, ADMSSW.PACKAGE,resource),
                                triplePattern(resource, PACKAGE_DEB.TITLE,title),
                                triplePattern(resource,PACKAGE_DEB.VERSION,version),
                                triplePattern(resource,PACKAGE_DEB.ARCHITECTURE,arch)
                        ).build();
            }
        });
    }

    @Test
    public void deb2(){
        System.err.println(new PostProcessorSparqlQuery() {


            @Override
            public String build() {
                return
                        select(true,var(project)

                        )
                                .

                                        where(
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
                                .build();
            }
        });

    }

    @Test
    public void deb3(){
        System.err.println(new PostProcessorSparqlQuery(){
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
            }}.toString());
    }
}
