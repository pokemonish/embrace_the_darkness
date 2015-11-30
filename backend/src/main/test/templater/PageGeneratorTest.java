package templater;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

/**
 * Created by fatman on 06/11/15.
 */
public class PageGeneratorTest {

    private static final String TEST_HTML = "test.html";
    private static final String TEST_FIELD_NAME = "testField";
    private static final String TEST_FIELD_VALUE = "green unicorn";
    private final HashMap<String, Object> pageTestVariables = new HashMap<>();

    @Before
    public void setUp() throws Exception {
        pageTestVariables.put(TEST_FIELD_NAME, TEST_FIELD_VALUE);
    }

    @Test
    public void testGetPage() throws Exception {
        String resultHtml = PageGenerator.getPage(TEST_HTML, pageTestVariables);
        assert(resultHtml.contains(TEST_FIELD_VALUE));
    }
}