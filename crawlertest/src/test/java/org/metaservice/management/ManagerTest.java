package org.metaservice.management;

import org.metaservice.core.ProductionConfig;

import java.io.File;

/**
 * Created by ilo on 08.01.14.
 */
public class ManagerTest {
    @org.junit.Test
    public void testInstall() throws Exception {
         Manager manager = new Manager(new ProductionConfig(), null, null);
        manager.install(new File("C:\\Users\\ilo\\.m2\\repository\\org\\metaservice\\metaservice-core-deb\\1.0\\metaservice-core-deb-1.0.jar"));
    }
}
