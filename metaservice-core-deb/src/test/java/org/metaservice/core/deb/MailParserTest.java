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

package org.metaservice.core.deb;

import org.junit.Test;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: ilo
 * Date: 01.11.13
 * Time: 14:51
 * To change this template use File | Settings | File Templates.
 */
public class MailParserTest {
    @Test
     public void testit() throws IOException, AddressException {
        for(InternetAddress a : InternetAddress.parse("holland@blumen.at,Nikola Ilo <Foo@long.at>, BErnd <FOO@AAAA.at>",true)) {
            System.err.println(a.getPersonal());
        }
    }

}
