package org.metaservice.core.postprocessor;

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
            File f = new File("whitelist.txt");
            try(BufferedReader bufferedReader = new BufferedReader(new FileReader(f))){
                String s;
                while((s = bufferedReader.readLine())!= null){
                    whiteList.add(s);
                }
            } catch (IOException e) {
                LOGGER.error("error loading whitelist",e);
            }
        }
    }

    public boolean isEnabled(){return enabled;}

    public boolean process(PostProcessingTask task){
        return whiteList.contains(task.getChangedURI().toString());
    }
}
