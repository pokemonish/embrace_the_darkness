package accountservice;

import db.AlreadyExistsException;
import frontendservice.MessageRegistered;
import messagesystem.Address;
import messagesystem.MessageSystem;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static  org.mockito.Mockito.*;

/**
 * Created by fatman on 24/12/15.
 */
public class MessageRegisterTest {

    private static final Address ADDRESS = new Address();

    private static final String TEST_USER_NAME = "USER";

    private AccountServiceTh accountServiceThMock = mock(AccountServiceTh.class, RETURNS_DEEP_STUBS);
    private UserProfile userProfile = new UserProfile(TEST_USER_NAME, "", "");
    private MessageRegister messageRegister = new MessageRegister(ADDRESS, ADDRESS, userProfile);
    private MessageSystem messageSystemMock = mock(MessageSystem.class);

    @Before
    public void setUp() {
        when(accountServiceThMock.getMessageSystem()).thenReturn(messageSystemMock);
    }

    @Test
    public void testExecSuccess() throws AccountServiceException {
        messageRegister.exec(accountServiceThMock);
        verify(accountServiceThMock).addUser(eq(userProfile));
        verify(messageSystemMock)
                .sendMessage(eq(new MessageRegistered(
                        ADDRESS, ADDRESS, userProfile.getLogin(),
                        Statuses.SignUpStatuses.SUCCESS)));
    }

    @Test
    public void testExecError() throws AccountServiceException {
        doThrow(new AccountServiceException(new SQLException())).when(accountServiceThMock).addUser(any());

        messageRegister.exec(accountServiceThMock);
        verify(accountServiceThMock).addUser(eq(userProfile));
        verify(messageSystemMock)
                .sendMessage(eq(new MessageRegistered(
                        ADDRESS, ADDRESS, userProfile.getLogin(),
                        Statuses.SignUpStatuses.ERROR)));
    }

    @Test
    public void testExecExists() throws AccountServiceException {
        doThrow(new AccountServiceException(new AlreadyExistsException(""))).when(accountServiceThMock).addUser(any());

        messageRegister.exec(accountServiceThMock);
        verify(accountServiceThMock).addUser(eq(userProfile));
        verify(messageSystemMock)
                .sendMessage(eq(new MessageRegistered(
                        ADDRESS, ADDRESS, userProfile.getLogin(),
                        Statuses.SignUpStatuses.USER_ALREADY_EXISTS)));
    }
}