package resources;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by fatman on 09/11/15.
 */
public class ConfigTest {

    private static final int TEST_VALUE_1 = 8080;
    private static final String TEST_VALUE_2 = "/api/v1/auth/signin";

    private static final String TEST_FILE_PATH = "cfg/test_server.properties";

    private final Config config;

    public ConfigTest() throws Exception, NumberFormatException {
        config = new Config(TEST_FILE_PATH);
    }

    @Test
    public void testGetParameter() throws Exception {
        assertEquals(config.getPort(), TEST_VALUE_1);
        assertEquals(config.getSignInUrl(), TEST_VALUE_2);
    }
}