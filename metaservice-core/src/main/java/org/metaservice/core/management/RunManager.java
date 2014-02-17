package org.metaservice.core.management;

import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.core.utils.ProcessUtil;

import java.io.IOException;
import java.util.Map;

/**
 * Created by ilo on 12.02.14.
 */
public class RunManager {
    private static final String RUN_EXECUTABLE = "/opt/metaservice/run.sh";
    public static final String MAINCLASS = "MAINCLASS";
    public static final String GROUP_ID = "GROUP_ID";
    public static final String ARTIFACT_ID = "ARTIFACT_ID";
    public static final String VERSION = "VERSION";

    public void runProvider(ManagerConfig.Module module,MetaserviceDescriptor.ProviderDescriptor providerDescriptor) throws ManagerException {
        ProcessBuilder pb = new ProcessBuilder(RUN_EXECUTABLE,providerDescriptor.getId());

        MetaserviceDescriptor.ModuleInfo moduleInfo =module.metaserviceDescriptor.getModuleInfo();
        Map<String, String> env = pb.environment();
        env.put(MAINCLASS, "org.metaservice.core.jms.JMSProviderRunner");
        env.put(GROUP_ID, moduleInfo.getGroupId());
        env.put(ARTIFACT_ID,moduleInfo.getArtifactId());
        env.put(VERSION, moduleInfo.getVersion());
        try {
            Process p = pb.start();
        } catch (IOException e) {
            throw new ManagerException(e);
        }
    }

    public void runPostProcessor(ManagerConfig.Module module,MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor) throws ManagerException {
        ProcessBuilder pb = new ProcessBuilder(RUN_EXECUTABLE,postProcessorDescriptor.getId());

        MetaserviceDescriptor.ModuleInfo moduleInfo =module.metaserviceDescriptor.getModuleInfo();
        Map<String, String> env = pb.environment();
        env.put(MAINCLASS, "org.metaservice.core.jms.JMSPostProcessorRunner");
        env.put(GROUP_ID, moduleInfo.getGroupId());
        env.put(ARTIFACT_ID,moduleInfo.getArtifactId());
        env.put(VERSION,moduleInfo.getVersion());
        try {
            Process p = pb.start();
        } catch (IOException e) {
            throw new ManagerException(e);
        }
    }

    public void runFrontend() throws ManagerException {
        ProcessBuilder pb = new ProcessBuilder(RUN_EXECUTABLE);

        Map<String, String> env = pb.environment();
        env.put(MAINCLASS, "org.metaservice.frontend.rest.RestFrontend");
        env.put(GROUP_ID, "org.metaservice");
        env.put(ARTIFACT_ID,"metaservice-frontend-rest");
        env.put(VERSION,"0.1");
        try {
            Process p = pb.start();
        } catch (IOException e) {
            throw new ManagerException(e);
        }
    }

    public void runCrawlerAndWaitForFinish(String id, String group_id, String artifact_id, String version) throws ManagerException {
        try{
            System.out.println("Starting Crawler " + id);
            ProcessBuilder pb = new ProcessBuilder(RUN_EXECUTABLE,id);
            Map<String, String> env = pb.environment();
            env.put(MAINCLASS, "org.metaservice.core.crawler.CrawlerRunner");
            env.put(GROUP_ID, group_id);
            env.put(ARTIFACT_ID, artifact_id);
            env.put(VERSION,version);

            Process p = pb.start();
            ProcessUtil.debug(p);

            System.out.println("Finished Crawler " + id);
        } catch (IOException | InterruptedException e) {
            throw new ManagerException(e);
        }
    }
}
