package frontend;

import game_socket_mechanics.GameWebSocket;
import game_socket_mechanics.WebSocketServiceImpl;
import base.GameUser;
import com.google.gson.JsonObject;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Created by fatman on 05/11/15.
 */
public class WebSocketServiceImplTest extends Mockito {

    private GameWebSocket gameWebSocketMock = mock(GameWebSocket.class);
    private GameUser gameUserMock = mock(GameUser.class);
    private WebSocketServiceImpl webSocketService = new WebSocketServiceImpl();
    private JsonObject testJsonObject = new JsonObject();

    @Test
    public void testNotifyMyNewScore() throws Exception {
        webSocketService.addUser(gameWebSocketMock);
        webSocketService.notifyMyNewScore(gameUserMock);
        verify(gameWebSocketMock, times(1)).setMyScore(any());
    }

    @Test
    public void testNotifyEnemyNewScore() throws Exception {

    }

    @Test
    public void testNotifyStartGame() throws Exception {
        webSocketService.addUser(gameWebSocketMock);
        webSocketService.notifyStartGame(gameUserMock);
        verify(gameWebSocketMock, times(1)).startGame(eq(gameUserMock));
    }

    @Test
    public void testNotifyEnemyAction() throws Exception {
        webSocketService.addUser(gameWebSocketMock);
        webSocketService.notifyEnemyAction(gameUserMock, testJsonObject);
        verify(gameWebSocketMock, times(1)).sendEnemyAction(eq(testJsonObject));
    }

    @Test
    public void testNotifyGameOver() throws Exception {
        webSocketService.addUser(gameWebSocketMock);
        webSocketService.notifyGameOver(gameUserMock, true);
        verify(gameWebSocketMock, times(1)).gameOver(eq(gameUserMock), eq(true));
        webSocketService.notifyGameOver(gameUserMock, false);
        verify(gameWebSocketMock, times(1)).gameOver(eq(gameUserMock), eq(false));
    }
}