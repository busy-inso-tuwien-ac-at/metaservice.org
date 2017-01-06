/*
 * Copyright 2015 Nikola Ilo
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

package org.metaservice.core.ld;

import org.jetbrains.annotations.NotNull;
import org.metaservice.api.provider.Provider;
import org.metaservice.api.provider.ProviderException;
import org.metaservice.api.rdf.vocabulary.ADMSSW;
import org.metaservice.api.rdf.vocabulary.DOAP;
import org.openrdf.model.*;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.util.iterators.Iterators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by ilo on 12.07.2014.
 */
public class LDProvider implements Provider<Model>{
    private final static Logger LOGGER = LoggerFactory.getLogger(LDProvider.class);
    @Override
    public void provideModelFor(@NotNull Model o, @NotNull RepositoryConnection resultConnection, @NotNull HashMap<String, String> properties) throws ProviderException {
        try {
            ValueFactory valueFactory = resultConnection.getValueFactory();
            LOGGER.debug("properties {}",properties);
            HashMap<BNode, URI> bnodeMapping = new HashMap<>();
            HashMap<Value,URI> releases = new HashMap<>();

            //todo fix bnode naming
            int bnodeId =0 ;
            URI projUri =null;
            URI newProjUri=null;
            Set<Resource> resourceSet = o.filter(null, RDF.TYPE, DOAP.PROJECT).subjects();
            if(resourceSet.size()> 0) {
                Resource resource =resourceSet.iterator().next();
                if (resource instanceof URI) {
                    projUri = (URI) resource;
                    Literal label = o.filter(projUri, DOAP.NAME, null).objectLiteral();
                    newProjUri = valueFactory.createURI("http://metaservice.org/d/projects/" + label.stringValue().replaceAll(" ", "")); //todo cleanup name
                    LOGGER.info("<{}>", newProjUri.stringValue());
                    resultConnection.add(projUri, OWL.SAMEAS, newProjUri);
                    resultConnection.add(newProjUri, OWL.SAMEAS, projUri);
                    resultConnection.add(newProjUri, RDF.TYPE, ADMSSW.SOFTWARE_PROJECT);
                    for (Value value : Iterators.asList(o.filter(projUri, DOAP.RELEASE, null).objects().iterator())) {
                        Literal revision = o.filter((Resource) value, DOAP.REVISION, null).objectLiteral();
                        URI releaseURI = valueFactory.createURI("http://metaservice.org/d/releases/" + label.stringValue().replaceAll(" ", "") + "/" + revision.stringValue().replaceAll(" ", ""));
                        if (value instanceof URI) {
                            resultConnection.add((URI)value, OWL.SAMEAS, releaseURI);
                            resultConnection.add(releaseURI, OWL.SAMEAS, value);
                        }
                        resultConnection.add(releaseURI, ADMSSW.PROJECT, newProjUri);
                        resultConnection.add(releaseURI, RDF.TYPE, ADMSSW.SOFTWARE_RELEASE);
                        releases.put(value, releaseURI);
                        LOGGER.info("<{}>", releaseURI.stringValue());
                    }
                }
            }


            for (Statement statement : o) {
                Resource subject = statement.getSubject();
                URI predicate;
                Value object = statement.getObject();
                if(subject.equals(projUri)){
                    subject = newProjUri;
                }else if(releases.containsKey(subject)){
                    subject = releases.get(subject);
                }else if (subject instanceof BNode) {
                    if(newProjUri != null) {
                        if (!bnodeMapping.containsKey(subject)) {
                            bnodeMapping.put((BNode) statement.getSubject(), valueFactory.createURI(newProjUri.toString() + "#" + bnodeId++));
                        }
                        subject = bnodeMapping.get(subject);
                    }
                }
                predicate = statement.getPredicate();
                if(object.equals(projUri)&&!predicate.equals(DOAP.HOMEPAGE)){
                    object = newProjUri;
                }else if(releases.containsKey(object)){
                    object = releases.get(object);
                }else if(object instanceof BNode){
                    if(newProjUri != null) {
                        if (!bnodeMapping.containsKey(object)) {
                            bnodeMapping.put((BNode) object, valueFactory.createURI(newProjUri + "#" + bnodeId++));
                        }
                        object = bnodeMapping.get(object);
                    }
                }
                if(subject instanceof BNode ||object instanceof BNode){
                    LOGGER.info("Bnode statement {} {} {}",subject,predicate,object);
                }
                resultConnection.add(valueFactory.createStatement(subject,predicate,object));
            }

        } catch (RepositoryException e) {
            throw new ProviderException(e);
        }
    }
}
