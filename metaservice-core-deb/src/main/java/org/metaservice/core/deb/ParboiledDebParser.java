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

import org.apache.commons.io.IOUtils;
import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.parser.Parser;
import org.metaservice.api.parser.ParserException;
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
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class ParboiledDebParser implements Parser<Package> {
    public static final Logger LOGGER = LoggerFactory.getLogger(ParboiledDebParser.class);

    @Override
    public List<Package> parse(Reader reader, ArchiveAddress archiveParameters) throws ParserException {
        String s ="";
        try{
            String distribution = archiveParameters.getPath().replaceFirst(archiveParameters.getParameters().get(DebianPackageProvider.PROPERTY_DISTRIBUTION_REGEX),"$1");
            archiveParameters.getParameters().put(DebianPackageProvider.PROPERTY_DISTRIBUTION,distribution);
            s = IOUtils.toString(reader);
            ArrayList<Package> result = new ArrayList<>();
            PackagesParser parser = Parboiled.createParser(PackagesParser.class);
            BasicParseRunner runner = new BasicParseRunner(parser.List());
            // retrieve version from archive
            ParsingResult<?> parsingResult = runner.run(s);
            for (SuperNode node : ((SuperNode) parsingResult.valueStack.pop()).getChildren()) {
                result.add((Package) node);
            }
            return result;
        }catch (Exception e){
            try {
                String errorFilename = "errorRefresh";
                FileWriter fileWriter = new FileWriter(errorFilename);
                //    fileWriter.write(archivesrc +"\n");
                fileWriter.write(e.toString() + "\n");
                fileWriter.write(s);
                fileWriter.close();
                LOGGER.error("Parsing failed, dumping to file " + errorFilename);
                throw new ParserException(e);
            } catch (IOException e1) {
                LOGGER.error("Error dumping");
                throw new ParserException(e1);
            }
        }
    }
}
