package org.metaservice.core.nist.cve;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaservice.api.provider.Provider;
import org.metaservice.api.provider.ProviderException;
import org.metaservice.core.nist.cpe.CPE;
import org.metaservice.nist.cve.jaxb.*;
import org.openrdf.model.BNode;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.DC;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import javax.inject.Inject;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.HashMap;

/**
 * Created by ilo on 26.02.14.
 */
public class CVEProvider implements Provider<VulnerabilityType> {
    private final ValueFactory valueFactory;

    @Inject
    public CVEProvider(ValueFactory valueFactory) {
        this.valueFactory = valueFactory;
    }

    public void addIfNotNull(@NotNull RepositoryConnection resultConnection, @NotNull Resource subject, @NotNull URI predicate,@Nullable String text,@Nullable String language) throws RepositoryException {
        if(text != null){
            if(language != null)
                resultConnection.add(subject, predicate, valueFactory.createLiteral(text,language));
            else
                resultConnection.add(subject, predicate, valueFactory.createLiteral(text));
        }

    }

    public void addIfNotNull(@NotNull RepositoryConnection resultConnection, @NotNull Resource subject, @NotNull URI predicate,@Nullable String object) throws RepositoryException {
        if(object != null)
            resultConnection.add(subject, predicate, valueFactory.createLiteral(object));
    }
    public void addIfNotNull(@NotNull RepositoryConnection resultConnection, @NotNull Resource subject, @NotNull URI predicate,@Nullable XMLGregorianCalendar object) throws RepositoryException {
        if(object != null)
            resultConnection.add(subject, predicate, valueFactory.createLiteral(object));
    }
    public void addIfNotNull(@NotNull RepositoryConnection resultConnection, @NotNull Resource subject, @NotNull URI predicate,@Nullable Integer object) throws RepositoryException {
        if(object != null)
            resultConnection.add(subject, predicate, valueFactory.createLiteral(object));
    }
    public void addIfNotNull(@NotNull RepositoryConnection resultConnection, @NotNull Resource subject, @NotNull URI predicate,@Nullable Boolean object) throws RepositoryException {
        if(object != null)
            resultConnection.add(subject, predicate, valueFactory.createLiteral(object));
    }

    private void addIfNotEmpty(@NotNull RepositoryConnection resultConnection,@NotNull Resource uri,@NotNull URI relation,@NotNull BNode bNode) throws RepositoryException {
        if(resultConnection.getStatements(bNode,null,null,false).hasNext())
            resultConnection.add(uri,relation,bNode);
    }

    @Override
    public void provideModelFor(@NotNull VulnerabilityType o, @NotNull RepositoryConnection resultConnection, @NotNull HashMap<String, String> properties) throws ProviderException {
        URI uri = CVE.getById(o.getId());
        try {
            resultConnection.add(uri, RDF.TYPE,CVE.CVE);
            addIfNotNull(resultConnection, uri, CVE.CVE_ID, o.getCveId());
            addIfNotNull(resultConnection,uri,CVE.CCE_ID,o.getCceId());
            addIfNotNull(resultConnection,uri,CVE.DISCLOSURE_DATETIME,o.getDisclosureDatetime());
            addIfNotNull(resultConnection,uri,CVE.DISCOVERED_DATETIME,o.getDiscoveredDatetime());
            addIfNotNull(resultConnection,uri,CVE.EXPLOIT_PUBLISHED_DATETIME,o.getExploitPublishDatetime());
            addIfNotNull(resultConnection,uri,CVE.LAST_MODIFIED_DATETIME,o.getLastModifiedDatetime());
            addIfNotNull(resultConnection,uri,CVE.PUBLISHED_DATETIME,o.getPublishedDatetime());
            addIfNotNull(resultConnection,uri,CVE.SUMMARY,o.getSummary());

            if(o.getVulnerableSoftwareList() != null && o.getVulnerableSoftwareList().getProducts() != null){
                for(String cpeId : o.getVulnerableSoftwareList().getProducts()){
                    resultConnection.add(uri,CVE.VULNERABLE_SOFTWARE,CPE.getById(cpeId));
                }
            }

            if(o.getOsvdbExt() != null && o.getOsvdbExt().getExploitLocation() != null){
                AssociatedExploitLocationType location = o.getOsvdbExt().getExploitLocation();
                addIfNotNull(resultConnection,uri,CVE.OSVDB_LOCATION_DIALUP,location.isDialup());
                addIfNotNull(resultConnection,uri,CVE.OSVDB_LOCATION_PHYSICAL_ACCESS,location.isPhysicalAccess());
                addIfNotNull(resultConnection,uri,CVE.OSVDB_LOCATION_UNKNOWN,location.isUnknown());
                addIfNotNull(resultConnection, uri, CVE.OSVDB_LOCATION_VOLUNTARILY_INTERACT, location.isVoluntarilyInteract());
            }
            if(o.getSecurityProtection() != null){
                addIfNotNull(resultConnection,uri,CVE.SECURITY_PROTECTION,o.getSecurityProtection().value());
            }

            if(o.getVulnerableConfigurations() != null){
                for(PlatformType platformType : o.getVulnerableConfigurations()){
                    BNode bNode = valueFactory.createBNode();
                    addIfNotNull(resultConnection,bNode,DC.IDENTIFIER,platformType.getId());
                    if(platformType.getRemarks() != null){
                        for(TextType textType : platformType.getRemarks()){
                            addIfNotNull(resultConnection, bNode, DC.DESCRIPTION, textType.getValue(), textType.getLang());
                        }
                    }
                    if(platformType.getTitles() != null){
                        for(TextType textType :platformType.getTitles()){
                            addIfNotNull(resultConnection, bNode, DC.TITLE, textType.getValue(), textType.getLang());
                        }
                    }
                    if(platformType.getLogicalTest() != null){
                        addIfNotNull(resultConnection,bNode,DC.SUBJECT,"LOGICALTEST");//todo
                    }
                    addIfNotEmpty(resultConnection, uri, DC.RELATION, bNode);
                }
            }

            if(o.getAttackScenarios() != null){
                for(ReferenceType referenceType : o.getAttackScenarios()){
                    BNode reference = valueFactory.createBNode();
                    addIfNotNull(resultConnection,reference, RDFS.SEEALSO,referenceType.getHref());
                    addIfNotNull(resultConnection,reference,DC.TITLE,referenceType.getValue(),referenceType.getLang());
                    addIfNotEmpty(resultConnection, uri, CVE.ATTACK_SCENARIO, reference);
                }
            }

            if(o.getFixActions() != null){
                for(FixActionType fixActionType : o.getFixActions()){
                    addIfNotNull(resultConnection,uri,DC.RELATION,"FIXACTION");//todo
                }
            }

            if(o.getAssessmentChecks() != null){
                for(CheckReferenceType checkReferenceType : o.getAssessmentChecks()){
                    BNode reference = valueFactory.createBNode();
                    addIfNotNull(resultConnection,reference, RDFS.SEEALSO,checkReferenceType.getHref());
                    addIfNotNull(resultConnection,reference,DC.TITLE,checkReferenceType.getName());
                    addIfNotNull(resultConnection,reference,DC.DESCRIPTION,checkReferenceType.getSystem());
                    addIfNotEmpty(resultConnection, uri, CVE.ASSESSMENT_CHECK, reference);
                }
            }

            if(o.getCwes() != null){
                for(CweReferenceType cweReferenceType : o.getCwes()){
                    addIfNotNull(resultConnection,uri,CVE.CWE,cweReferenceType.getId());
                }
            }
        } catch (RepositoryException e) {
            throw new ProviderException(e);
        }
    }

}
