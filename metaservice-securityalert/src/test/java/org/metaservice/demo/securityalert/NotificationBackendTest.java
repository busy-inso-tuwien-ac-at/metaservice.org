package org.metaservice.demo.securityalert;

import org.junit.Test;

import static org.junit.Assert.*;

public class NotificationBackendTest {
     @Test
    public void testNma(){
         new NotificationBackend().notify("http://metaservice.org/d/projects/wordpress","Wordpress","http://heise.de");
     }
}