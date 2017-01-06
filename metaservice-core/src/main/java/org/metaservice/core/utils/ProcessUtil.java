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
        String input = IOUtils.toString(exec.getInputStream(), "utf-8");
        String error = IOUtils.toString(exec.getErrorStream(), "utf-8");
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
