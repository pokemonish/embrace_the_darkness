package frontend;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by fatman on 04/11/15.
 */
public class SignInServletTest extends AuthBasicTest {

    SignInServlet signInServlet = new SignInServlet(mockedAccountService);

    @Before
    public void doBeforeTests() throws ServletException, IOException {
        when(mockedResponse.getWriter()).thenReturn(mockedWriter);
        when(mockedRequest.getReader()).thenReturn(mockedReader);
        when(mockedAccountService.addUser(eq(EMAIL_TEST), any())).thenReturn(true).thenReturn(false);
        when(mockedRequest.getSession()).thenReturn(mockedSession);
        when(mockedSession.getId()).thenReturn(TEST_SESSION_ID);

    }

    public void testDoPost(String message, long expectedStatus, int timesNumber)
            throws ServletException, IOException {
        when(mockedReader.readLine()).thenReturn(parametersJson.toString()).thenReturn(null);

        signInServlet.doPost(mockedRequest, mockedResponse);
        testDoPostAfter(message, expectedStatus, timesNumber);
    }

    @Test
    public void testNoDataDoPost() throws ServletException, IOException {
        testDoPost("login is required", HttpServletResponse.SC_OK, 1);
    }

    @Test
    public void testNoLoginDoPost() throws ServletException, IOException {
        parametersJson.put("password", PASSWORD_TEST);
        testDoPost("login is required", HttpServletResponse.SC_OK, 1);
    }

    @Test
    public void testNoPasswordDoPost() throws ServletException, IOException {
        parametersJson.put("email", EMAIL_TEST);
        testDoPost("password is required", HttpServletResponse.SC_OK, 1);
    }

    @Test
    public void testWrongPasswordDoPost() throws ServletException, IOException {
        when(mockedAccountService.getUser(eq(EMAIL_TEST))).thenReturn(TEST_USER_PROFILE);

        parametersJson.put("email", EMAIL_TEST);
        final String WRONG_PASSWORD = "wrongPassword";
        parametersJson.put("password", WRONG_PASSWORD);

        testDoPost("Wrong login/password", HttpServletResponse.SC_OK, 1);
    }

    @Test
    public void testWrongLoginDoPost() throws ServletException, IOException {
        when(mockedAccountService.getUser(eq(EMAIL_TEST))).thenReturn(null);

        parametersJson.put("email", EMAIL_TEST);
        parametersJson.put("password", PASSWORD_TEST);

        testDoPost("Wrong login/password", HttpServletResponse.SC_OK, 1);
    }

    @Test
    public void testRightDataDoPost() throws ServletException, IOException {
        when(mockedAccountService.getUser(eq(EMAIL_TEST))).thenReturn(TEST_USER_PROFILE);

        parametersJson.put("email", EMAIL_TEST);
        parametersJson.put("password", PASSWORD_TEST);

        testDoPost("Login passed", HttpServletResponse.SC_OK, 1);

        verify(mockedAccountService, times(1)).addSessions(eq(TEST_SESSION_ID), eq(TEST_USER_PROFILE));
    }

    @Test
    public void testDuplicateDoPost() throws ServletException, IOException {
        when(mockedAccountService.getSessions(TEST_SESSION_ID)).thenReturn(TEST_USER_PROFILE);

        parametersJson.put("email", EMAIL_TEST);
        parametersJson.put("password", PASSWORD_TEST);

        testDoPost("You are alredy logged in", HttpServletResponse.SC_OK, 1);
    }

}