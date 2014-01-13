package org.metaservice.core.maven;

import org.apache.maven.model.*;
import org.metaservice.api.rdf.vocabulary.ADMSSW;
import org.metaservice.api.provider.Provider;
import org.metaservice.api.provider.ProviderException;
import org.metaservice.api.rdf.vocabulary.FOAF;
import org.openrdf.model.*;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import javax.inject.Inject;


import static org.metaservice.core.maven.PACKAGE_MAVEN.*;

public class MavenProvider implements Provider<org.apache.maven.model.Model> {
    private ValueFactory valueFactory;
    public static String rooturl = "http://metaservice.org/d/packages/maven/";

    @Inject
    public MavenProvider(ValueFactory valueFactory) {
        this.valueFactory = valueFactory;
    }

    @Override
    public void provideModelFor(org.apache.maven.model.Model pom, RepositoryConnection resultConnection) throws ProviderException {

        try{
        URI projectURI =   valueFactory.createURI(rooturl + pom.getGroupId() + "/" + pom.getArtifactId());
        URI releaseURI = valueFactory.createURI(rooturl +  pom.getGroupId() + "/" + pom.getArtifactId()+ "/" + pom.getVersion());
        URI packageURI = valueFactory.createURI(rooturl + pom.getGroupId() + "/" + pom.getArtifactId() + "/" + pom.getVersion() + "/" + pom.getPackaging());

        Literal groupId = valueFactory.createLiteral(pom.getGroupId());
        Literal artifactId = valueFactory.createLiteral(pom.getArtifactId());
        Literal version = valueFactory.createLiteral(pom.getVersion());
        Literal packaging = valueFactory.createLiteral(pom.getPackaging());

        //TODO bnode or ui link parent?
        BNode parent = valueFactory.createBNode();
        resultConnection.add(parent, GROUP_ID, valueFactory.createLiteral(pom.getParent().getGroupId()));
        resultConnection.add(parent, ARTIFACT_ID, valueFactory.createLiteral(pom.getParent().getArtifactId()));
        resultConnection.add(parent, VERSION, valueFactory.createLiteral(pom.getParent().getVersion()));

        //project
        resultConnection.add(projectURI, RDF.TYPE, ADMSSW.SOFTWARE_PROJECT);
        resultConnection.add(projectURI, ADMSSW.RELEASE, releaseURI);
        resultConnection.add(projectURI, GROUP_ID,groupId);
        resultConnection.add(projectURI, ARTIFACT_ID, artifactId);

        //release
        resultConnection.add(releaseURI, ADMSSW.PACKAGE, packageURI);
        resultConnection.add(releaseURI, RDF.TYPE, ADMSSW.SOFTWARE_RELEASE);
        resultConnection.add(releaseURI, GROUP_ID,groupId);
        resultConnection.add(releaseURI, ARTIFACT_ID, artifactId);
        resultConnection.add(releaseURI, VERSION, version);


        //package
        resultConnection.add(packageURI, RDF.TYPE, PACKAGE_MAVEN.PACKAGE);
        resultConnection.add(packageURI, GROUP_ID,groupId);
        resultConnection.add(packageURI, ARTIFACT_ID, artifactId);
        resultConnection.add(packageURI, VERSION, version);
        resultConnection.add(packageURI, PACKAGE_MAVEN.PACKAGING, packaging);




        if(pom.getDescription() != null){
            Literal description = valueFactory.createLiteral(pom.getDescription());
            resultConnection.add(projectURI, PACKAGE_MAVEN.DESCRIPTION, description);
            resultConnection.add(releaseURI, PACKAGE_MAVEN.DESCRIPTION,description);
            resultConnection.add(packageURI, PACKAGE_MAVEN.DESCRIPTION,description);
        }

        if(pom.getUrl() != null){
            Literal url = valueFactory.createLiteral(pom.getUrl());
            resultConnection.add(projectURI, PACKAGE_MAVEN.URL,url);
            resultConnection.add(releaseURI, PACKAGE_MAVEN.URL,url);
            resultConnection.add(packageURI, PACKAGE_MAVEN.URL,url);
        }

        if(pom.getOrganization() != null){
            if(pom.getOrganization().getName() != null){
                Literal organizationName = valueFactory.createLiteral(pom.getOrganization().getName());
                resultConnection.add(projectURI, PACKAGE_MAVEN.ORGANIZATION,organizationName);
                resultConnection.add(releaseURI, PACKAGE_MAVEN.ORGANIZATION,organizationName);
                resultConnection.add(packageURI, PACKAGE_MAVEN.ORGANIZATION,organizationName);
            }

            if(pom.getOrganization().getUrl() != null){
                Literal organizationName = valueFactory.createLiteral(pom.getOrganization().getName());
                resultConnection.add(projectURI, PACKAGE_MAVEN.ORGANIZATION,organizationName);
                resultConnection.add(releaseURI, PACKAGE_MAVEN.ORGANIZATION,organizationName);
                resultConnection.add(packageURI, PACKAGE_MAVEN.ORGANIZATION,organizationName);
            }
        }

        if(pom.getDevelopers() != null){
            for(Developer d : pom.getDevelopers()){
                BNode developer = valueFactory.createBNode();
                resultConnection.add(packageURI,PACKAGE_MAVEN.DEVELOPER,developer);
                if(d.getEmail() != null){
                    resultConnection.add(developer, FOAF.MBOX, valueFactory.createLiteral(d.getEmail()));
                }
                if(d.getName() !=null){
                    resultConnection.add(developer, FOAF.NAME, valueFactory.createLiteral(d.getName()));
                }
                if(d.getUrl() != null){
                    resultConnection.add(developer,PACKAGE_MAVEN.URL, valueFactory.createLiteral(d.getUrl()));
                }
                if(d.getOrganization() != null){
                    resultConnection.add(developer,PACKAGE_MAVEN.ORGANIZATION, valueFactory.createLiteral(d.getOrganization()));
                }
                if(d.getOrganizationUrl() != null){
                    resultConnection.add(developer, PACKAGE_MAVEN.ORGANIZATION_URL, valueFactory.createLiteral(d.getOrganizationUrl()));
                }
            }
        }

        if(pom.getContributors() != null){
            for(Contributor c : pom.getContributors()){
                BNode contributor = valueFactory.createBNode();
                resultConnection.add(packageURI,PACKAGE_MAVEN.CONTRIBUTOR,contributor);
                if(c.getEmail() != null){
                    resultConnection.add(contributor, FOAF.MBOX, valueFactory.createLiteral(c.getEmail()));
                }
                if(c.getName() !=null){
                    resultConnection.add(contributor, FOAF.NAME, valueFactory.createLiteral(c.getName()));
                }
                if(c.getUrl() != null){
                    resultConnection.add(contributor,PACKAGE_MAVEN.URL, valueFactory.createLiteral(c.getUrl()));
                }
                if(c.getOrganization() != null){
                    resultConnection.add(contributor,PACKAGE_MAVEN.ORGANIZATION, valueFactory.createLiteral(c.getOrganization()));
                }
                if(c.getOrganizationUrl() != null){
                    resultConnection.add(contributor, PACKAGE_MAVEN.ORGANIZATION_URL, valueFactory.createLiteral(c.getOrganizationUrl()));
                }
            }
        }

        if(pom.getCiManagement() !=null){
            if(pom.getCiManagement().getUrl() != null){
                resultConnection.add(packageURI,PACKAGE_MAVEN.CI_MANAGEMENT_URL,valueFactory.createLiteral(pom.getCiManagement().getUrl()));
            }
            if(pom.getCiManagement().getSystem() != null) {
                resultConnection.add(packageURI,PACKAGE_MAVEN.CI_MANAGEMENT_SYSTEM,valueFactory.createLiteral(pom.getCiManagement().getSystem()));
            }


        }

        if(pom.getIssueManagement() != null){
            if(pom.getIssueManagement().getSystem() != null){
                resultConnection.add(packageURI,PACKAGE_MAVEN.ISSUE_MANAGEMENT_SYSTEM, valueFactory.createLiteral(pom.getIssueManagement().getSystem()));
            }
            if(pom.getIssueManagement().getUrl()  != null){
                resultConnection.add(packageURI,PACKAGE_MAVEN.ISSUE_MANAGEMENT_URL, valueFactory.createLiteral(pom.getIssueManagement().getUrl()));
            }
        }

        if(pom.getLicenses() != null){
            for(License license : pom.getLicenses()){
                BNode node = valueFactory.createBNode();
                resultConnection.add(releaseURI,PACKAGE_MAVEN.LICENSE,node);
                if(license.getName() != null){
                   resultConnection.add(node,PACKAGE_MAVEN.LICENSE_NAME,valueFactory.createLiteral(license.getName()));
                }
                if(license.getUrl() != null){
                    resultConnection.add(node,PACKAGE_MAVEN.LICENSE_URL,valueFactory.createLiteral(license.getUrl()));
                }
            }
        }

        if(pom.getDependencies() != null){
            for (Dependency dependency : pom.getDependencies()){
                URI d = valueFactory.createURI(rooturl + dependency.getGroupId() + "/" + dependency.getArtifactId() + ((dependency.getVersion() != null)?"/" + dependency.getVersion():""));
                if(dependency.isOptional()){
                    if("compile".equals(dependency.getScope())){
                         resultConnection.add(packageURI,PACKAGE_MAVEN.DEPENDENCY_COMPILE_OPTIONAL,d);
                    } else if("system".equals(dependency.getScope())){
                        resultConnection.add(packageURI,PACKAGE_MAVEN.DEPENDENCY_SYSTEM_OPTIONAL,d);
                    } else if("provided".equals(dependency.getScope())){
                        resultConnection.add(packageURI,PACKAGE_MAVEN.DEPENDENCY_PROVIDED_OPTIONAL,d);
                    } else if("runtime".equals(dependency.getScope())){
                        resultConnection.add(packageURI,PACKAGE_MAVEN.DEPENDENCY_RUNTIME_OPTIONAL,d);
                    } else if("test".equals(dependency.getScope())){
                        resultConnection.add(packageURI,PACKAGE_MAVEN.DEPENDENCY_TEST_OPTIONAL,d);
                    } else {
                        throw new RuntimeException(dependency.getScope());
                    }
                }else{
                    if("compile".equals(dependency.getScope())){
                        resultConnection.add(packageURI,PACKAGE_MAVEN.DEPENDENCY_COMPILE,d);
                    } else if("system".equals(dependency.getScope())){
                        resultConnection.add(packageURI,PACKAGE_MAVEN.DEPENDENCY_SYSTEM,d);
                    } else if("provided".equals(dependency.getScope())){
                        resultConnection.add(packageURI,PACKAGE_MAVEN.DEPENDENCY_PROVIDED,d);
                    } else if("runtime".equals(dependency.getScope())){
                        resultConnection.add(packageURI,PACKAGE_MAVEN.DEPENDENCY_RUNTIME,d);
                    } else if("test".equals(dependency.getScope())){
                        resultConnection.add(packageURI, PACKAGE_MAVEN.DEPENDENCY_TEST, d);
                    } else {
                        throw new RuntimeException(dependency.getScope());
                    }
                }
            }
        }
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }
}
