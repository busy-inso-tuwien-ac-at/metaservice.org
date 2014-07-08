package org.metaservice.core.archive;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by ilo on 27.05.2014.
 */
public class TestUtils {
    public static String readFile(String file) throws IOException {
        FileReader fileInputStream = new FileReader(file);
        return IOUtils.toString(fileInputStream);
    }

    public static String readFile(File f) throws IOException {
        return readFile(f.toString());
    }
}
