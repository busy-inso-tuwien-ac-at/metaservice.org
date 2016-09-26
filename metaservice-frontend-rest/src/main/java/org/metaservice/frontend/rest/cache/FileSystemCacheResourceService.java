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

package org.metaservice.frontend.rest.cache;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.metaservice.frontend.rest.api.ResourceService;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * Created by ilo on 02.09.2015.
 */
public class FileSystemCacheResourceService  implements ResourceService{
    private final ResourceService resourceService;

    public FileSystemCacheResourceService(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @Override
    public InputStream getResource(String resource, Calendar date, String mimetype) {
        boolean latest = date == null;
        if(date == null){
            date = DatatypeConverter.parseDateTime("2025-01-01T00:00:00Z");
        }
        try {
            if(latest){
                File cacheFile = getCacheFile(resource,mimetype);
                if(cacheFile.exists()){
                    return new FileInputStream(cacheFile);
                }
                return cacheResource(resourceService.getResource(resource,date, mimetype),resource,mimetype);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resourceService.getResource(resource,date,mimetype);
    }

    private File getCacheFile(String resource,String mimetype) {
        return new File("/opt/metaservice_cache/"+resource.replaceFirst(Pattern.quote("http://"),"")+getExtension(mimetype));
    }

    private InputStream cacheResource(InputStream inputStream,String resource,String mimetype) throws IOException {
        byte[] content = org.apache.commons.io.IOUtils.toByteArray(inputStream);

        Path source = Files.createTempFile(DigestUtils.md5Hex(resource), getExtension(mimetype));
        try(OutputStream outputStream = new FileOutputStream(source.toFile())){
            IOUtils.write(content,outputStream);
        }

        Path target = getCacheFile(resource,mimetype).toPath();
        if(!target.getParent().toFile().isDirectory()) {
            Files.createDirectories(target.getParent());
        }
        Files.move(source,target, StandardCopyOption.ATOMIC_MOVE,StandardCopyOption.REPLACE_EXISTING);
        return new ByteArrayInputStream(content);
    }


    String getExtension(String mimetype){
        switch (mimetype){
            case "application/ld+json":
                return ".jsonld";
            case "application/rdf+xml":
                return ".rdf";
            case "application/x-turtle":
                return ".ttl";
            default:
                throw new RuntimeException();
        }
    }
}
