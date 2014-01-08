import org.junit.Test;
import org.metaservice.core.maven.MavenPomParser;

/**
 * Created by ilo on 08.01.14.
 */
public class PomReaderTest {

    @Test
    public void testParse(){
        new MavenPomParser().parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "        <groupId>org.metaservice</groupId>\n" +
                "    <artifactId>metaservice-core-maven</artifactId>\n" +
                "    <version>1.0</version>\n" +
                "</project>");
    }

    @Test
    public void testParseParent(){
        new MavenPomParser().parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
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
                "</project>");
    }
}
