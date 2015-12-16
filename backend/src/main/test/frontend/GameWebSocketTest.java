package frontend;

import game_socket_mechanics.GameWebSocket;
import game_socket_mechanics.WebSocketServiceImpl;
import base.GameMechanics;
import base.GameUser;
import base.WebSocketService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mechanics.GameMechanicsImpl;
import org.eclipse.jetty.websocket.api.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by fatman on 02/11/15.
 */

public class GameWebSocketTest extends Mockito {

    private static final String TEST_USER_NAME = "testUser1";
    private static final String TEST_USER_ACTION = "jump";
    private final GameUser testUser = mock(GameUser.class);

    private final GameMechanics mockedGameMechanics = mock(GameMechanicsImpl.class);
    private final WebSocketService mockedWebSocketService = mock(WebSocketServiceImpl.class);
    private final GameWebSocket gameWebSocket =
            new GameWebSocket(TEST_USER_NAME,
                                mockedGameMechanics,
                                mockedWebSocketService);
    private final Session mockedSession = mock(Session.class, RETURNS_DEEP_STUBS);
    private final JsonArray testEnemies = new JsonArray();

    @Before
    public void beforeGameWebSocketTest() {
        String[] enemyNames = new String[2];
        enemyNames[0] = "testEnemy1";
        JsonObject testEnemy = new JsonObject();
        testEnemy.addProperty("name", "testEnemy1");
        testEnemies.add(testEnemy);
        enemyNames[1] = "testEnemy2";
        testEnemy = new JsonObject();
        testEnemy.addProperty("name", "testEnemy2");
        testEnemies.add(testEnemy);
        when(testUser.getEnemyNames()).thenReturn(enemyNames);
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

        JsonObject jsonTest = new JsonObject();
        jsonTest.addProperty("status", "start");
        jsonTest.add("enemyNames", testEnemies);

        System.out.append(jsonTest.toString());
        verify(mockedSession.getRemote(), times(1))
                .sendString(eq(jsonTest.toString()));
    }

    @Test
    public void testStartGameNoCrush() throws IOException {
        gameWebSocket.setSession(mockedSession);
        //doThrow(new Exception()).when(mockedSession.getRemote()).sendString(any());
        gameWebSocket.startGame(testUser);

    }

    @Test
    public void testGameOver() throws Exception {

    }

    @Test
    public void testOnMessage() throws Exception {

        JsonObject jsonTest = new JsonObject();

        gameWebSocket.onMessage(jsonTest.toString());
        verify(mockedGameMechanics, times(0))
                .processGameLogicData(any(), any());

        jsonTest.addProperty("type", "game logic");

        gameWebSocket.onMessage(jsonTest.toString());

        verify(mockedGameMechanics, times(1))
                .processGameLogicData(eq(TEST_USER_NAME), eq(jsonTest));
    }

    @Test
    public void testEmptyStringOnMessage() {

        final String emptyString = "";

        gameWebSocket.onMessage(emptyString);
    }

    @Test
    public void testOnOpen() throws Exception {
        gameWebSocket.onOpen(mockedSession);

        assertEquals(gameWebSocket.getSession(), mockedSession);
        verify(mockedWebSocketService, times(1)).addUser(eq(gameWebSocket));
        verify(mockedGameMechanics, times(1)).addUser(eq(TEST_USER_NAME));
    }

    @Test
    public void testSetMyScore() throws Exception {

    }

    @Test
    public void testSendEnemyAction() throws IOException {
        gameWebSocket.setSession(mockedSession);

        JsonObject jsonTest = new JsonObject();

        jsonTest.addProperty("activePlayer", TEST_USER_NAME);
        jsonTest.addProperty("action", TEST_USER_ACTION);

        gameWebSocket.sendEnemyAction(jsonTest);

        verify(mockedSession.getRemote(), times(1))
                .sendString(matches("(.*" + TEST_USER_NAME + ".*"
                                    + TEST_USER_ACTION + ".*)|(.*"
                                    + TEST_USER_ACTION + ".*"
                                    + TEST_USER_NAME + ".*)"));
    }

    @Test
    public void testSetSession() throws Exception {
        gameWebSocket.setSession(mockedSession);

        assertEquals(gameWebSocket.getSession(), mockedSession);
    }

    @Test
    public void testOnClose() throws Exception {

        final int CODE = 500;

        gameWebSocket.onClose(CODE, " NOT OK");
        verify(mockedGameMechanics, times(1)).deleteIfWaiter(eq(TEST_USER_NAME));
    }
}