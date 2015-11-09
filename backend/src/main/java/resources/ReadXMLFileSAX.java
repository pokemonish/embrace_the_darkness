package resources;

import org.jetbrains.annotations.Nullable;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

@SuppressWarnings({"unused"})
public class ReadXMLFileSAX {

    private static SaxHandler s_handler;

    @Nullable
    public static Object readXML(String xmlFile) {
        s_handler = new SaxHandler();
        return parseXML(xmlFile);
    }

    @Nullable
    public static Object readXML(String xmlFile, String className, Object parameter) {
        s_handler = new SaxHandlerParameter(className, parameter);
        return parseXML(xmlFile);
    }

    @Nullable
    private static Object parseXML(String xmlFile) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            saxParser.parse(xmlFile, s_handler);

            return s_handler.getObject();

        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
