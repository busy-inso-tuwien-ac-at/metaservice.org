package org.metaservice.core.utils;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.metaservice.api.MetaserviceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by ilo on 17.02.14.
 */
public class ProcessUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessUtil.class);

    public static void debug(@NotNull Process exec) throws IOException, InterruptedException, ProcessUtilException {
        StringWriter writer = new StringWriter();
        IOUtils.copy(exec.getInputStream(), writer, "utf-8");
        String input = writer.getBuffer().toString();
        writer = new StringWriter();
        IOUtils.copy(exec.getErrorStream(), writer, "utf-8");
        String error = writer.getBuffer().toString();
        exec.getErrorStream().close();
        exec.getInputStream().close();
        int ret = exec.waitFor();
        if(ret != 0){
            throw new ProcessUtilException("Error during execution",input,error);
        }

    }

    public static class ProcessUtilException extends MetaserviceException {
        private String stdout;
        private String stderr;

        public ProcessUtilException(String message, String stdout, String stderr) {
            super(message);
            this.stdout = stdout;
            this.stderr = stderr;
        }

        public String getStdout() {
            return stdout;
        }

        public String getStderr() {
            return stderr;
        }
    }
}
