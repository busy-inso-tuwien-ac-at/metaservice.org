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

package org.metaservice.core.postprocessor;

import org.metaservice.api.messaging.PostProcessingTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashSet;

/**
 * Created by ilo on 27.05.2014.
 */
public class Debug {
    private final Logger LOGGER = LoggerFactory.getLogger(Debug.class);
    private final boolean enabled;
    private final HashSet<String> whiteList = new HashSet<>();

    public Debug() {
        this.enabled = System.getProperty("msdebug") !=null;
        if(enabled){
            LOGGER.warn("ATTENTION: DBEUG WHITELIST IS ENABLED");
            File f = new File("/opt/metaservice/whitelist.txt");
            try(BufferedReader bufferedReader = new BufferedReader(new FileReader(f))){
                String s;
                while((s = bufferedReader.readLine())!= null){
                    whiteList.add(s);
                    LOGGER.info("ON WHITELIST " + s);
                }
            } catch (IOException e) {
                LOGGER.error("error loading whitelist",e);
            }
        }
    }

    public boolean isEnabled(){return enabled;}

    public boolean process(PostProcessingTask task){
        for(String s: whiteList){
            if(task.getChangedURI().toString().contains(s))
                return true;
        }
        return false;
    }
}
