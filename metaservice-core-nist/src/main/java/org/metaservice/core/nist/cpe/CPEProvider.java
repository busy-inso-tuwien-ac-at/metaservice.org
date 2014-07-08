package org.metaservice.core.nist.cpe;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaservice.api.provider.AbstractProvider;
import org.metaservice.api.provider.ProviderException;
import org.metaservice.api.rdf.vocabulary.CPE;
import org.metaservice.api.rdf.vocabulary.DC;
import org.metaservice.nist.cpe.jaxb.*;
import org.mitre.cpe.common.LogicalValue;
import org.mitre.cpe.common.WellFormedName;
import org.mitre.cpe.naming.util.CPEFactory;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.text.ParseException;
import java.util.HashMap;

/**
 * Created by ilo on 25.02.14.
 */
public class CPEProvider extends AbstractProvider<ItemType> {
    private final Logger LOGGER = LoggerFactory.getLogger(CPEProvider.class);
    protected void addIfNotAny(@NotNull RepositoryConnection resultConnection, @NotNull Resource subject, @NotNull URI predicate,@Nullable Object object) throws RepositoryException {
        if(object != null && !LogicalValue.ANY.equals(object))
            resultConnection.add(subject, predicate, valueFactory.createLiteral(object.toString()));
    }
    @Inject
    public CPEProvider(ValueFactory valueFactory) {
        super(valueFactory);
    }

    @Override
    public void provideModelFor(@NotNull ItemType o, @NotNull RepositoryConnection resultConnection, @NotNull HashMap<String, String> properties) throws ProviderException {
        try {
            if(o.getName() == null)
            {
                LOGGER.warn("name is null");
                return;
            }
            URI uri = CPE.getById(o.getName());
            resultConnection.add(uri, RDF.TYPE,CPE.CPE);
            String name = o.getName();

            resultConnection.add(uri, OWL.SAMEAS, valueFactory.createURI(name));
            resultConnection.add(uri, CPE.NAME,valueFactory.createLiteral(name));
            resultConnection.add(uri, CPE.DEPRECATED,valueFactory.createLiteral(o.isDeprecated()));
            try {
                WellFormedName wellFormedName = CPEFactory.newCPEName(name).getWellFormedName();
                addIfNotAny(resultConnection, uri, CPE.PART, wellFormedName.get(WellFormedName.Attribute.PART));
                addIfNotAny(resultConnection, uri, CPE.VENDOR, wellFormedName.get(WellFormedName.Attribute.VENDOR));
                addIfNotAny(resultConnection, uri, CPE.VERSION, wellFormedName.get(WellFormedName.Attribute.VERSION).toString().replaceAll("\\\\[.]", ".")); //todo check replace
                addIfNotAny(resultConnection, uri, CPE.PRODUCT,wellFormedName.get(WellFormedName.Attribute.PRODUCT));
                addIfNotAny(resultConnection, uri, CPE.UPDATE, wellFormedName.get(WellFormedName.Attribute.UPDATE));
                addIfNotAny(resultConnection, uri, CPE.EDITION, wellFormedName.get(WellFormedName.Attribute.EDITION));
                addIfNotAny(resultConnection, uri, CPE.LANGUAGE, wellFormedName.get(WellFormedName.Attribute.LANGUAGE));
                addIfNotAny(resultConnection, uri, CPE.SW_EDITION,wellFormedName.get(WellFormedName.Attribute.SW_EDITION));
                addIfNotAny(resultConnection, uri, CPE.TARGET_SW, wellFormedName.get(WellFormedName.Attribute.TARGET_SW));
                addIfNotAny(resultConnection, uri, CPE.TARGET_HW, wellFormedName.get(WellFormedName.Attribute.TARGET_HW));
                addIfNotAny(resultConnection, uri, CPE.OTHER, wellFormedName.get(WellFormedName.Attribute.OTHER));
            } catch (ParseException e) {
                throw new ProviderException("Could not parse",e);
            }
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
            if(o.getReferences() != null){
                int i = 0;
                for(ReferencesType.Reference reference : o.getReferences().getReference()){
                    URI referenceUri = valueFactory.createURI(uri.toString()+"#Reference" + i++);
                    resultConnection.add(uri,CPE.REFERENCE, referenceUri);
                    if(reference.getHref() != null) {
                        resultConnection.add(referenceUri, CPE.REFERENCE_HREF, valueFactory.createURI(reference.getHref()));
                    }
                    if(reference.getValue() != null) {
                        resultConnection.add(referenceUri, CPE.REFERENCE_VALUE, valueFactory.createLiteral(reference.getValue()));
                    }
                }
            }
            if(o.getCheck() != null){
                int i = 0;
                for(CheckType checkType : o.getCheck()){
                    if(checkType.getHref() != null ||checkType.getValue() != null ||checkType.getSystem() != null){
                        URI checkUri = valueFactory.createURI(uri.toString()+"#Check" + i++);
                        resultConnection.add(uri,CPE.CHECK,checkUri);
                        if(checkType.getValue() != null){
                            resultConnection.add(checkUri,CPE.CHECK_VALUE,valueFactory.createLiteral(checkType.getValue()));
                        }
                        if(checkType.getSystem() != null){
                            resultConnection.add(checkUri,CPE.CHECK_SYSTEM,valueFactory.createLiteral(checkType.getSystem()));
                        }
                        if(checkType.getHref() != null){
                            resultConnection.add(checkUri,CPE.CHECK_HREF,valueFactory.createLiteral(checkType.getHref()));
                        }
                    }
                }
            }

        } catch (RepositoryException e) {
            throw new ProviderException(e);
        }
    }
}
