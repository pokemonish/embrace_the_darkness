package frontend;

import main.AccountService;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by fatman on 05/11/15.
 */
public class SignOutServletTest extends AuthBasicTest {

    private SignOutServlet signOutServlet = new SignOutServlet(mockedAccountService);

    @Before
    public void doBeforeTests() throws ServletException, IOException  {
        when(mockedResponse.getWriter()).thenReturn(mockedWriter);
        when(mockedRequest.getReader()).thenReturn(mockedReader);
        when(mockedRequest.getSession()).thenReturn(mockedSession);
        when(mockedSession.getId()).thenReturn(TEST_SESSION_ID);
    }

    @Test
    public void testAlreadyLoggedOutDoPost() throws ServletException, IOException {
        when(mockedAccountService.getSessions(eq(TEST_SESSION_ID))).thenReturn(null);
        signOutServlet.doPost(mockedRequest, mockedResponse);
        testDoPostAfter("You are alredy signed out", HttpServletResponse.SC_OK, 1);
    }

    @Test
    public void testCorrectDoPost() throws ServletException, IOException {
        when(mockedAccountService.getSessions(eq(TEST_SESSION_ID))).thenReturn(TEST_USER_PROFILE);
        signOutServlet.doPost(mockedRequest, mockedResponse);
        testDoPostAfter("Signed out successfully!\nSee you soon!", HttpServletResponse.SC_OK, 1);
    }
}