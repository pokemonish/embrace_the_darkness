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

    private static final String CONFIG_FILE_PATH_DEFAULT = "cfg/server.properties";
    private static String s_filePath = CONFIG_FILE_PATH_DEFAULT;

    private final Properties properties = new Properties();

    public Config() {
        setUp();
    }

    public Config(String newfilePath) {
        s_filePath = newfilePath;
        setUp();
    }

    private void setUp() {
        try (final FileInputStream fis = new FileInputStream(s_filePath)) {

            properties.load(fis);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getParameter(String key) throws Exception{
        String parameter = properties.getProperty(key);
        if (parameter == null) throw new Exception("File " +
                CONFIG_FILE_PATH_DEFAULT + " is missing required property: " + key);
        return parameter;
    }
}
