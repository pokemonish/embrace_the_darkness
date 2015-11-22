package frontend;

import base.UserProfile;
import com.google.gson.JsonObject;
import main.AccountService;
import org.jetbrains.annotations.NotNull;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by fatman on 04/11/15.
 */

public class AuthBasicTest extends Mockito {

    @NotNull
    protected PrintWriter mockedWriter = mock(PrintWriter.class);
    @NotNull
    protected HttpServletResponse mockedResponse = mock(HttpServletResponse.class);
    @NotNull
    protected HttpServletRequest mockedRequest = mock(HttpServletRequest.class);

    protected ArgumentCaptor<JsonObject> jsonObjectCaptor = ArgumentCaptor.forClass(JsonObject.class);
    protected ArgumentCaptor<Integer> integerCaptor = ArgumentCaptor.forClass(Integer.class);

    protected static final String EMAIL_TEST = "testEmail";
    protected static final String PASSWORD_TEST = "testPassword";
    protected static final UserProfile TEST_USER_PROFILE = new UserProfile(EMAIL_TEST, PASSWORD_TEST, "");


    @NotNull
    protected BufferedReader mockedReader = mock(BufferedReader.class);
    @NotNull
    protected AccountService mockedAccountService = mock(AccountService.class);

    protected final HttpSession mockedSession = mock(HttpSession.class);
    protected static final String TEST_SESSION_ID = "TEST_SESSION_ID";

    protected JsonObject parametersJson = new JsonObject();

    public void testDoPostAfter(String message, long expectedStatus, int timesNumber)
            throws ServletException, IOException {

        verify(mockedWriter, times(timesNumber)).println(jsonObjectCaptor.capture());
        JsonObject generatedJson = jsonObjectCaptor.getValue();

        assertNotNull(generatedJson.get("Status"));

        assertEquals(message, generatedJson.get("Status").getAsString());

        verify(mockedResponse, times(timesNumber)).setStatus(integerCaptor.capture());
        long status = integerCaptor.getValue();
        assertEquals(status, expectedStatus);
    }
}
