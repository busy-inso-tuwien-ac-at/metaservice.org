/*
 * Copyright 2015 Nikola Ilo
 * Research Group for Industrial Software, Vienna University of Technology, Austria
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaservice.api.postprocessor;

import org.junit.Test;
import org.metaservice.api.rdf.vocabulary.ADMSSW;
import org.metaservice.api.rdf.vocabulary.DEB;
import org.metaservice.api.rdf.vocabulary.DOAP;
import org.metaservice.api.sparql.nodes.Variable;
import org.openrdf.model.vocabulary.RDFS;

/**
 * Created by ilo on 06.03.14.
 */
public class PostProcessorSparqlBuilderTest {
    private final Variable resource = new Variable("package");
    private final Variable version = new Variable("version");
    private final Variable project = new Variable("project");
    private final Variable title = new Variable("label");
    private final Variable release = new Variable("release");
    private final Variable arch = new Variable("arch");
    private final Variable _package = new Variable("package");
    private final Variable _package2 = new Variable("package2");
    @Test
    public void debTest(){
        System.err.println(new PostProcessorSparqlQuery(){

            @Override
            public String build() {
                return select(
                        var(version),
                        var(title),
                        var(resource)
                )
                        .where(triplePattern(project, DOAP.RELEASE, release),
                                triplePattern(release, ADMSSW.PACKAGE, resource),
                                triplePattern(resource, RDFS.LABEL, title)
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
                        select(DISTINCT,var(project)

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
                return select(DISTINCT,
                        aggregate("SAMPLE", _package2, _package),
                        var(arch)
                )
                        .where(
                                triplePattern(project, DOAP.RELEASE, release),
                                triplePattern(release, ADMSSW.PACKAGE, _package2),
                                triplePattern(_package2, DEB.ARCHITECTURE, arch)
                        )
                        .groupBy(arch)
                        .build();
            }}.toString());
    }
}
