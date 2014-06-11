package org.metaservice.core.descriptor;

import org.junit.Assert;
import org.junit.Test;
import org.metaservice.api.descriptor.MetaserviceDescriptor;
import org.metaservice.api.messaging.config.JAXBMetaserviceDescriptorImpl;
import org.metaservice.api.messaging.config.ManagerConfig;
import org.metaservice.api.messaging.descriptors.DescriptorHelper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * Created by ilo on 21.02.14.
 */
public class DescriptorHelperImplTest {

    private DescriptorHelper descriptorHelper = new DescriptorHelperImpl();
    @Test
    public void testModule(){

    }

    @Test
    public void testPostprocessor(){
        ManagerConfig.Module module = createModule();
        List<ManagerConfig.Module> list = Arrays.asList(module);
        String postProcessorString = descriptorHelper.getStringFromPostProcessor(module.getMetaserviceDescriptor().getModuleInfo(), module.getMetaserviceDescriptor().getPostProcessorList().get(0));
        MetaserviceDescriptor.PostProcessorDescriptor postProcessorDescriptor = descriptorHelper.getPostProcessorFromString(list, postProcessorString);
        Assert.assertEquals(postProcessorDescriptor,module.getMetaserviceDescriptor().getPostProcessorList().get(0));
    }

    @Test
    public void testReversibleModule(){
        ManagerConfig.Module module = createModule();
        List<ManagerConfig.Module> list = Arrays.asList(module);
        String moduleString = descriptorHelper.getModuleIdentifierStringFromModule(module.getMetaserviceDescriptor().getModuleInfo());
        ManagerConfig.Module result = descriptorHelper.getModuleFromString(list, moduleString);
        Assert.assertEquals(result,module);

        String providerString = descriptorHelper.getStringFromProvider(module.getMetaserviceDescriptor().getModuleInfo(), module.getMetaserviceDescriptor().getProviderList().get(0));
        result = descriptorHelper.getModuleFromString(list, providerString);

        Assert.assertEquals(result,module);

        String postProcessorString = descriptorHelper.getStringFromPostProcessor(module.getMetaserviceDescriptor().getModuleInfo(), module.getMetaserviceDescriptor().getPostProcessorList().get(0));
        result = descriptorHelper.getModuleFromString(list, postProcessorString);

        Assert.assertEquals(result,module);

    }

    public ManagerConfig.Module createModule(){
        MetaserviceDescriptor.ModuleInfo moduleInfo = new JAXBMetaserviceDescriptorImpl.ModuleInfoImpl("groupId","artifactID","versionId");


        JAXBMetaserviceDescriptorImpl.ProviderDescriptorImpl providerDescriptor = new JAXBMetaserviceDescriptorImpl.ProviderDescriptorImpl();
        providerDescriptor.setId("providerId");

        JAXBMetaserviceDescriptorImpl.PostProcessorDescriptorImpl postProcessorDescriptor = new JAXBMetaserviceDescriptorImpl.PostProcessorDescriptorImpl();
        postProcessorDescriptor.setId("postprocessorId");

        JAXBMetaserviceDescriptorImpl metaserviceDescriptor = new JAXBMetaserviceDescriptorImpl();
        metaserviceDescriptor.setModuleInfo(moduleInfo);
        metaserviceDescriptor.getProviderList().add(providerDescriptor);
        metaserviceDescriptor.getPostProcessorList().add(postProcessorDescriptor);


        ManagerConfig.Module module = new ManagerConfig.Module();
        module.setMetaserviceDescriptor(metaserviceDescriptor);
        return module;
    }

}
