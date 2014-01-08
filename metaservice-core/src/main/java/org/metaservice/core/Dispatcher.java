package org.metaservice.core;

import org.metaservice.api.archive.Archive;
import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.archive.ArchiveException;
import org.metaservice.api.ns.METASERVICE;
import org.metaservice.api.parser.Parser;
import org.metaservice.api.provider.Provider;
import org.metaservice.api.provider.ProviderException;
import org.openrdf.model.*;
import org.openrdf.model.impl.TreeModel;
import org.openrdf.model.vocabulary.DC;
import org.openrdf.query.*;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Scope - Distributed
 * @param <T>
 */
public class Dispatcher<T> {
    private final Logger LOGGER = LoggerFactory.getLogger(Dispatcher.class);
    private final Provider<T> provider;
    private final Parser<T> parser;
    private HashMap<String,Archive> archiveMap = new HashMap<>();

    private ValueFactory valueFactory;
    private RepositoryConnection connection;
    private final TupleQuery repoSelect;
    private final Set<Archive> archives;

    //TODO make repoMap dynamic - such that it may be cloned automatically, or use an existing copy
    // this could be achieved by using a hash to search in the local filesystem

    @Inject
    public Dispatcher(
            Provider<T> provider,
            Parser<T> parser,
            Set<Archive> archives ,
            ValueFactory valueFactory,
            RepositoryConnection connection
    ) throws RepositoryException, MalformedQueryException {
        this.provider = provider;
        this.parser = parser;
        this.valueFactory = valueFactory;
        this.connection = connection;
        this.archives = archives;
        repoSelect = this.connection.prepareTupleQuery(QueryLanguage.SPARQL, "SELECT ?repo ?time ?path { ?resource <" + METASERVICE.METADATA + "> ?m. ?m <" + METASERVICE.SOURCE + "> ?repo; <" + METASERVICE.TIME + "> ?time; <" + METASERVICE.PATH + "> ?path.}");

    }

    public void refresh(URI uri) {
        repoSelect.setBinding("resource",uri);
        try {
            //TODO get correct archive
            TupleQueryResult result =repoSelect.evaluate();
            if(!result.hasNext())
                throw new RuntimeException("CANNOT REFRESH - no metainformation available");
            BindingSet set = result.next();

            String time = set.getBinding("time").getValue().stringValue();
            String repo = set.getBinding("repo").getValue().stringValue();
            String path = set.getBinding("path").getValue().stringValue();
            LOGGER.info("READING time {}",time);
            LOGGER.info("READING repo {}",repo);
            LOGGER.info("READING path {}",path);

            Archive archive = getArchive(repo);
            if(archive == null){
                return;
            }

            String toParse = "";
            try{
                toParse = archive.getContent(time,path);

                // retrieve version from archive
                for (T node : parser.parse(toParse)) {
                    Model model = provider.provideModelFor(node); //TODO time repo path
                }
            }catch (Exception e){
                try {
                    String errorFilename = "errorRefresh";
                    FileWriter fileWriter = new FileWriter(errorFilename);
                    fileWriter.write(e.toString() + "\n");
                    fileWriter.write(toParse);
                    fileWriter.close();
                    LOGGER.error("Parsing failed, dumping to file " + errorFilename,e);
                } catch (IOException e1) {
                    LOGGER.error("Error dumping",e1);
                }
            }
            LOGGER.info("done processing {} {}", time, path);
            // delete existing?
            // put again
        } catch (QueryEvaluationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void create(ArchiveAddress address){
        Archive archive = getArchive(address.getArchiveUri());
        if(archive == null){
            return;
        }


        try {
            String content = archive.getContent(address.getTime(),address.getPath());
            List<T> objects = parser.parse(content);
            Model result = new TreeModel();
            final BNode metadata =  generateMetadata(address,result);
            for(T object: objects){
                try {
                    Model model = provider.provideModelFor(object);
                    for(Statement statement : model){
                        //add metadata to every statement
                        result.add(statement.getSubject(),statement.getPredicate(),statement.getObject(),metadata);
                    }
                } catch (ProviderException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            connection.add(result);
        } catch (RepositoryException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ArchiveException e) {
            e.printStackTrace();
        }
    }

    private BNode generateMetadata(ArchiveAddress address,Model model) throws RepositoryException {
        BNode metadata = valueFactory.createBNode();
        Value pathLiteral = valueFactory.createLiteral(address.getPath());
        Value timeLiteral = valueFactory.createLiteral(address.getTime());
        Value repoLiteral = valueFactory.createLiteral(address.getArchiveUri());
        model.add(metadata, METASERVICE.PATH, pathLiteral);
        model.add(metadata, METASERVICE.TIME, timeLiteral);
        model.add(metadata, METASERVICE.SOURCE, repoLiteral);
        model.add(metadata, DC.DATE, valueFactory.createLiteral(new Date()));
        model.add(metadata, DC.CREATOR, valueFactory.createLiteral(provider.getClass().getCanonicalName()));
        return metadata;
    }

    private Archive getArchive(String archiveUri) {
        for(Archive archive : archives){
            if(archive.getSourceBaseUri().equals(archiveUri)){
                return archive;
            }
        }
        return null;
    }
}
