package org.metaservice.manager;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.repository.RepositoryPolicy;
import org.eclipse.aether.resolution.*;
import org.eclipse.aether.version.Version;
import org.jetbrains.annotations.NotNull;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.config.ManagerConfig;
import org.metaservice.manager.maven.TransferListener;
import org.slf4j.Logger;

import javax.inject.Inject;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ilo on 18.02.14.
 */
public class MavenManager {
    private final static Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MavenManager.class);
    private final RepositorySystem system;

    private  final DefaultRepositorySystemSession session;


    private  final RemoteRepository repo;
    private  final RemoteRepository repoSnapshot;


    public void setManager(Manager manager) {
        this.manager = manager;
    }

    private Manager manager;

    @Inject
    public MavenManager(
            RepositorySystem system
    ) {
        this.system = system;
        this.repo = new RemoteRepository.Builder( "maven-metaservice-libs-release", "default", "http://maven.metaservice.org/artifactory/libs-release" ).build();
        this.repoSnapshot = new RemoteRepository.Builder( "maven-metaservice-libs-snapshot", "default", "http://maven.metaservice.org/artifactory/libs-snapshot" )
                .setSnapshotPolicy(new RepositoryPolicy(true, RepositoryPolicy.UPDATE_POLICY_ALWAYS, RepositoryPolicy.CHECKSUM_POLICY_IGNORE))
                .build();
        session  = MavenRepositorySystemUtils.newSession();
        session.setUpdatePolicy("always");
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, new LocalRepository( "local-modules" )));
        session.setTransferListener(new TransferListener());

    }


    private List<Version> getVersions(MetaserviceDescriptor.ModuleInfo moduleInfo) throws VersionRangeResolutionException, ManagerException {
        Artifact artifact = new DefaultArtifact( moduleInfo.getGroupId()+
                ":"+moduleInfo.getArtifactId()+":[0,)" );
        VersionRangeRequest rangeRequest = new VersionRangeRequest();
        rangeRequest.setArtifact(artifact);
        rangeRequest.addRepository( repo );
        rangeRequest.addRepository(repoSnapshot);
        VersionRangeResult rangeResult = system.resolveVersionRange( session, rangeRequest );
        List<Version> versions = rangeResult.getVersions();
        LOGGER.debug("Available versions {}", versions);
        retrieveArtifact(moduleInfo,versions.get(versions.size()-1).toString());
        return versions;
    }

    private Version getLatestVersion(MetaserviceDescriptor.ModuleInfo moduleInfo) throws VersionRangeResolutionException, ManagerException {
        List<Version> versions = getVersions(moduleInfo);
        return versions.get(versions.size()-1);
    }

    public void updateModule(@NotNull ManagerConfig.Module module,boolean replace,boolean latest) throws ManagerException {
        try {
            String currentVersion = module.getMetaserviceDescriptor().getModuleInfo().getVersion();
            Version latestVersion = getLatestVersion(module.getMetaserviceDescriptor().getModuleInfo());
            //if inplace update replace
            if(latest && latestVersion.toString().equals(currentVersion)){
                if(!replace){
                    throw new ManagerException("Version already exists, use --replace to replace current version");
                }
            }
            String version;
            if(latest){
                version = latestVersion.toString();
            }else{
                version = currentVersion;
            }
            File file = retrieveArtifact(module.getMetaserviceDescriptor().getModuleInfo(),version);
            manager.add(file,true);

            //todo check installed/notinstalled?
        } catch (VersionRangeResolutionException e) {
            throw  new ManagerException("Could not update Artifact" , e);
        }
    }


    private File retrieveArtifact(MetaserviceDescriptor.ModuleInfo moduleInfo,String version) throws ManagerException {
        if(version == null)
            version = moduleInfo.getVersion();
        return retrieveArtifact(moduleInfo.getGroupId(),moduleInfo.getArtifactId(),version);
    }



    private File retrieveArtifact(String groupId,String artifactId,String version) throws ManagerException {
        try {
            Artifact artifact = new DefaultArtifact( groupId+ ":"+artifactId+":"+version );
            invalidateLastUpdatedInSession(session);
            ArtifactRequest artifactRequest = new ArtifactRequest();
            artifactRequest.setArtifact( artifact );
            artifactRequest.setRepositories(Arrays.asList(repo));
            ArtifactResult artifactResult = system.resolveArtifact( session, artifactRequest );
            artifact = artifactResult.getArtifact();
            LOGGER.info(artifact + " resolved to  " + artifact.getFile());
            return artifact.getFile();
        } catch (ArtifactResolutionException e) {
            throw new ManagerException("could not resolve Artifact ", e );
        }
    }

    private void invalidateLastUpdatedInSession(DefaultRepositorySystemSession session) {
        session.getData().set("updateCheckManager.checks",null);
    }


    public void retrieveAndAddModule(String groupId, String artifactId, String version,boolean override) throws ManagerException {
        manager.add(retrieveArtifact(groupId, artifactId, version), override);
    }
}
