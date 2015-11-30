package frontend;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by fatman on 09/11/15.
 */
public class AuthServiceImplTest {

    private static final String TEST_USER_NAME = "testUserName";
    private static final String TEST_SESSION_ID = "testSessionId";
    private final AuthServiceImpl authService = new AuthServiceImpl();

    @Test
    public void testSaveUserName() throws Exception {
        authService.saveUserName(TEST_SESSION_ID, TEST_USER_NAME);
        assertEquals(authService.getUserName(TEST_SESSION_ID), TEST_USER_NAME);
    }
}