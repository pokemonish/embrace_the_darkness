package resources;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by fatman on 08/11/15.
 */
public class Config {

    private static final String SIGN_IN_URL = "sign_in_url";
    private static final String SIGN_UP_URL = "sign_up_url";
    private static final String SIGN_OUT_URL = "sign_out_url";
    private static final String ADMIN_URL = "admin_url";
    private static final String POST_NAME_URL = "post_name_url";
    private static final String PORT = "port";
    private static final String GAMEPLAY_URL = "gameplay_url";
    private static final String RESOURCE_BASE = "resource_base";

    private final int port;
    private final String signInUrl;
    private final String signUpUrl;
    private final String signOutUrl;
    private final String adminUrl;
    private final String gameplayUrl;
    private final String resourceBase;
    private final String postNameUrl;

    public int getPort() {
        return port;
    }

    public String getSignInUrl() {
        return signInUrl;
    }

    public String getSignUpUrl() {
        return signUpUrl;
    }

    public String getSignOutUrl() {
        return signOutUrl;
    }

    public String getAdminUrl() {
        return adminUrl;
    }

    public String getGameplayUrl() {
        return gameplayUrl;
    }

    public String getResourceBase() {
        return resourceBase;
    }

    public String getPostNameUrl() {
        return postNameUrl;
    }

    private static final String CONFIG_FILE_PATH_DEFAULT = "cfg/server.properties";
    private static String s_filePath = CONFIG_FILE_PATH_DEFAULT;

    private final Properties properties = new Properties();

    public Config() throws Exception, NumberFormatException {

        setUp();

        port = Integer.valueOf(getParameter(PORT));
        signInUrl = getParameter(SIGN_IN_URL);
        signUpUrl = getParameter(SIGN_UP_URL);
        signOutUrl = getParameter(SIGN_OUT_URL);
        adminUrl = getParameter(ADMIN_URL);
        gameplayUrl = getParameter(GAMEPLAY_URL);
        resourceBase = getParameter(RESOURCE_BASE);
        postNameUrl = getParameter(POST_NAME_URL);
    }

    public Config(String newfilePath) throws Exception, NumberFormatException {
        s_filePath = newfilePath;
        setUp();

        port = Integer.valueOf(properties.getProperty(PORT));
        signInUrl = getParameter(SIGN_IN_URL);
        signUpUrl = getParameter(SIGN_UP_URL);
        signOutUrl = getParameter(SIGN_OUT_URL);
        adminUrl = getParameter(ADMIN_URL);
        gameplayUrl = getParameter(GAMEPLAY_URL);
        resourceBase = getParameter(RESOURCE_BASE);
        postNameUrl = getParameter(POST_NAME_URL);
    }

    private void setUp() {
        try (final FileInputStream fis = new FileInputStream(s_filePath)) {

            properties.load(fis);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getParameter(String key) throws Exception {
        String parameter = properties.getProperty(key);
        if (parameter == null) throw new Exception("File " +
                CONFIG_FILE_PATH_DEFAULT + " is missing required property: " + key);
        return parameter;
    }
}
