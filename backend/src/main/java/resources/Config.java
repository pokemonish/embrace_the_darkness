package resources;

import org.jetbrains.annotations.TestOnly;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by fatman on 08/11/15.
 */
public final class Config {

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
    private static final String DO_CREATE_DATABASE = "do_create_db";
    private static final String DO_DELETE_DATABASE = "do_delete_db";

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
    private final boolean doCreateDB;
    private final boolean doDeleteDB;

    public boolean isDoCreateDB() {
        return doCreateDB;
    }

    public boolean isDoDeleteDB() {
        return doDeleteDB;
    }

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

    public static final String CONFIG_FILE_PATH_DEFAULT = "cfg/server.properties";
    private static String s_filePath = CONFIG_FILE_PATH_DEFAULT;

    private final Properties properties = new Properties();

    @TestOnly
    public static void setConfigFilePath(String filePath) {
        s_filePath = filePath;
        try {
            s_config = new Config();
        } catch (ConfigException e) {
            System.out.print(e.getMessage());
            System.exit(1);
        }
    }

    private static Config s_config = null;

    public static Config getInstance() {
        if (s_config == null) {
            try {
                s_config = new Config();
            } catch (ConfigException e) {
                System.out.print(e.getMessage());
                System.exit(1);
            }
        }
        return s_config;
    }

    private Config() throws ConfigException {

        this(s_filePath);
    }


    private Config(String newfilePath) throws ConfigException {
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
        doCreateDB = Boolean.valueOf(getParameter(DO_CREATE_DATABASE));
        doDeleteDB = Boolean.valueOf(getParameter(DO_DELETE_DATABASE));
    }

    private void setUp()  throws ConfigException {
        try (final FileInputStream fis = new FileInputStream(s_filePath)) {

            properties.load(fis);
        } catch (IOException ignore) {
            ConfigException ce =
                    new ConfigException("Exception reading config file " +
                    s_filePath);

            ce.setStackTrace(ignore.getStackTrace());
            throw ce;
        }
    }

    private String getParameter(String key) throws ConfigException {
        String parameter = properties.getProperty(key);
        if (parameter == null) throw new ConfigException("File " +
                CONFIG_FILE_PATH_DEFAULT + " is missing required property: " + key);
        return parameter;
    }
}
