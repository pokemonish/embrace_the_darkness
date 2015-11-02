package mechanics;

import base.GameMechanics;
import base.WebSocketService;
import frontend.WebSocketServiceImpl;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Created by fatman on 02/11/15.
 */
public class GameMechanicsImplTest extends Mockito {

    private final WebSocketService mockedWebSocketService = mock(WebSocketServiceImpl.class);

    private final GameMechanics gameMechanics = new GameMechanicsImpl(mockedWebSocketService);

    private static final int USERS_NUMBER = 3;
    private static final int TEST_USERS_NUMBER = 15;
    private static final String USER = "user";

    @Test
    public void testAddUser() throws Exception {
        for (int i = 0; i < TEST_USERS_NUMBER; ++i) {
            verify(mockedWebSocketService, times((i / USERS_NUMBER) * USERS_NUMBER)).notifyStartGame(any());
            gameMechanics.addUser(USER + i);
        }

        verify(mockedWebSocketService, times(TEST_USERS_NUMBER)).notifyStartGame(any());
    }

    @Test
    public void testDeleteIfWaiter() throws Exception {
        for (int i = 0; i < TEST_USERS_NUMBER; ++i) {
            gameMechanics.addUser(USER + i);
            gameMechanics.deleteIfWaiter(USER + i);
        }

        verify(mockedWebSocketService, times(0)).notifyStartGame(any());
    }

    @Test
    public void testIncrementScore() throws Exception {

    }

    @Test
    public void testRun() throws Exception {

    }

    @Test
    public void testProcessGameLogicData() throws Exception {

    }

    @Test
    public void testSendOtherPlayers() throws Exception {
        for (int i = 0; i < USERS_NUMBER; ++i) {
            gameMechanics.addUser(USER + i);
        }

        JSONObject testJsonData = new JSONObject();
        testJsonData.put("action", "jump");

        gameMechanics.sendOtherPlayers(USER + 1, testJsonData);

        verify(mockedWebSocketService, times(USERS_NUMBER - 1)).notifyEnemyAction(any(), eq(testJsonData));
    }
}