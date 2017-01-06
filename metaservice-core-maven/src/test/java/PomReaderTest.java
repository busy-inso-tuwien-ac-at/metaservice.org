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

import org.junit.Test;
import org.metaservice.core.maven.MavenPomParser;

import java.io.StringReader;
import java.util.HashMap;

/**
 * Created by ilo on 08.01.14.
 */
public class PomReaderTest {

    @Test
    public void testParse(){
        new MavenPomParser().parse(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "        <groupId>org.metaservice</groupId>\n" +
                "    <artifactId>metaservice-core-maven</artifactId>\n" +
                "    <version>1.0</version>\n" +
                "</project>"),null);
    }

    @Test
    public void testParseParent(){
        new MavenPomParser().parse(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <parent>\n" +
                "        <artifactId>maven</artifactId>\n" +
                "        <groupId>org.apache.maven</groupId>\n" +
                "        <version>3.1.1</version>\n" +
                "        <relativePath>../metaservice-parent/pom.xml</relativePath>\n" +
                "    </parent>\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "        <groupId>org.metaservice</groupId>\n" +
                "    <artifactId>metaservice-core-maven</artifactId>\n" +
                "    <version>1.0</version>\n" +
                "</project>"), null);
    }
}
