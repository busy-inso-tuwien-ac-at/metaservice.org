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

package org.metaservice.core.file;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 * Created by ilo on 29.05.2014.
 */
public class FileIdentifier {
    private final String sha1sum;
    private final long size;

    public FileIdentifier(String sha1sum, long size) {
        this.sha1sum = sha1sum;
        this.size = size;
    }

    public String getSha1sum() {
        return sha1sum;
    }

    public long getSize() {
        return size;
    }

    public URI getUri(){
        return ValueFactoryImpl.getInstance().createURI("http://metaservice.org/d/files/"+size+"/"+sha1sum);
    }
}
