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
