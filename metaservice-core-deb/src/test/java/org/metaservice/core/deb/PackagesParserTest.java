package org.metaservice.core.deb;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.metaservice.core.deb.parser.PackagesParser;
import org.metaservice.core.utils.MetaserviceHttpClient;
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
        String input = readFile("crawlertest/error12933");
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
        String input = readFile("crawlertest/error12933");
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

    String readFile(String file) throws IOException {
        FileReader fileInputStream = new FileReader(file);
        StringWriter stringWriter = new StringWriter();
        IOUtils.copy(fileInputStream,stringWriter);
        return stringWriter.getBuffer().toString();
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
