package frontend;

import base.GameMechanics;
import base.GameUser;
import base.WebSocketService;
import mechanics.GameMechanicsImpl;
import org.eclipse.jetty.server.session.JDBCSessionManager;
import org.eclipse.jetty.websocket.api.Session;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Array;
import java.rmi.Remote;

import static org.junit.Assert.*;

/**
 * Created by fatman on 02/11/15.
 */
public class GameWebSocketTest extends Mockito {

    private static final String TEST_USER_NAME = "testUser1";
    private final GameUser testUser = mock(GameUser.class);

    private final GameMechanics mockedGameMechanics = mock(GameMechanicsImpl.class);
    private final WebSocketService mockedWebSocketService = mock(WebSocketServiceImpl.class);
    private final GameWebSocket gameWebSocket = new GameWebSocket(TEST_USER_NAME, mockedGameMechanics, mockedWebSocketService);
    private final Session mockedSession = mock(Session.class);
    private final String[] testEnemies = new String[2];
    private final ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

    @Before
    public void beforeGameWebSocketTest() {
        testEnemies[0] = "testEnemy1";
        testEnemies[1] = "testEnemy2";
        when(testUser.getEnemyNames()).thenReturn(testEnemies);
    }

    @Test
    public void testGetMyName() throws Exception {
        String userName = gameWebSocket.getMyName();
        assertEquals(TEST_USER_NAME, userName);

    }

    @Test
    public void testStartGame() throws IOException {
        gameWebSocket.setSession(mockedSession);
        gameWebSocket.startGame(testUser);

        JSONObject jsonTest = new JSONObject();
        jsonTest.put("status", "start");
        jsonTest.put("enemyNames", testEnemies);

        //when(mockedSession.getRemote().sendString(argumentCaptor.capture()));
        verify(mockedSession, times(1)).getRemote();
        assertEquals(argumentCaptor.getValue(), jsonTest.toString());
    }

    @Test
    public void testGameOver() throws Exception {

    }

    @Test
    public void testOnMessage() throws Exception {

    }

    @Test
    public void testOnOpen() throws Exception {

    }

    @Test
    public void testSetMyScore() throws Exception {

    }

    @Test
    public void testSetEnemyScore() throws Exception {

    }

    @Test
    public void testSendEnemyAction() throws Exception {

    }

    @Test
    public void testGetSession() throws Exception {

    }

    @Test
    public void testSetSession() throws Exception {
        gameWebSocket.setSession(mockedSession);

        assertEquals(gameWebSocket.getSession(), mockedSession);
    }

    @Test
    public void testOnClose() throws Exception {

    }
}