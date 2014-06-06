package org.metaservice.core.file;


import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import org.apache.commons.io.IOUtils;
import org.openrdf.model.URI;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by ilo on 29.05.2014.
 */
public class FileUriUtils {

    static String regex = "^http://metaservice.org/d/files/([0-9]+)/([0-9a-z]+)$";

    public static Path getFile(String sha1sum,long size){
        return Paths.get("/opt/metaservice_data/files/" + size + "/" + sha1sum);
    }

    public static Path getFile(URI uri) throws FileProcessingException  {
        String size = uri.toString().replaceAll(regex, "$1");
        String sha1sum = uri.toString().replaceAll(regex, "$2");
        return getFile(sha1sum, Long.valueOf(size));
    }

    public static FileIdentifier storeFile(java.net.URI file) throws FileProcessingException {
        try {
            return storeFile(file.toURL().openStream());
        } catch (IOException e) {
            throw new FileProcessingException(e);
        }
    }

    public static FileIdentifier storeFile(InputStream inputStream) throws FileProcessingException {

        try {
            File localDir = com.google.common.io.Files.createTempDir();
            File localFile = new File(localDir.toString()+"/x");
            IOUtils.copy(inputStream,new FileOutputStream(localFile));
            HashCode x = com.google.common.io.Files.hash(localFile, Hashing.sha1());
            long size = localFile.length();
            Path target = getFile(x.toString(),size);
            Files.createDirectories(target.getParent());
            Files.move(localFile.toPath(),target);
            return new FileIdentifier(x.toString(),size);
        } catch (IOException e) {
            throw new FileProcessingException(e);
        }
    }


    public static boolean isFileUri(URI uri){
        return  (uri.toString().matches(regex));
    }

    public static Path getFile(FileIdentifier fileIdentifier) {
        return getFile(fileIdentifier.getSha1sum(),fileIdentifier.getSize());
    }
}
