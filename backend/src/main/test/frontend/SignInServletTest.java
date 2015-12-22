package frontend;

import accountservice.AccountServiceException;
import accountservice.Statuses;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by fatman on 04/11/15.
 */

public class SignInServletTest extends AuthBasicTest {

    SignInServlet signInServlet = new SignInServlet(mockedFrontEnd);

    @Before
    public void doBeforeTests() throws ServletException, IOException, AccountServiceException {
        when(mockedResponse.getWriter()).thenReturn(mockedWriter);
        when(mockedRequest.getReader()).thenReturn(mockedReader);
        when(mockedRequest.getSession()).thenReturn(mockedSession);
        when(mockedSession.getAttribute("userId")).thenReturn(TEST_USER_ID);
        when(mockedFrontEnd.authenticate(any(), any())).thenReturn(String.valueOf(TEST_USER_ID));
    }

    public void testDoPost(String message, long expectedStatus, int timesNumber)
            throws ServletException, IOException {
        when(mockedReader.readLine()).thenReturn(parametersJson.toString()).thenReturn(null);

        signInServlet.doPost(mockedRequest, mockedResponse);
        testDoPostAfter(message, expectedStatus, timesNumber);
    }

    @Test
    public void testInvalidJson() throws IOException, ServletException {
        String invalidJsonString = "This string is not Json at all!!!111";

        when(mockedReader.readLine()).thenReturn(invalidJsonString).thenReturn(null);

        signInServlet.doPost(mockedRequest, mockedResponse);
        testDoPostAfter("Request is invalid. Can't parse json.",
                HttpServletResponse.SC_BAD_REQUEST, 1);
    }

    @Test
    public void testNoDataDoPost() throws ServletException, IOException {
        testDoPost("login is required", HttpServletResponse.SC_OK, 1);
    }

    @Test
    public void testNoLoginDoPost() throws ServletException, IOException {
        parametersJson.addProperty("password", PASSWORD_TEST);
        testDoPost("login is required", HttpServletResponse.SC_OK, 1);
    }

    @Test
    public void testNoPasswordDoPost() throws ServletException, IOException {
        parametersJson.addProperty("email", EMAIL_TEST);
        testDoPost("password is required", HttpServletResponse.SC_OK, 1);
    }

    @Test
    public void testWrongPasswordDoPost() throws ServletException, IOException, AccountServiceException {
        when(mockedFrontEnd.getAuthStatus(eq(String.valueOf(TEST_USER_ID))))
                .thenReturn(Statuses.AuthorizationStates.WRONG_AUTHORIZATION_DATA);

        parametersJson.addProperty("email", EMAIL_TEST);
        final String WRONG_PASSWORD = "wrongPassword";
        parametersJson.addProperty("password", WRONG_PASSWORD);

        testDoPost("Wrong login/password", HttpServletResponse.SC_OK, 1);
    }

    @Test
    public void testProcessingRequest() throws ServletException, IOException {
        when(mockedFrontEnd.getAuthStatus(eq(String.valueOf(TEST_USER_ID))))
                .thenReturn(Statuses.AuthorizationStates.WAITING_FOR_AUTHORIZATION);

        parametersJson.addProperty("email", EMAIL_TEST);
        parametersJson.addProperty("password", PASSWORD_TEST);

        testDoPost("Your request for authorization is processing, please, wait.", HttpServletResponse.SC_OK, 1);
    }

    /*@Test
    public void testTooLongRequest() throws ServletException, IOException {
        when(mockedFrontEnd.getAuthStatus(any())).thenReturn(null).thenReturn(null)
                .thenReturn(Statuses.AuthorizationStates.WAITING_FOR_AUTHORIZATION);

        parametersJson.addProperty("email", EMAIL_TEST);
        parametersJson.addProperty("password", PASSWORD_TEST);

        testDoPost("Request took too long", HttpServletResponse.SC_OK, 1);
    }*/

    @Test
    public void testWrongLoginDoPost() throws ServletException, IOException, AccountServiceException {
        when(mockedFrontEnd.getAuthStatus(eq(String.valueOf(TEST_USER_ID))))
                .thenReturn(Statuses.AuthorizationStates.WRONG_AUTHORIZATION_DATA);

        parametersJson.addProperty("email", EMAIL_TEST);
        parametersJson.addProperty("password", PASSWORD_TEST);

        testDoPost("Wrong login/password", HttpServletResponse.SC_OK, 1);
    }

    @Test
    public void testRightDataDoPost() throws ServletException, IOException, AccountServiceException {
        when(mockedFrontEnd.getAuthStatus(eq(String.valueOf(TEST_USER_ID))))
                .thenReturn(null).thenReturn(null).thenReturn(Statuses.AuthorizationStates.AUTHORIZED);

        parametersJson.addProperty("email", EMAIL_TEST);
        parametersJson.addProperty("password", PASSWORD_TEST);

        testDoPost("Login passed", HttpServletResponse.SC_OK, 1);
    }

    @Test
    public void testDuplicateDoPost() throws ServletException, IOException {
        when(mockedFrontEnd.getAuthStatus(eq(String.valueOf(TEST_USER_ID))))
                .thenReturn(Statuses.AuthorizationStates.AUTHORIZED);

        parametersJson.addProperty("email", EMAIL_TEST);
        parametersJson.addProperty("password", PASSWORD_TEST);

        testDoPost("You are already logged in", HttpServletResponse.SC_OK, 1);
    }

}