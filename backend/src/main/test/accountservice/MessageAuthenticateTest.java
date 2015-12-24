package accountservice;

import frontendservice.MessageIsAuthenticated;
import messagesystem.Address;
import messagesystem.MessageSystem;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Created by fatman on 24/12/15.
 */
public class MessageAuthenticateTest {

    private static final String USER_NAME_TEST = "USER";
    private static final String PASSOWRD_TEST = "PASSWORD";
    private static final String SESSION_TEST = "SESSION";

    private UserProfile userProfile = new UserProfile(USER_NAME_TEST, PASSOWRD_TEST, "");

    private AccountServiceTh accountServiceMock = mock(AccountServiceTh.class);
    private static final Address ADDRESS = new Address();
    private MessageAuthenticate messageAuthenticate =
            new MessageAuthenticate(ADDRESS, ADDRESS, USER_NAME_TEST, PASSOWRD_TEST, SESSION_TEST);

    private MessageSystem messageSystemMock = mock(MessageSystem.class);

    @Before
    public void setUp() {
        when(accountServiceMock.getMessageSystem()).thenReturn(messageSystemMock);
        when(accountServiceMock.getSessions(eq(SESSION_TEST))).thenReturn(userProfile);
    }

    @Test
    public void testExecAuthorized() throws Exception {
        when(accountServiceMock.authenticate(
                eq(USER_NAME_TEST), eq(PASSOWRD_TEST),
                eq(SESSION_TEST))).thenReturn(Statuses.AuthorizationStates.AUTHORIZED);
        messageAuthenticate.exec(accountServiceMock);
        verify(messageSystemMock)
                .sendMessage(eq(new MessageIsAuthenticated(
                        ADDRESS, ADDRESS, SESSION_TEST,
                        Statuses.AuthorizationStates.AUTHORIZED,
                        userProfile)));
    }


    @Test
    public void testExecAnyWrong() throws Exception {
        for(Statuses.AuthorizationStates status: Statuses.AuthorizationStates.values()) {
            if (status != Statuses.AuthorizationStates.AUTHORIZED) {

                when(accountServiceMock.authenticate(
                        eq(USER_NAME_TEST), eq(PASSOWRD_TEST),
                        eq(SESSION_TEST))).thenReturn(status);
                messageAuthenticate.exec(accountServiceMock);
                verify(messageSystemMock)
                        .sendMessage(eq(new MessageIsAuthenticated(
                                ADDRESS, ADDRESS, SESSION_TEST, status, null)));
            }
        }
    }
}