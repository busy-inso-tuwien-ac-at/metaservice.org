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

package org.metaservice.core.injection.providers;

import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.config.JAXBMetaserviceDescriptorImpl;

import com.google.inject.Provider;
import javax.xml.bind.JAXB;
import java.io.InputStream;

/**
 * Created by ilo on 10.02.14.
 */
public class JAXBMetaserviceDescriptorProvider implements Provider<MetaserviceDescriptor> {
    private final InputStream inputStream;

    public JAXBMetaserviceDescriptorProvider(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public MetaserviceDescriptor get() {
        MetaserviceDescriptor descriptor = JAXB.unmarshal(inputStream, JAXBMetaserviceDescriptorImpl.class);
        return descriptor;
    }
}
