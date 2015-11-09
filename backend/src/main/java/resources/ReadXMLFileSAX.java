package resources;

import org.jetbrains.annotations.Nullable;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

public class ReadXMLFileSAX {

    private static SaxHandler handler;

    @Nullable
    public static Object readXML(String xmlFile) {
        handler = new SaxHandler();
        return parseXML(xmlFile);
    }

    @Nullable
    public static Object readXML(String xmlFile, String className, Object parameter) {
        handler = new SaxHandlerParameter(className, parameter);
        return parseXML(xmlFile);
    }

    private static Object parseXML(String xmlFile) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            saxParser.parse(xmlFile, handler);

            return handler.getObject();

        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
