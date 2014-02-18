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
import org.eclipse.aether.transfer.*;
import org.eclipse.aether.version.Version;
import org.metaservice.api.descriptor.MetaserviceDescriptor;

import javax.inject.Inject;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ilo on 18.02.14.
 */
public class MavenManager {
    private final RepositorySystem system;

    private  final DefaultRepositorySystemSession session;


    private  final RemoteRepository repo;
    private  final RemoteRepository repoSnapshot;

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
        session.setTransferListener(new AbstractTransferListener() {

            private PrintStream out = System.out;

            private Map<TransferResource, Long> downloads = new ConcurrentHashMap<TransferResource, Long>();

            private int lastLength;

            @Override
            public void transferInitiated( TransferEvent event )
            {
                String message = event.getRequestType() == TransferEvent.RequestType.PUT ? "Uploading" : "Downloading";

                out.println( message + ": " + event.getResource().getRepositoryUrl() + event.getResource().getResourceName() );
            }

            @Override
            public void transferProgressed( TransferEvent event )
            {
                TransferResource resource = event.getResource();
                downloads.put( resource, Long.valueOf( event.getTransferredBytes() ) );

                StringBuilder buffer = new StringBuilder( 64 );

                for ( Map.Entry<TransferResource, Long> entry : downloads.entrySet() )
                {
                    long total = entry.getKey().getContentLength();
                    long complete = entry.getValue().longValue();

                    buffer.append( getStatus( complete, total ) ).append( "  " );
                }

                int pad = lastLength - buffer.length();
                lastLength = buffer.length();
                pad( buffer, pad );
                buffer.append( '\r' );

                out.print( buffer );
            }

            private String getStatus( long complete, long total )
            {
                if ( total >= 1024 )
                {
                    return toKB( complete ) + "/" + toKB( total ) + " KB ";
                }
                else if ( total >= 0 )
                {
                    return complete + "/" + total + " B ";
                }
                else if ( complete >= 1024 )
                {
                    return toKB( complete ) + " KB ";
                }
                else
                {
                    return complete + " B ";
                }
            }

            private void pad( StringBuilder buffer, int spaces )
            {
                String block = "                                        ";
                while ( spaces > 0 )
                {
                    int n = Math.min( spaces, block.length() );
                    buffer.append( block, 0, n );
                    spaces -= n;
                }
            }

            @Override
            public void transferSucceeded( TransferEvent event )
            {
                transferCompleted( event );

                TransferResource resource = event.getResource();
                long contentLength = event.getTransferredBytes();
                if ( contentLength >= 0 )
                {
                    String type = ( event.getRequestType() == TransferEvent.RequestType.PUT ? "Uploaded" : "Downloaded" );
                    String len = contentLength >= 1024 ? toKB( contentLength ) + " KB" : contentLength + " B";

                    String throughput = "";
                    long duration = System.currentTimeMillis() - resource.getTransferStartTime();
                    if ( duration > 0 )
                    {
                        long bytes = contentLength - resource.getResumeOffset();
                        DecimalFormat format = new DecimalFormat( "0.0", new DecimalFormatSymbols( Locale.ENGLISH ) );
                        double kbPerSec = ( bytes / 1024.0 ) / ( duration / 1000.0 );
                        throughput = " at " + format.format( kbPerSec ) + " KB/sec";
                    }

                    out.println( type + ": " + resource.getRepositoryUrl() + resource.getResourceName() + " (" + len
                            + throughput + ")" );
                }
            }

            @Override
            public void transferFailed( TransferEvent event )
            {
                transferCompleted( event );

                if ( !( event.getException() instanceof MetadataNotFoundException ) )
                {
                    event.getException().printStackTrace( out );
                }
            }

            private void transferCompleted( TransferEvent event )
            {
                downloads.remove( event.getResource() );

                StringBuilder buffer = new StringBuilder( 64 );
                pad( buffer, lastLength );
                buffer.append( '\r' );
                out.print( buffer );
            }

            public void transferCorrupted( TransferEvent event )
            {
                event.getException().printStackTrace( out );
            }

            protected long toKB( long bytes )
            {
                return ( bytes + 1023 ) / 1024;
            }

        });
    }


    public void addUpdateFromMaven(MetaserviceDescriptor.ModuleInfo moduleInfo){

        try {
            Artifact artifact = new DefaultArtifact( moduleInfo.getGroupId()+
                    ":"+moduleInfo.getArtifactId()+":["+moduleInfo.getVersion()+",)" );
            VersionRangeRequest rangeRequest = new VersionRangeRequest();
            rangeRequest.setArtifact( artifact );
            rangeRequest.addRepository( repo );
            rangeRequest.addRepository(repoSnapshot);
            VersionRangeResult rangeResult = system.resolveVersionRange( session, rangeRequest );

            List<Version> versions = rangeResult.getVersions();

            System.out.println( "Available versions " + versions );
            retrieveArtifact(moduleInfo,versions.get(versions.size()-1).toString());
        } catch (VersionRangeResolutionException e) {
            e.printStackTrace();
        }

    }

    public void retrieveArtifact(MetaserviceDescriptor.ModuleInfo moduleInfo,String version){
        if(version == null)
            version = moduleInfo.getVersion();
        try {
            Artifact artifact = new DefaultArtifact( moduleInfo.getGroupId()+
                    ":"+moduleInfo.getArtifactId()+":"+version );
            System.out.println(version);
            System.out.println(artifact);
            invalidateLastUpdatedInSession(session);

            ArtifactRequest artifactRequest = new ArtifactRequest();
            artifactRequest.setArtifact( artifact );

            artifactRequest.setRepositories(Arrays.asList(repo));
            ArtifactResult artifactResult = system.resolveArtifact( session, artifactRequest );
            artifact = artifactResult.getArtifact();
            System.out.println( artifact + " resolved to  " + artifact.getFile() );
        } catch (ArtifactResolutionException e) {
            e.printStackTrace();
        }
    }

    private void invalidateLastUpdatedInSession(DefaultRepositorySystemSession session) {
        session.getData().set("updateCheckManager.checks",null);
    }
}
