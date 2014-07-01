package org.metaservice.demo.wordpress;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.provider.Provider;
import org.metaservice.api.provider.ProviderException;
import org.metaservice.api.rdf.vocabulary.*;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import java.util.HashMap;

/**
 * Created by ilo on 21.06.2014.
 */
public class WordpressProvider  implements Provider<VersionEntry> {
    private static final String ns = "http://metaservice.org/d/";
    @Override
    public void provideModelFor(@NotNull VersionEntry o, @NotNull RepositoryConnection resultConnection, @NotNull HashMap<String, String> properties) throws ProviderException {
        ValueFactory valueFactory = resultConnection.getValueFactory();
        URI projectURI =valueFactory.createURI(ns+"projects/","wordpress");
        URI releaseURI =valueFactory.createURI(ns+"releases/wordpress/",o.getName());
        URI zipURI = valueFactory.createURI(ns+"packages/wordpress/"+ o.getName()+"/","zip");
        URI tarURI = valueFactory.createURI(ns+"packages/wordpress/" + o.getName()+"/","targz");
        URI iisURI = valueFactory.createURI(ns+"packages/wordpress/" + o. getName() + "/","iiszip");

        try {
            resultConnection.add(projectURI, RDF.TYPE, ADMSSW.SOFTWARE_PROJECT);
            resultConnection.add(projectURI, DOAP.BUG_DATABASE,  valueFactory.createURI("https://core.trac.wordpress.org/"));
            resultConnection.add(projectURI, DOAP.NAME, valueFactory.createLiteral("wordpress"));
            resultConnection.add(projectURI, DC.TITLE,valueFactory.createLiteral("Wordpress"));
            resultConnection.add(projectURI, DC.DESCRIPTION, valueFactory.createLiteral("WordPress is web software you can use to create a beautiful website or blog. We like to say that WordPress is both free and priceless at the same time."));
            resultConnection.add(projectURI, DOAP.HOMEPAGE, valueFactory.createURI("http://wordpress.org/"));
            resultConnection.add(projectURI, DOAP.DOWNLOAD_PAGE, valueFactory.createURI("http://wordpress.org/download/"));
            resultConnection.add(projectURI, DOAP.RELEASE, releaseURI);
            resultConnection.add(projectURI, DOAP.LICENSE,valueFactory.createLiteral("GPLv2"));
            resultConnection.add(releaseURI, RDF.TYPE, ADMSSW.SOFTWARE_RELEASE);
            resultConnection.add(releaseURI, DC.TITLE,valueFactory.createLiteral( "Wordpress " + o.getName()));
            resultConnection.add(releaseURI, ADMSSW.PROJECT, projectURI);
            resultConnection.add(releaseURI, DOAP.REVISION, valueFactory.createLiteral(o.getName()));

            if(o.getZip()!= null && ! o.getZip().isEmpty()){
                resultConnection.add(releaseURI, ADMSSW.PACKAGE, zipURI);
                resultConnection.add(zipURI, RDF.TYPE,ADMSSW.SOFTWARE_PACKAGE);
                resultConnection.add(zipURI, RDF.TYPE, METASERVICE_FILE.FILE);
                resultConnection.add(zipURI, ADMSSW.RELEASE,releaseURI);
                resultConnection.add(zipURI, DC.TITLE,valueFactory.createLiteral(zipURI.getLocalName()));
                resultConnection.add(zipURI, SCHEMA.DOWNLOAD_URL,valueFactory.createURI(o.getZip()));
            }
            if(o.getTar()!= null&& !o.getTar().isEmpty()) {
                resultConnection.add(releaseURI, ADMSSW.PACKAGE, tarURI);
                resultConnection.add(tarURI, RDF.TYPE, ADMSSW.SOFTWARE_PACKAGE);
                resultConnection.add(tarURI, RDF.TYPE, METASERVICE_FILE.FILE);
                resultConnection.add(tarURI, ADMSSW.RELEASE,releaseURI);
                resultConnection.add(tarURI, DC.TITLE, valueFactory.createLiteral(tarURI.getLocalName()));
                resultConnection.add(tarURI, SCHEMA.DOWNLOAD_URL,valueFactory.createURI(o.getTar()));
            }
            if(o.getIis() != null && !o.getIis().isEmpty()) {
                resultConnection.add(releaseURI, ADMSSW.PACKAGE, iisURI);
                resultConnection.add(iisURI, RDF.TYPE, ADMSSW.SOFTWARE_PACKAGE);
                resultConnection.add(iisURI, RDF.TYPE, METASERVICE_FILE.FILE);
                resultConnection.add(iisURI, ADMSSW.RELEASE,releaseURI);
                resultConnection.add(iisURI, DC.TITLE, valueFactory.createLiteral(iisURI.getLocalName()));
                resultConnection.add(iisURI, SCHEMA.DOWNLOAD_URL,valueFactory.createURI(o.getIis()));
            }
        } catch (RepositoryException e) {
            throw new ProviderException(e);
        }
    }
}
