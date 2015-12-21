package mechanics;

import base.GameMechanics;
import base.WebSocketService;
import com.google.gson.JsonObject;
import gamesocketmech.WebSocketServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import utils.TimeHelper;

/**
 * Created by fatman on 02/11/15.
 */

public class GameMechanicsImplTest extends Mockito {

    private final WebSocketService mockedWebSocketService =
            mock(WebSocketServiceImpl.class);
    private final MechanicsParameters mechanicsParametersMock =
            mock(MechanicsParameters.class);

    private GameMechanics gameMechanics;

    private static final int USERS_NUMBER = 3;
    private static final int TEST_USERS_NUMBER = 15;
    private static final String USER = "user";

    private static final class Worker extends Thread {
        private GameMechanicsImpl gameMechanics;

        private Worker(GameMechanicsImpl mechanics) {
            gameMechanics = mechanics;
        }

        @Override
        public void run() {
            TimeHelper.sleep(2*1000);
            gameMechanics.setIsActive(false);
        }
    }

    @Before
    public void setUp() {
        when(mechanicsParametersMock.getGameTime()).thenReturn(1000);
        when(mechanicsParametersMock.getPlayersNumber()).thenReturn(USERS_NUMBER);
        when(mechanicsParametersMock.getStepTime()).thenReturn(100);
        gameMechanics = new GameMechanicsImpl(mockedWebSocketService, mechanicsParametersMock);
    }

    @Test
    public void testAddUser() throws Exception {
        for (int i = 0; i < TEST_USERS_NUMBER; ++i) {
            verify(mockedWebSocketService, times((i / USERS_NUMBER) * USERS_NUMBER))
                                                            .notifyStartGame(any());
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
        for (int i = 0; i < USERS_NUMBER; ++i) {
            gameMechanics.addUser(USER + i);
        }

        JsonObject testJsonData = new JsonObject();
        testJsonData.addProperty("data", "jump");

        gameMechanics.processGameLogicData(USER + 1, testJsonData);

        verify(mockedWebSocketService, times(USERS_NUMBER - 1))
                .notifyEnemyAction(any(), any());
    }

    @Test
    public void testSendOtherPlayers() throws Exception {
        for (int i = 0; i < USERS_NUMBER; ++i) {
            gameMechanics.addUser(USER + i);
        }

        JsonObject testJsonData = new JsonObject();
        testJsonData.addProperty("action", "jump");

        gameMechanics.sendOtherPlayers(USER + 1, testJsonData);

        verify(mockedWebSocketService, times(USERS_NUMBER - 1))
                .notifyEnemyAction(any(), eq(testJsonData));
    }

    @Test
    public void testGameOverHandling() throws Exception {
        for (int i = 0; i < USERS_NUMBER; ++i) {
            gameMechanics.addUser(USER + i);
        }

        JsonObject testJsonData = new JsonObject();
        testJsonData.addProperty("data", "dead");

        for (int i = 0; i < USERS_NUMBER; ++i) {
            gameMechanics.processGameLogicData(USER + i, testJsonData);
        }

        Worker worker = new Worker((GameMechanicsImpl)gameMechanics);
        worker.start();

        gameMechanics.run();

        verify(mockedWebSocketService, times(USERS_NUMBER)).notifyGameOver(any(), anyBoolean());
    }
}