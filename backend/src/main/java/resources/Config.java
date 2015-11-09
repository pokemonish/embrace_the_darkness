package resources;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by fatman on 08/11/15.
 */
public class Config {

    public static final String SIGN_IN_URL = "sign_in_url";
    public static final String SIGN_UP_URL = "sign_up_url";
    public static final String SIGN_OUT_URL = "sign_out_url";
    public static final String ADMIN_URL = "admin_url";
    public static final String POST_NAME_URL = "post_name_url";
    public static final String PORT = "port";
    public static final String GAMEPLAY_URL = "gameplay_url";
    public static final String RESOURCE_BASE = "resource_base";

    private static final String CONFIG_FILE_PATH = "cfg/server.properties";

    private final Properties properties = new Properties();

    public Config() {

        try (final FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {

            properties.load(fis);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getParameter(String key) throws Exception{
        String parameter = properties.getProperty(key);
        if (parameter == null) throw new Exception("File " +
                CONFIG_FILE_PATH + " is missing required property: " + key);
        return parameter;
    }
}
