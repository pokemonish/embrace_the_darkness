package resources;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by fatman on 08/11/15.
 */
public class Config {

    private static final String HOST = "host";
    private static final String SIGN_IN_URL = "sign_in_url";
    private static final String SIGN_UP_URL = "sign_up_url";
    private static final String SIGN_OUT_URL = "sign_out_url";
    private static final String ADMIN_URL = "admin_url";
    private static final String POST_NAME_URL = "post_name_url";
    private static final String PORT = "port";
    private static final String GAMEPLAY_URL = "gameplay_url";
    private static final String RESOURCE_BASE = "resource_base";
    private static final String DB_TYPE = "db_type";
    private static final String DB_PORT = "db_port";
    private static final String DB_NAME = "db_name";
    private static final String DB_USER = "db_user";
    private static final String DB_PASSWORD = "db_password";
    private static final String DB_DRIVER = "db_driver";

    private final String host;
    private final int port;
    private final String signInUrl;
    private final String signUpUrl;
    private final String signOutUrl;
    private final String adminUrl;
    private final String gameplayUrl;
    private final String resourceBase;
    private final String postNameUrl;
    private final String dbType;
    private final String dbPort;
    private final String dbName;
    private final String dbUser;
    private final String dbPassword;
    private final String dbDriver;


    public String getHost() {
        return host;
    }

    public String getDbType() {
        return dbType;
    }

    public String getDbPort() {
        return dbPort;
    }

    public String getDbName() {
        return dbName;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getDbDriver() {
        return dbDriver;
    }

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

    public Config() throws MissingParametersException, NumberFormatException, IOException {

        setUp();

        host = getParameter(HOST);
        port = Integer.valueOf(getParameter(PORT));
        signInUrl = getParameter(SIGN_IN_URL);
        signUpUrl = getParameter(SIGN_UP_URL);
        signOutUrl = getParameter(SIGN_OUT_URL);
        adminUrl = getParameter(ADMIN_URL);
        gameplayUrl = getParameter(GAMEPLAY_URL);
        resourceBase = getParameter(RESOURCE_BASE);
        postNameUrl = getParameter(POST_NAME_URL);
        dbType = getParameter(DB_TYPE);
        dbPort = getParameter(DB_PORT);
        dbName = getParameter(DB_NAME);
        dbUser = getParameter(DB_USER);
        dbPassword = getParameter(DB_PASSWORD);
        dbDriver = getParameter(DB_DRIVER);
    }

    public Config(String newfilePath) throws MissingParametersException, NumberFormatException, IOException {
        s_filePath = newfilePath;
        setUp();

        host = getParameter(HOST);
        port = Integer.valueOf(properties.getProperty(PORT));
        signInUrl = getParameter(SIGN_IN_URL);
        signUpUrl = getParameter(SIGN_UP_URL);
        signOutUrl = getParameter(SIGN_OUT_URL);
        adminUrl = getParameter(ADMIN_URL);
        gameplayUrl = getParameter(GAMEPLAY_URL);
        resourceBase = getParameter(RESOURCE_BASE);
        postNameUrl = getParameter(POST_NAME_URL);
        dbType = getParameter(DB_TYPE);
        dbPort = getParameter(DB_PORT);
        dbName = getParameter(DB_NAME);
        dbUser = getParameter(DB_USER);
        dbPassword = getParameter(DB_PASSWORD);
        dbDriver = getParameter(DB_DRIVER);
    }

    private void setUp() throws IOException {
        try (final FileInputStream fis = new FileInputStream(s_filePath)) {

            properties.load(fis);
        }
    }

    private String getParameter(String key) throws MissingParametersException {
        String parameter = properties.getProperty(key);
        if (parameter == null) throw new MissingParametersException("File " +
                CONFIG_FILE_PATH_DEFAULT + " is missing required property: " + key);
        return parameter;
    }

    private static final class MissingParametersException extends Exception {
        private MissingParametersException(String message) {
            super(message);
        }
    }
}
