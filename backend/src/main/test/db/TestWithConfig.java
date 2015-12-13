package db;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import resources.Config;

/**
 * Created by fatman on 12/12/15.
 */
public class TestWithConfig {
    private static final String TEST_FILE_PATH = "cfg/test.properties";

    @BeforeClass
    public static void setTestConfig() {
        Config.setConfigFilePath(TEST_FILE_PATH);
    }

    @AfterClass
    public static void setProductionConfig() {
        Config.setConfigFilePath(Config.CONFIG_FILE_PATH_DEFAULT);
    }
}
