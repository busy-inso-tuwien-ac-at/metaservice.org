package org.metaservice.api.postprocessor;
import org.junit.Test;
import org.metaservice.api.rdf.vocabulary.METASERVICE;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 * Created by ilo on 05.03.14.
 */
public class SPARQLQueryBuilderTest {

    @Test
    public void genericTest(){
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();
        Variable time = new Variable("time");
        Variable resource = new Variable("resource");
        URI uri ;
        String query =
                Default.getInstance()
                .select().var(time)
                .where()
                    .pattern(resource, METASERVICE.TIME, time)
                        .orderByAsc(time)
                .build();

        System.err.println(query);
    }
}
