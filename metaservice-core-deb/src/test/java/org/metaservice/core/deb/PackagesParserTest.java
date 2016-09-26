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

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.metaservice.core.deb.parser.PackagesParser;
import org.metaservice.core.utils.MetaserviceHttpClient;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.parboiled.Parboiled;
import org.parboiled.errors.ErrorUtils;
import org.parboiled.errors.ParseError;
import org.parboiled.parserunners.BasicParseRunner;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.parserunners.TracingParseRunner;
import org.parboiled.support.ParseTreeUtils;
import org.parboiled.support.ParsingResult;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: ilo
 * Date: 16.10.13
 * Time: 18:47
 * To change this template use File | Settings | File Templates.
 */
public class PackagesParserTest {
    @NotNull
    MetaserviceHttpClient clientMetaservice = new MetaserviceHttpClient();
    PackagesParser parser = Parboiled.createParser(PackagesParser.class);
    @Test
    public void checkLocalFileSplit() throws IOException {
        String input = IOUtils.toString(new FileReader("crawlertest/error12933"));
        String[] splitInput = input.split("\nPackage:");
        boolean first = true;
        for(String s : splitInput){
            if(!first){
                s =  "Package:" + s;
            }else{
                first = false;
            }
            checkString(s);
        }
    }

    @Test
    public void checkLocalFile() throws IOException {
        String input = IOUtils.toString(new FileReader("crawlertest/error12933"));
        checkString(input);
    }

    void checkString(String s){
        try{
            BasicParseRunner runner = new BasicParseRunner(parser.List());
            ParsingResult<?> result =  runner.run(s);
            //   String parseTreePrintOut = ParseTreeUtils.printNodeTree(result);
            Object o =  result.valueStack.pop();
            // System.err.println(o);

            // System.out.println(parseTreePrintOut);
            for(ParseError e : result.parseErrors){
                ErrorUtils.printParseError(e);
            }
        }catch (RuntimeException e){
            System.err.println(s);
            throw e;
        }
    }





    @Test
    public void checkSome() throws IOException {
        byte[] packageFile = clientMetaservice.getBinary("http://snapshot.debian.org/archive/debian/20080507T000000Z/dists/lenny/main/binary-i386/Packages.gz");
        InputStream fileStream = new ByteArrayInputStream(packageFile);
        InputStream             gzipStream = new GZIPInputStream(fileStream);
        StringWriter writer = new StringWriter();
        IOUtils.copy(gzipStream, writer, "utf-8");
        String input = writer.toString();
        String[] splitInput = input.split("\nPackage:");

        boolean first = true;
        for(String s : splitInput){
            if(!first){
                s =  "Package:" + s;
            }else{
                first = false;
            }
            System.err.println(s);
            ReportingParseRunner runner = new ReportingParseRunner(parser.List());
            ParsingResult<?> result =  runner.run(s);
            String parseTreePrintOut = ParseTreeUtils.printNodeTree(result);
            System.out.println((result.valueStack.pop()));

            // System.out.println(parseTreePrintOut);
            for(ParseError e : result.parseErrors){
                ErrorUtils.printParseError(e);
            }

        }

    }
    @Test
    public void dependencytest() throws IOException {
        String input = "Package: wordpress\n"+
                "Priority: optional\n"+
                "Section: universe/web\n"+
                "Installed-Size: 8796\n"+
                "Maintainer: Ubuntu Developers <ubuntu-devel-discuss@lists.ubuntu.com>\n"+
                "Original-Maintainer: Giuseppe Iuculano <iuculano@debian.org>\n"+
                "Architecture: all\n"+
                "Version: 2.9.2-1ubuntu1.2\n"+
                "Depends: apache2 | httpd, mysql-client, libapache2-mod-php5 | php5, php5-mysql, libphp-phpmailer (>= 1.73-4), php5-gd, libjs-prototype, libjs-scriptaculous, tinymce (>= 3.2.6-0.1), libphp-snoopy, libjs-jquery (>= 1.3.3-1), php-gettext, libjs-cropper\n"+
                "Recommends: wordpress-l10n\n"+
                "Suggests: mysql-server (>> 4.0.20-8)\n"+
                "Filename: pool/universe/w/wordpress/wordpress_2.9.2-1ubuntu1.2_all.deb\n"+
                "Size: 2017410\n"+
                "MD5sum: 339e8845494d62265ef2b4c3f6d48aea\n"+
                "SHA1: 67826fe4241fdf011f42158bdd565e8cf2694f69\n"+
                "SHA256: b32f503042c90917c5d012e4b3873923673ad9f14bbc20e432a1184748bd1b86\n"+
                "Description: weblog manager\n"+
                " WordPress is a full featured web blogging tool:\n"+
                "    * Instant publishing (no rebuilding)\n"+
                "    * Comment pingback support with spam protection\n"+
                "    * Non-crufty URLs\n"+
                "    * Themable\n"+
                "    * Plugin support\n"+
                "Homepage: http://wordpress.org\n"+
                "Bugs: https://bugs.launchpad.net/ubuntu/+filebug\n"+
                "Origin: Ubuntu\n";
        ReportingParseRunner runner = new ReportingParseRunner(parser.List());
        ParsingResult<?> result =  runner.run(input);
        String parseTreePrintOut = ParseTreeUtils.printNodeTree(result);
        System.err.println((result.valueStack.pop()));

        System.err.println(ValueFactoryImpl.getInstance().createURI("http://"));
        // System.out.println(parseTreePrintOut);
        for(ParseError e : result.parseErrors){
            ErrorUtils.printParseError(e);
        }
    }


    @Test
    public  void runTrace(){
        String x = "Package: php-htmlpurifier\n" +
                "Description: Standards-compliant HTML filter\n" +
                "     are standards compliant.\n" +
                " .";
        ReportingParseRunner runner = new TracingParseRunner(parser.List());
        ParsingResult<?> result =  runner.run(x);
        String parseTreePrintOut = ParseTreeUtils.printNodeTree(result);
        System.out.println(parseTreePrintOut);
        System.out.println((result.valueStack.pop()));

    }
}
