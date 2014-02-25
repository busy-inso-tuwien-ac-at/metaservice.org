package org.metaservice.core.nist.cpe;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.metaservice.api.provider.Provider;
import org.metaservice.api.provider.ProviderException;
import org.metaservice.api.rdf.vocabulary.DC;
import org.metaservice.nist.cpe.jaxb.CheckType;
import org.metaservice.nist.cpe.jaxb.ItemType;
import org.metaservice.nist.cpe.jaxb.NotesType;
import org.metaservice.nist.cpe.jaxb.TextType;
import org.openrdf.model.BNode;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import java.util.HashMap;

/**
 * Created by ilo on 25.02.14.
 */
public class CPEProvider implements Provider<ItemType> {
    private final String ns ="http://metaservice.org/d/releases/cpe/";
    private final ValueFactory valueFactory;

    public CPEProvider(ValueFactory valueFactory) {
        this.valueFactory = valueFactory;
    }

    @Override
    public void provideModelFor(@NotNull ItemType o, @NotNull RepositoryConnection resultConnection, @NotNull HashMap<String, String> properties) throws ProviderException {
        try {
            URI uri = getURI(o.getName());
            resultConnection.add(uri, RDF.TYPE,CPE.CPE);
            resultConnection.add(uri, CPE.DEPRECATED,valueFactory.createLiteral(o.isDeprecated()));
            resultConnection.add(uri,CPE.CPE,valueFactory.createLiteral(o.getDeprecationDate()));
            resultConnection.add(uri,CPE.CPE,valueFactory.createLiteral(o.getDeprecatedBy()));
            resultConnection.add(uri,CPE.NAME,valueFactory.createLiteral(o.getName()));

            for(NotesType notesType : o.getNotes()){
                resultConnection.add(uri,CPE.NOTE,valueFactory.createLiteral(StringUtils.join(notesType.getNote()," "),notesType.getLang()));
            }
            for(TextType textType : o.getTitle()){
                resultConnection.add(uri, DC.TITLE,valueFactory.createLiteral(textType.getValue(),textType.getLang()));
            }
            for(CheckType checkType : o.getCheck()){
                BNode bNode = valueFactory.createBNode();
                resultConnection.add(uri,CPE.CHECK,bNode);
                resultConnection.add(bNode,CPE.CHECK_VALUE,valueFactory.createLiteral(checkType.getValue()));
                resultConnection.add(bNode,CPE.CHECK_SYSTEM,valueFactory.createLiteral(checkType.getSystem()));
                resultConnection.add(bNode,CPE.CHECK_HREF,valueFactory.createLiteral(checkType.getHref()));
            }

        } catch (RepositoryException e) {
            throw new ProviderException(e);
        }

    }


    private URI getURI(String name){
        return valueFactory.createURI(ns,name);
    }
}
