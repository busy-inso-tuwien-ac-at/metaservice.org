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
