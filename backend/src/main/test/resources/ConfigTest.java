package resources;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by fatman on 09/11/15.
 */
public class ConfigTest {

    private static final String TEST_VALUE_1 = "angelBlueWithTeenageTraces";
    private static final String TEST_VALUE_2 = "angelBlueWithPrettyFaces";

    private static final String TEST_FILE_PATH = "cfg/test.properties";


    @Test
    public void testGetParameter() throws Exception {
        Config.setConfigFilePath(TEST_FILE_PATH);
        assertEquals(Config.getInstance().getGameplayUrl(), TEST_VALUE_1);
        assertEquals(Config.getInstance().getSignInUrl(), TEST_VALUE_2);
    }

    @After
    public void cleanUp() {
        Config.setConfigFilePath(Config.CONFIG_FILE_PATH_DEFAULT);
    }
}