/*
 * Copyright 2015 Nikola Ilo
 * Research Group for Industrial Software, Vienna University of Technology, Austria
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

package org.metaservice.core.nvd.cve;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.provider.AbstractProvider;
import org.metaservice.api.provider.ProviderException;
import org.metaservice.api.rdf.vocabulary.CVE;
import org.metaservice.api.rdf.vocabulary.DCTERMS;
import org.metaservice.api.rdf.vocabulary.CPE;
import org.metaservice.nist.cve.jaxb.*;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.DC;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import javax.inject.Inject;
import java.util.HashMap;

/**
 * Created by ilo on 26.02.14.
 */
public class CVEProvider extends AbstractProvider<VulnerabilityType> {

    @Inject
    public CVEProvider(ValueFactory valueFactory) {
        super(valueFactory);
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
                    resultConnection.add(CPE.getById(cpeId), DCTERMS.IS_REFERENCED_BY,uri);
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
                int i = 0;
                for(PlatformType platformType : o.getVulnerableConfigurations()){
                    URI platformURI = valueFactory.createURI(uri.toString()+"#Platform"+i++);
                    addIfNotNull(resultConnection,platformURI,DC.IDENTIFIER,platformType.getId());
                    if(platformType.getRemarks() != null){
                        for(TextType textType : platformType.getRemarks()){
                            addIfNotNull(resultConnection, platformURI, DC.DESCRIPTION, textType.getValue(), textType.getLang());
                        }
                    }
                    if(platformType.getTitles() != null){
                        for(TextType textType :platformType.getTitles()){
                            addIfNotNull(resultConnection, platformURI, DC.TITLE, textType.getValue(), textType.getLang());
                        }
                    }
                    if(platformType.getLogicalTest() != null){
                        addIfNotNull(resultConnection,platformURI,DC.SUBJECT,"LOGICALTEST");//todo
                    }
                    addIfNotEmpty(resultConnection, uri, DC.RELATION, platformURI);
                }
            }

            if(o.getAttackScenarios() != null){
                int i =0;
                for(ReferenceType referenceType : o.getAttackScenarios()){
                    URI scenarioUri = valueFactory.createURI(uri.toString()+"#Scenario"+i++);
                    addIfNotNull(resultConnection,scenarioUri, RDFS.SEEALSO,referenceType.getHref());
                    addIfNotNull(resultConnection,scenarioUri,DC.TITLE,referenceType.getValue(),referenceType.getLang());
                    addIfNotEmpty(resultConnection, uri, CVE.ATTACK_SCENARIO, scenarioUri);
                }
            }

            if(o.getFixActions() != null){
                for(FixActionType fixActionType : o.getFixActions()){
                    addIfNotNull(resultConnection,uri,DC.RELATION,"FIXACTION");//todo
                }
            }

            if(o.getAssessmentChecks() != null){
                int i = 0;
                for(CheckReferenceType checkReferenceType : o.getAssessmentChecks()){
                    URI assessmentURI = valueFactory.createURI(uri.toString()+"#Assessment"+i++);
                    addIfNotNull(resultConnection,assessmentURI, RDFS.SEEALSO,checkReferenceType.getHref());
                    addIfNotNull(resultConnection,assessmentURI,DC.TITLE,checkReferenceType.getName());
                    addIfNotNull(resultConnection,assessmentURI,DC.DESCRIPTION,checkReferenceType.getSystem());
                    addIfNotEmpty(resultConnection, uri, CVE.ASSESSMENT_CHECK, assessmentURI);
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
