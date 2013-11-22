package org.metaservice.core.deb;

import org.junit.Test;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: ilo
 * Date: 01.11.13
 * Time: 14:51
 * To change this template use File | Settings | File Templates.
 */
public class mailparser {
    @Test
     public void testit() throws IOException, AddressException {
        for(InternetAddress a : InternetAddress.parse("holland@blumen.at,Nikola Ilo <Foo@long.at>, BErnd <FOO@AAAA.at>",true)) {
            System.err.println(a.getPersonal());
        }
    }

}
