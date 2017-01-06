/*
 * Copyright 2015 Nikola Ilo
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

package org.metaservice.core.deb;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import org.metaservice.core.injection.MetaserviceTestModule;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryConnection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by ilo on 06.06.2014.
 */
public class WhiteListGenerator {
    private RepositoryConnection repositoryConnection;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new MetaserviceTestModule());
        repositoryConnection = injector.getInstance(RepositoryConnection.class);
    }

    @Test
    public void generateWhiteList()throws Exception{
        HashSet<String> resultSet = new HashSet<>();

        String uri = "http://metaservice.org/d/packages/ubuntu/openssl/";

        TupleQuery tupleQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,
                "SELECT * { ?s a <http://purl.org/adms/sw/SoftwarePackage> FILTER(STRSTARTS(STR(?s), \""+uri+"\")) } LIMIT 10000");
         TupleQueryResult result = tupleQuery.evaluate();
        BindingSet bindings;
        while(result.hasNext()){
            bindings = result.next();
            String s = bindings.getBinding("s").getValue().toString();
            resultSet.add(s);
            s = s.replaceFirst("packages","releases").replaceFirst("/[^/]*$","");
            resultSet.add(s);
            s = s.replaceFirst("releases","projects").replaceFirst("/[^/]*$","");
            resultSet.add(s);
        }
        ArrayList<String > resultList = new ArrayList<>();
        resultList.addAll(resultSet);
        Collections.sort(resultList);
        System.out.println();
        System.out.println();
        System.out.println();
        for(String s : resultList){
            System.out.println(s);
        }

    }

}
