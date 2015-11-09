package frontend;

import base.GameMechanics;
import base.GameUser;
import base.WebSocketService;
import mechanics.GameMechanicsImpl;
import org.eclipse.jetty.websocket.api.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by fatman on 02/11/15.
 */
@SuppressWarnings("unchecked")
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
    private final JSONArray testEnemies = new JSONArray();

    @Before
    public void beforeGameWebSocketTest() {
        String[] enemyNames = new String[2];
        enemyNames[0] = "testEnemy1";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "testEnemy1");
        testEnemies.add(0, jsonObject);
        enemyNames[1] = "testEnemy2";
        jsonObject = new JSONObject();
        jsonObject.put("name", "testEnemy2");
        testEnemies.add(1, jsonObject);
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

        JSONObject jsonTest = new JSONObject();
        jsonTest.put("status", "start");
        jsonTest.put("enemyNames", testEnemies);

        System.out.append(jsonTest.toJSONString());
        verify(mockedSession.getRemote(), times(1))
                .sendString(eq(jsonTest.toJSONString()));
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

        JSONObject jsonTest = new JSONObject();

        gameWebSocket.onMessage(jsonTest.toJSONString());
        verify(mockedGameMechanics, times(0))
                .processGameLogicData(any(), any());

        jsonTest.put("type", "game logic");

        gameWebSocket.onMessage(jsonTest.toJSONString());

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

        JSONObject jsonTest = new JSONObject();

        jsonTest.put("activePlayer", TEST_USER_NAME);
        jsonTest.put("action", TEST_USER_ACTION);

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