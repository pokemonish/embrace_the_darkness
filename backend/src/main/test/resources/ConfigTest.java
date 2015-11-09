package resources;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by fatman on 09/11/15.
 */
public class ConfigTest {

    private static final String TEST_VALUE_KEY_1 = "test_value";
    private static final String TEST_VALUE_KEY_2 = "test_value_2";

    private static final String TEST_VALUE_1 = "angelBlueWithTeenageTraces";
    private static final String TEST_VALUE_2 = "angelBlueWithPrettyFaces";

    private static final String TEST_FILE_PATH = "cfg/test.properties";

    private final Config config = new Config(TEST_FILE_PATH);

    @Test
    public void testGetParameter() throws Exception {
        assertEquals(config.getParameter(TEST_VALUE_KEY_1), TEST_VALUE_1);
        assertEquals(config.getParameter(TEST_VALUE_KEY_2), TEST_VALUE_2);
    }
}