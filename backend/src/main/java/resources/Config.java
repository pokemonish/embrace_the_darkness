package resources;

import utils.ParametersConsumer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by fatman on 08/11/15.
 */
public class Config {

    private Map<String, String> parameters = new HashMap<>();

    private static final String CONFIG_FILE_PATH = "cfg/server.properties";

    public Config() throws Exception {


        try (final FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {

            final Properties properties = new Properties();
            properties.load(fis);

            ParametersConsumer parametersConsumer = new ParametersConsumer();
            properties.forEach(parametersConsumer);

            parameters = parametersConsumer.getParameters();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getParameter(String key) throws Exception{
        String parameter = parameters.get(key);
        if (parameter == null) throw new Exception("File " +
                CONFIG_FILE_PATH + " is missing required parameters");
        return parameter;
    }
}
