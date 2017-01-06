/*
 * Copyright 2015 Nikola Ilo
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

import org.hamcrest.number.OrderingComparison;
import org.junit.Test;
import org.metaservice.core.deb.postprocessor.DebianVersionReasoner;
import org.metaservice.core.deb.util.DebianVersionComparator;

import java.util.Comparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: ilo
 * Date: 29.10.13
 * Time: 00:25
 * To change this template use File | Settings | File Templates.
 */
public class DebianVersionReasonerTest {

    @Test
    public void testVersion(){
        Comparator<String> c = DebianVersionComparator.getInstance();

        assertEquals(c.compare("", ""), 0);
        assertEquals(c.compare("~", ""), -1);
        assertEquals(c.compare("", "~"), 1);

        assertThat(c.compare("~~", "~~a"), OrderingComparison.lessThan(0));
        assertThat(c.compare("~~a", "~"), OrderingComparison.lessThan(0));
        assertThat(c.compare("~", ""), OrderingComparison.lessThan(0));
        assertThat(c.compare("", "a"), OrderingComparison.lessThan(0));
        assertThat(c.compare("1", "2"), OrderingComparison.lessThan(0));
        assertThat(c.compare("2", "1"), OrderingComparison.greaterThan(0));
        assertThat(c.compare("0", "4"), OrderingComparison.lessThan(0));
        assertThat(c.compare("a", "."), OrderingComparison.lessThan(0));
        assertThat(c.compare("A", ";"), OrderingComparison.lessThan(0));
        assertThat(c.compare("A","a"),OrderingComparison.lessThan(0));



        // List of
        assertThat(c.compare("7.6p2-4","7.6-0"),OrderingComparison.greaterThan(0));
        assertThat(c.compare("1.0.3-3","1.0-1"),OrderingComparison.greaterThan(0));
        assertThat(c.compare("1.3","1.2.2-2"),OrderingComparison.greaterThan(0));
        assertThat(c.compare("1.3","1.2.2"),OrderingComparison.greaterThan(0));

        // Important attributes
        // disabled as dpkg --compare-versions doesn't like themâ€¦ (versions have to start with a number)
        //assertThat(c.compare("-","."),OrderingComparison.lessThan(0));
        //assertThat(c.compare("p","-"),OrderingComparison.lessThan(0));
        //assertThat(c.compare("a","-"),OrderingComparison.lessThan(0));
        //assertThat(c.compare("z","-"),OrderingComparison.lessThan(0));
        //assertThat(c.compare("a","."),OrderingComparison.lessThan(0));
        //assertThat(c.compare("z","."),OrderingComparison.lessThan(0));

        // disabled as dpkg --compare-versions doesn't like themâ€¦ (versions have to start with a number)
        assertThat(c.compare("III-alpha9.8","III-alpha9.8-1.5"),OrderingComparison.lessThan(0));

        // Epochs
        assertThat(c.compare("1:0.4","10.3"),OrderingComparison.greaterThan(0));
        assertThat(c.compare("1:1.25-4","1:1.25-8"),OrderingComparison.lessThan(0));
        assertThat(c.compare("0:1.18.36","1.18.36"),OrderingComparison.comparesEqualTo(0));

        // native version
        assertThat(c.compare("1.18.36","1.18.35"),OrderingComparison.greaterThan(0));
        assertThat(c.compare("0:1.18.36","1.18.35"),OrderingComparison.greaterThan(0));

        // Funky, but allowed, characters in upstream version
        assertThat(c.compare("9:1.18.36:5.4-20","10:0.5.1-22"),OrderingComparison.lessThan(0));
        assertThat(c.compare("9:1.18.36:5.4-20","9:1.18.36:5.5-1"),OrderingComparison.lessThan(0));
        assertThat(c.compare("9:1.18.36:5.4-20","9:1.18.37:4.3-22"),OrderingComparison.lessThan(0));
        assertThat(c.compare("1.18.36-0.17.35-18","1.18.36-19"),OrderingComparison.greaterThan(0));

        // Junk
        assertThat(c.compare("1:1.2.13-3","1:1.2.13-3.1"),OrderingComparison.lessThan(0));
        assertThat(c.compare("2.0.7pre1-4","2.0.7r-1"),OrderingComparison.lessThan(0));

        // Test some properties of text strings
        assertThat(c.compare("0-pre","0-pre"),OrderingComparison.comparesEqualTo(0));
        assertThat(c.compare("0-pre","0-pree"),OrderingComparison.lessThan(0));

        assertThat(c.compare("1.1.6r2-2","1.1.6r-1"),OrderingComparison.greaterThan(0));
        assertThat(c.compare("2.6b2-1","2.6b-2"),OrderingComparison.greaterThan(0));

        assertThat(c.compare("98.1p5-1","98.1-pre2-b6-2"),OrderingComparison.lessThan(0));
        assertThat(c.compare("0.4a6-2","0.4-1"),OrderingComparison.greaterThan(0));

        assertThat(c.compare("1:3.0.5-2","1:3.0.5.1"),OrderingComparison.lessThan(0));

        // //205960
        assertThat(c.compare("3.0~rc1-1","3.0-1"),OrderingComparison.lessThan(0));

        // //573592 - debian policy 5.6.12
        assertThat(c.compare("1.0","1.0-0"),OrderingComparison.comparesEqualTo(0));
        assertThat(c.compare("0.2","1.0-0"),OrderingComparison.lessThan(0));
        assertThat(c.compare("1.0","1.0-0+b1"),OrderingComparison.lessThan(0));
        assertThat(c.compare("1.0","1.0-0~"),OrderingComparison.greaterThan(0));

        // if a version includes a dash
        // it should be the debrev dash - policy says soâ€¦
        assertThat(c.compare("0:0-0-0","0-0"),OrderingComparison.greaterThan(0));

        // do we like strange versions? Yes we like strange versions¦
        assertThat(c.compare("0","0"),OrderingComparison.comparesEqualTo(0));
        assertThat(c.compare("0","00"),OrderingComparison.comparesEqualTo(0));

        // "steal" the testcases from cupt
        assertThat(c.compare("1.2.3","1.2.3"),OrderingComparison.comparesEqualTo(0));
        assertThat(c.compare("4.4.3-2","4.4.3-2"),OrderingComparison.comparesEqualTo(0));
        assertThat(c.compare("1:2ab:5","1:2ab:5"),OrderingComparison.comparesEqualTo(0));
        assertThat(c.compare("7:1-a:b-5","7:1-a:b-5"),OrderingComparison.comparesEqualTo(0));
        assertThat(c.compare("57:1.2.3abYZ+~-4-5","57:1.2.3abYZ+~-4-5"),OrderingComparison.comparesEqualTo(0));
        assertThat(c.compare("1.2.3","0:1.2.3"),OrderingComparison.comparesEqualTo(0));
        assertThat(c.compare("1.2.3","1.2.3-0"),OrderingComparison.comparesEqualTo(0));
        assertThat(c.compare("009","9"),OrderingComparison.comparesEqualTo(0));
        assertThat(c.compare("009ab5","9ab5"),OrderingComparison.comparesEqualTo(0));
        assertThat(c.compare("1.2.3","1.2.3-1"),OrderingComparison.lessThan(0));
        assertThat(c.compare("1.2.3","1.2.4"),OrderingComparison.lessThan(0));
        assertThat(c.compare("1.2.4","1.2.3"),OrderingComparison.greaterThan(0));
        assertThat(c.compare("1.2.24","1.2.3"),OrderingComparison.greaterThan(0));
        assertThat(c.compare("0.10.0","0.8.7"),OrderingComparison.greaterThan(0));
        assertThat(c.compare("3.2","2.3"),OrderingComparison.greaterThan(0));
        assertThat(c.compare("1.3.2a","1.3.2"),OrderingComparison.greaterThan(0));
        assertThat(c.compare("0.5.0~git","0.5.0~git2"),OrderingComparison.lessThan(0));
        assertThat(c.compare("2a","21"),OrderingComparison.lessThan(0));
        assertThat(c.compare("1.3.2a","1.3.2b"),OrderingComparison.lessThan(0));
        assertThat(c.compare("1:1.2.3","1.2.4"),OrderingComparison.greaterThan(0));
        assertThat(c.compare("1:1.2.3","1:1.2.4"),OrderingComparison.lessThan(0));
        assertThat(c.compare("1.2a+~bCd3","1.2a++"),OrderingComparison.lessThan(0));
        assertThat(c.compare("1.2a+~bCd3","1.2a+~"),OrderingComparison.greaterThan(0));
        assertThat(c.compare("5:2","304-2"),OrderingComparison.greaterThan(0));
        assertThat(c.compare("5:2","304:2"),OrderingComparison.lessThan(0));
        assertThat(c.compare("25:2","3:2"),OrderingComparison.greaterThan(0));
        assertThat(c.compare("1:2:123","1:12:3"),OrderingComparison.lessThan(0));
        assertThat(c.compare("1.2-5","1.2-3-5"),OrderingComparison.lessThan(0));
        assertThat(c.compare("5.10.0","5.005"),OrderingComparison.greaterThan(0));
        assertThat(c.compare("3a9.8","3.10.2"),OrderingComparison.lessThan(0));
        assertThat(c.compare("3a9.8","3~10"),OrderingComparison.greaterThan(0));
        assertThat(c.compare("1.4+OOo3.0.0~","1.4+OOo3.0.0-4"),OrderingComparison.lessThan(0));
        assertThat(c.compare("2.4.7-1","2.4.7-z"),OrderingComparison.lessThan(0));
        assertThat(c.compare("1.002-1+b2","1.00"),OrderingComparison.greaterThan(0));
        assertThat(c.compare("2.18-0ubuntu2","2.11.1-0ubuntu7.13"),OrderingComparison.greaterThan(0));
        assertThat(c.compare("2.11.1-0ubuntu7.13","2.11.1-0ubuntu7"),OrderingComparison.greaterThan(0));


    }

    @Test
    public void testRegex(){
        assertEquals("http://metaservice.org/d/packages/debian/libc6/2.13-20ubuntu5.2/amd64".matches(DebianVersionReasoner.URI_REGEX),true);
        assertEquals("http://metaservice.org/d/releases/debian/libc6/2.13-20ubuntu5.2".matches(DebianVersionReasoner.URI_REGEX),true);
        assertEquals("http://metaservice.org/d/projects/debian/libc6".matches(DebianVersionReasoner.URI_REGEX),false);
        assertEquals("http://metaservice.org/d/releases/debian/libc6/".matches(DebianVersionReasoner.URI_REGEX), false);
        assertEquals("http://metaservice.org/d/packages/debian/libc6/2.13-20ubuntu5.2#".matches(DebianVersionReasoner.URI_REGEX),false);

    }
}
