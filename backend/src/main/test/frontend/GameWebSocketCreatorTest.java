package frontend;

import game_socket_mechanics.GameWebSocket;
import game_socket_mechanics.GameWebSocketCreator;
import base.GameMechanics;
import base.WebSocketService;
import main.AccountService;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * Created by fatman on 05/11/15.
 */
public class GameWebSocketCreatorTest extends Mockito {

    private AccountService accountServiceMock = mock(AccountService.class, RETURNS_DEEP_STUBS);
    private GameMechanics gameMechanicsMock = mock(GameMechanics.class);
    private WebSocketService webSocketServiceMock = mock(WebSocketService.class);
    private ServletUpgradeRequest servletUpgradeRequestMock = mock(ServletUpgradeRequest.class, RETURNS_DEEP_STUBS);
    private ServletUpgradeResponse servletUpgradeResponseMock = mock(ServletUpgradeResponse.class);

    private static final String TEST_SESSION_ID = "TEST_SESSION_ID";
    private static final String TEST_USER_NAME = "TEST_USER_NAME";

    private GameWebSocketCreator gameWebSocketCreator =
        new GameWebSocketCreator(accountServiceMock,
                                gameMechanicsMock,
                                webSocketServiceMock);


    @Test
    public void testCreateWebSocket() throws Exception {
        when(servletUpgradeRequestMock.getHttpServletRequest().getSession().getId()).thenReturn(TEST_SESSION_ID);
        when(accountServiceMock.getUser(TEST_SESSION_ID).getLogin()).thenReturn(TEST_USER_NAME);
        GameWebSocket gameWebSocketTest = gameWebSocketCreator
                .createWebSocket(servletUpgradeRequestMock, servletUpgradeResponseMock);

        verify(servletUpgradeRequestMock.getHttpServletRequest().getSession(), times(1)).getId();

        GameWebSocket expectedGameWebSocket =
                new GameWebSocket(TEST_USER_NAME, gameMechanicsMock,
                                    webSocketServiceMock);
        assertEquals(gameWebSocketTest, expectedGameWebSocket);
    }
}