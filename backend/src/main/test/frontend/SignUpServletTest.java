package frontend;

import accountservice.AccountServiceException;
import accountservice.Statuses;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Created by fatman on 02/11/15.
 */

public class SignUpServletTest extends AuthBasicTest {

    SignUpServlet signUpServlet = new SignUpServlet(mockedFrontEnd);

    @Before
    public void doBeforeTests() throws ServletException, IOException, AccountServiceException {
        when(mockedResponse.getWriter()).thenReturn(mockedWriter);
        when(mockedRequest.getReader()).thenReturn(mockedReader);
    }

    public void testDoPost(String message, long expectedStatus, int timesNumber)
            throws ServletException, IOException {
        when(mockedReader.readLine()).thenReturn(parametersJson.toString()).thenReturn(null);

        signUpServlet.doPost(mockedRequest, mockedResponse);
        testDoPostAfter(message, expectedStatus, timesNumber);
    }

    @Test
    public void testInvalidJson() throws IOException, ServletException {
        String invalidJsonString = "This string is not Json at all!!!111";

        when(mockedReader.readLine()).thenReturn(invalidJsonString).thenReturn(null);

        signUpServlet.doPost(mockedRequest, mockedResponse);
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
    public void testCorrectDataDoPost() throws ServletException, IOException, AccountServiceException {
        when(mockedFrontEnd.getRegistrationResult(TEST_USER_PROFILE.getLogin()))
                .thenReturn(Statuses.SignUpStatuses.SUCCESS);

        parametersJson.addProperty("email", EMAIL_TEST);
        parametersJson.addProperty("password", PASSWORD_TEST);
        testDoPost("New user created", HttpServletResponse.SC_OK, 1);
    }

    @Test
    public void testDuplicateDataDoPost() throws ServletException, IOException, AccountServiceException {
        when(mockedFrontEnd.getRegistrationResult(TEST_USER_PROFILE.getLogin()))
                .thenReturn(Statuses.SignUpStatuses.SUCCESS)
                .thenReturn(Statuses.SignUpStatuses.USER_ALREADY_EXISTS);

        parametersJson.addProperty("email", EMAIL_TEST);
        parametersJson.addProperty("password", PASSWORD_TEST);
        testDoPost("New user created", HttpServletResponse.SC_OK, 1);
        testDoPost("User with name: " + EMAIL_TEST + " already exists", HttpServletResponse.SC_OK, 2);
    }
}