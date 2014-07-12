import org.junit.Test;
import org.metaservice.api.MetaserviceException;
import org.metaservice.api.postprocessor.PostProcessorSparqlQuery;
import org.metaservice.api.rdf.vocabulary.DOAP;
import org.metaservice.api.rdf.vocabulary.METASERVICE_SWDEP;
import org.metaservice.api.rdf.vocabulary.SPDX;
import org.metaservice.api.sparql.nodes.BoundVariable;
import org.metaservice.api.sparql.nodes.Variable;
import org.metaservice.core.AbstractDispatcher;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

/**
 * Created by ilo on 08.07.2014.
 */
public class LicenseCheckTest {
    @Test
    public void licenseTest() throws MetaserviceException, RepositoryException {

        final BoundVariable resource = new BoundVariable("resource");
        final Variable resourceLicense = new Variable("resourceLicense");
        final Variable dependency = new Variable("dependency");
        final Variable dependencyLicense = new Variable("dependencyLicense");
        final Variable collection = new Variable("collection");
        Repository repository = AbstractDispatcher.createTempRepository(true);
        RepositoryConnection repositoryConnection = repository.getConnection();
        ValueFactory valueFactory = repositoryConnection.getValueFactory();


        PostProcessorSparqlQuery postProcessorSparqlQuery = new PostProcessorSparqlQuery() {
            @Override
            public String build() {
                return select(true,var(resourceLicense),var(dependency),var(dependencyLicense))
                        .where(
                                triplePattern(resource, DOAP.LICENSE, resourceLicense),
                                triplePattern(resource, METASERVICE_SWDEP.LINKS, dependency),
                                triplePattern(dependency, DOAP.LICENSE, dependencyLicense)
                        )
                        .build();
            }
        };
        PostProcessorSparqlQuery postProcessorSparqlQuery2 = new PostProcessorSparqlQuery() {
            @Override
            public String build() {
                return select(true,var(resourceLicense),var(dependency),var(dependencyLicense))
                        .where(
                                triplePattern(resource, DOAP.LICENSE, resourceLicense),
                                triplePattern(resource, METASERVICE_SWDEP.LINKS, collection),
                                triplePattern(collection, RDF.TYPE, METASERVICE_SWDEP.ANY_ONE_OF_SOFTWARE),
                                triplePattern(collection, RDF.LI, dependency),
                                triplePattern(dependency, DOAP.LICENSE, dependencyLicense)
                        )
                        .build();
            }
        };


        //rule 1: links to gpl -> also gpl
        //rule 2: links to lgpl ->
    }
}
