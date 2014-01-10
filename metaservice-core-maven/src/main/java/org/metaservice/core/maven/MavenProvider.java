package org.metaservice.core.maven;

import org.apache.maven.model.Contributor;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Developer;
import org.apache.maven.model.License;
import org.metaservice.api.ns.ADMSSW;
import org.metaservice.api.provider.Provider;
import org.metaservice.api.provider.ProviderException;
import org.openrdf.model.*;
import org.openrdf.model.impl.TreeModel;
import org.openrdf.model.vocabulary.FOAF;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.model.vocabulary.RDF;

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
    public Model provideModelFor(org.apache.maven.model.Model pom) throws ProviderException {
        Model model = new TreeModel();

        URI projectURI =   valueFactory.createURI(rooturl + pom.getGroupId() + "/" + pom.getArtifactId());
        URI releaseURI = valueFactory.createURI(rooturl +  pom.getGroupId() + "/" + pom.getArtifactId()+ "/" + pom.getVersion());
        URI packageURI = valueFactory.createURI(rooturl + pom.getGroupId() + "/" + pom.getArtifactId() + "/" + pom.getVersion() + "/" + pom.getPackaging());

        Literal groupId = valueFactory.createLiteral(pom.getGroupId());
        Literal artifactId = valueFactory.createLiteral(pom.getArtifactId());
        Literal version = valueFactory.createLiteral(pom.getVersion());
        Literal packaging = valueFactory.createLiteral(pom.getPackaging());

        //TODO bnode or ui link parent?
        BNode parent = valueFactory.createBNode();
        model.add(parent,GROUP_ID,valueFactory.createLiteral(pom.getParent().getGroupId()));
        model.add(parent,ARTIFACT_ID,valueFactory.createLiteral(pom.getParent().getArtifactId()));
        model.add(parent,VERSION,valueFactory.createLiteral(pom.getParent().getVersion()));

        //project
        model.add(projectURI, RDF.TYPE, ADMSSW.SOFTWARE_PROJECT);
        model.add(projectURI, ADMSSW.RELEASE, releaseURI);
        model.add(projectURI, GROUP_ID,groupId);
        model.add(projectURI, ARTIFACT_ID, artifactId);

        //release
        model.add(releaseURI, ADMSSW.PACKAGE, packageURI);
        model.add(releaseURI, RDF.TYPE, ADMSSW.SOFTWARE_RELEASE);
        model.add(releaseURI, GROUP_ID,groupId);
        model.add(releaseURI, ARTIFACT_ID, artifactId);
        model.add(releaseURI, VERSION, version);


        //package
        model.add(packageURI, RDF.TYPE, PACKAGE_MAVEN.PACKAGE);
        model.add(packageURI, GROUP_ID,groupId);
        model.add(packageURI, ARTIFACT_ID, artifactId);
        model.add(packageURI, VERSION, version);
        model.add(packageURI, PACKAGE_MAVEN.PACKAGING,packaging);




        if(pom.getDescription() != null){
            Literal description = valueFactory.createLiteral(pom.getDescription());
            model.add(projectURI, PACKAGE_MAVEN.DESCRIPTION,description);
            model.add(releaseURI, PACKAGE_MAVEN.DESCRIPTION,description);
            model.add(packageURI, PACKAGE_MAVEN.DESCRIPTION,description);
        }

        if(pom.getUrl() != null){
            Literal url = valueFactory.createLiteral(pom.getUrl());
            model.add(projectURI, PACKAGE_MAVEN.URL,url);
            model.add(releaseURI, PACKAGE_MAVEN.URL,url);
            model.add(packageURI, PACKAGE_MAVEN.URL,url);
        }

        if(pom.getOrganization() != null){
            if(pom.getOrganization().getName() != null){
                Literal organizationName = valueFactory.createLiteral(pom.getOrganization().getName());
                model.add(projectURI, PACKAGE_MAVEN.ORGANIZATION,organizationName);
                model.add(releaseURI, PACKAGE_MAVEN.ORGANIZATION,organizationName);
                model.add(packageURI, PACKAGE_MAVEN.ORGANIZATION,organizationName);
            }

            if(pom.getOrganization().getUrl() != null){
                Literal organizationName = valueFactory.createLiteral(pom.getOrganization().getName());
                model.add(projectURI, PACKAGE_MAVEN.ORGANIZATION,organizationName);
                model.add(releaseURI, PACKAGE_MAVEN.ORGANIZATION,organizationName);
                model.add(packageURI, PACKAGE_MAVEN.ORGANIZATION,organizationName);
            }
        }

        if(pom.getDevelopers() != null){
            for(Developer d : pom.getDevelopers()){
                BNode developer = valueFactory.createBNode();
                model.add(packageURI,PACKAGE_MAVEN.DEVELOPER,developer);
                if(d.getEmail() != null){
                    model.add(developer, FOAF.MBOX, valueFactory.createLiteral(d.getEmail()));
                }
                if(d.getName() !=null){
                    model.add(developer, FOAF.NAME, valueFactory.createLiteral(d.getName()));
                }
                if(d.getUrl() != null){
                    model.add(developer,PACKAGE_MAVEN.URL, valueFactory.createLiteral(d.getUrl()));
                }
                if(d.getOrganization() != null){
                    model.add(developer,PACKAGE_MAVEN.ORGANIZATION, valueFactory.createLiteral(d.getOrganization()));
                }
                if(d.getOrganizationUrl() != null){
                    model.add(developer, PACKAGE_MAVEN.ORGANIZATION_URL, valueFactory.createLiteral(d.getOrganizationUrl()));
                }
            }
        }

        if(pom.getContributors() != null){
            for(Contributor c : pom.getContributors()){
                BNode contributor = valueFactory.createBNode();
                model.add(packageURI,PACKAGE_MAVEN.CONTRIBUTOR,contributor);
                if(c.getEmail() != null){
                    model.add(contributor, FOAF.MBOX, valueFactory.createLiteral(c.getEmail()));
                }
                if(c.getName() !=null){
                    model.add(contributor, FOAF.NAME, valueFactory.createLiteral(c.getName()));
                }
                if(c.getUrl() != null){
                    model.add(contributor,PACKAGE_MAVEN.URL, valueFactory.createLiteral(c.getUrl()));
                }
                if(c.getOrganization() != null){
                    model.add(contributor,PACKAGE_MAVEN.ORGANIZATION, valueFactory.createLiteral(c.getOrganization()));
                }
                if(c.getOrganizationUrl() != null){
                    model.add(contributor, PACKAGE_MAVEN.ORGANIZATION_URL, valueFactory.createLiteral(c.getOrganizationUrl()));
                }
            }
        }

        if(pom.getCiManagement() !=null){
            if(pom.getCiManagement().getUrl() != null){
                model.add(packageURI,PACKAGE_MAVEN.CI_MANAGEMENT_URL,valueFactory.createLiteral(pom.getCiManagement().getUrl()));
            }
            if(pom.getCiManagement().getSystem() != null) {
                model.add(packageURI,PACKAGE_MAVEN.CI_MANAGEMENT_SYSTEM,valueFactory.createLiteral(pom.getCiManagement().getSystem()));
            }


        }

        if(pom.getIssueManagement() != null){
            if(pom.getIssueManagement().getSystem() != null){
                model.add(packageURI,PACKAGE_MAVEN.ISSUE_MANAGEMENT_SYSTEM, valueFactory.createLiteral(pom.getIssueManagement().getSystem()));
            }
            if(pom.getIssueManagement().getUrl()  != null){
                model.add(packageURI,PACKAGE_MAVEN.ISSUE_MANAGEMENT_URL, valueFactory.createLiteral(pom.getIssueManagement().getUrl()));
            }
        }

        if(pom.getLicenses() != null){
            for(License license : pom.getLicenses()){
                BNode node = valueFactory.createBNode();
                model.add(releaseURI,PACKAGE_MAVEN.LICENSE,node);
                if(license.getName() != null){
                   model.add(node,PACKAGE_MAVEN.LICENSE_NAME,valueFactory.createLiteral(license.getName()));
                }
                if(license.getUrl() != null){
                    model.add(node,PACKAGE_MAVEN.LICENSE_URL,valueFactory.createLiteral(license.getUrl()));
                }
            }
        }

        if(pom.getDependencies() != null){
            for (Dependency dependency : pom.getDependencies()){
                URI d = valueFactory.createURI(rooturl + dependency.getGroupId() + "/" + dependency.getArtifactId() + ((dependency.getVersion() != null)?"/" + dependency.getVersion():""));
                if(dependency.isOptional()){
                    if("compile".equals(dependency.getScope())){
                         model.add(packageURI,PACKAGE_MAVEN.DEPENDENCY_COMPILE_OPTIONAL,d);
                    } else if("system".equals(dependency.getScope())){
                        model.add(packageURI,PACKAGE_MAVEN.DEPENDENCY_SYSTEM_OPTIONAL,d);
                    } else if("provided".equals(dependency.getScope())){
                        model.add(packageURI,PACKAGE_MAVEN.DEPENDENCY_PROVIDED_OPTIONAL,d);
                    } else if("runtime".equals(dependency.getScope())){
                        model.add(packageURI,PACKAGE_MAVEN.DEPENDENCY_RUNTIME_OPTIONAL,d);
                    } else if("test".equals(dependency.getScope())){
                        model.add(packageURI,PACKAGE_MAVEN.DEPENDENCY_TEST_OPTIONAL,d);
                    } else {
                        throw new RuntimeException(dependency.getScope());
                    }
                }else{
                    if("compile".equals(dependency.getScope())){
                        model.add(packageURI,PACKAGE_MAVEN.DEPENDENCY_COMPILE,d);
                    } else if("system".equals(dependency.getScope())){
                        model.add(packageURI,PACKAGE_MAVEN.DEPENDENCY_SYSTEM,d);
                    } else if("provided".equals(dependency.getScope())){
                        model.add(packageURI,PACKAGE_MAVEN.DEPENDENCY_PROVIDED,d);
                    } else if("runtime".equals(dependency.getScope())){
                        model.add(packageURI,PACKAGE_MAVEN.DEPENDENCY_RUNTIME,d);
                    } else if("test".equals(dependency.getScope())){
                        model.add(packageURI,PACKAGE_MAVEN.DEPENDENCY_TEST,d);
                    } else {
                        throw new RuntimeException(dependency.getScope());
                    }
                }
            }
        }
        return model;
    }
}
