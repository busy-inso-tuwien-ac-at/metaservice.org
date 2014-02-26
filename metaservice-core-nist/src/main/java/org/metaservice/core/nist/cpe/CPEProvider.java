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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;

/**
 * Created by ilo on 25.02.14.
 */
public class CPEProvider implements Provider<ItemType> {
    private final Logger LOGGER = LoggerFactory.getLogger(CPEProvider.class);
    private final String ns ="http://metaservice.org/d/releases/cpe/";
    private final ValueFactory valueFactory;

    @Inject
    public CPEProvider(ValueFactory valueFactory) {
        this.valueFactory = valueFactory;
    }

    @Override
    public void provideModelFor(@NotNull ItemType o, @NotNull RepositoryConnection resultConnection, @NotNull HashMap<String, String> properties) throws ProviderException {
        try {
            if(o.getName() == null)
            {
                LOGGER.warn("name is null");
                return;
            }
            URI uri = getURI(o.getName());
            resultConnection.add(uri, RDF.TYPE,CPE.CPE);
            resultConnection.add(uri,CPE.NAME,valueFactory.createLiteral(o.getName()));
            resultConnection.add(uri, CPE.DEPRECATED,valueFactory.createLiteral(o.isDeprecated()));
            if(o.getDeprecationDate() != null){
                resultConnection.add(uri,CPE.DEPRECATION_DATE,valueFactory.createLiteral(o.getDeprecationDate()));
            }

            if(o.getDeprecatedBy() != null){
                resultConnection.add(uri,CPE.CPE,valueFactory.createLiteral(o.getDeprecatedBy()));
            }
            if(o.getNotes() != null){
                for(NotesType notesType : o.getNotes()){
                    if(notesType.getNote() != null){
                        String note =StringUtils.join(notesType.getNote()," ");
                        if(notesType.getLang() != null){
                            resultConnection.add(uri,CPE.NOTE,valueFactory.createLiteral(note,notesType.getLang()));
                        }else{
                            resultConnection.add(uri,CPE.NOTE,valueFactory.createLiteral(note));
                        }
                    }
                }
            }
            if(o.getTitle() !=null){
                for(TextType textType : o.getTitle()){
                    if(textType.getValue() != null){
                        if(textType.getLang() != null){
                            resultConnection.add(uri, DC.TITLE,valueFactory.createLiteral(textType.getValue(),textType.getLang()));
                        }else{
                            resultConnection.add(uri, DC.TITLE,valueFactory.createLiteral(textType.getValue()));
                        }
                    }
                }
            }
            if(o.getCheck() != null){
                for(CheckType checkType : o.getCheck()){
                    if(checkType.getHref() != null ||checkType.getValue() != null ||checkType.getSystem() != null){
                        BNode bNode = valueFactory.createBNode();
                        resultConnection.add(uri,CPE.CHECK,bNode);
                        if(checkType.getValue() != null){
                            resultConnection.add(bNode,CPE.CHECK_VALUE,valueFactory.createLiteral(checkType.getValue()));
                        }
                        if(checkType.getSystem() != null){
                            resultConnection.add(bNode,CPE.CHECK_SYSTEM,valueFactory.createLiteral(checkType.getSystem()));
                        }
                        if(checkType.getHref() != null){
                            resultConnection.add(bNode,CPE.CHECK_HREF,valueFactory.createLiteral(checkType.getHref()));
                        }
                    }
                }
            }

        } catch (RepositoryException e) {
            throw new ProviderException(e);
        }
    }


    private URI getURI(String name){
        return valueFactory.createURI(ns,name);
    }
}
