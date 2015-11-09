package resources;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import utils.ReflectionHelper;

/**
 * Created by fatman on 09/11/15.
 */
public class SaxHandlerParameter extends SaxHandler {

    private Object parameterForConstructor;
    private String classToPassParameterTo;

    SaxHandlerParameter(String clazz, Object parameter) {
        parameterForConstructor = parameter;
        classToPassParameterTo = clazz;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        if (!qName.equals(CLASS)) {
            element = qName;
        } else {
            String className = attributes.getValue(0);
            if (className.equals(classToPassParameterTo)) {
                object = ReflectionHelper.createInstanceWithParameters(className, parameterForConstructor);
            } else {
                object = ReflectionHelper.createInstance(className);
            }
        }
    }
}
