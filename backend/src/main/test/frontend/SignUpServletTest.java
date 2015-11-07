package frontend;

import main.AccountService;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by fatman on 02/11/15.
 */
public class SignUpServletTest extends AuthBasicTest {

    SignUpServlet signUpServlet = new SignUpServlet(mockedAccountService);

    @Before
    public void doBeforeTests() throws ServletException, IOException{

        when(mockedResponse.getWriter()).thenReturn(mockedWriter);
        when(mockedRequest.getReader()).thenReturn(mockedReader);
        when(mockedAccountService.addUser(eq(EMAIL_TEST), any())).thenReturn(true).thenReturn(false);

    }

    public void testDoPost(String message, long expectedStatus, int timesNumber)
            throws ServletException, IOException {
        when(mockedReader.readLine()).thenReturn(parametersJson.toString()).thenReturn(null);

        signUpServlet.doPost(mockedRequest, mockedResponse);
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
    public void testCorrectDataDoPost() throws ServletException, IOException {
        parametersJson.put("email", EMAIL_TEST);
        parametersJson.put("password", PASSWORD_TEST);
        testDoPost("New user created\n", HttpServletResponse.SC_OK, 1);

        verify(mockedAccountService, times(1)).addUser(eq(EMAIL_TEST), eq(TEST_USER_PROFILE));

    }

    @Test
    public void testDuplicateDataDoPost() throws ServletException, IOException {
        parametersJson.put("email", EMAIL_TEST);
        parametersJson.put("password", PASSWORD_TEST);
        testDoPost("New user created\n", HttpServletResponse.SC_OK, 1);
        testDoPost("User with name: " + EMAIL_TEST + " already exists", HttpServletResponse.SC_OK, 2);
        verify(mockedAccountService, times(2)).addUser(eq(EMAIL_TEST), eq(TEST_USER_PROFILE));
    }
}