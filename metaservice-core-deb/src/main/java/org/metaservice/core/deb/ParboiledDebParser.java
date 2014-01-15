package org.metaservice.core.deb;

import org.metaservice.api.parser.Parser;
import org.metaservice.core.deb.parser.PackagesParser;
import org.metaservice.core.deb.parser.ast.Package;
import org.metaservice.core.deb.parser.ast.SuperNode;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.BasicParseRunner;
import org.parboiled.support.ParsingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParboiledDebParser implements Parser<Package> {
    public static final Logger LOGGER = LoggerFactory.getLogger(ParboiledDebParser.class);

    @Override
    public List<Package> parse(String s) {
        ArrayList<Package> result = new ArrayList<>();
        PackagesParser parser = Parboiled.createParser(PackagesParser.class);
        BasicParseRunner runner = new BasicParseRunner(parser.List());
        try{
            // retrieve version from archive
            ParsingResult<?> parsingResult = runner.run(s);
            for (SuperNode node : ((SuperNode) parsingResult.valueStack.pop()).getChildren()) {
                result.add((Package) node);
            }
        }catch (Exception e){
            try {
                String errorFilename = "errorRefresh";
                FileWriter fileWriter = new FileWriter(errorFilename);
                //    fileWriter.write(archivesrc +"\n");
                fileWriter.write(e.toString() + "\n");
                fileWriter.write(s);
                fileWriter.close();
                LOGGER.error("Parsing failed, dumping to file " + errorFilename,e);
            } catch (IOException e1) {
                LOGGER.error("Error dumping",e1);
            }
        }
        return result;
    }
}